package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.dao.UserDAO;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Floodgate/基岩版玩家支持监听器
 * 处理基岩版玩家的自动登录和表单登录
 */
public class FloodgateListener implements Listener {

    private final NeoLogin plugin;
    private final ConfigManager configManager;
    private final PlayerManager playerManager;
    private final UserDAO userDAO;
    private final I18n i18n;
    private final boolean floodgateEnabled;

    public FloodgateListener(NeoLogin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.playerManager = plugin.getPlayerManager();
        this.userDAO = plugin.getUserDAO();
        this.i18n = plugin.getI18n();
        
        // 检查 Floodgate 插件是否存在
        this.floodgateEnabled = plugin.getServer().getPluginManager().isPluginEnabled("floodgate");
        
        if (floodgateEnabled) {
            plugin.getLogger().info("检测到 Floodgate 插件，基岩版支持已启用！");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 检查是否启用了基岩版功能
        if (!configManager.isBedrockEnabled()) {
            return;
        }

        // 检查玩家是否为基岩版玩家
        if (!isBedrockPlayer(player)) {
            return;
        }

        // 检查是否应该自动登录
        if (shouldAutoLogin(player)) {
            handleAutoLogin(player);
            return;
        }

        // 如果启用了表单功能且 Floodgate 可用，发送登录/注册表单
        if (configManager.isBedrockFormsEnabled() && floodgateEnabled) {
            plugin.getFoliaUtil().runTaskLater(() -> {
                openLoginForm(player);
            }, 20L); // 延迟1秒后打开表单
        }
    }

    /**
     * 检查玩家是否为基岩版玩家
     */
    public boolean isBedrockPlayer(Player player) {
        // 方法1：检查 UUID 前缀 (基岩版玩家的 UUID 以 00000000-0000-0000- 开头)
        if (player.getUniqueId().toString().startsWith("00000000-0000-0000-")) {
            return true;
        }

        // 方法2：检查玩家名前缀
        String prefix = configManager.getBedrockPrefix();
        if (prefix != null && !prefix.isEmpty() && player.getName().startsWith(prefix)) {
            return true;
        }

        // 方法3：使用 Floodgate API
        if (floodgateEnabled) {
            try {
                return isFloodgatePlayer(player);
            } catch (Exception ignored) {
                // Floodgate API 不可用
            }
        }

        return false;
    }

    /**
     * 使用 Floodgate API 检查玩家是否为基岩版
     */
    private boolean isFloodgatePlayer(Player player) {
        try {
            org.geysermc.floodgate.api.FloodgateApi api = org.geysermc.floodgate.api.FloodgateApi.getInstance();
            return api.isFloodgatePlayer(player.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查是否应该自动登录
     */
    private boolean shouldAutoLogin(Player player) {
        // 检查 UUID 自动登录
        if (configManager.isBedrockAutoLoginByUUID() && 
                player.getUniqueId().toString().startsWith("00000000-0000-0000-")) {
            return true;
        }

        // 检查前缀自动登录
        String prefix = configManager.getBedrockPrefix();
        if (configManager.isBedrockAutoLoginByPrefix() && 
                prefix != null && !prefix.isEmpty() && 
                player.getName().startsWith(prefix)) {
            return true;
        }

        // 检查 Floodgate 自动登录
        if (configManager.isBedrockAutoLoginByFloodgate() && floodgateEnabled) {
            try {
                if (isFloodgatePlayer(player)) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }

        return false;
    }

    /**
     * 处理自动登录
     */
    private void handleAutoLogin(Player player) {
        // 检查玩家是否已注册
        if (!userDAO.isRegistered(player.getUniqueId())) {
            // 基岩版玩家自动登录时，如果未注册，自动为其注册一个随机密码
            // 因为基岩版玩家通过设备认证，不需要密码
            String randomPassword = generateRandomPassword();
            String ipAddress = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "N/A";
            userDAO.registerUser(player.getUniqueId(), player.getName(), randomPassword, ipAddress);
            plugin.getLogger().info("基岩版玩家 " + player.getName() + " 已自动注册");
        }

        // 设置为已登录
        playerManager.setLoggedIn(player);
        // 恢复玩家登录前的飞行状态
        playerManager.restoreFlightState(player);
        player.sendMessage(i18n.as("bedrock.autoLoginSuccess", true, player.getName()));
        plugin.getLogger().info("基岩版玩家 " + player.getName() + " 已自动登录");
    }

    /**
     * 打开登录/注册表单
     */
    private void openLoginForm(Player player) {
        if (!floodgateEnabled) {
            return;
        }

        try {
            org.geysermc.floodgate.api.FloodgateApi api = org.geysermc.floodgate.api.FloodgateApi.getInstance();
            org.geysermc.floodgate.api.player.FloodgatePlayer floodgatePlayer = api.getPlayer(player.getUniqueId());

            if (floodgatePlayer == null) {
                return;
            }

            boolean isRegistered = userDAO.isRegistered(player.getUniqueId());
            
            String title = isRegistered ? 
                    i18n.as("bedrock.form.loginTitle", false) : 
                    i18n.as("bedrock.form.registerTitle", false);
            String description = isRegistered ? 
                    i18n.as("bedrock.form.loginDescription", false) : 
                    i18n.as("bedrock.form.registerDescription", false);

            org.geysermc.cumulus.form.CustomForm.Builder builder = org.geysermc.cumulus.form.CustomForm.builder()
                    .title(title)
                    .label(description)
                    .input(i18n.as("bedrock.form.passwordField", false), 
                           i18n.as("bedrock.form.passwordPlaceholder", false));

            // 如果是注册，添加确认密码字段
            if (!isRegistered) {
                builder.input(i18n.as("bedrock.form.confirmPasswordField", false),
                              i18n.as("bedrock.form.passwordPlaceholder", false));
            }

            builder.validResultHandler(response -> {
                handleFormResponse(player, isRegistered, response);
            });

            builder.closedOrInvalidResultHandler(() -> {
                // 表单被关闭，重新打开
                plugin.getFoliaUtil().runTaskLater(() -> {
                    if (player.isOnline() && !playerManager.isLoggedIn(player)) {
                        player.sendMessage(i18n.as("bedrock.form.mustComplete", true));
                        openLoginForm(player);
                    }
                }, 40L);
            });

            floodgatePlayer.sendForm(builder.build());
        } catch (Exception e) {
            plugin.getLogger().warning("打开基岩版表单失败: " + e.getMessage());
        }
    }

    /**
     * 处理表单响应
     */
    private void handleFormResponse(Player player, boolean isRegistered, 
                                     org.geysermc.cumulus.response.CustomFormResponse response) {
        try {
            String password = response.next();
            
            if (password == null || password.isEmpty()) {
                player.sendMessage(i18n.as("bedrock.form.passwordEmpty", true));
                plugin.getFoliaUtil().runTaskLater(() -> openLoginForm(player), 20L);
                return;
            }

            if (isRegistered) {
                // 登录验证
                String ipAddress = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "N/A";
                
                plugin.getFoliaUtil().runTaskAsync(() -> {
                    boolean success = userDAO.verifyPassword(player.getUniqueId(), password, ipAddress);
                    
                    plugin.getFoliaUtil().runTask(() -> {
                        if (success) {
                            playerManager.setLoggedIn(player);
                            // 恢复玩家登录前的飞行状态
                            playerManager.restoreFlightState(player);
                            player.sendMessage(i18n.as("login.success_message", true, player.getName()));
                        } else {
                            player.sendMessage(i18n.as("login.wrongPassword", true));
                            openLoginForm(player);
                        }
                    });
                });
            } else {
                // 注册
                String confirmPassword = response.next();
                
                if (confirmPassword == null || !password.equals(confirmPassword)) {
                    player.sendMessage(i18n.as("register.passwordMismatch", true));
                    plugin.getFoliaUtil().runTaskLater(() -> openLoginForm(player), 20L);
                    return;
                }

                String ipAddress = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "N/A";
                
                plugin.getFoliaUtil().runTaskAsync(() -> {
                    boolean success = userDAO.registerUser(player.getUniqueId(), player.getName(), password, ipAddress);
                    
                    plugin.getFoliaUtil().runTask(() -> {
                        if (success) {
                            playerManager.setLoggedIn(player);
                            // 恢复玩家登录前的飞行状态
                            playerManager.restoreFlightState(player);
                            player.sendMessage(i18n.as("register.success", true));
                        } else {
                            player.sendMessage(i18n.as("register.error", true));
                            openLoginForm(player);
                        }
                    });
                });
            }
        } catch (Exception e) {
            player.sendMessage(i18n.as("bedrock.form.error", true));
            plugin.getLogger().warning("处理基岩版表单响应失败: " + e.getMessage());
            plugin.getFoliaUtil().runTaskLater(() -> openLoginForm(player), 40L);
        }
    }

    /**
     * 生成随机密码
     * 使用 SecureRandom 以确保密码学安全
     */
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.security.SecureRandom random = new java.security.SecureRandom();
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 检查 Floodgate 是否已启用
     */
    public boolean isFloodgateEnabled() {
        return floodgateEnabled;
    }
}

