package de.marcely.warpgui.config;

import de.marcely.warpgui.Warp;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class WarpConfig implements Serializable {

    private static final long serialVersionUID = -1266449831520034396L;

    @Getter
    private List<Warp> warps = new ArrayList<>();

    @Nullable
    public static WarpConfig load(Logger logger) {
        final File file = getFile();

        if (!file.exists())
            return null;

        try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file.toPath()))){
            return (WarpConfig) stream.readObject();
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Failed to load legacy warps data", e);
        }

        return null;
    }

    public static File getFile() {
        return new File("plugins/Essentials_WarpGUI/warps.cfg");
    }
}