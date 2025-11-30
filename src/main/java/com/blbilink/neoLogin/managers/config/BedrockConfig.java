package com.blbilink.neoLogin.managers.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * 基岩版配置类，管理基岩版玩家相关配置项
 */
public class BedrockConfig {

    private boolean enabled;
    private boolean autoLoginByFloodgate;
    private boolean autoLoginByUUID;
    private boolean autoLoginByPrefix;
    private String prefix;
    private boolean formsEnabled;

    /**
     * 从配置文件加载基岩版配置
     *
     * @param config 配置文件对象
     */
    public void load(FileConfiguration config) {
        enabled = config.getBoolean("bedrock.enabled", false);
        autoLoginByFloodgate = config.getBoolean("bedrock.autologin.floodgate", false);
        autoLoginByUUID = config.getBoolean("bedrock.autologin.uuid", false);
        autoLoginByPrefix = config.getBoolean("bedrock.autologin.prefix", false);
        prefix = config.getString("bedrock.autologin.prefix_value", "*");
        formsEnabled = config.getBoolean("bedrock.forms", true);
    }

    // Getters
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAutoLoginByFloodgate() {
        return autoLoginByFloodgate;
    }

    public boolean isAutoLoginByUUID() {
        return autoLoginByUUID;
    }

    public boolean isAutoLoginByPrefix() {
        return autoLoginByPrefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isFormsEnabled() {
        return formsEnabled;
    }
}

