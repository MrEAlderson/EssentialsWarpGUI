package de.marcely.warpgui;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Deprecated
@Data
public class WarpV2 {

    private String name;
    private ItemStack icon;
    private String prefix, suffix, displayName;
    private Integer forceSlot;
    private List<String> lores;
}
