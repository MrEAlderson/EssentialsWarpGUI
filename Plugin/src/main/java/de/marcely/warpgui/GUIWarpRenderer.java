package de.marcely.warpgui;

import de.marcely.warpgui.storage.GeneralConfig;
import de.marcely.warpgui.util.gui.AddItemCondition;
import de.marcely.warpgui.util.gui.ChestGUI;
import de.marcely.warpgui.util.gui.ClickListener;
import de.marcely.warpgui.util.gui.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GUIWarpRenderer {

    private final GUIWarpContainer container;

    private boolean supportsAsync = true;

    public GUIWarpRenderer(GUIWarpContainer container) {
        this.container = container;
    }

    public void open(Player player) {
        open(player, 1, new AtomicBoolean(true), true);
    }

    public void open(Player player, int page, AtomicBoolean closeSound, boolean openSound) {
        if (!this.supportsAsync) {
            if (Bukkit.isPrimaryThread())
                openNow(player, page, closeSound, openSound);
            else {
                Bukkit.getScheduler().runTask(this.container.getPlugin(), () -> {
                   openNow(player, page, closeSound, openSound);
                });
            }

            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.container.getPlugin(), () -> {
            try {
                openNow(player, page, closeSound, openSound);
            } catch(ConcurrentModificationException e) {
                this.container.getPlugin().getLogger().log(
                        Level.SEVERE,
                        "Server does not support async inventories. Opening them on the main-thread from now on",
                        e);

                this.supportsAsync = false;
            }
        });
    }

    private void openNow(Player player, int page, AtomicBoolean closeSound, boolean openSound) {
        try {
            openNowUnchecked(player, page, closeSound, openSound);
        } catch(Exception e) {
            this.container.getPlugin().getLogger().log(
                    Level.SEVERE,
                    "An error occurred while opening the GUI",
                    e);
        }
    }

    private void openNowUnchecked(Player player, int page, AtomicBoolean closeSound, boolean openSound) throws Exception {
        // debug
        this.container.getPlugin().getProvider().fetchWarps();

        final Logger logger = this.container.getPlugin().getLogger();
        final boolean heightFixed = GeneralConfig.inventoryHeight <= 0;

        // validate whether offsets are even possible...
        if (GeneralConfig.inventoryOffsetLeft + GeneralConfig.inventoryOffsetRight >= 9) {
            GeneralConfig.inventoryOffsetLeft = 0;
            GeneralConfig.inventoryOffsetRight = 0;

            logger.warning("Detected that the offsets on the x-axis are too large. They have been reset.");
        }

        if (GeneralConfig.inventoryOffsetTop + GeneralConfig.inventoryOffsetTop >= 5) {
            GeneralConfig.inventoryOffsetTop = 0;
            GeneralConfig.inventoryOffsetBottom = 0;

            logger.warning("Detected that the offsets on the y-axis are too large. They have been reset.");
        }

        // do some calculations
        final int offsetX = GeneralConfig.inventoryOffsetRight + GeneralConfig.inventoryOffsetLeft;
        final int offsetY = GeneralConfig.inventoryOffsetTop + Math.max(1, GeneralConfig.inventoryOffsetBottom);
        final int maxItemsCount = 6*9
                - (offsetY)*9
                - (offsetX)*(6-offsetY);
        final List<GUIWarp> addItems = this.container.getAll().stream()
                .filter(k -> k.hasHook() && k.isOutOfBounds() && k.getHook().hasPermission(player))
                .sorted(Comparator.comparing(k ->
                        k.getColorlessDisplayName().toLowerCase()))
                .skip(maxItemsCount*(page-1))
                .limit(maxItemsCount+1 /* so that we know whether there is a next page */)
                .collect(Collectors.toList());
        final boolean hasNextPage = addItems.size() > maxItemsCount;
        final int guiHeight = Math.min(6, heightFixed ?
                GeneralConfig.inventoryHeight :
                Math.max(1, (int) Math.ceil(addItems.size() / ((double) 9-offsetX)))+offsetY);
        final String guiTitle =
                (page != 1 ? GeneralConfig.inventoryTitlePage : GeneralConfig.inventoryTitle)
                        .replace("{page}", "" + page);

        // render gui
        final ChestGUI gui = new ChestGUI(guiHeight, guiTitle){
            @Override
            public void onClose(Player player) {
                if (closeSound.get())
                    GeneralConfig.soundClose.play(player);
            }
        };
        final AddItemCondition addCondition = AddItemCondition.within(
                        GeneralConfig.inventoryOffsetLeft,
                        8-GeneralConfig.inventoryOffsetRight,
                        GeneralConfig.inventoryOffsetTop,
                        5-Math.max(1, GeneralConfig.inventoryOffsetBottom));

        // add items
        for (GUIWarp warp : addItems) {
            gui.addItem(
                    getGUIItem(warp, closeSound),
                    addCondition
            );
        }

        // format items
        if (GeneralConfig.inventoryCenterX != null) {
            gui.formatAnyRow(
                    GeneralConfig.inventoryCenterX,
                    GeneralConfig.inventoryOffsetLeft,
                    8-GeneralConfig.inventoryOffsetRight
            );
        }
        if (GeneralConfig.inventoryCenterY != null) {
            gui.formatAnyColumn(
                    GeneralConfig.inventoryCenterY,
                    GeneralConfig.inventoryOffsetTop,
                    5-Math.max(1, GeneralConfig.inventoryOffsetBottom));
        }

        // border
        if (GeneralConfig.inventoryOffsetMaterial != null) {
            final GUIItem borderItem = new GUIItem(GeneralConfig.inventoryOffsetMaterial, ClickListener.Silent.INSTANCE);

            if (offsetY > 0) {
                for (int ix = 0; ix < 9; ix++) {
                    for (int iy = 0; iy < GeneralConfig.inventoryOffsetTop; iy++)
                        gui.setItem(borderItem, ix, iy);

                    for (int iy = 0; iy < GeneralConfig.inventoryOffsetBottom; iy++)
                        gui.setItem(borderItem, ix, guiHeight-iy-1);
                }
            }

            if (offsetX > 0) {
                for (int iy = 0; iy < guiHeight; iy++) {
                    for (int ix = 0; ix < GeneralConfig.inventoryOffsetLeft; ix++)
                        gui.setItem(borderItem, ix, iy);

                    for (int ix = 0; ix < GeneralConfig.inventoryOffsetRight; ix++)
                        gui.setItem(borderItem, 8-ix, iy);
                }
            }
        }

        // page bar
        if (GeneralConfig.nextPageButtonMaterial != null && (hasNextPage || page != 1)) {
            final int y = guiHeight-1;

            // prev page
            if (page > 1)
                gui.setItem(getChangePageButton(page, -1, closeSound), 0, y);

            // next page
            if (page < 1 || hasNextPage)
                gui.setItem(getChangePageButton(page, 1, closeSound), 8, y);

            // deco
            if (GeneralConfig.nextPageBarMaterial != null) {
                final GUIItem item = new GUIItem(GeneralConfig.nextPageBarMaterial, ClickListener.Silent.INSTANCE);

                for (int ix=0; ix<9; ix++) {
                    if (gui.getItem(ix, y) == null)
                        gui.setItem(item, ix, y);
                }
            }
        }

        // set items
        final int pastGuiSlotsCount = (heightFixed ? GeneralConfig.inventoryHeight : 6)*9;
        final List<GUIWarp> setItems = this.container.getAll().stream()
                .filter(warp ->
                        warp.hasHook() &&
                        !warp.isOutOfBounds() &&
                        page-1 == warp.getSlotY()/(pastGuiSlotsCount/(9-offsetX)) &&
                        warp.getHook().hasPermission(player))
                .collect(Collectors.toList());

        for (GUIWarp warp : setItems) {
            final int y = warp.getSlotY()%(pastGuiSlotsCount/(9-offsetX));

            if (y >= gui.getHeight())
                gui.setHeight(6);

            gui.setItem(
                    getGUIItem(warp, closeSound),
                    warp.getSlotX(),
                    y
            );
        }

        // fill space
        if (GeneralConfig.inventoryBackgroundMaterial != null)
            gui.fillSpace(GeneralConfig.inventoryBackgroundMaterial);

        // hooray!
        if (openSound)
            GeneralConfig.soundOpen.play(player);

        Bukkit.getScheduler().runTask(this.container.getPlugin(), () -> { // must be synced, otherwise there will be an error
            gui.open(player);
        });
    }

    private GUIItem getGUIItem(GUIWarp warp, AtomicBoolean closeSound) {
        return new GUIItem(warp.getDisplayedIcon(), (player, leftClick, shiftClick) -> {
            if (warp.hasHook()) {
                closeSound.set(false);
                warp.getHook().teleport(player);
                closeSound.set(true); // teleport possibly failed
            } else
                Message.WARP_HOOK_DISAPPEAR.send(player);

            GeneralConfig.soundClickWarp.play(player);
        });
    }

    private GUIItem getChangePageButton(int currentPage, int rel, AtomicBoolean closeSound) {
        final ItemStack is = GeneralConfig.nextPageButtonMaterial.clone();
        final ItemMeta im = is.getItemMeta();

        im.setDisplayName((rel >= 1 ? Message.NEXT_PAGE : Message.PREV_PAGE).get());
        im.setLore(Arrays.asList(Message.PAGE_INFO
                .placeholder("new-page", "" + (currentPage+rel))
                .placeholder("old-page", "" + currentPage)
                .getLines()));

        is.setItemMeta(im);

        return new GUIItem(is, (player, leftClick, shiftClick) -> {
            GeneralConfig.soundSwitchPage.play(player);

            closeSound.set(false);
            open(player, currentPage+rel, new AtomicBoolean(true), false);
        });
    }
}
