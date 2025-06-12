package com.blbilink.neoLogin.managers;

import com.blbilink.neoLibrary.utils.ConfigUtil; //
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class ConfigManager {

    private final JavaPlugin plugin;
    private final FileConfiguration config;

    // General
    private String prefix;
    private String language;

    // Database
    private ConfigurationSection databaseSection;

    // Auto Teleport
    private boolean autoTeleportEnabled;
    private boolean autoTeleportOnJoin;
    private boolean autoTeleportOnDeath;
    private Location teleportLocation;
    private boolean autoTeleportBack;

    // Not Logged In Player Limit
    private boolean notLoggedInLimitEnabled;
    private boolean limitMove;
    private boolean limitBlockPlace;
    private boolean limitBlockBreak;
    private boolean limitBlockInteract;
    private boolean limitChat;
    private boolean limitCommand;
    private List<String> commandWhitelist;
    private boolean limitItemUse;

    /**
     * 构造函数，初始化时会自动加载和解析配置文件。
     * @param plugin 插件主类的实例
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        // 使用 NeoLibrary 的 ConfigUtil 来加载和自动更新 config.yml
        ConfigUtil configUtil = new ConfigUtil(plugin, "config.yml");
        this.config = configUtil.getConfig();
        // 加载并缓存所有配置值
        loadAndCacheValues();
    }

    /**
     * 从 FileConfiguration 对象中读取所有配置项并缓存到类的字段中。
     */
    public void loadAndCacheValues() {
        // [插件配置]
        prefix = config.getString("prefix", "§8[§fNeo§bLogin§8] §f");
        language = config.getString("language", "zh_CN");

        // [数据库配置]
        // 获取数据库的整个配置段，方便后续传递给 DatabaseUtil
        databaseSection = config.getConfigurationSection("database"); 

        // [自动传送配置]
        autoTeleportEnabled = config.getBoolean("autoTeleport.enabled", false);
        autoTeleportOnJoin = config.getBoolean("autoTeleport.on.join", false);
        autoTeleportOnDeath = config.getBoolean("autoTeleport.on.death", false);
        loadTeleportLocation();
        autoTeleportBack = config.getBoolean("autoTeleport.playerJoinTp_AutoBack", false);

        // [未登录玩家限制配置]
        notLoggedInLimitEnabled = config.getBoolean("notLoggedInPlayerLimit.enabled", true);
        limitMove = config.getBoolean("notLoggedInPlayerLimit.type.move", true);
        limitBlockPlace = config.getBoolean("notLoggedInPlayerLimit.type.blockPlace", true);
        limitBlockBreak = config.getBoolean("notLoggedInPlayerLimit.type.blockBreak", true);
        limitBlockInteract = config.getBoolean("notLoggedInPlayerLimit.type.blockInteract", true);
        limitChat = config.getBoolean("notLoggedInPlayerLimit.type.chat", true);
        limitCommand = config.getBoolean("notLoggedInPlayerLimit.type.command", true);
        commandWhitelist = config.getStringList("notLoggedInPlayerLimit.type.commandWhitelist");
        limitItemUse = config.getBoolean("notLoggedInPlayerLimit.type.itemUse", true);
    }

    /**
     * 解析配置文件中的坐标信息，并转换成 Bukkit 的 Location 对象。
     */
    private void loadTeleportLocation() {
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
    
    // --- Getters ---
    // 通过这些方法，插件的其他部分可以安全地获取配置值

    public String getPrefix() { return prefix; }
    public String getLanguage() { return language; }

    /**
     * 获取数据库配置段。
     * <p>
     * 你可以将此对象直接传递给 NeoLibrary 的 DatabaseUtil 进行初始化。
     * 例如: {@code new DatabaseUtil(plugin).initialize(configManager.getDatabaseSection());}
     *
     * @return 数据库配置段 (ConfigurationSection)
     */
    public ConfigurationSection getDatabaseSection() {
        return databaseSection;
    }

    public boolean isAutoTeleportEnabled() { return autoTeleportEnabled; }
    public boolean isAutoTeleportOnJoin() { return autoTeleportOnJoin; }
    public boolean isAutoTeleportOnDeath() { return autoTeleportOnDeath; }
    public Location getTeleportLocation() { return teleportLocation; }
    public boolean isAutoTeleportBack() { return autoTeleportBack; }
    public boolean isNotLoggedInLimitEnabled() { return notLoggedInLimitEnabled; }
    public boolean isLimitMove() { return limitMove; }
    public boolean isLimitBlockPlace() { return limitBlockPlace; }
    public boolean isLimitBlockBreak() { return limitBlockBreak; }
    public boolean isLimitBlockInteract() { return limitBlockInteract; }
    public boolean isLimitChat() { return limitChat; }
    public boolean isLimitCommand() { return limitCommand; }
    public List<String> getCommandWhitelist() { return Collections.unmodifiableList(commandWhitelist); } // 返回一个不可修改的列表，更安全
    public boolean isLimitItemUse() { return limitItemUse; }
}