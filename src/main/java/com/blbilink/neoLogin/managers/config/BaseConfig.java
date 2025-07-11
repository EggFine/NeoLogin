package com.blbilink.neoLogin.managers.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * 基础配置类，管理插件的通用配置项
 */
public class BaseConfig {

    private String prefix;
    private String language;

    /**
     * 从配置文件加载基础配置
     *
     * @param config 配置文件对象
     */
    public void load(FileConfiguration config) {
        prefix = config.getString("prefix", "§8[§fNeo§bLogin§8] §f");
        language = config.getString("language", "zh_CN");
    }

    // Getters
    public String getPrefix() {
        return prefix;
    }

    public String getLanguage() {
        return language;
    }
} 