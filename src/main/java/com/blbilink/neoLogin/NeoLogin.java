package com.blbilink.neoLogin;

import com.blbilink.neoLibrary.utils.FoliaUtil;
import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLibrary.utils.Metrics;
import com.blbilink.neoLibrary.utils.TextUtil;
import com.blbilink.neoLogin.commands.LoginCommand;
import com.blbilink.neoLogin.commands.NeoLoginCommand;
import com.blbilink.neoLogin.commands.RegisterCommand;
import com.blbilink.neoLogin.commands.ResetPasswordCommand;
import com.blbilink.neoLogin.dao.UserDAO;
import com.blbilink.neoLogin.effects.ParticleEffect;
import com.blbilink.neoLogin.listeners.AutoTeleportListener;
import com.blbilink.neoLogin.listeners.FloodgateListener;
import com.blbilink.neoLogin.listeners.LoginReminderListener;
import com.blbilink.neoLogin.listeners.PlayerChatListener;
import com.blbilink.neoLogin.listeners.PlayerCommandListener;
import com.blbilink.neoLogin.listeners.PlayerConnectListener;
import com.blbilink.neoLogin.listeners.PlayerDamageListener;
import com.blbilink.neoLogin.listeners.PlayerInteractionListener;
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
    private DatabaseManager databaseManager;
    private ParticleEffect particleEffect;
    private FloodgateListener floodgateListener;

    @Override
    public void onEnable() {
        getLogger().info(TextUtil.getLogo("Loading...", "NeoLogin",
                "The Next Generation blbiLogin\nSpigotMC: https://www.spigotmc.org/resources/125813/", this,
                Collections.singletonList("EggFine"), null));

        // bStats Metrics
        new Metrics(this, 26113);

        // Folia 兼容性
        foliaUtil = new FoliaUtil(this);

        // 初始化配置管理器
        configManager = new ConfigManager(this);

        // 初始化多语言管理器
        i18n = new I18n(this, configManager.getPrefix(), configManager.getLanguage());
        i18n.loadLanguage();

        // 初始化数据库
        databaseManager = new DatabaseManager(this);
        databaseManager.init();

        // 初始化用户数据访问对象（使用已初始化的数据库管理器）
        userDAO = new UserDAO(databaseManager);

        // 初始化玩家登录状态管理器
        playerManager = new PlayerManager(this);

        // 注册事件监听器
        registerListeners();

        // 注册命令
        registerCommands();
    }

    private void registerListeners() {
        // 注册基础监听器
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new AutoTeleportListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectListener(this), this);
        
        // 注册登录提醒监听器并添加登录成功回调
        LoginReminderListener loginReminderListener = new LoginReminderListener(this);
        getServer().getPluginManager().registerEvents(loginReminderListener, this);
        playerManager.registerLoginCallback(loginReminderListener::onPlayerLoggedIn);
        
        // PlayerAttackListener 功能已合并到 PlayerInteractionListener，不再单独注册
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);

        // 注册新增的监听器
        getServer().getPluginManager().registerEvents(new PlayerInteractionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandListener(this), this);

        // 注册基岩版支持监听器
        floodgateListener = new FloodgateListener(this);
        getServer().getPluginManager().registerEvents(floodgateListener, this);

        // 注册粒子效果监听器并添加登录成功回调
        particleEffect = new ParticleEffect(this);
        getServer().getPluginManager().registerEvents(particleEffect, this);
        playerManager.registerLoginCallback(particleEffect::onPlayerLoggedIn);
    }

    private void registerCommands() {
        // 注册基础命令
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("register").setExecutor(new RegisterCommand(this));

        // 注册新增命令
        NeoLoginCommand neoLoginCommand = new NeoLoginCommand(this);
        getCommand("neologin").setExecutor(neoLoginCommand);
        getCommand("neologin").setTabCompleter(neoLoginCommand);

        ResetPasswordCommand resetPasswordCommand = new ResetPasswordCommand(this);
        getCommand("resetpassword").setExecutor(resetPasswordCommand);
        getCommand("resetpassword").setTabCompleter(resetPasswordCommand);
    }

    @Override
    public void onDisable() {
        // 清理粒子效果任务
        if (particleEffect != null) {
            particleEffect.cleanup();
        }

        // 清理玩家管理器中的数据
        if (playerManager != null) {
            playerManager.clearLoggedInPlayers();
        }
        
        // 关闭数据库连接池
        if (databaseManager != null) {
            databaseManager.close();
        }
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

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ParticleEffect getParticleEffect() {
        return particleEffect;
    }

    public FloodgateListener getFloodgateListener() {
        return floodgateListener;
    }
}