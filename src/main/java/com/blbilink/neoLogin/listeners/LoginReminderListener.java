package com.blbilink.neoLogin.listeners;

import com.blbilink.neoLibrary.utils.FoliaUtil;
import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Sound;
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
    private final ConfigManager configManager;
    // 存储每个玩家的提醒任务
    private final Map<UUID, FoliaUtil.Cancellable> reminderTasks = new HashMap<>();

    public LoginReminderListener(NeoLogin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.i18n = plugin.getI18n();
        this.foliaUtil = plugin.getFoliaUtil();
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setAllowFlight(true);
        player.setFlying(true);
        
        // 如果玩家未登录，启动定时提醒任务
        if (!playerManager.isLoggedIn(player)) {
            startReminder(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // 玩家退出时停止提醒任务
        stopReminder(player.getUniqueId());
    }

    /**
     * 启动登录提醒任务
     */
    private void startReminder(Player player) {
        UUID playerId = player.getUniqueId();
        
        // 如果已经有提醒任务在运行，先停止
        stopReminder(playerId);
        
        // 创建新的定时任务，每3秒执行一次
        FoliaUtil.Cancellable task = foliaUtil.runTaskTimerAsync((cancellable) -> {
            // 检查玩家是否还在线
            Player onlinePlayer = plugin.getServer().getPlayer(playerId);
            if (onlinePlayer == null) {
                stopReminder(playerId);
                return;
            }

            // 检查玩家是否已登录
            if (playerManager.isLoggedIn(onlinePlayer)) {
                stopReminder(playerId);
                return;
            }

            // 检查玩家是否已注册
            if (playerManager.isRegistered(onlinePlayer)) {
                loginReminder(onlinePlayer);
            }else{
                registerReminder(onlinePlayer);
            }
            
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            
        }, 0L, 60L); // 0tick延迟，每60tick（3秒）执行一次
        
        reminderTasks.put(playerId, task);
    }

    /**
     * 启动登录提醒任务
     * @param player
     * @return
     */
    private void loginReminder(Player player) {
        // 发送ActionBar消息提醒
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(i18n.as("login.noLoginMessage", true, player.getName())));

        // 发送Title消息提醒
        player.sendTitle(i18n.as("login.noLoginTitle", false, player.getName()),null, 0, 100, 0);

        // 发送Subtitle消息提醒
        player.sendTitle(null, i18n.as("login.noLoginActionbar", false, player.getName()), 0, 100, 0);

        // 发送Message消息提醒
        player.sendMessage(i18n.as("login.noLoginMessage", true, player.getName()));
    }

    /**
     * 注册提醒任务
     * @param player
     */
    private void registerReminder(Player player) {
        // 发送ActionBar消息提醒
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(i18n.as("register.noRegisterMessage", true, player.getName())));

        // 发送Title消息提醒
        player.sendTitle(i18n.as("register.noRegisterTitle", false, player.getName()),null, 0, 100, 0);

        // 发送Subtitle消息提醒
        if(configManager.isRegisterConfirmPassword()){
            player.sendTitle(null, i18n.as("register.noRegisterActionbar2", false, player.getName()), 0, 100, 0);
        }else{
            player.sendTitle(null, i18n.as("register.noRegisterActionbar", false, player.getName()), 0, 100, 0);
        }

        // 发送Message消息提醒
        if(configManager.isRegisterConfirmPassword()){
            player.sendMessage(i18n.as("register.noRegisterMessage2", true, player.getName()));
        }else{
            player.sendMessage(i18n.as("register.noRegisterMessage", true, player.getName()));
        }
    }

    /**
     * 停止登录提醒任务
     */
    private void stopReminder(UUID playerId) {
        FoliaUtil.Cancellable task = reminderTasks.remove(playerId);
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    /**
     * 当玩家登录成功时调用此方法停止提醒
     */
    public void onPlayerLoggedIn(Player player) {
        stopReminder(player.getUniqueId());
    }

} 