package de.marcely.warpgui.warp;

import de.marcely.warpgui.warp.provider.WarpProvider;
import org.bukkit.entity.Player;

public interface Warp {

    String getName();

    WarpProvider<?> getProvider();

    boolean hasPermission(Player player);

    void teleport(Player player);

    default boolean exists() {
        return getProvider().getWarps().contains(this);
    }
}