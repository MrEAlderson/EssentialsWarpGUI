package de.marcely.warpgui.warp.provider;

import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.GUIWarpContainer;
import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.warp.Warp;
import de.marcely.warpgui.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public interface WarpProvider <K extends Warp> {

    WarpGUIPlugin getPlugin();

    String getName();

    String getVersion();

    void register() throws Exception;

    void unregister();

    Collection<K> getWarps();

    @Nullable
    K getWarp(String name);

    void fetchWarps();


    default void updateGUIHooks() {
        final GUIWarpContainer container = getPlugin().getContainer();

        // update existing
        for (GUIWarp gWarp : new ArrayList<>(container.getAll())) {
            final Warp hWarp = getWarp(gWarp.getName());

            gWarp.setHook(hWarp);

            if (hWarp == null && gWarp.isDefault())
                container.remove(gWarp);
        }

        // add new ones
        for (Warp hWarp : getWarps()) {
            final GUIWarp gWarp = new GUIWarp(container, hWarp.getName());

            gWarp.setHook(hWarp);

            container.add(gWarp);
        }
    }


    @Nullable
    static WarpProvider<?> findProvider(WarpGUIPlugin guiPlugin) {
        final AtomicReference<WarpProvider<?>> foundPlugin = new AtomicReference<>();
        final TriConsumer<String, String, Function<Plugin, WarpProvider<?>>> testPlugin = (name, requiredClass, factory) -> {
            if (foundPlugin.get() != null)
                return;

            final Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

            if (plugin == null)
                return;

            try {
                Class.forName(requiredClass);

                foundPlugin.set(factory.apply(plugin));
            } catch (ClassNotFoundException e) { }
        };

        // top gets prioritized
        testPlugin.accept(
                "EssentialsX",
                "net.essentialsx.api.v2.services.BalanceTop",
                plugin -> new EssentialsXWarpProvider(guiPlugin, plugin));
        testPlugin.accept(
                "Essentials",
                "net.essentialsx.api.v2.services.BalanceTop",
                plugin -> new EssentialsXWarpProvider(guiPlugin, plugin));

        testPlugin.accept(
                "EssentialsX",
                "com.earth2me.essentials.Essentials",
                plugin -> new EssentialsWarpProvider(guiPlugin, plugin));
        testPlugin.accept(
                "Essentials",
                "com.earth2me.essentials.Essentials",
                plugin -> new EssentialsWarpProvider(guiPlugin, plugin));

        return foundPlugin.get();
    }

    static String[] getLegalProviderPlugins() {
        return new String[] {
                "EssentialsX", "Essentials"
        };
    }
}