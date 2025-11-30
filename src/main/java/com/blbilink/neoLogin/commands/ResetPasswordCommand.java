package com.blbilink.neoLogin.commands;

import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.dao.UserDAO;
import com.blbilink.neoLogin.managers.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 重置密码命令
 * 玩家: /resetpassword <旧密码> <新密码>
 * 控制台: /resetpassword <玩家名> <新密码>
 */
public class ResetPasswordCommand implements CommandExecutor, TabCompleter {

    private final NeoLogin plugin;
    private final UserDAO userDAO;
    private final PlayerManager playerManager;
    private final I18n i18n;

    public ResetPasswordCommand(NeoLogin plugin) {
        this.plugin = plugin;
        this.userDAO = plugin.getUserDAO();
        this.playerManager = plugin.getPlayerManager();
        this.i18n = plugin.getI18n();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            return handlePlayerCommand((Player) sender, args);
        } else {
            return handleConsoleCommand(sender, args);
        }
    }

    /**
     * 处理玩家执行的命令
     * 用法: /resetpassword <旧密码> <新密码>
     */
    private boolean handlePlayerCommand(Player player, String[] args) {
        // 检查玩家是否已登录
        if (!playerManager.isLoggedIn(player)) {
            player.sendMessage(i18n.as("resetpassword.notLoggedIn", true));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(i18n.as("resetpassword.usage.player", true));
            return true;
        }

        String oldPassword = args[0];
        String newPassword = args[1];

        // 验证新密码长度
        int minLength = plugin.getConfigManager().getRegisterPasswordMinLength();
        int maxLength = plugin.getConfigManager().getRegisterPasswordLength();

        if (newPassword.length() < minLength) {
            player.sendMessage(i18n.as("register.passwordTooShort", true));
            return true;
        }

        if (newPassword.length() > maxLength) {
            player.sendMessage(i18n.as("register.passwordTooLong", true));
            return true;
        }

        // 异步验证旧密码并重置
        plugin.getFoliaUtil().runTaskAsync(() -> {
            // 验证旧密码
            String ipAddress = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "N/A";
            boolean oldPasswordCorrect = userDAO.verifyPassword(player.getUniqueId(), oldPassword, ipAddress);

            if (!oldPasswordCorrect) {
                plugin.getFoliaUtil().runTask(() -> {
                    player.sendMessage(i18n.as("resetpassword.wrongOldPassword", true));
                });
                return;
            }

            // 重置密码
            boolean success = userDAO.resetPassword(player.getUniqueId(), newPassword);

            plugin.getFoliaUtil().runTask(() -> {
                if (success) {
                    player.kickPlayer(i18n.as("resetpassword.kickMessage", false, player.getName()));
                } else {
                    player.sendMessage(i18n.as("resetpassword.failed", true));
                }
            });
        });

        return true;
    }

    /**
     * 处理控制台执行的命令
     * 用法: /resetpassword <玩家名> <新密码>
     */
    private boolean handleConsoleCommand(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(i18n.as("resetpassword.usage.console", false));
            return true;
        }

        String playerName = args[0];
        String newPassword = args[1];

        // 先尝试查找在线玩家
        Player targetPlayer = Bukkit.getPlayer(playerName);
        
        // 异步检查并重置密码
        plugin.getFoliaUtil().runTaskAsync(() -> {
            // 通过玩家名获取 UUID（支持离线玩家）
            java.util.UUID targetUUID;
            if (targetPlayer != null && targetPlayer.isOnline()) {
                targetUUID = targetPlayer.getUniqueId();
            } else {
                // 尝试从数据库获取 UUID
                targetUUID = userDAO.getUUIDByUsername(playerName);
            }

            if (targetUUID == null) {
                plugin.getFoliaUtil().runTask(() -> {
                    sender.sendMessage(i18n.as("resetpassword.playerNotFound", false));
                });
                return;
            }

            // 检查玩家是否已注册
            if (!userDAO.isRegistered(targetUUID)) {
                plugin.getFoliaUtil().runTask(() -> {
                    sender.sendMessage(i18n.as("resetpassword.playerNotRegistered", false));
                });
                return;
            }

            boolean success = userDAO.resetPassword(targetUUID, newPassword);

            plugin.getFoliaUtil().runTask(() -> {
                if (success) {
                    // 如果玩家在线，踢出玩家
                    if (targetPlayer != null && targetPlayer.isOnline()) {
                        targetPlayer.kickPlayer(i18n.as("resetpassword.kickMessage", false, targetPlayer.getName()));
                    }
                    sender.sendMessage(i18n.as("resetpassword.adminSuccess", false, playerName, newPassword));
                } else {
                    sender.sendMessage(i18n.as("resetpassword.failed", false));
                }
            });
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        // 控制台执行时，第一个参数补全在线玩家名
        if (!(sender instanceof Player) && args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            StringUtil.copyPartialMatches(args[0], playerNames, completions);
            Collections.sort(completions);
        }

        return completions;
    }
}

