package com.blbilink.neoLogin.managers.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 自动传送配置类，管理自动传送相关配置项
 */
public class AutoTeleportConfig {

    private final JavaPlugin plugin;
    
    private boolean autoTeleportEnabled;
    private boolean autoTeleportOnJoin;
    private boolean autoTeleportOnDeath;
    private Location teleportLocation;
    private boolean autoTeleportBack;

    public AutoTeleportConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 从配置文件加载自动传送配置
     *
     * @param config 配置文件对象
     */
    public void load(FileConfiguration config) {
        autoTeleportEnabled = config.getBoolean("autoTeleport.enabled", false);
        autoTeleportOnJoin = config.getBoolean("autoTeleport.on.join", false);
        autoTeleportOnDeath = config.getBoolean("autoTeleport.on.death", false);
        loadTeleportLocation(config);
        autoTeleportBack = config.getBoolean("autoTeleport.playerJoinTp_AutoBack", false);
    }

    /**
     * 解析配置文件中的坐标信息，并转换成 Bukkit 的 Location 对象。
     */
    private void loadTeleportLocation(FileConfiguration config) {
        String worldName = config.getString("autoTeleport.locationPos.world");
        // 检查世界名称是否有效
        if (worldName == null || worldName.isEmpty()) {
            teleportLocation = null;
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("自动传送功能设置的世界 '" + worldName + "' 不存在或未加载！该功能可能无法正常工作。");
            teleportLocation = null;
            return;
        }

        double x = config.getDouble("autoTeleport.locationPos.x");
        double y = config.getDouble("autoTeleport.locationPos.y");
        double z = config.getDouble("autoTeleport.locationPos.z");
        float yaw = (float) config.getDouble("autoTeleport.locationPos.yaw");
        float pitch = (float) config.getDouble("autoTeleport.locationPos.pitch");
        teleportLocation = new Location(world, x, y, z, yaw, pitch);
    }

    // Getters
    public boolean isAutoTeleportEnabled() {
        return autoTeleportEnabled;
    }

    public boolean isAutoTeleportOnJoin() {
        return autoTeleportOnJoin;
    }

    public boolean isAutoTeleportOnDeath() {
        return autoTeleportOnDeath;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public boolean isAutoTeleportBack() {
        return autoTeleportBack;
    }
} 