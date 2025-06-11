package com.blbilink.neoLogin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.blbilink.neoLogin.NeoLogin;

public class PlayerMoveListener implements Listener {
    private final NeoLogin plugin;

    public PlayerMoveListener(NeoLogin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        // 检查玩家是否已登录
        if (!plugin.getPlayerManager().isLoggedIn(player)) {
            // 检查配置文件中是否启用了未登录玩家限制
            boolean restrictionsEnabled = plugin.getConfigManager().isNotLoggedInLimitEnabled();
            boolean moveRestricted = plugin.getConfigManager().isLimitMove();
            
            // 如果启用了限制且设置了禁止移动，则取消事件
            if (restrictionsEnabled && moveRestricted) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
