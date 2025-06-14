package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLibrary.utils.FoliaUtil;
import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginReminderListener implements Listener {

    private final NeoLogin plugin;
    private final PlayerManager playerManager;
    private final I18n i18n;
    private final FoliaUtil foliaUtil;
    
    // 存储每个玩家的提醒任务
    private final Map<UUID, FoliaUtil.Cancellable> reminderTasks = new HashMap<>();

    public LoginReminderListener(NeoLogin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.i18n = plugin.getI18n();
        this.foliaUtil = plugin.getFoliaUtil();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // 如果玩家未登录，启动定时提醒任务
        if (!playerManager.isLoggedIn(player)) {
            startLoginReminder(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // 玩家退出时停止提醒任务
        stopLoginReminder(player.getUniqueId());
    }

    /**
     * 启动登录提醒任务
     */
    @SuppressWarnings("deprecation")
    private void startLoginReminder(Player player) {
        UUID playerId = player.getUniqueId();
        
        // 如果已经有提醒任务在运行，先停止
        stopLoginReminder(playerId);
        
        // 创建新的定时任务，每3秒执行一次
        FoliaUtil.Cancellable task = foliaUtil.runTaskTimerAsync((cancellable) -> {
            // 检查玩家是否还在线
            Player onlinePlayer = plugin.getServer().getPlayer(playerId);
            if (onlinePlayer == null) {
                stopLoginReminder(playerId);
                return;
            }
            
            // 检查玩家是否已登录
            if (playerManager.isLoggedIn(onlinePlayer)) {
                stopLoginReminder(playerId);
                return;
            }
            
            // 发送ActionBar消息提醒
            String message = "请登录";
            onlinePlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            
        }, 0L, 60L); // 0tick延迟，每60tick（3秒）执行一次
        
        reminderTasks.put(playerId, task);
    }

    /**
     * 停止登录提醒任务
     */
    private void stopLoginReminder(UUID playerId) {
        FoliaUtil.Cancellable task = reminderTasks.remove(playerId);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    /**
     * 当玩家登录成功时调用此方法停止提醒
     */
    public void onPlayerLoggedIn(Player player) {
        stopLoginReminder(player.getUniqueId());
    }

} 