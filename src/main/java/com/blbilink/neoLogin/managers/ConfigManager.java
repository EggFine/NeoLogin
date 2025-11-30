package com.blbilink.neoLogin.managers;

import com.blbilink.neoLibrary.utils.ConfigUtil;
import com.blbilink.neoLogin.managers.config.*;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * 配置管理器，负责协调各个配置模块
 * 重构后的版本，将配置按功能模块分离，提高代码可维护性
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    // 配置模块
    private final BaseConfig baseConfig;
    private final DatabaseConfig databaseConfig;
    private final AutoTeleportConfig autoTeleportConfig;
    private final RegisterConfig registerConfig;
    private final PlayerLimitConfig playerLimitConfig;
    private final LoginConfig loginConfig;
    private final BedrockConfig bedrockConfig;
    private final ParticleConfig particleConfig;

    /**
     * 构造函数，初始化时会自动加载和解析配置文件。
     * 
     * @param plugin 插件主类的实例
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        // 使用 NeoLibrary 的 ConfigUtil 来加载和自动更新 config.yml
        ConfigUtil configUtil = new ConfigUtil(plugin, "config.yml");
        this.config = configUtil.getConfig();
        
        // 初始化配置模块
        this.baseConfig = new BaseConfig();
        this.databaseConfig = new DatabaseConfig();
        this.autoTeleportConfig = new AutoTeleportConfig(plugin);
        this.registerConfig = new RegisterConfig();
        this.playerLimitConfig = new PlayerLimitConfig();
        this.loginConfig = new LoginConfig();
        this.bedrockConfig = new BedrockConfig();
        this.particleConfig = new ParticleConfig();

        // 加载并缓存所有配置值
        loadAndCacheValues();
    }

    /**
     * 从 FileConfiguration 对象中读取所有配置项并缓存到各配置模块中。
     */
    public void loadAndCacheValues() {
        // 重新加载配置文件
        ConfigUtil configUtil = new ConfigUtil(plugin, "config.yml");
        this.config = configUtil.getConfig();
        
        baseConfig.load(config);
        databaseConfig.load(config);
        autoTeleportConfig.load(config);
        registerConfig.load(config);
        playerLimitConfig.load(config);
        loginConfig.load(config);
        bedrockConfig.load(config);
        particleConfig.load(config);
    }

    // --- 基础配置 Getters ---
    public String getPrefix() {
        return baseConfig.getPrefix();
    }

    public String getLanguage() {
        return baseConfig.getLanguage();
    }

    // --- 数据库配置 Getters ---
    /**
     * 获取数据库配置段。
     * <p>
     * 可以将此对象直接传递给 NeoLibrary 的 DatabaseUtil 进行初始化。
     * 例如:
     * {@code new DatabaseUtil(plugin).initialize(configManager.getDatabaseSection());}
     *
     * @return 数据库配置段 (ConfigurationSection)
     */
    public ConfigurationSection getDatabaseSection() {
        return databaseConfig.getDatabaseSection();
    }

    // --- 自动传送配置 Getters ---
    public boolean isAutoTeleportEnabled() {
        return autoTeleportConfig.isAutoTeleportEnabled();
    }

    public boolean isAutoTeleportOnJoin() {
        return autoTeleportConfig.isAutoTeleportOnJoin();
    }

    public boolean isAutoTeleportOnDeath() {
        return autoTeleportConfig.isAutoTeleportOnDeath();
    }

    public Location getTeleportLocation() {
        return autoTeleportConfig.getTeleportLocation();
    }

    public boolean isAutoTeleportBack() {
        return autoTeleportConfig.isAutoTeleportBack();
    }

    // --- 注册配置 Getters ---
    public boolean isRegisterEnabled() {
        return registerConfig.isRegisterEnabled();
    }

    public int getRegisterPasswordLength() {
        return registerConfig.getRegisterPasswordLength();
    }

    public int getRegisterPasswordMinLength() {
        return registerConfig.getRegisterPasswordMinLength();
    }

    public boolean isRegisterConfirmPassword() {
        return registerConfig.isRegisterConfirmPassword();
    }

    public boolean isRegisterAutoLogin() {
        return registerConfig.isRegisterAutoLogin();
    }

    public boolean isRegisterReward() {
        return registerConfig.isRegisterReward();
    }

    public ConfigurationSection getRegisterSend() {
        return registerConfig.getRegisterSend();
    }

    // --- 登录配置 Getters ---
    public ConfigurationSection getLoginSend() {
        return loginConfig.getLoginSend();
    }

    // --- 玩家限制配置 Getters ---
    public boolean isNotLoggedInLimitEnabled() {
        return playerLimitConfig.isNotLoggedInLimitEnabled();
    }

    public boolean isLimitMove() {
        return playerLimitConfig.isLimitMove();
    }

    public boolean isLimitBlockPlace() {
        return playerLimitConfig.isLimitBlockPlace();
    }

    public boolean isLimitBlockBreak() {
        return playerLimitConfig.isLimitBlockBreak();
    }

    public boolean isLimitBlockInteract() {
        return playerLimitConfig.isLimitBlockInteract();
    }

    public boolean isLimitChat() {
        return playerLimitConfig.isLimitChat();
    }

    public boolean isLimitCommand() {
        return playerLimitConfig.isLimitCommand();
    }

    public List<String> getCommandWhitelist() {
        return playerLimitConfig.getCommandWhitelist();
    }

    public boolean isLimitItemUse() {
        return playerLimitConfig.isLimitItemUse();
    }

    public boolean isLimitDamage() {
        return playerLimitConfig.isLimitDamage();
    }

    public boolean isLimitAttacking() {
        return playerLimitConfig.isLimitAttacking();
    }

    // --- 配置模块直接访问方法（如需要） ---
    /**
     * 获取基础配置模块实例
     */
    public BaseConfig getBaseConfig() {
        return baseConfig;
    }

    /**
     * 获取数据库配置模块实例
     */
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    /**
     * 获取自动传送配置模块实例
     */
    public AutoTeleportConfig getAutoTeleportConfig() {
        return autoTeleportConfig;
    }

    /**
     * 获取注册配置模块实例
     */
    public RegisterConfig getRegisterConfig() {
        return registerConfig;
    }

    /**
     * 获取玩家限制配置模块实例
     */
    public PlayerLimitConfig getPlayerLimitConfig() {
        return playerLimitConfig;
    }

    /**
     * 获取登录配置模块实例
     */
    public LoginConfig getLoginConfig() {
        return loginConfig;
    }

    /**
     * 获取基岩版配置模块实例
     */
    public BedrockConfig getBedrockConfig() {
        return bedrockConfig;
    }

    /**
     * 获取粒子效果配置模块实例
     */
    public ParticleConfig getParticleConfig() {
        return particleConfig;
    }

    // --- 基岩版配置 Getters ---
    public boolean isBedrockEnabled() {
        return bedrockConfig.isEnabled();
    }

    public boolean isBedrockAutoLoginByFloodgate() {
        return bedrockConfig.isAutoLoginByFloodgate();
    }

    public boolean isBedrockAutoLoginByUUID() {
        return bedrockConfig.isAutoLoginByUUID();
    }

    public boolean isBedrockAutoLoginByPrefix() {
        return bedrockConfig.isAutoLoginByPrefix();
    }

    public String getBedrockPrefix() {
        return bedrockConfig.getPrefix();
    }

    public boolean isBedrockFormsEnabled() {
        return bedrockConfig.isFormsEnabled();
    }

    // --- 粒子效果配置 Getters ---
    public boolean isParticleEnabled() {
        return particleConfig.isEnabled();
    }

    public String getParticleType() {
        return particleConfig.getParticleType();
    }
}