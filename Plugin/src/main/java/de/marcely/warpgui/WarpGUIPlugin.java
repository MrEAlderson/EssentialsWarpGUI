package de.marcely.warpgui;

import de.marcely.warpgui.command.WarpCommandInjector;
import de.marcely.warpgui.command.WarpConfigCommand;
import de.marcely.warpgui.warp.provider.WarpProvider;
import de.marcely.warpgui.storage.GeneralConfig;
import de.marcely.warpgui.storage.WarpsStorage;
import de.marcely.warpgui.storage.MessagesConfig;
import de.marcely.warpgui.util.AdaptedGson;
import de.marcely.warpgui.util.gui.GUIContainer;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class WarpGUIPlugin extends JavaPlugin {

    @Getter
    private WarpProvider<?> provider = null;
    @Getter
    private final GUIWarpContainer container = new GUIWarpContainer(this);
    @Getter
    private final GUIWarpRenderer renderer = new GUIWarpRenderer(container);

    @Override
    public void onEnable() {
        // find provider
        this.provider = WarpProvider.findProvider(this);

        if (this.provider == null) {
            getLogger().warning("Could not find a valid provider!");
            getLogger().warning("Make sure that you have either of the following plugins installed: " +
                    String.join(", ", WarpProvider.getLegalProviderPlugins()));
            getLogger().warning("Shutting down plugin...");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            this.provider.register();
        } catch(Exception e) {
            getLogger().log(
                    Level.SEVERE,
                    "Failed to register the provider " + this.provider.getName() + " v" + this.provider.getVersion() +
                            ". Updating it could potentially fix it.",
                    e);
            getLogger().warning("Shutting down plugin...");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // init and load everything
        GUIContainer.init(this);
        AdaptedGson.init(this);

        Bukkit.getPluginManager().registerEvents(new WarpCommandInjector(this.renderer), this);

        reloadConfig();

        {
            final WarpConfigCommand cmd = new WarpConfigCommand();

            cmd.registerDefaultCommands(this);

            getCommand("warpcfg").setExecutor(cmd);
            getCommand("warpcfg").setTabCompleter(cmd);
        }

        // metrics
        {
            final Metrics metrics = new Metrics(this, 17931);

            metrics.addCustomChart(new SimplePie("used_provider", () -> {
                return this.provider.getName();
            }));
            metrics.addCustomChart(new SimplePie("used_provider_version", () -> {
                return this.provider.getName() + " v" + this.provider.getVersion();
            }));
            metrics.addCustomChart(new SingleLineChart("warps_amount", () -> {
                return this.provider.getWarps().size();
            }));
        }
    }

    @Override
    public void onDisable() {
        if (GUIContainer.getInstance() != null)
            GUIContainer.getInstance().closeAll();

        if (this.provider != null)
            this.provider.unregister();
    }

    @Override
    public void reloadConfig() {
        GUIContainer.getInstance().closeAll();

        MessagesConfig.loadAndUpdate(this);
        GeneralConfig.loadAndUpdate(this);
        WarpsStorage.loadAll(this.container);
    }
}
