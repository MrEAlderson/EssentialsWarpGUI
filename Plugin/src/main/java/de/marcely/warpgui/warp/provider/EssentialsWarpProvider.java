package de.marcely.warpgui.warp.provider;

import de.marcely.warpgui.WarpGUIPlugin;
import org.bukkit.plugin.Plugin;

public class EssentialsWarpProvider extends EssentialsXWarpProvider {

    public EssentialsWarpProvider(WarpGUIPlugin guiPlugin, Plugin essentialsPlugin) {
        super(guiPlugin, essentialsPlugin);
    }

    @Override
    public String getName() {
        return "Essentials";
    }
}
