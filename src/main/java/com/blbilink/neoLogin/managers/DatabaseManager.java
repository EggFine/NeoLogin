package com.blbilink.neoLogin.managers;

import com.blbilink.neoLibrary.utils.DatabaseUtil;
import com.blbilink.neoLogin.NeoLogin;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private final NeoLogin plugin;
    private final ConfigManager configManager;
    private DatabaseUtil databaseUtil;

    public DatabaseManager(NeoLogin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    /**
     * 初始化数据库连接并创建所需的数据表。
     */
    public void init() {
        ConfigurationSection dbConfig = configManager.getDatabaseSection();
        if (dbConfig == null) {
            plugin.getLogger().severe("在 config.yml 中找不到 'database' 配置项！数据库功能将无法使用。");
            return;
        }

        String dbType = dbConfig.getString("type", "sqlite");
        plugin.getLogger().info("正在初始化数据库, 类型: " + dbType + "...");

        this.databaseUtil = new DatabaseUtil(plugin);

        // 修正 1: initialize() 返回布尔值，不抛出异常
        boolean success = databaseUtil.initialize(dbConfig);

        if (success) {
            plugin.getLogger().info("数据库连接池初始化成功！");
            // 初始化成功后，创建数据表
            setupTables();
        } else {
            plugin.getLogger().severe("数据库初始化失败！请检查您的 config.yml 配置和数据库驱动。");
        }
    }

    /**
     * 创建插件所需的用户数据表。
     */
    private void setupTables() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS neologin_users ("
                + "uuid VARCHAR(36) NOT NULL PRIMARY KEY,"
                + "username VARCHAR(16) NOT NULL,"
                + "password_hash VARCHAR(255) NOT NULL,"
                + "ip_address VARCHAR(45),"
                + "registration_date BIGINT NOT NULL,"
                + "last_login_date BIGINT"
                + ");";
        try {
            // 修正 2: 使用 executeUpdate 方法
            databaseUtil.executeUpdate(createTableSQL);
            plugin.getLogger().info("数据表结构已验证/创建。");
        } catch (SQLException e) {
            plugin.getLogger().severe("创建数据表时出错！");
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库连接池。
     * 应在插件卸载时调用。
     */
    public void close() {
        if (databaseUtil != null) {
            // 修正 3: 使用 close 方法
            databaseUtil.close();
        }
    }

    /**
     * 从连接池获取一个数据库连接。
     * <p>
     * <b>重要提示：</b> 推荐使用 try-with-resources 语句来确保连接被正确关闭和归还。
     * </p>
     *
     * @return 数据库连接对象, 如果发生错误则返回 null
     */
    public Connection getConnection() {
        // 增加 isInitialized() 判断，更加健壮
        if (databaseUtil == null || !databaseUtil.isInitialized()) {
            plugin.getLogger().severe("数据库未初始化或已关闭，无法获取连接！");
            return null;
        }
        try {
            return databaseUtil.getConnection();
        } catch (SQLException e) {
            plugin.getLogger().severe("无法从连接池获取数据库连接！");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 DatabaseUtil 的实例，方便在其他地方直接调用其高级功能（如异步方法）。
     *
     * @return DatabaseUtil 实例，如果未初始化则可能为 null
     */
    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }
}