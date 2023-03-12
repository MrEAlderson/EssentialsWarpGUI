package de.marcely.warpgui.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");
    private static Method OF_HEX_METHOD = null;

    static {
        try {
            OF_HEX_METHOD = net.md_5.bungee.api.ChatColor.class.getMethod("of", String.class);
            OF_HEX_METHOD.setAccessible(true);
        } catch (Exception e) { }
    }

    public static String translate(String str) {
        // hex
        if (OF_HEX_METHOD != null){
            Matcher match = HEX_PATTERN.matcher(str);

            while(match.find()){
                final String strColor = str.substring(match.start(), match.end());
                final net.md_5.bungee.api.ChatColor color = ofHex(strColor.substring(1));

                if(color == null)
                    continue;

                str = str.replace(strColor, color.toString());
                match = HEX_PATTERN.matcher(str);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @Nullable
    private static net.md_5.bungee.api.ChatColor ofHex(String hex) {
        if (OF_HEX_METHOD == null)
            return null;

        try{
            return (net.md_5.bungee.api.ChatColor) OF_HEX_METHOD.invoke(null, hex);
        }catch(Exception e){ }

        return null;
    }
}
