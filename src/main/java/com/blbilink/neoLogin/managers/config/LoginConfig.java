package com.blbilink.neoLogin.managers.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class LoginConfig {
    private ConfigurationSection loginSend;

    public void load(FileConfiguration config) {
        loginSend = config.getConfigurationSection("login.send");
    }

    public ConfigurationSection getLoginSend() {
        return loginSend;
    }
}
