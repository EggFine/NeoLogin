package com.blbilink.neoLogin.managers;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 玩家登录状态管理器
 * <p>
 * 该类用于跟踪已登录和未登录的玩家。
 * 使用 UUID 来存储玩家信息，以避免因玩家对象被不当持有而导致的内存泄漏。
 */
public class PlayerManager {

    // 使用 Set 存储已登录玩家的 UUID，提供 O(1) 的平均时间复杂度用于增删查操作。
    private final Set<UUID> loggedInPlayers = new HashSet<>();

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
     * 在插件卸载时，清空所有已登录玩家的记录。
     * 这可以防止在插件重载时出现状态不一致的问题。
     */
    public void clearLoggedInPlayers() {
        loggedInPlayers.clear();
    }
}
