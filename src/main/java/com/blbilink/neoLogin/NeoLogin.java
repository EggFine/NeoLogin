package com.blbilink.neoLogin;

import com.blbilink.neoLibrary.utils.FoliaUtil;
import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLibrary.utils.Metrics;
import com.blbilink.neoLibrary.utils.TextUtil;
import com.blbilink.neoLogin.dao.UserDAO;
import com.blbilink.neoLogin.listeners.AutoTeleportListener;
import com.blbilink.neoLogin.listeners.LoginReminderListener;
import com.blbilink.neoLogin.listeners.PlayerConnectListener;
import com.blbilink.neoLogin.listeners.PlayerMoveListener;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.DatabaseManager;
import com.blbilink.neoLogin.managers.PlayerManager;

import java.util.Collections;

import org.bukkit.plugin.java.JavaPlugin;

public final class NeoLogin extends JavaPlugin {

    private FoliaUtil foliaUtil;
    private I18n i18n;
    private ConfigManager configManager;
    private PlayerManager playerManager;
    private UserDAO userDAO;

    @Override
    public void onEnable() {
        getLogger().info(TextUtil.getLogo("Loading...", "NeoLogin", "The Next Generation blbiLogin\nSpigotMC: https://www.spigotmc.org/resources/125813/", this, Collections.singletonList("EggFine"), null));

        // bStats Metrics
        new Metrics(this, 26113);

        // Folia 兼容性
        foliaUtil = new FoliaUtil(this);

        // 初始化配置管理器
        configManager = new ConfigManager(this);

        // 初始化多语言管理器
        i18n = new I18n(this, configManager.getPrefix(), configManager.getLanguage());
        i18n.loadLanguage();

        // 初始化玩家登录状态管理器
        playerManager = new PlayerManager();

        // 初始化用户数据访问对象
        userDAO = new UserDAO(new DatabaseManager(this));

        // 注册事件监听器
        registerListeners();
    }

    private void registerListeners() {
        // 注册监听器
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new AutoTeleportListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectListener(this), this);
        getServer().getPluginManager().registerEvents(new LoginReminderListener(this), this);       
    }

    @Override
    public void onDisable() {
        // 插件关闭逻辑
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public I18n getI18n() {
        return i18n;
    }
    
    public FoliaUtil getFoliaUtil() {
        return foliaUtil;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }


}