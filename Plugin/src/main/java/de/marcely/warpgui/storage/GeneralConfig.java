package de.marcely.warpgui.storage;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import de.marcely.warpgui.SoundData;
import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.util.ChatColorUtil;
import de.marcely.warpgui.util.ItemStackStringifier;
import de.marcely.warpgui.util.ItemStackUtil;
import de.marcely.warpgui.util.YamlConfigurationDescriptor;
import de.marcely.warpgui.util.gui.CenterFormat;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GeneralConfig {

    public static String inventoryTitle = "Warps";
    public static String inventoryTitlePage = "Warps - Page {page}";
    public static int inventoryHeight = 6;
    public static CenterFormat inventoryCenterX = CenterFormat.CENTRALIZED;
    public static CenterFormat inventoryCenterY = null;
    public static int inventoryOffsetTop = 1;
    public static int inventoryOffsetRight = 1;
    public static int inventoryOffsetLeft = 1;
    public static int inventoryOffsetBottom = 1;
    public static ItemStack inventoryBackgroundMaterial = null;
    public static ItemStack inventoryOffsetMaterial = null;

    public static ItemStack nextPageBarMaterial = ItemStackUtil.setEmptyName(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem());
    public static ItemStack nextPageButtonMaterial = XMaterial.ARROW.parseItem();

    public static SoundData soundOpen = new SoundData(XSound.ENTITY_CHICKEN_EGG, .7f, 1.5f);
    public static SoundData soundClose = new SoundData(XSound.ENTITY_CHICKEN_EGG, .7f, .5f);
    public static SoundData soundClickWarp = new SoundData(XSound.UI_BUTTON_CLICK, .7f, 1.5f);
    public static SoundData soundTeleportWarp = new SoundData(XSound.BLOCK_END_PORTAL_SPAWN, .3f, 2f);
    public static SoundData soundSwitchPage = new SoundData(XSound.ENTITY_HORSE_STEP_WOOD, 1f, 1.2f);

    public static Set<String> listenToCommands = new HashSet<>(Arrays.asList("warp", "warps"));

    public static void loadAndUpdate(WarpGUIPlugin plugin) {
        final File file = getFile(plugin);

        if (!file.exists()) {
            save(plugin);
            return;
        }

        final ItemStackStringifier isStringifier = plugin.getItemStackStringifier();
        final YamlConfiguration root = YamlConfiguration.loadConfiguration(file);

        inventoryTitle = ChatColorUtil.translate(root.getString("inventory-title", "Warps"));
        inventoryTitlePage = ChatColorUtil.translate(root.getString("inventory-title-page", "Warps - Page {page}"));
        inventoryHeight = Math.min(6, root.getInt("inventory-height", -1));
        inventoryCenterX = getCenterFormat(root, "inventory-center-x");
        inventoryCenterY = getCenterFormat(root, "inventory-center-y");
        inventoryOffsetTop = root.getInt("inventory-offset-top", 1);
        inventoryOffsetRight = root.getInt("inventory-offset-right", 1);
        inventoryOffsetLeft = root.getInt("inventory-offset-left", 1);
        inventoryOffsetBottom = root.getInt("inventory-offset-bottom", 1);
        inventoryBackgroundMaterial = ItemStackUtil.setEmptyName(
                isStringifier.parse(root.getString("inventory-background-material", "")));
        inventoryOffsetMaterial = ItemStackUtil.setEmptyName(
                isStringifier.parse(root.getString("inventory-offset-material", "")));

        nextPageBarMaterial = ItemStackUtil.setEmptyName(
                isStringifier.parse(root.getString("next-page-bar-material", "")));
        nextPageButtonMaterial = isStringifier.parse(root.getString("next-page-button-material", ""));

        soundOpen = SoundData.parse(root, "sound.open");
        soundClose = SoundData.parse(root, "sound.close");
        soundClickWarp = SoundData.parse(root, "sound.click-warp");
        soundTeleportWarp = SoundData.parse(root, "sound.teleport-warp");
        soundSwitchPage = SoundData.parse(root, "sound.switch-page");

        listenToCommands = root.getStringList("listen-to-commands").stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(HashSet::new));

        if (listenToCommands.isEmpty())
            listenToCommands.addAll(Arrays.asList("warp", "warps"));

        if (!root.getString("version", "").equals(plugin.getDescription().getVersion())) // auto-update
            save(plugin);
    }

    public static void save(WarpGUIPlugin plugin) {
        final ItemStackStringifier isStringifier = plugin.getItemStackStringifier();
        final YamlConfigurationDescriptor root = new YamlConfigurationDescriptor();

        root.set("version", plugin.getDescription().getVersion());

        root.addEmptyLine();

        root.addComment("The title of the inventory");
        root.set("inventory-title", inventoryTitle);

        root.addEmptyLine();

        root.addComment("The title of the inventory that will be displayed on pages 2+");
        root.set("inventory-title-page", inventoryTitlePage);

        root.addEmptyLine();

        root.addComment("The height of the inventory. Must be between 1-6. Use -1 for auto resize");
        root.set("inventory-height", inventoryHeight);

        root.addEmptyLine();

        root.addComment("Whether an auto-formatting shall be applied at the given axis");
        root.addComment("You may choose between:");
        root.addComment(" - NONE: Items get added from left to right / top to bottom");
        root.addComment(" - CENTRALIZED: Items will be formatted as OOOXXXOOO (X = Item, O = Air)");
        root.addComment(" - ALIGNED: Items will be formatted as OXOXOXOXO (X = Item, O = Air)");
        root.set("inventory-center-x", inventoryCenterX != null ? inventoryCenterX.name() : "NONE");
        root.set("inventory-center-y", inventoryCenterY != null ? inventoryCenterY.name() : "NONE");

        root.addEmptyLine();

        root.addComment("The amount of offset there shall be for the warp items");
        root.set("inventory-offset-top", inventoryOffsetTop);
        root.set("inventory-offset-right", inventoryOffsetRight);
        root.set("inventory-offset-left", inventoryOffsetLeft);
        root.set("inventory-offset-bottom", inventoryOffsetBottom);

        root.addEmptyLine();

        root.addComment("Fill the empty slots with some material");
        root.set("inventory-background-material", isStringifier.serialize(inventoryBackgroundMaterial));
        root.set("inventory-offset-material", isStringifier.serialize(inventoryOffsetMaterial));

        root.addEmptyLine();

        root.addComment("A bar at the bottom will be displayed with a button to switch to the next page in case there are too many items");
        root.set("next-page-bar-material", isStringifier.serialize(nextPageBarMaterial));
        root.set("next-page-button-material", isStringifier.serialize(nextPageButtonMaterial));

        root.addEmptyLine();

        root.addComment("Sound that is being played on various events");
        root.addComment("Note that custom resource pack sounds only are supported with 1.9+");
        soundOpen.serialize(root, "sound.open");
        soundClose.serialize(root, "sound.close");
        soundClickWarp.serialize(root, "sound.click-warp");
        soundTeleportWarp.serialize(root, "sound.teleport-warp");
        soundSwitchPage.serialize(root, "sound.switch-page");

        root.addEmptyLine();

        root.addComment("The commands with which the warp GUI shall open");
        root.set("listen-to-commands", new ArrayList<>(listenToCommands));

        try {
            root.save(getFile(plugin));
        } catch(Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save " + getFile(plugin).getName() +
                    " (Possible out of storage or missing permissions?)", e);
        }
    }

    private static File getFile(Plugin plugin) {
        return new File(plugin.getDataFolder(), "config.yml");
    }

    @Nullable
    private static CenterFormat getCenterFormat(Configuration conf, String key) {
        final String value = conf.getString(key, null);

        if (value == null || value.equalsIgnoreCase("none"))
            return null;

        try {
            return CenterFormat.valueOf(value.toUpperCase());
        } catch(Exception e) {
            return null;
        }
    }
}
