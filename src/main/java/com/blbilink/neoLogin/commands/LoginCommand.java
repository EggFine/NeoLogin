package com.blbilink.neoLogin.commands;

import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.dao.UserDAO;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LoginCommand implements CommandExecutor {

    private final NeoLogin plugin;
    private final UserDAO userDAO;
    private final PlayerManager playerManager;
    private final ConfigManager configManager;
    private final I18n i18n;

    public LoginCommand(NeoLogin plugin) {
        this.plugin = plugin;
        this.userDAO = plugin.getUserDAO();
        this.playerManager = plugin.getPlayerManager(); // 使用新的 PlayerManager
        this.configManager = plugin.getConfigManager();
        this.i18n = plugin.getI18n();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n.as("login.onlyPlayer", false));
            return true;
        }

        Player player = (Player) sender;

        if (playerManager.isLoggedIn(player)) {
            player.sendMessage(i18n.as("login.alreadyLoggedIn", true));
            return true;
        }

        if (!userDAO.isRegistered(player.getUniqueId())) {
            player.sendMessage(i18n.as("login.notRegistered", true));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(i18n.as("login.usage", true));
            return true;
        }

        String password = args[0];
        String ipAddress = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "N/A";

        // 在后台线程验证密码
        plugin.getFoliaUtil().runTaskAsync(() -> {
            boolean success = userDAO.verifyPassword(player.getUniqueId(), password, ipAddress);

            // 在主线程处理结果
            plugin.getFoliaUtil().runTask(() -> {
                if (success) {
                    playerManager.setLoggedIn(player); // 标记为已登录

                    // 发送消息
                    if(configManager.getLoginSend().getBoolean("success.title")){
                        player.sendTitle(i18n.as("login.success_title", false, player.getName()), null, 10, 100, 10);
                    }
                    if(configManager.getLoginSend().getBoolean("success.subtitle")){
                        player.sendTitle(null, i18n.as("login.success_subtitle", false, player.getName()), 10, 100, 10);
                    }
                    if(configManager.getLoginSend().getBoolean("success.message")){
                        player.sendMessage(i18n.as("login.success_message", true, player.getName()));
                    }
                    if(configManager.getLoginSend().getBoolean("success.sound")){
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }

                    // 恢复玩家登录前的飞行状态
                    playerManager.restoreFlightState(player);

                    // 检查是否需要传送回原位
                    if (configManager.isAutoTeleportBack()) {
                        Location originalLocation = playerManager.getAndRemoveOriginalLocation(player.getUniqueId());
                        if (originalLocation != null) {
                            player.teleport(originalLocation);
                            player.sendMessage(i18n.as("login.teleportBack", true));
                        }
                    }
                } else {
                    player.sendMessage(i18n.as("login.wrongPassword", true));
                }
            });
        });

        return true;
    }
}