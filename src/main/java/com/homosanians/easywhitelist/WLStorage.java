package com.homosanians.easywhitelist;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.Collection;
import java.util.ArrayList;

public class WLStorage
{
    private Plugin m;
    private ArrayList<String> whitelists;
    private boolean WhitelistEnabled;
    private String nowhitelistmsg;
    
    public WLStorage(final Plugin m) {
        this.whitelists = new ArrayList<String>();
        this.WhitelistEnabled = false;
        this.nowhitelistmsg = "";
        this.m = m;
        this.reload();
    }
    
    public void reload() {
        this.m.reloadConfig();
        final FileConfiguration config = this.m.getConfig();
        this.whitelists = new ArrayList<String>(config.getStringList("whitelisted"));
        this.WhitelistEnabled = config.getBoolean("whitelist");
        this.nowhitelistmsg = Utility.TransColor(config.getString("no_whitelisted"));
        Utility.sendConsole("&e&lEasyWhitelist > &7Config reloaded.");
    }
    
    public void saveWhitelists() {
        final FileConfiguration config = this.m.getConfig();
        config.set("whitelisted", (Object)this.whitelists);
        config.set("whitelist", (Object)this.isWhitelisting());
        this.m.saveConfig();
    }
    
    public boolean isWhitelisted(final String name) {
        return this.whitelists.contains(name.toLowerCase());
    }
    
    public void addWhitelist(final String name) {
        if (this.whitelists.contains(name.toLowerCase())) {
            return;
        }
        this.whitelists.add(name.toLowerCase());
        this.saveWhitelists();
    }
    
    public void removeWhitelist(final String name) {
        if (!this.whitelists.contains(name.toLowerCase())) {
            return;
        }
        this.whitelists.remove(name.toLowerCase());
        this.saveWhitelists();
    }
    
    public void setWhitelist(final Boolean onoff) {
        this.WhitelistEnabled = onoff;
        this.saveWhitelists();
    }
    
    public ArrayList<String> getWhiteLists() {
        return this.whitelists;
    }
    
    public boolean isWhitelisting() {
        return this.WhitelistEnabled;
    }
    
    public String getNoWhitelistMsg() {
        return this.nowhitelistmsg;
    }
}
