package de.marcely.warpgui.util;

import org.jetbrains.annotations.Nullable;

public class StringUtil {

    @Nullable
    public static Integer parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e) {
            return null;
        }
    }
}
