package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * 玩家聊天限制监听器
 * 处理未登录玩家的聊天消息限制
 */
public class PlayerChatListener implements Listener {

    private final ConfigManager configManager;
    private final PlayerManager playerManager;

    public PlayerChatListener(NeoLogin plugin) {
        this.configManager = plugin.getConfigManager();
        this.playerManager = plugin.getPlayerManager();
    }

    /**
     * 处理玩家聊天事件
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // 检查是否启用了限制且玩家未登录
        if (configManager.isNotLoggedInLimitEnabled() 
                && configManager.isLimitChat() 
                && !playerManager.isLoggedIn(player)) {
            event.setCancelled(true);
        }
    }
}

