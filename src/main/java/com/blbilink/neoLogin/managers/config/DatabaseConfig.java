package com.blbilink.neoLogin.managers.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 数据库配置类，管理数据库相关配置项
 */
public class DatabaseConfig {

    private ConfigurationSection databaseSection;

    /**
     * 从配置文件加载数据库配置
     *
     * @param config 配置文件对象
     */
    public void load(FileConfiguration config) {
        databaseSection = config.getConfigurationSection("database");
    }

    /**
     * 获取数据库配置段。
     * <p>
     * 可以将此对象直接传递给 NeoLibrary 的 DatabaseUtil 进行初始化。
     * 例如:
     * {@code new DatabaseUtil(plugin).initialize(databaseConfig.getDatabaseSection());}
     *
     * @return 数据库配置段 (ConfigurationSection)
     */
    public ConfigurationSection getDatabaseSection() {
        return databaseSection;
    }
} 