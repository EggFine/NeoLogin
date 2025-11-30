package com.blbilink.neoLogin.commands;

import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * NeoLogin 主命令
 * /neologin reload - 重载配置
 * /neologin savelocation - 保存当前位置为传送点
 */
public class NeoLoginCommand implements CommandExecutor, TabCompleter {

    private final NeoLogin plugin;
    private final I18n i18n;

    public NeoLoginCommand(NeoLogin plugin) {
        this.plugin = plugin;
        this.i18n = plugin.getI18n();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                return handleReload(sender);
            case "savelocation":
            case "saveloc":
                return handleSaveLocation(sender);
            default:
                sendHelp(sender);
                return true;
        }
    }

    /**
     * 处理 reload 子命令
     */
    private boolean handleReload(CommandSender sender) {
        // 检查权限
        if (sender instanceof Player && !sender.hasPermission("neologin.admin")) {
            sender.sendMessage(i18n.as("command.noPermission", true));
            return true;
        }

        try {
            // 重载配置文件
            plugin.reloadConfig();
            plugin.getConfigManager().loadAndCacheValues();
            
            // 重载语言文件
            plugin.getI18n().loadLanguage();

            sender.sendMessage(i18n.as("command.reloaded", sender instanceof Player));
        } catch (Exception e) {
            sender.sendMessage(i18n.as("command.reloadFailed", sender instanceof Player));
            plugin.getLogger().severe("配置重载失败: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 处理 savelocation 子命令
     */
    private boolean handleSaveLocation(CommandSender sender) {
        // 只有玩家可以执行此命令
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n.as("command.onlyPlayer", false));
            return true;
        }

        Player player = (Player) sender;

        // 检查权限
        if (!player.hasPermission("neologin.admin")) {
            player.sendMessage(i18n.as("command.noPermission", true));
            return true;
        }

        Location loc = player.getLocation();

        // 获取配置文件
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // 保存位置到配置文件
        config.set("autoTeleport.locationPos.world", loc.getWorld().getName());
        config.set("autoTeleport.locationPos.x", loc.getX());
        config.set("autoTeleport.locationPos.y", loc.getY());
        config.set("autoTeleport.locationPos.z", loc.getZ());
        config.set("autoTeleport.locationPos.yaw", loc.getYaw());
        config.set("autoTeleport.locationPos.pitch", loc.getPitch());

        try {
            config.save(configFile);
            // 重载配置以应用更改
            plugin.reloadConfig();
            plugin.getConfigManager().loadAndCacheValues();
            
            player.sendMessage(i18n.as("command.locationSaved", true));
            player.sendMessage(i18n.as("command.locationInfo", true, 
                    loc.getWorld().getName(), 
                    String.format("%.2f", loc.getX()),
                    String.format("%.2f", loc.getY()),
                    String.format("%.2f", loc.getZ())));
        } catch (IOException e) {
            player.sendMessage(i18n.as("command.saveLocationFailed", true));
            plugin.getLogger().severe("保存位置失败: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 发送帮助信息
     */
    private void sendHelp(CommandSender sender) {
        boolean isPlayer = sender instanceof Player;
        sender.sendMessage(i18n.as("command.help.header", isPlayer));
        sender.sendMessage(i18n.as("command.help.reload", isPlayer));
        sender.sendMessage(i18n.as("command.help.savelocation", isPlayer));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("reload", "savelocation");
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
            Collections.sort(completions);
        }

        return completions;
    }
}

