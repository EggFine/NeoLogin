package com.blbilink.neoLogin;

import com.blbilink.neoLibrary.utils.ConfigUtil;
import com.blbilink.neoLibrary.utils.FoliaUtil;
import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLibrary.utils.Metrics;
import com.blbilink.neoLibrary.utils.TextUtil;

import java.util.Collections;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class NeoLogin extends JavaPlugin {

    public FoliaUtil foliaUtil;
    private ConfigUtil mainConfig;
    private I18n i18n;

    @Override
    public void onEnable() {
        getLogger().info(TextUtil.getLogo("Loading...", "NeoLogin", "The Next Generation blbiLogin\nSpigotMC: https://www.spigotmc.org/resources/125813/", this, Collections.singletonList("EggFine"), null));
        Metrics metrics = new Metrics(this, 26113);
        foliaUtil = new FoliaUtil(this);

        ConfigUtil mainConfig = new ConfigUtil(this, "config.yml");
        FileConfiguration config = mainConfig.getConfig();

        String prefix = config.getString("prefix");
        String lang = config.getString("language", "zh_CN"); // "zh_CN" 作为默认值

        I18n i18n = new I18n(this, prefix, lang);
        i18n.loadLanguage();
    }

    @Override
    public void onDisable() {
        
    }
}
