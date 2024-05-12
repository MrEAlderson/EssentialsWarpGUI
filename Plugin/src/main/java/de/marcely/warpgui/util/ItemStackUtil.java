package de.marcely.warpgui.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class ItemStackUtil {

    @Nullable
    public static ItemStack setEmptyName(@Nullable ItemStack is) {
        if (is == null)
            return null;

        final ItemMeta im = is.getItemMeta();

        if (im == null)
            return is;

        im.setDisplayName(" ");
        is.setItemMeta(im);

        return is;
    }
}
