package de.marcely.warpgui.util.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

public class GUIContainer implements Listener {

    private static GUIContainer INSTANCE;

    final Map<Player, GUI> openInventories = new HashMap<>();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final GUI gui = this.openInventories.get(player);

        if (gui != null) {
            // cancel shift to put item into inv
            if (event.getClickedInventory() != null &&
                    event.getClickedInventory().getType() == InventoryType.PLAYER &&
                    event.isShiftClick() && !gui.areItemsMoveable()) {

                event.setCancelled(true);
                return;
            }

            // cancell if clicking on item
            if ((event.getClickedInventory() != null &&
                    event.getClickedInventory().getType() != InventoryType.PLAYER ||
                    event.getClick() == ClickType.DOUBLE_CLICK)) {

                if (gui.areItemsMoveable())
                    return;

                event.setCancelled(true);

                if (gui instanceof ClickableGUI) {
                    final ClickableGUI cgui = (ClickableGUI) gui;
                    final GUIItem item = cgui.getItem(event.getSlot());

                    if (item != null)
                        item.getListener().onClick(player, event.isLeftClick(), event.isShiftClick());

                }
            }
        }
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        final Player player = (Player) event.getWhoClicked();

        // cancel if clicking on GUI
        if (event.getInventory().getType() != InventoryType.PLAYER) {
            final GUI gui = this.openInventories.get(player);

            if (gui != null && !gui.areItemsMoveable())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        final GUI gui = this.openInventories.get(player);

        if (gui != null && !gui.ignoresCancelEvent()) {
            this.openInventories.remove(player);

            gui.onClose(player);
            return;
        }
    }

    public void closeAll() {
        for (Player player : new ArrayList<>(this.openInventories.keySet()))
            player.closeInventory();
    }

    public static void init(Plugin plugin) {
        if (INSTANCE != null)
            throw new RuntimeException("Already initialized");

        Bukkit.getPluginManager().registerEvents(INSTANCE = new GUIContainer(), plugin);
    }

    public static GUIContainer getInstance() {
        return INSTANCE;
    }
}