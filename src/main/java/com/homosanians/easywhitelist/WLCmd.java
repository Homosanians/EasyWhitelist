package com.homosanians.easywhitelist;

import java.util.Iterator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class WLCmd implements CommandExecutor
{
    private Plugin m;
    String prefix;
    
    public WLCmd(final Plugin m) {
        this.prefix = "&6&lE - &e&lWhiteList > &7";
        this.m = m;
    }
    
    public boolean onCommand(final CommandSender arg0, final Command arg1, final String arg2, final String[] arg3) {
        if (!arg0.hasPermission("easywhitelist.admin")) {
            return true;
        }
        this.remanage(arg0, arg3);
        return true;
    }
    
    private void remanage(final CommandSender snd, final String[] args) {
        if (args.length == 0) {
            Utility.sendMsg(snd, "&a&lWhitelist &7>");
            Utility.sendMsg(snd, "&e> &7/easywl &aadd &f<name>");
            Utility.sendMsg(snd, "&e> &7/easywl &cremove &f<name>");
            Utility.sendMsg(snd, "&e> &7/easywl &flist");
            Utility.sendMsg(snd, "&e> &7/easywl &a&lon");
            Utility.sendMsg(snd, "&e> &7/easywl &c&loff");
            Utility.sendMsg(snd, "&e> &7/easywl &creload");
            return;
        }
        final String lowerCase;
        switch (lowerCase = args[0].toLowerCase()) {
            case "reload": {
                this.m.getStorage().reload();
                return;
            }
            case "remove": {
                if (args.length < 2) {
                    Utility.sendMsg(snd, "&7Please input a name!");
                    return;
                }
                final String name = args[1];
                this.m.getStorage().removeWhitelist(name);
                Utility.sendMsg(snd, String.valueOf(this.prefix) + "Whitelist removed for &c" + name);
                return;
            }
            case "on": {
                this.m.getStorage().setWhitelist(true);
                Utility.sendMsg(snd, String.valueOf(this.prefix) + "&fWhitelist is now &a&lON&f!");
                return;
            }
            case "add": {
                if (args.length < 2) {
                    Utility.sendMsg(snd, "&7Please input a name!");
                    return;
                }
                final String name = args[1];
                this.m.getStorage().addWhitelist(name);
                Utility.sendMsg(snd, String.valueOf(this.prefix) + "Whitelisted &a" + name);
                return;
            }
            case "off": {
                this.m.getStorage().setWhitelist(false);
                Utility.sendMsg(snd, String.valueOf(this.prefix) + "&8Whitelist is &c&lOFF!&8");
                return;
            }
            case "list": {
                String names = "";
                for (final String str : this.m.getStorage().getWhiteLists()) {
                    names = String.valueOf(names) + str + "&e&l, &7";
                }
                Utility.sendMsg(snd, "&a&lWhitelisted: &7" + names);
                return;
            }
            default:
                break;
        }
        Utility.sendMsg(snd, "&a&lWhitelist &7>");
        Utility.sendMsg(snd, "&e> &7/easywl &aadd &f<name>");
        Utility.sendMsg(snd, "&e> &7/easywl &cremove &f<name>");
        Utility.sendMsg(snd, "&e> &7/easywl &flist");
        Utility.sendMsg(snd, "&e> &7/easywl &a&lon");
        Utility.sendMsg(snd, "&e> &7/easywl &c&loff");
        Utility.sendMsg(snd, "&e> &7/easywl &creload");
    }
}
