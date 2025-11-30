package com.blbilink.neoLogin.effects;

import com.blbilink.neoLibrary.utils.FoliaUtil;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 粒子效果管理器
 * 为未登录玩家生成粒子效果以提供视觉提示
 */
public class ParticleEffect implements Listener {

    private final NeoLogin plugin;
    private final ConfigManager configManager;
    private final PlayerManager playerManager;
    private final FoliaUtil foliaUtil;

    // 存储每个玩家的粒子任务
    private final Map<UUID, FoliaUtil.Cancellable> particleTasks = new ConcurrentHashMap<>();

    public ParticleEffect(NeoLogin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.playerManager = plugin.getPlayerManager();
        this.foliaUtil = plugin.getFoliaUtil();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 检查是否启用了粒子效果
        if (!configManager.isParticleEnabled()) {
            return;
        }

        // 如果玩家未登录，启动粒子效果
        if (!playerManager.isLoggedIn(player)) {
            startParticleEffect(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        stopParticleEffect(event.getPlayer().getUniqueId());
    }

    /**
     * 启动玩家的粒子效果
     */
    public void startParticleEffect(Player player) {
        UUID playerId = player.getUniqueId();

        // 如果已有任务在运行，先停止
        stopParticleEffect(playerId);

        // 创建粒子效果任务，每10tick执行一次 (使用异步任务，但在回调中使用同步方法生成粒子)
        FoliaUtil.Cancellable task = foliaUtil.runTaskTimerAsync((cancellable) -> {
            Player onlinePlayer = plugin.getServer().getPlayer(playerId);
            
            // 检查玩家是否还在线
            if (onlinePlayer == null || !onlinePlayer.isOnline()) {
                stopParticleEffect(playerId);
                return;
            }

            // 检查玩家是否已登录
            if (playerManager.isLoggedIn(onlinePlayer)) {
                stopParticleEffect(playerId);
                return;
            }

            // 在主线程生成粒子效果
            foliaUtil.runTask(() -> {
                Player p = plugin.getServer().getPlayer(playerId);
                if (p != null && p.isOnline()) {
                    spawnParticlesAroundPlayer(p);
                }
            });
        }, 0L, 10L);

        particleTasks.put(playerId, task);
    }

    /**
     * 停止玩家的粒子效果
     */
    public void stopParticleEffect(UUID playerId) {
        FoliaUtil.Cancellable task = particleTasks.remove(playerId);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    /**
     * 在玩家周围生成粒子效果
     */
    private void spawnParticlesAroundPlayer(Player player) {
        Location playerLocation = player.getLocation();
        double radius = 1.5; // 粒子圈的半径
        int particleCount = 12; // 每圈粒子数量

        for (int i = 0; i < particleCount; i++) {
            // 计算粒子位置
            double angle = 2 * Math.PI * i / particleCount;
            double x = playerLocation.getX() + radius * Math.cos(angle);
            double y = playerLocation.getY() + 1.0; // 玩家腰部高度
            double z = playerLocation.getZ() + radius * Math.sin(angle);

            // 设置粒子位置
            Location particleLocation = new Location(playerLocation.getWorld(), x, y, z);

            // 生成粒子效果
            try {
                // 尝试使用配置的粒子类型
                Particle particle = getConfiguredParticle();
                playerLocation.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
            } catch (Exception e) {
                // 如果粒子类型不存在，使用默认的
                playerLocation.getWorld().spawnParticle(Particle.FLAME, particleLocation, 1, 0, 0, 0, 0);
            }
        }
    }

    /**
     * 获取配置的粒子类型
     */
    private Particle getConfiguredParticle() {
        String particleType = configManager.getParticleType();
        try {
            return Particle.valueOf(particleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 默认使用 FLAME 粒子
            return Particle.FLAME;
        }
    }

    /**
     * 当玩家登录成功时调用此方法停止粒子效果
     */
    public void onPlayerLoggedIn(Player player) {
        stopParticleEffect(player.getUniqueId());
    }

    /**
     * 清理所有粒子任务
     */
    public void cleanup() {
        for (FoliaUtil.Cancellable task : particleTasks.values()) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        particleTasks.clear();
    }
}

