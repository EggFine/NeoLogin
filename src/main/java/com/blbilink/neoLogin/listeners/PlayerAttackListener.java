package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;
import com.blbilink.neoLogin.NeoLogin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.entity.Player;

public class PlayerAttackListener implements Listener {
    private final ConfigManager configManager;
    private final PlayerManager playerManager;

    public PlayerAttackListener(NeoLogin plugin) {
        this.configManager = plugin.getConfigManager();
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        // 检查攻击者是否为玩家
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            // 只有当配置启用攻击限制且攻击者未登录时，才取消攻击
            if (configManager.isLimitAttacking() && !playerManager.isLoggedIn(attacker)) {
                event.setCancelled(true);
            }
        }
    }
}
