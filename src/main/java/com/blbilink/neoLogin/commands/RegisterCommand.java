package com.blbilink.neoLogin.commands;

import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.dao.UserDAO;
import com.blbilink.neoLogin.managers.ConfigManager;
import com.blbilink.neoLogin.managers.PlayerManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegisterCommand implements CommandExecutor {

    private final NeoLogin plugin;
    private final UserDAO userDAO;
    private final PlayerManager playerManager;
    private final I18n i18n;
    private final ConfigManager configManager;

    public RegisterCommand(NeoLogin plugin) {
        this.plugin = plugin;
        this.userDAO = plugin.getUserDAO();
        this.playerManager = plugin.getPlayerManager();
        this.i18n = plugin.getI18n();
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(i18n.as("login.onlyPlayer", false));
            return true;
        }

        Player player = (Player) sender;

        if (userDAO.isRegistered(player.getUniqueId())) {
            player.sendMessage(i18n.as("register.alreadyRegistered", true));
            return true;
        }

        if (configManager.isRegisterConfirmPassword()) {
            registerWithConfirmPassword(player, args);
        } else {
            register(player, args);
        }

        return true;
    }

    private void register(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage(i18n.as("register.usage", true));
            return;
        }

        String password = args[0];

        // 使用配置的最小密码长度进行验证
        int minLength = configManager.getRegisterPasswordMinLength();
        int maxLength = configManager.getRegisterPasswordLength();

        if (password.length() < minLength) {
            player.sendMessage(i18n.as("register.passwordTooShort", true));
            return;
        }

        if (password.length() > maxLength) {
            player.sendMessage(i18n.as("register.passwordTooLong", true));
            return;
        }

        performRegistration(player, password);
    }

    private void registerWithConfirmPassword(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(i18n.as("register.usage", true));
            return;
        }

        String password = args[0];
        String confirmPassword = args[1];

        if (!password.equals(confirmPassword)) {
            player.sendMessage(i18n.as("register.passwordMismatch", true));
            return;
        }

        // 使用配置的最小密码长度进行验证
        int minLength = configManager.getRegisterPasswordMinLength();
        int maxLength = configManager.getRegisterPasswordLength();

        if (password.length() < minLength) {
            player.sendMessage(i18n.as("register.passwordTooShort", true));
            return;
        }

        if (password.length() > maxLength) {
            player.sendMessage(i18n.as("register.passwordTooLong", true));
            return;
        }

        performRegistration(player, password);
    }

    private void performRegistration(Player player, String password) {
        String ipAddress = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "N/A";

        plugin.getFoliaUtil().runTaskAsync(() -> {
            boolean success = userDAO.registerUser(player.getUniqueId(), player.getName(), password, ipAddress);

            plugin.getFoliaUtil().runTask(() -> {
                if (success) {
                    playerManager.setLoggedIn(player);
                    player.sendMessage(i18n.as("register.success", true));
                    // 恢复玩家登录前的飞行状态
                    playerManager.restoreFlightState(player);
                    if (configManager.isRegisterReward()) {
                        playerManager.giveRegisterReward(player);
                    }
                } else {
                    player.sendMessage(i18n.as("register.error", true));
                }
            });
        });
    }
}