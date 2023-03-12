package de.marcely.warpgui.config;

import de.marcely.configmanager2.EZConfigManager;
import de.marcely.configmanager2.IOResult;
import de.marcely.configmanager2.objects.*;
import de.marcely.warpgui.WarpV2;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Deprecated
public class WarpV2Config {

    @Nullable
    public static Collection<WarpV2> loadLegacyFiles(Logger logger, Function<String, Optional<ItemStack>> itemParser) {
        final List<WarpV2> warps = new ArrayList<>();

        // convert v1 to v2
        {
            final File legacyFile = WarpConfig.getFile();

            if (legacyFile.exists()) {
                final WarpConfig legacyConfig = WarpConfig.load(logger);

                if (legacyConfig == null)
                    return null;

                warps.addAll(legacyConfig.getWarps().stream()
                        .map(lWarp -> {
                            final WarpV2 nWarp = new WarpV2();
                            final Optional<ItemStack> icon = itemParser.apply(lWarp.getIconName());

                            nWarp.setName(lWarp.getName());
                            nWarp.setIcon(icon.isPresent() ? icon.get() : new ItemStack(Material.STONE));
                            nWarp.setPrefix(lWarp.getPrefix());
                            nWarp.setSuffix("");
                            nWarp.setDisplayName(lWarp.getName());
                            nWarp.setForceSlot(null);
                            nWarp.setLores(lWarp.getLores());

                            return nWarp;
                        })
                        .collect(Collectors.toList()));
            }
        }

        // load v2
        final EZConfigManager cm = new EZConfigManager(getFile(), false);

        if (!cm.getFile().exists())
            return warps;

        if (cm.load() != IOResult.RESULT_SUCCESS)
            return null;

        for(Tree warpTree:cm.getRootTree().getTreeChilds()){
            final WarpV2 warp = new WarpV2();

            warp.setName(warpTree.getName());
            warp.setIcon(new ItemStack(Material.STONE));
            warp.setPrefix("");
            warp.setSuffix("");
            warp.setDisplayName(warpTree.getName());
            warp.setForceSlot(null);
            warp.setLores(new ArrayList<>());

            // load base configs
            {
                final Config config_icon = warpTree.getConfigChild("icon");
                final Config config_prefix = warpTree.getConfigChild("prefix");
                final Config config_suffix = warpTree.getConfigChild("suffix");
                final Config config_displayName = warpTree.getConfigChild("display-name");
                final Config config_forceSlot = warpTree.getConfigChild("force-slot");

                if(config_icon != null){
                    final Optional<ItemStack> is = itemParser.apply(config_icon.getValue());

                    if(is.isPresent())
                        warp.setIcon(is.get());
                    else
                        logger.warning("Failed to parse icon of warp \"" + warp.getName() + "\": It has been reset back to default");
                }

                if(config_prefix != null && config_prefix.getValue() != null)
                    warp.setPrefix(config_prefix.getValue());

                if(config_suffix != null && config_suffix.getValue() != null)
                    warp.setSuffix(config_suffix.getValue());

                if(config_displayName != null && config_displayName.getValue() != null)
                    warp.setDisplayName(config_displayName.getValue());

                if(config_forceSlot != null)
                    warp.setForceSlot(config_forceSlot.getValueAsInt());
            }

            // load lore
            final Tree loreTree = warpTree.getTreeChild("lore");

            if(loreTree != null){
                for(Config config:loreTree.getChilds()){
                    if(config.getType() != ListItem.TYPE_LISTITEM)
                        continue;

                    warp.getLores().add(config.getValue());
                }
            }

            warps.add(warp);
        }

        return warps;
    }

    private static File getFile() {
        return new File("plugins/Essentials_WarpGUI/warps.yml");
    }

    public static boolean existLegacyFiles() {
        return WarpConfig.getFile().exists() || getFile().exists();
    }

    public static void deleteLegacyFiles() {
        WarpConfig.getFile().delete();
        getFile().delete();
    }
}