package com.blbilink.neoLogin.managers.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册配置类，管理玩家注册相关配置项
 */
public class RegisterConfig {

    private boolean registerEnabled;
    private int registerPasswordLength;
    private int registerPasswordMinLength;
    private boolean registerConfirmPassword;
    private boolean registerAutoLogin;
    private boolean registerGiveReward;
    private ConfigurationSection registerSend;
    
    // 奖励配置
    private List<String> rewardItems;
    private int rewardExperience;
    private List<String> rewardPlayerCommands;
    private List<String> rewardConsoleCommands;

    /**
     * 从配置文件加载注册配置
     *
     * @param config 配置文件对象
     */
    public void load(FileConfiguration config) {
        registerEnabled = config.getBoolean("register.enabled", true);
        registerPasswordLength = config.getInt("register.passwordLength", 15);
        registerPasswordMinLength = config.getInt("register.passwordMinLength", 1);
        registerConfirmPassword = config.getBoolean("register.confirmPassword", true);
        registerAutoLogin = config.getBoolean("register.autoLogin", true);
        registerGiveReward = config.getBoolean("register.reward.enable", false);
        registerSend = config.getConfigurationSection("register.send");
        
        // 加载奖励配置
        rewardItems = config.getStringList("register.reward.items");
        if (rewardItems == null) {
            rewardItems = new ArrayList<>();
        }
        rewardExperience = config.getInt("register.reward.experience", 0);
        rewardPlayerCommands = config.getStringList("register.reward.playerCommands");
        if (rewardPlayerCommands == null) {
            rewardPlayerCommands = new ArrayList<>();
        }
        rewardConsoleCommands = config.getStringList("register.reward.consoleCommands");
        if (rewardConsoleCommands == null) {
            rewardConsoleCommands = new ArrayList<>();
        }
    }

    // Getters
    public boolean isRegisterEnabled() {
        return registerEnabled;
    }

    public int getRegisterPasswordLength() {
        return registerPasswordLength;
    }

    public int getRegisterPasswordMinLength() {
        return registerPasswordMinLength;
    }

    public boolean isRegisterConfirmPassword() {
        return registerConfirmPassword;
    }

    public boolean isRegisterAutoLogin() {
        return registerAutoLogin;
    }

    public boolean isRegisterReward() {
        return registerGiveReward;
    }

    public ConfigurationSection getRegisterSend() {
        return registerSend;
    }
    
    public List<String> getRewardItems() {
        return rewardItems;
    }
    
    public int getRewardExperience() {
        return rewardExperience;
    }
    
    public List<String> getRewardPlayerCommands() {
        return rewardPlayerCommands;
    }
    
    public List<String> getRewardConsoleCommands() {
        return rewardConsoleCommands;
    }
} 