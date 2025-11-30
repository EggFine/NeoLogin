package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * 玩家交互限制监听器
 * 处理未登录玩家的方块放置/破坏/交互、物品丢弃、背包点击等限制
 */
public class PlayerInteractionListener implements Listener {

    private final ConfigManager configManager;
    private final PlayerManager playerManager;

    public PlayerInteractionListener(NeoLogin plugin) {
        this.configManager = plugin.getConfigManager();
        this.playerManager = plugin.getPlayerManager();
    }

    /**
     * 检查玩家是否可以进行交互
     * @param player 玩家对象
     * @return 如果玩家可以交互返回 true
     */
    private boolean canInteract(Player player) {
        // 如果未启用限制或玩家已登录，允许交互
        if (!configManager.isNotLoggedInLimitEnabled()) {
            return true;
        }
        return playerManager.isLoggedIn(player);
    }

    /**
     * 处理玩家与方块交互事件
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!canInteract(player) && configManager.isLimitBlockInteract()) {
            event.setCancelled(true);
        }
    }

    /**
     * 处理方块放置事件
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!canInteract(player) && configManager.isLimitBlockPlace()) {
            event.setCancelled(true);
        }
    }

    /**
     * 处理方块破坏事件
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!canInteract(player) && configManager.isLimitBlockBreak()) {
            event.setCancelled(true);
        }
    }

    /**
     * 处理玩家丢弃物品事件
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!canInteract(player) && configManager.isLimitItemUse()) {
            event.setCancelled(true);
        }
    }

    /**
     * 处理玩家点击背包事件
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!canInteract(player) && configManager.isLimitItemUse()) {
            event.setCancelled(true);
        }
    }

    /**
     * 处理实体被目标定位事件
     * 防止怪物攻击未登录玩家
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getTarget();
        if (!canInteract(player) && configManager.isLimitDamage()) {
            event.setCancelled(true);
        }
    }

    /**
     * 处理玩家攻击事件
     * 同时保护攻击者和被攻击者
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // 检查攻击者
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            if (!canInteract(attacker) && configManager.isLimitAttacking()) {
                event.setCancelled(true);
                return;
            }
        }

        // 检查被攻击者（保护未登录玩家不被攻击）
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            if (!canInteract(victim) && configManager.isLimitDamage()) {
                event.setCancelled(true);
            }
        }
    }
}

