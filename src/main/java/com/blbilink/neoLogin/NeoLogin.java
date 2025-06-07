package com.blbilink.neoLogin;

import com.blbilink.neoLibrary.utils.FoliaUtil;
import com.blbilink.neoLibrary.utils.I18n;
import com.blbilink.neoLibrary.utils.Metrics;
import com.blbilink.neoLibrary.utils.TextUtil;

import java.util.Collections;

import org.bukkit.plugin.java.JavaPlugin;

public final class NeoLogin extends JavaPlugin {

    public FoliaUtil foliaUtil;

    @Override
    public void onEnable() {
        getLogger().info(TextUtil.getLogo("Loading...", "NeoLogin", "The Next Generation blbiLogin\nSpigotMC: https://www.spigotmc.org/resources/125813/", this, Collections.singletonList("EggFine"), null));
        Metrics metrics = new Metrics(this, 26113);
        foliaUtil = new FoliaUtil(this);
    }

    @Override
    public void onDisable() {
        
    }
}
