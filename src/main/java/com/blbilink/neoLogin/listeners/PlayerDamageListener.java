package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;
import com.blbilink.neoLogin.NeoLogin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Player;

public class PlayerDamageListener implements Listener {
    private final ConfigManager configManager;
    private final PlayerManager playerManager;

    public PlayerDamageListener(NeoLogin plugin) {
        this.configManager = plugin.getConfigManager();
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            // 修复: 添加总开关检查
            if (configManager.isNotLoggedInLimitEnabled() 
                    && configManager.isLimitDamage() 
                    && !playerManager.isLoggedIn(player)) {
                event.setCancelled(true);
            }
        }
    }
}
