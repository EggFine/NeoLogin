package com.blbilink.neoLogin.commands;

import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLogin.NeoLogin;
import com.blbilink.neoLogin.dao.UserDAO;
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

    public RegisterCommand(NeoLogin plugin) {
        this.plugin = plugin;
        this.userDAO = plugin.getUserDAO();
        this.playerManager = plugin.getPlayerManager(); // 使用新的 PlayerManager
        this.i18n = plugin.getI18n();
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

        if (args.length != 2) {
            player.sendMessage(i18n.as("register.usage", true));
            return true;
        }

        String password = args[0];
        String confirmPassword = args[1];

        if (!password.equals(confirmPassword)) {
            player.sendMessage(i18n.as("register.passwordMismatch", true));
            return true;
        }

        if (password.length() < 6) {
            player.sendMessage(i18n.as("register.passwordTooShort", true));
            return true;
        }

        String ipAddress = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "N/A";

        plugin.getFoliaUtil().runTaskAsync(() -> {
            boolean success = userDAO.registerUser(player.getUniqueId(), player.getName(), password, ipAddress);

            plugin.getFoliaUtil().runTask(() -> {
                if (success) {
                    playerManager.setLoggedIn(player); // 注册成功后自动登录
                    player.sendMessage(i18n.as("register.success", true));
                } else {
                    player.sendMessage(i18n.as("register.error", true));
                }
            });
        });

        return true;
    }
}