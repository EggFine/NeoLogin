package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerConnectListener implements Listener {
    private final PlayerManager playerManager;
    private final ConfigManager configManager;

    public PlayerConnectListener(NeoLogin plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 检查是否需要自动传送到登录点
        if (configManager.isAutoTeleportEnabled() && configManager.isAutoTeleportOnJoin()) {
            Location loginLocation = configManager.getTeleportLocation();
            if (loginLocation != null) {
                // 缓存玩家的原始位置，以便登录后返回
                playerManager.cacheOriginalLocation(player.getUniqueId(), player.getLocation());
                player.teleport(loginLocation);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // 玩家退出时，清除其登录状态和位置缓存
        playerManager.setLoggedOut(player);
        playerManager.getAndRemoveOriginalLocation(player.getUniqueId());
    }


}