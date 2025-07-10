package com.blbilink.neoLogin.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.dao.UserDAO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 玩家登录状态管理器
 * <p>
 * 该类用于跟踪已登录和未登录的玩家。
 * 使用 UUID 来存储玩家信息，以避免因玩家对象被不当持有而导致的内存泄漏。
 */
public class PlayerManager {
    private final I18n i18n;
    private final UserDAO userDAO;

    // 使用 Set 存储已登录玩家的 UUID，提供 O(1) 的平均时间复杂度用于增删查操作。
    private final Set<UUID> loggedInPlayers = new HashSet<>();

    // 用于缓存玩家原始位置的 Map
    private final Map<UUID, Location> originalLocations = new HashMap<>();

    public PlayerManager(NeoLogin plugin) {
        this.i18n = plugin.getI18n();
        this.userDAO = plugin.getUserDAO();
    }

    /**
     * 将玩家标记为已登录状态。
     *
     * @param player 要标记的玩家对象
     */
    public void setLoggedIn(Player player) {
        loggedInPlayers.add(player.getUniqueId());
    }

    /**
     * 将玩家标记为未登录状态（例如，当玩家退出服务器时）。
     *
     * @param player 要标记的玩家对象
     */
    public void setLoggedOut(Player player) {
        loggedInPlayers.remove(player.getUniqueId());
    }

    /**
     * 检查一个玩家是否已经登录。
     *
     * @param player 要检查的玩家对象
     * @return 如果玩家已登录，则返回 true，否则返回 false
     */
    public boolean isLoggedIn(Player player) {
        return loggedInPlayers.contains(player.getUniqueId());
    }

    /**
     * 检查玩家是否已注册
     * @param player 要检查的玩家对象
     * @return 如果玩家已注册，则返回 true，否则返回 false
     */
    public boolean isRegistered(Player player) {
        return userDAO.isRegistered(player.getUniqueId());
    }

    /**
     * 在插件卸载时，清空所有已登录玩家的记录。
     * 这可以防止在插件重载时出现状态不一致的问题。
     */
    public void clearLoggedInPlayers() {
        loggedInPlayers.clear();
    }

    /**
     * 缓存玩家的原始位置。
     * 这应该在玩家因未登录而被传送到登录点之前调用。
     * @param uuid 玩家的 UUID
     * @param location 要缓存的原始位置
     */
    public void cacheOriginalLocation(UUID uuid, Location location) {
        originalLocations.put(uuid, location);
    }

    /**
     * 获取并移除玩家缓存的原始位置。
     * 使用 "get and remove" 的模式可以确保该位置只被使用一次。
     * @param uuid 玩家的 UUID
     * @return 玩家的原始位置，如果没有缓存则返回 null
     */
    public Location getAndRemoveOriginalLocation(UUID uuid) {
        return originalLocations.remove(uuid);
    }

    /**
     * 给予注册成功的玩家物品
     * @param player 玩家对象
     */
    public void giveRegisterReward(Player player) {
        Material material = Material.NETHERITE_INGOT;
        int amount = 20;
        player.getInventory().addItem(new ItemStack(material, amount));
        player.sendMessage("§a注册成功，获得 "+ amount +" 个"+ material.name());
        player.sendMessage(i18n.as("register.reward", true));
    }
}
