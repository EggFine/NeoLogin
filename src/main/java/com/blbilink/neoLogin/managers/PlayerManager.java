package com.blbilink.neoLogin.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.dao.UserDAO;
import com.blbilink.neoLogin.managers.config.RegisterConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 玩家登录状态管理器
 * <p>
 * 该类用于跟踪已登录和未登录的玩家。
 * 使用 UUID 来存储玩家信息，以避免因玩家对象被不当持有而导致的内存泄漏。
 */
public class PlayerManager {
    private final NeoLogin plugin;
    private final I18n i18n;
    private final UserDAO userDAO;

    // 使用线程安全的 Set 存储已登录玩家的 UUID，提供 O(1) 的平均时间复杂度用于增删查操作。
    // 使用 ConcurrentHashMap.newKeySet() 以支持异步任务中的并发访问
    private final Set<UUID> loggedInPlayers = ConcurrentHashMap.newKeySet();

    // 用于缓存玩家原始位置的 Map，使用 ConcurrentHashMap 以支持并发访问
    private final Map<UUID, Location> originalLocations = new ConcurrentHashMap<>();

    // 用于缓存玩家登录前的飞行状态，使用 ConcurrentHashMap 以支持并发访问
    private final Set<UUID> allowFlightPlayers = ConcurrentHashMap.newKeySet();

    // 登录成功时的回调列表
    private final List<Consumer<Player>> loginCallbacks = new ArrayList<>();

    public PlayerManager(NeoLogin plugin) {
        this.plugin = plugin;
        this.i18n = plugin.getI18n();
        this.userDAO = plugin.getUserDAO();
    }

    /**
     * 注册登录成功时的回调
     * @param callback 回调函数
     */
    public void registerLoginCallback(Consumer<Player> callback) {
        loginCallbacks.add(callback);
    }

    /**
     * 将玩家标记为已登录状态。
     *
     * @param player 要标记的玩家对象
     */
    public void setLoggedIn(Player player) {
        loggedInPlayers.add(player.getUniqueId());
        // 触发所有登录成功回调
        for (Consumer<Player> callback : loginCallbacks) {
            try {
                callback.accept(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
     * 缓存玩家的飞行状态。
     * 这应该在玩家因未登录而被设置飞行之前调用。
     * @param uuid 玩家的 UUID
     * @param allowFlight 玩家是否允许飞行
     */
    public void cacheAllowFlight(UUID uuid, boolean allowFlight) {
        if (allowFlight) {
            allowFlightPlayers.add(uuid);
        }
    }

    /**
     * 获取并移除玩家缓存的飞行状态。
     * @param uuid 玩家的 UUID
     * @return 玩家是否原本允许飞行
     */
    public boolean getAndRemoveAllowFlight(UUID uuid) {
        return allowFlightPlayers.remove(uuid);
    }

    /**
     * 恢复玩家的飞行状态。
     * 如果玩家登录前允许飞行，则恢复飞行权限；否则禁用飞行。
     * 创造模式和OP玩家不受影响。
     * @param player 玩家对象
     */
    public void restoreFlightState(Player player) {
        // 创造模式和OP玩家不需要处理
        if (player.getGameMode() == org.bukkit.GameMode.CREATIVE || player.isOp()) {
            return;
        }

        boolean wasAllowedToFly = getAndRemoveAllowFlight(player.getUniqueId());
        if (wasAllowedToFly) {
            // 恢复飞行权限
            player.setAllowFlight(true);
        } else {
            // 禁用飞行
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    /**
     * 给予注册成功的玩家奖励
     * 从配置文件读取奖励内容并发放
     * @param player 玩家对象
     */
    public void giveRegisterReward(Player player) {
        RegisterConfig registerConfig = plugin.getConfigManager().getRegisterConfig();
        
        // 给予物品奖励
        giveItemRewards(player, registerConfig.getRewardItems());
        
        // 给予经验奖励
        int experience = registerConfig.getRewardExperience();
        if (experience > 0) {
            player.giveExp(experience);
        }
        
        // 执行玩家命令
        for (String cmd : registerConfig.getRewardPlayerCommands()) {
            if (cmd != null && !cmd.isEmpty()) {
                String command = cmd.replace("%player%", player.getName());
                player.performCommand(command);
            }
        }
        
        // 执行控制台命令
        for (String cmd : registerConfig.getRewardConsoleCommands()) {
            if (cmd != null && !cmd.isEmpty()) {
                String command = cmd.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }
        
        // 发送奖励提示消息
        player.sendMessage(i18n.as("register.reward", true));
    }
    
    /**
     * 给予玩家物品奖励
     * @param player 玩家对象
     * @param items 物品列表，格式: "MATERIAL:amount" 或 "MATERIAL:amount:data"
     */
    private void giveItemRewards(Player player, List<String> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        for (String itemString : items) {
            try {
                String[] parts = itemString.split(":");
                if (parts.length < 2) {
                    plugin.getLogger().warning("无效的物品配置格式: " + itemString);
                    continue;
                }
                
                // 解析物品类型
                Material material = Material.matchMaterial(parts[0]);
                if (material == null) {
                    plugin.getLogger().warning("未知的物品类型: " + parts[0]);
                    continue;
                }
                
                // 解析数量
                int amount = Integer.parseInt(parts[1]);
                if (amount <= 0) {
                    continue;
                }
                
                // 创建物品堆
                ItemStack itemStack = new ItemStack(material, amount);
                
                // 给予玩家物品，如果背包满了则掉落在地上
                Map<Integer, ItemStack> leftover = player.getInventory().addItem(itemStack);
                if (!leftover.isEmpty()) {
                    for (ItemStack drop : leftover.values()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), drop);
                    }
                }
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("无效的物品数量配置: " + itemString);
            } catch (Exception e) {
                plugin.getLogger().warning("处理物品奖励时出错: " + itemString + " - " + e.getMessage());
            }
        }
    }
}
