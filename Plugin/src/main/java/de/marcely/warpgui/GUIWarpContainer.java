package de.marcely.warpgui;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIWarpContainer {

    @Getter
    private final WarpGUIPlugin plugin;

    private final Map<String, GUIWarp> warps = new ConcurrentHashMap<>();

    public GUIWarpContainer(WarpGUIPlugin plugin) {
        this.plugin = plugin;
    }

    public Collection<GUIWarp> getAll() {
        return this.warps.values();
    }

    public void clear() {
        this.warps.clear();
    }

    public boolean add(GUIWarp warp) {
        if (warp.getContainer() != this)
            throw new IllegalStateException("Warp container mismatch");

        warp.updateAttributes();

        return this.warps.putIfAbsent(warp.getName().toLowerCase(Locale.ENGLISH), warp) == null;
    }

    public boolean remove(GUIWarp warp) {
        return this.warps.remove(warp.getName().toLowerCase(Locale.ENGLISH), warp);
    }

    @Nullable
    public GUIWarp getHooked(String name) {
        final GUIWarp warp = this.warps.get(name.toLowerCase(Locale.ENGLISH));

        if (warp == null || !warp.hasHook())
            return null;

        return warp;
    }
}
