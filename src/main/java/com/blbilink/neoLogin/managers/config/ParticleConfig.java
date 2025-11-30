package com.blbilink.neoLogin.managers.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * 粒子效果配置类，管理未登录玩家的粒子效果配置项
 */
public class ParticleConfig {

    private boolean enabled;
    private String particleType;

    /**
     * 从配置文件加载粒子效果配置
     *
     * @param config 配置文件对象
     */
    public void load(FileConfiguration config) {
        enabled = config.getBoolean("particle.enabled", false);
        particleType = config.getString("particle.type", "FLAME");
    }

    // Getters
    public boolean isEnabled() {
        return enabled;
    }

    public String getParticleType() {
        return particleType;
    }
}

