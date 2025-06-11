package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class AutoTeleportListener implements Listener {

    private final NeoLogin plugin;
    private final ConfigManager configManager;
    private final PlayerManager playerManager;

    public AutoTeleportListener(NeoLogin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.playerManager = plugin.getPlayerManager();
    }

    /**
     * 处理玩家加入游戏的事件
     */
    @EventHandler(priority = EventPriority.HIGHEST) // 使用较高优先级确保最后执行传送
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 检查总开关和加入时传送开关是否都为 true
        if (!configManager.isAutoTeleportEnabled() || !configManager.isAutoTeleportOnJoin()) {
            return;
        }

        Player player = event.getPlayer();
        Location teleportLocation = configManager.getTeleportLocation();

        // 检查是否设置了有效的传送点
        if (teleportLocation == null) {
            plugin.getLogger().warning("玩家加入时自动传送功能已开启，但未设置有效的目标地点 (autoTeleport.locationPos)，请使用 /neologin savelocation 设置。");
            return;
        }

        // 如果启用了登录后返回功能，先缓存玩家当前位置
        if (configManager.isAutoTeleportBack()) {
            // 对于新玩家，他们的位置可能是默认出生点
            playerManager.cacheOriginalLocation(player.getUniqueId(), player.getLocation());
        }

        // 使用 NeoLibrary 的 FoliaUtil 来执行传送，它能自动兼容 Folia 和 Spigot/Paper
        plugin.getFoliaUtil().runTask(() -> {
            player.teleport(teleportLocation);
        });
    }

    /**
     * 处理玩家死亡后重生的事件
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // 检查总开关和死亡时传送开关是否都为 true
        if (!configManager.isAutoTeleportEnabled() || !configManager.isAutoTeleportOnDeath()) {
            return;
        }

        Location teleportLocation = configManager.getTeleportLocation();

        // 检查是否设置了有效的传送点
        if (teleportLocation == null) {
            plugin.getLogger().warning("玩家死亡后自动传送功能已开启，但未设置有效的目标地点 (autoTeleport.locationPos)，请使用 /neologin savelocation 设置。");
            return;
        }

        // 直接设置玩家的重生点，这是处理重生传送的最佳方式
        event.setRespawnLocation(teleportLocation);
    }
}