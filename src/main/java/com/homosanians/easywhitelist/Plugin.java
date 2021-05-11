package com.homosanians.easywhitelist;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin
{
    private static Plugin instance;
    private WLStorage storage;

    public void onEnable() {
        Plugin.instance = this;
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.getCommand("easywl").setExecutor((CommandExecutor)new WLCmd(this));
        this.getServer().getPluginManager().registerEvents((Listener)new WLEvent(this), (org.bukkit.plugin.Plugin)this);
        this.storage = new WLStorage(this);
        Utility.sendConsole("&eE-Whitelist > &7Loaded!");
    }

    public static Plugin getInstance() {
        return Plugin.instance;
    }

    public void onDisable() {
        // send to ops and console that somwthing went wrong
        this.storage.saveWhitelists();
    }

    public WLStorage getStorage() {
        return this.storage;
    }
}
