package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

/**
 * 玩家命令限制监听器
 * 处理未登录玩家的命令使用限制，支持白名单
 */
public class PlayerCommandListener implements Listener {

    private final ConfigManager configManager;
    private final PlayerManager playerManager;

    public PlayerCommandListener(NeoLogin plugin) {
        this.configManager = plugin.getConfigManager();
        this.playerManager = plugin.getPlayerManager();
    }

    /**
     * 处理玩家命令预处理事件
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        // 如果未启用限制或命令限制未启用，直接返回
        if (!configManager.isNotLoggedInLimitEnabled() || !configManager.isLimitCommand()) {
            return;
        }

        // 如果玩家已登录，允许执行命令
        if (playerManager.isLoggedIn(player)) {
            return;
        }

        // 检查命令是否在白名单中
        String message = event.getMessage().toLowerCase();
        List<String> whitelist = configManager.getCommandWhitelist();

        for (String allowedCommand : whitelist) {
            String cmd = allowedCommand.toLowerCase();
            // 支持 "/login" 或 "login" 格式的白名单
            if (!cmd.startsWith("/")) {
                cmd = "/" + cmd;
            }
            // 检查命令是否以白名单命令开头（支持带参数的命令）
            if (message.startsWith(cmd + " ") || message.equals(cmd)) {
                return; // 命令在白名单中，允许执行
            }
        }

        // 命令不在白名单中，取消事件
        event.setCancelled(true);
    }
}

