package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

/**
 * 处理玩家连接和断开连接事件
 * 注意：自动传送逻辑已移至 AutoTeleportListener，避免重复处理
 */
public class PlayerConnectListener implements Listener {
    private final PlayerManager playerManager;

    public PlayerConnectListener(NeoLogin plugin) {
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // 玩家退出时，清除其登录状态和位置缓存
        playerManager.setLoggedOut(player);
        playerManager.getAndRemoveOriginalLocation(player.getUniqueId());
    }
}