package com.blbilink.neoLogin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.blbilink.neoLogin.managers.PlayerManager;

public class PlayerMoveLisnter implements Listener {
    private final PlayerManager playerManager;

    public PlayerMoveLisnter(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!playerManager.isLoggedIn(player)) {
            event.setCancelled(true);
            return;
        }
        
        
    }
}
