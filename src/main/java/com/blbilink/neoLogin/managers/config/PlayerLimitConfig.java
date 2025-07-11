package com.blbilink.neoLogin.managers.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * 玩家限制配置类，管理未登录玩家的行为限制配置项
 */
public class PlayerLimitConfig {

    private boolean notLoggedInLimitEnabled;
    private boolean limitMove;
    private boolean limitBlockPlace;
    private boolean limitBlockBreak;
    private boolean limitBlockInteract;
    private boolean limitChat;
    private boolean limitCommand;
    private List<String> commandWhitelist;
    private boolean limitItemUse;
    private boolean limitDamage;
    private boolean limitAttacking;

    /**
     * 从配置文件加载玩家限制配置
     *
     * @param config 配置文件对象
     */
    public void load(FileConfiguration config) {
        notLoggedInLimitEnabled = config.getBoolean("notLoggedInPlayerLimit.enabled", true);
        limitMove = config.getBoolean("notLoggedInPlayerLimit.type.move", true);
        limitBlockPlace = config.getBoolean("notLoggedInPlayerLimit.type.blockPlace", true);
        limitBlockBreak = config.getBoolean("notLoggedInPlayerLimit.type.blockBreak", true);
        limitBlockInteract = config.getBoolean("notLoggedInPlayerLimit.type.blockInteract", true);
        limitChat = config.getBoolean("notLoggedInPlayerLimit.type.chat", true);
        limitCommand = config.getBoolean("notLoggedInPlayerLimit.type.command", true);
        commandWhitelist = config.getStringList("notLoggedInPlayerLimit.type.commandWhitelist");
        limitItemUse = config.getBoolean("notLoggedInPlayerLimit.type.itemUse", true);
        limitDamage = config.getBoolean("notLoggedInPlayerLimit.type.damage", true);
        limitAttacking = config.getBoolean("notLoggedInPlayerLimit.type.attacking", true);
    }

    // Getters
    public boolean isNotLoggedInLimitEnabled() {
        return notLoggedInLimitEnabled;
    }

    public boolean isLimitMove() {
        return limitMove;
    }

    public boolean isLimitBlockPlace() {
        return limitBlockPlace;
    }

    public boolean isLimitBlockBreak() {
        return limitBlockBreak;
    }

    public boolean isLimitBlockInteract() {
        return limitBlockInteract;
    }

    public boolean isLimitChat() {
        return limitChat;
    }

    public boolean isLimitCommand() {
        return limitCommand;
    }

    public List<String> getCommandWhitelist() {
        return Collections.unmodifiableList(commandWhitelist);
    }

    public boolean isLimitItemUse() {
        return limitItemUse;
    }

    public boolean isLimitDamage() {
        return limitDamage;
    }

    public boolean isLimitAttacking() {
        return limitAttacking;
    }
} 