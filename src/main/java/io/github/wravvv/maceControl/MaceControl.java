package io.github.wravvv.maceControl;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MaceControl extends JavaPlugin {

    public static FileConfiguration config;

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new Events(),this);

        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();

    }
}
