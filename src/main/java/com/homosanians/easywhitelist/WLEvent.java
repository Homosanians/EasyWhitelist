package com.homosanians.easywhitelist;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.Listener;

public class WLEvent implements Listener
{
    private Plugin m;
    
    public WLEvent(final Plugin m) {
        this.m = m;
    }
    
    @EventHandler
    public void onConnect(final PlayerLoginEvent e) {
        final Player p = e.getPlayer();
        if (p == null) {
            return;
        }
        if (!this.m.getStorage().isWhitelisting()) {
            return;
        }
        if (this.m.getStorage().isWhitelisted(p.getName())) {
            return;
        }
        e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, Utility.formatHexColor(this.m.getStorage().getNoWhitelistMsg()));
    }
}
