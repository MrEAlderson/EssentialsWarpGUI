package de.marcely.warpgui.command;

import de.marcely.warpgui.GUIWarpRenderer;
import de.marcely.warpgui.storage.GeneralConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class WarpCommandInjector implements Listener {

    private final GUIWarpRenderer renderer;

    public WarpCommandInjector(GUIWarpRenderer renderer) {
        this.renderer = renderer;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        final String[] parts = event.getMessage()
                .split(" ");

        if (parts.length != 1)
            return;

        final String cmd = parts[0]
                .substring(1)
                .toLowerCase();

        if (!GeneralConfig.listenToCommands.contains(cmd))
            return;

        this.renderer.open(event.getPlayer());

        event.setCancelled(true);
    }
}
