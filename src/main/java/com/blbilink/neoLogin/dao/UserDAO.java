package com.blbilink.neoLogin.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.blbilink.neoLogin.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Data Access Object for user data.
 * 封装了所有对用户数据的数据库操作。
 */
public class UserDAO {

    private final DatabaseManager databaseManager;

    public UserDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * 检查玩家是否已注册。
     * @param uuid 玩家的UUID
     * @return 如果已注册则返回 true
     */
    public boolean isRegistered(UUID uuid) {
        String sql = "SELECT 1 FROM neologin_users WHERE uuid = ? LIMIT 1;";
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 注册一个新用户。
     * @param uuid 玩家UUID
     * @param username 玩家名
     * @param password 明文密码
     * @return 注册成功返回 true
     */
    public boolean registerUser(UUID uuid, String username, String password, String ipAddress) {
        // 使用 BCrypt 进行密码哈希
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        String sql = "INSERT INTO neologin_users (uuid, username, password_hash, ip_address, registration_date) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, username);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, ipAddress);
            pstmt.setLong(5, System.currentTimeMillis());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 验证玩家的密码并更新登录信息。
     * @param uuid 玩家UUID
     * @param password 尝试登录的明文密码
     * @return 密码正确返回 true
     */
    public boolean verifyPassword(UUID uuid, String password, String ipAddress) {
        String sqlSelect = "SELECT password_hash FROM neologin_users WHERE uuid = ?;";
        String storedHash;

        // 1. 获取哈希
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    storedHash = rs.getString("password_hash");
                } else {
                    return false; // 用户不存在
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // 2. 验证密码
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHash);
        if (result.verified) {
            // 3. 密码正确，异步更新最后登录时间和IP
            updateLastLoginAsync(uuid, ipAddress);
        }
        return result.verified;
    }

    /**
     * 异步更新玩家的最后登录时间和IP地址。
     */
    private void updateLastLoginAsync(UUID uuid, String ipAddress) {
        String sqlUpdate = "UPDATE neologin_users SET last_login_date = ?, ip_address = ? WHERE uuid = ?;";
        databaseManager.getDatabaseUtil().executeUpdateAsync(sqlUpdate, System.currentTimeMillis(), ipAddress, uuid.toString());
    }
}