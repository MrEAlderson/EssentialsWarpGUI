package de.marcely.warpgui.storage;

import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.GUIWarpContainer;
import de.marcely.warpgui.WarpV2;
import de.marcely.warpgui.config.WarpV2Config;
import de.marcely.warpgui.util.AdaptedGson;
import de.marcely.warpgui.util.ItemStackStringifier;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WarpsStorage {

    public static void loadAll(GUIWarpContainer container) {
        try {
            final File folder = getFolder(container.getPlugin());

            container.clear();

            if (!folder.exists() || folder.list().length == 0) {
                // try to convert from legacy
                final Logger logger = container.getPlugin().getLogger();

                if (WarpV2Config.existLegacyFiles()) {
                    logger.info("Detected that you are coming from a legacy version! Auto-upgrading the warps...");

                    final ItemStackStringifier isStringifier = container.getPlugin().getItemStackStringifier();
                    final Collection<WarpV2> legacy = WarpV2Config.loadLegacyFiles(
                            logger,
                            str -> Optional.ofNullable(isStringifier.parse(str))
                    );

                    if (legacy == null)
                        return;

                    boolean allSuccessful = true;

                    for (WarpV2 lWarp : legacy) {
                        final GUIWarp nWarp = new GUIWarp(container, lWarp.getName());

                        nWarp.setDisplayName(lWarp.getPrefix() + lWarp.getDisplayName() + lWarp.getSuffix());
                        nWarp.setBaseIcon(lWarp.getIcon());
                        nWarp.setLore(lWarp.getLores());

                        if (lWarp.getForceSlot() != null) {
                            nWarp.setSlotX(lWarp.getForceSlot()%9);
                            nWarp.setSlotY(lWarp.getForceSlot()/9);
                        }

                        container.add(nWarp);

                        if (!save(nWarp))
                            allSuccessful = false;

                        logger.info("Converted warp \"" + nWarp.getName() + "\"");
                    }

                    if (allSuccessful)
                        WarpV2Config.deleteLegacyFiles();
                }

                return;
            }

            for (File file : folder.listFiles()) {
                if (!file.getName().endsWith(".json"))
                    continue;

                load(container, file);
            }

        } finally {
            // re-add missing ones
            container.getPlugin().getProvider().updateGUIHooks();
        }
    }

    private static void load(GUIWarpContainer container, File file) {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            final GUIWarp warp = AdaptedGson.get().fromJson(reader, GUIWarp.class);

            container.add(warp);
        }catch (Exception e) {
            container.getPlugin().getLogger().log(Level.SEVERE, "Failed to load warp " + file.getName() +
                    " (Possibly missing permissions or corrupted file?)", e);
        }
    }

    public static boolean save(GUIWarp warp) {
        try {
            final File file = getFile(warp);

            file.getParentFile().mkdirs();

            if (!warp.exists() || warp.isDefault()) {
                file.delete();
                return true;
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
                AdaptedGson.get().toJson(warp, writer);
            }

            return true;
        } catch(Exception e) {
            warp.getContainer().getPlugin().getLogger().log(Level.SEVERE, "Failed to save warp " + warp.getName() +
                    " (Possible out of storage or missing permissions?)", e);
            return false;
        }
    }

    private static File getFolder(Plugin plugin) {
        return new File(plugin.getDataFolder(), "warps");
    }

    private static File getFile(GUIWarp warp) {
        final File folder = getFolder(warp.getContainer().getPlugin());
        final Pattern pattern = Pattern.compile("[\\\\/:*?\\\"<>|\"?*.$]");
        String fileName = warp.getName().replace("@", "$");
        Matcher match = null;

        while ((match = pattern.matcher(fileName)).find()) {
            final String invalidChars = fileName.substring(match.start(), match.end());
            final String replacement = invalidChars.chars()
                    .mapToObj(i -> "@" + Integer.toHexString(i) + "@")
                    .collect(Collectors.joining(""));

            fileName = fileName.replace(
                    invalidChars,
                    replacement);
        }

        return new File(folder, fileName + ".json");
    }
}
