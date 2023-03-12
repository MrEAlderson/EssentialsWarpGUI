package de.marcely.warpgui;

import com.cryptomorin.xseries.XMaterial;
import de.marcely.warpgui.storage.WarpsStorage;
import de.marcely.warpgui.util.ChatColorUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import de.marcely.warpgui.warp.Warp;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GUIWarp {

    private final transient GUIWarpContainer container;
    private final String name;

    // attributes may not have translated ChatColor
    @Setter(value= AccessLevel.NONE)
    private String displayName;
    private List<String> lore = Collections.emptyList();
    private ItemStack baseIcon = XMaterial.GUNPOWDER.parseItem();

    private int slotX = -1, slotY = -1;

    @Setter(value= AccessLevel.NONE)
    private transient ItemStack displayedIcon;
    private transient String colorlessDisplayName;

    // hook
    @Getter @Setter
    private transient Warp hook;


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.colorlessDisplayName = ChatColor.stripColor(ChatColorUtil.translate(displayName));
    }

    public void updateDisplayedIcon() {
        if (this.baseIcon == null || this.baseIcon.getItemMeta() == null)
            this.baseIcon = XMaterial.GUNPOWDER.parseItem();

        final ItemStack is = this.displayedIcon = this.baseIcon.clone();
        final ItemMeta im = is.getItemMeta();

        im.setDisplayName(ChatColorUtil.translate(this.displayName));
        im.setLore(this.lore.stream()
                .map(ChatColorUtil::translate)
                .collect(Collectors.toList()));

        is.setItemMeta(im);
    }

    public boolean save() {
        return WarpsStorage.save(this);
    }

    public boolean isOutOfBounds() {
        return this.slotX < 0 || this.slotX >= 9 || this.slotY < 0;
    }

    public boolean exists() {
        return this.container.getAll().contains(this);
    }

    public boolean hasHook() {
        return this.hook != null;
    }

    public boolean isDefault() {
        return this.displayName.equals(this.name) &&
                this.lore.isEmpty() &&
                this.baseIcon.equals(XMaterial.GUNPOWDER.parseItem()) &&
                isOutOfBounds();
    }

    public void updateAttributes() {
        if (this.displayName == null)
            setDisplayName("&f" + this.name);
        else
            setDisplayName(this.displayName);

        updateDisplayedIcon();
    }
}