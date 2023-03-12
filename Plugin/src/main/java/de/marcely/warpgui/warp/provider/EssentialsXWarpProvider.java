package de.marcely.warpgui.warp.provider;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IConf;
import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.warp.EssentialsXWarp;
import lombok.Getter;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EssentialsXWarpProvider implements WarpProvider<EssentialsXWarp> {

    @Getter
    protected final WarpGUIPlugin plugin;
    @Getter
    protected final IEssentials hook;

    protected final Map<String, EssentialsXWarp> warps = new ConcurrentHashMap<>();

    @Getter
    private PluginCommand warpCommand;
    private IConf reloadListener;

    public EssentialsXWarpProvider(WarpGUIPlugin guiPlugin, Plugin essentialsPlugin) {
        this.plugin = guiPlugin;
        this.hook = (IEssentials) essentialsPlugin;
    }

    @Override
    public String getName() {
        return "EssentialsX";
    }

    @Override
    public String getVersion() {
        return this.hook.getDescription().getVersion();
    }

    @Override
    public void register() throws Exception {
        if ((this.warpCommand = ((JavaPlugin) this.hook).getCommand("warp")) == null)
            throw new IllegalStateException("EssentialsX didn't register warp command");

        this.hook.addReloadListener(this.reloadListener = () -> {
            // a tick later to make sure that everything has been loaded
            Bukkit.getScheduler().runTaskLater(
                    this.plugin,
                    this::fetchWarps,
                    1
            );
        });

        fetchWarps();
    }

    @Override
    public void unregister() {
        try{
            // try to remove reload listener. yes, it's actually that complicated.
            final Field field = Essentials.class.getDeclaredField("confList");

            field.setAccessible(true);
            ((Collection<IConf>) field.get(this.hook)).remove(this.reloadListener);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Collection<EssentialsXWarp> getWarps() {
        return this.warps.values();
    }

    @Override
    public @Nullable EssentialsXWarp getWarp(String name) {
        return this.warps.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public void fetchWarps() {
        this.warps.clear();

        for (String name : this.hook.getWarps().getList()) {
            this.warps.put(
                    name.toLowerCase(Locale.ENGLISH),
                    new EssentialsXWarp(this, name));
        }

        updateGUIHooks();
    }
}
