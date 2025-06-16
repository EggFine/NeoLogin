package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.NeoLogin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.entity.Player;

public class PlayerAttackListener implements Listener {
    private final ConfigManager configManager;

    public PlayerAttackListener(NeoLogin plugin) {
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (configManager.isLimitAttacking()) {
                event.setCancelled(true);
            }
        }
    }
}
