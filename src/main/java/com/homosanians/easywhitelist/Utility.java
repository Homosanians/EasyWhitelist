package com.homosanians.easywhitelist;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.Particle;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.Sound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import java.lang.reflect.Constructor;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.util.Iterator;
import java.util.Enumeration;
import java.net.URL;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public final class Utility
{
    private static final int CENTER_PX = 154;
    
    private Utility() {
    }
    
    public static final Class<?> getClass(final String classname) {
        final String servversion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + servversion + "." + classname);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static final List<Class> findClasses(final File directory, final String packageName) throws ClassNotFoundException {
        final List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        final File[] files = directory.listFiles();
        File[] array;
        for (int length = (array = files).length, i = 0; i < length; ++i) {
            final File file = array[i];
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, String.valueOf(packageName) + "." + file.getName()));
            }
            else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(String.valueOf(packageName) + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
    
    public static final Class[] getClasses(final String packageName) throws ClassNotFoundException, IOException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        final String path = packageName.replace('.', '/');
        final Enumeration<URL> resources = classLoader.getResources(path);
        final List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            final URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        final ArrayList<Class> classes = new ArrayList<Class>();
        for (final File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }
    
    public static final void sendPacket(final Player player, final Object packet) {
        try {
            final Object handle = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static final void sendTitle(final Player player, final int fadeIn, final Integer stay, final Integer fadeOut, String title, String subtitle) {
        try {
            if (title != null) {
                title = TransColor(title);
                Object e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatTitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor titleConstructor = getClass("PacketPlayOutTitle").getConstructor(getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object titlePacket = titleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut);
                sendPacket(player, titlePacket);
                e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                chatTitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                titleConstructor = getClass("PacketPlayOutTitle").getConstructor(getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"));
                titlePacket = titleConstructor.newInstance(e, chatTitle);
                sendPacket(player, titlePacket);
            }
            if (subtitle != null) {
                subtitle = TransColor(subtitle);
                Object e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatSubtitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor subtitleConstructor = getClass("PacketPlayOutTitle").getConstructor(getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
                e = getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                subtitleConstructor = getClass("PacketPlayOutTitle").getConstructor(getClass("PacketPlayOutTitle").getDeclaredClasses()[0], getClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
            }
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
    }
    
    public static final void executeConsole(final String cmd) {
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), cmd);
    }
    
    public static final void sendMsg(final Player b, final String msg) {
        if (msg.contains("%center%")) {
            sendCenteredMessage(b, msg.replaceAll("%center%", ""));
            return;
        }
        b.sendMessage(TransColor(msg));
    }
    
    public static final void sendMsg(final CommandSender b, final String msg) {
        if (!msg.contains("%center%")) {
            b.sendMessage(TransColor(msg));
            return;
        }
        if (b instanceof Player) {
            sendCenteredMessage((Player)b, msg.replaceAll("%center%", ""));
            return;
        }
        b.sendMessage(TransColor(msg.replaceAll("%center%", "")));
    }
    
    public static final void broadcast(final String msg) {
        if (msg.contains("%center%")) {
            broadCastCenteredMessage(msg.replaceAll("%center%", ""));
            return;
        }
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    
    public static final void sendConsole(final String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    
    public static final String TransColor(String c) {
        if (c == null) { c = ""; }
        return ChatColor.translateAlternateColorCodes('&', c);
    }
    
    public static final void sendCenteredMessage(final Player player, final String message) {
        if (message == null || message.equals("")) {
            sendMsg(player, message);
            return;
        }
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        char[] charArray;
        for (int length = (charArray = message.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            if (c == '§') {
                previousCode = true;
            }
            else if (previousCode) {
                previousCode = false;
                isBold = (c == 'l' || c == 'L');
            }
            else {
                final DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += (isBold ? dFI.getBoldLength() : dFI.getLength());
                ++messagePxSize;
            }
        }
        final int halvedMessageSize = messagePxSize / 2;
        final int toCompensate = 154 - halvedMessageSize;
        final int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        final StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(sb.toString()) + message));
    }
    
    public static final void broadCastCenteredMessage(final String message) {
        if (message == null || message.equals("")) {
            Bukkit.getServer().broadcastMessage("");
            return;
        }
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        char[] charArray;
        for (int length = (charArray = message.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            if (c == '§') {
                previousCode = true;
            }
            else if (previousCode) {
                previousCode = false;
                isBold = (c == 'l' || c == 'L');
            }
            else {
                final DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += (isBold ? dFI.getBoldLength() : dFI.getLength());
                ++messagePxSize;
            }
        }
        final int halvedMessageSize = messagePxSize / 2;
        final int toCompensate = 154 - halvedMessageSize;
        final int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        final StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(sb.toString()) + message));
    }
    
    public static final String[] TransColor(final String[] c) {
        String strf = "";
        final int length = c.length;
        int cr = 0;
        for (final String str : c) {
            if (++cr != length) {
                strf = String.valueOf(strf) + str + ";";
            }
            else {
                strf = String.valueOf(strf) + str;
            }
        }
        strf = TransColor(strf);
        return strf.split(";");
    }
    
    public static final List<String> TransColor(final List<String> strlist) {
        for (int x = 0; x < strlist.size(); ++x) {
            strlist.set(x, TransColor(strlist.get(x)));
        }
        return strlist;
    }
    
    public static final void PlaySoundAt(final World w, final Location p, final Sound s, final Float vol, final Float pit) {
        w.playSound(p, s, (float)vol, (float)pit);
    }
    
    public static final void PlaySound(final Player p, final Sound s, final Float vol, final Float pit) {
        p.playSound(p.getLocation(), s, (float)vol, (float)pit);
    }
    
    public static final ArrayList<Player> near(final Entity loc, final int radius) {
        final ArrayList<Player> nearby = new ArrayList<Player>();
        for (final Entity e : loc.getNearbyEntities((double)radius, (double)radius, (double)radius)) {
            if (e instanceof Player) {
                nearby.add((Player)e);
            }
        }
        return nearby;
    }
    
    public static final void PlayParticle(final World world, final Location loc, final Effect particle, final int count) {
        world.playEffect(loc, particle, count);
    }
    
    public static final void spawnParticle(final World world, final Particle particle, final Location loc, final Double Xoff, final Double Yoff, final Double Zoff, final int count) {
        world.spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), count, (double)Xoff, (double)Yoff, (double)Zoff);
    }
    
    public static final String normalizeTime(final int seconds) {
        int sec = seconds;
        int min = 0;
        int hour = 0;
        int day = 0;
        while (sec >= 60) {
            ++min;
            sec -= 60;
        }
        while (min >= 60) {
            ++hour;
            min -= 60;
        }
        while (hour >= 24) {
            ++day;
            hour -= 24;
        }
        if (sec == 0 && min == 0 && hour == 0 && day == 0) {
            return "&a&lZERO!";
        }
        if (min == 0 && hour == 0 && day == 0) {
            return String.valueOf(sec) + " Seconds";
        }
        if (hour == 0 && day == 0 && min > 0) {
            return String.valueOf(min) + " Minutes " + sec + " Seconds";
        }
        if (day == 0 && hour > 0) {
            return String.valueOf(hour) + " Hours " + min + " Minutes " + sec + " Seconds";
        }
        if (day > 0) {
            return String.valueOf(day) + " Days " + hour + " Hours " + min + " Minutes " + sec + " Seconds";
        }
        return "&a&lZERO!";
    }
    
    public static final String normalizeTime2(final int seconds) {
        int sec = seconds;
        int min = 0;
        int hour = 0;
        int day = 0;
        while (sec >= 60) {
            ++min;
            sec -= 60;
        }
        while (min >= 60) {
            ++hour;
            min -= 60;
        }
        while (hour >= 24) {
            ++day;
            hour -= 24;
        }
        if (sec == 0 && min == 0 && hour == 0 && day == 0) {
            return "&a&lZERO!";
        }
        if (min == 0 && hour == 0 && day == 0) {
            return String.valueOf(sec) + " sec";
        }
        if (hour == 0 && day == 0 && min > 0) {
            return String.valueOf(min) + " min " + sec + " sec";
        }
        if (day == 0 && hour > 0) {
            return String.valueOf(hour) + " h " + min + " min " + sec + " sec";
        }
        if (day > 0) {
            return String.valueOf(day) + " day " + hour + " h " + min + " min " + sec + " sec";
        }
        return "&a&lZERO!";
    }
    
    public static final boolean isEmpty(final Inventory inv) {
        for (int size = inv.getSize(), i = 0; i < size; ++i) {
            if (inv.getItem(i) == null) {
                return true;
            }
        }
        return false;
    }
    
    public static final boolean isEmpty(final PlayerInventory inv) {
        return isEmpty(inv);
    }
    
    public enum DefaultFontInfo
    {
        A("A", 0, 'A', 5), 
        a("a", 1, 'a', 5), 
        B("B", 2, 'B', 5), 
        b("b", 3, 'b', 5), 
        C("C", 4, 'C', 5), 
        c("c", 5, 'c', 5), 
        D("D", 6, 'D', 5), 
        d("d", 7, 'd', 5), 
        E("E", 8, 'E', 5), 
        e("e", 9, 'e', 5), 
        F("F", 10, 'F', 5), 
        f("f", 11, 'f', 4), 
        G("G", 12, 'G', 5), 
        g("g", 13, 'g', 5), 
        H("H", 14, 'H', 5), 
        h("h", 15, 'h', 5), 
        I("I", 16, 'I', 3), 
        i("i", 17, 'i', 1), 
        J("J", 18, 'J', 5), 
        j("j", 19, 'j', 5), 
        K("K", 20, 'K', 5), 
        k("k", 21, 'k', 4), 
        L("L", 22, 'L', 5), 
        l("l", 23, 'l', 1), 
        M("M", 24, 'M', 5), 
        m("m", 25, 'm', 5), 
        N("N", 26, 'N', 5), 
        n("n", 27, 'n', 5), 
        O("O", 28, 'O', 5), 
        o("o", 29, 'o', 5), 
        P("P", 30, 'P', 5), 
        p("p", 31, 'p', 5), 
        Q("Q", 32, 'Q', 5), 
        q("q", 33, 'q', 5), 
        R("R", 34, 'R', 5), 
        r("r", 35, 'r', 5), 
        S("S", 36, 'S', 5), 
        s("s", 37, 's', 5), 
        T("T", 38, 'T', 5), 
        t("t", 39, 't', 4), 
        U("U", 40, 'U', 5), 
        u("u", 41, 'u', 5), 
        V("V", 42, 'V', 5), 
        v("v", 43, 'v', 5), 
        W("W", 44, 'W', 5), 
        w("w", 45, 'w', 5), 
        X("X", 46, 'X', 5), 
        x("x", 47, 'x', 5), 
        Y("Y", 48, 'Y', 5), 
        y("y", 49, 'y', 5), 
        Z("Z", 50, 'Z', 5), 
        z("z", 51, 'z', 5), 
        NUM_1("NUM_1", 52, '1', 5), 
        NUM_2("NUM_2", 53, '2', 5), 
        NUM_3("NUM_3", 54, '3', 5), 
        NUM_4("NUM_4", 55, '4', 5), 
        NUM_5("NUM_5", 56, '5', 5), 
        NUM_6("NUM_6", 57, '6', 5), 
        NUM_7("NUM_7", 58, '7', 5), 
        NUM_8("NUM_8", 59, '8', 5), 
        NUM_9("NUM_9", 60, '9', 5), 
        NUM_0("NUM_0", 61, '0', 5), 
        EXCLAMATION_POINT("EXCLAMATION_POINT", 62, '!', 1), 
        AT_SYMBOL("AT_SYMBOL", 63, '@', 6), 
        NUM_SIGN("NUM_SIGN", 64, '#', 5), 
        DOLLAR_SIGN("DOLLAR_SIGN", 65, '$', 5), 
        PERCENT("PERCENT", 66, '%', 5), 
        UP_ARROW("UP_ARROW", 67, '^', 5), 
        AMPERSAND("AMPERSAND", 68, '&', 5), 
        ASTERISK("ASTERISK", 69, '*', 5), 
        LEFT_PARENTHESIS("LEFT_PARENTHESIS", 70, '(', 4), 
        RIGHT_PERENTHESIS("RIGHT_PERENTHESIS", 71, ')', 4), 
        MINUS("MINUS", 72, '-', 5), 
        UNDERSCORE("UNDERSCORE", 73, '_', 5), 
        PLUS_SIGN("PLUS_SIGN", 74, '+', 5), 
        EQUALS_SIGN("EQUALS_SIGN", 75, '=', 5), 
        LEFT_CURL_BRACE("LEFT_CURL_BRACE", 76, '{', 4), 
        RIGHT_CURL_BRACE("RIGHT_CURL_BRACE", 77, '}', 4), 
        LEFT_BRACKET("LEFT_BRACKET", 78, '[', 3), 
        RIGHT_BRACKET("RIGHT_BRACKET", 79, ']', 3), 
        COLON("COLON", 80, ':', 1), 
        SEMI_COLON("SEMI_COLON", 81, ';', 1), 
        DOUBLE_QUOTE("DOUBLE_QUOTE", 82, '\"', 3), 
        SINGLE_QUOTE("SINGLE_QUOTE", 83, '\'', 1), 
        LEFT_ARROW("LEFT_ARROW", 84, '<', 4), 
        RIGHT_ARROW("RIGHT_ARROW", 85, '>', 4), 
        QUESTION_MARK("QUESTION_MARK", 86, '?', 5), 
        SLASH("SLASH", 87, '/', 5), 
        BACK_SLASH("BACK_SLASH", 88, '\\', 5), 
        LINE("LINE", 89, '|', 1), 
        TILDE("TILDE", 90, '~', 5), 
        TICK("TICK", 91, '`', 2), 
        PERIOD("PERIOD", 92, '.', 1), 
        COMMA("COMMA", 93, ',', 1), 
        SPACE("SPACE", 94, ' ', 3), 
        DEFAULT("DEFAULT", 95, 'a', 4);
        
        private char character;
        private int length;
        
        private DefaultFontInfo(final String name, final int ordinal, final char character, final int length) {
            this.character = character;
            this.length = length;
        }
        
        public final char getCharacter() {
            return this.character;
        }
        
        public final int getLength() {
            return this.length;
        }
        
        public final int getBoldLength() {
            if (this == DefaultFontInfo.SPACE) {
                return this.getLength();
            }
            return this.length + 1;
        }
        
        public static final DefaultFontInfo getDefaultFontInfo(final char c) {
            DefaultFontInfo[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final DefaultFontInfo dFI = values[i];
                if (dFI.getCharacter() == c) {
                    return dFI;
                }
            }
            return DefaultFontInfo.DEFAULT;
        }
    }

    private static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static String formatHexColor(String msg) {
        Matcher matcher = hexPattern.matcher(msg);
        while (matcher.find()) {
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, ChatColor.of(color) + "");
            matcher = hexPattern.matcher(msg);
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
