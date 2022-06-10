package de.marcely.warpgui.version;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import de.marcely.warpgui.EssentialsWarpGUI;
import lombok.Getter;

public enum Version {

    R1_8(8, 1),
    R2_8(8, 2),
    R3_8(8, 3),
    R1_9(9, 1),
    R2_9(9, 2),

    R1_10(10, 1),

    R1_11(11, 1),

    R1_12(12, 1),

    R1_13(13, 1),
    R2_13(13, 2),

    R1_14(14, 1),

    R1_15(15, 1),

    R1_16(16, 1),
    R2_16(16, 2),
    R3_16(16, 3),

    R1_17(17, 1),

    R1_18(18, 1),
    R2_18(18, 2),

    R1_19(19, 1);

    @Getter
    private static VersionInstance current;

    @Getter
    private final int minor, revision;

    Version(int minor, int revision){
        this.minor = minor;
        this.revision = revision;
    }

    public static boolean init(EssentialsWarpGUI plugin){
        VersionInstance instance = null;

        try{
            try{
                instance = fetchVersion(Bukkit.getBukkitVersion());

                if(instance == null)
                    throw new IllegalStateException();
            }catch(Exception e1){
                instance = fetchVersion(Bukkit.getVersion());

                if(instance == null)
                    throw new IllegalStateException(e1);
            }
        }catch(Exception e){
            e.printStackTrace();

            final Logger logger = plugin.getLogger();
            final Version latestVer = Version.values()[Version.values().length-1];

            instance = new VersionInstance(latestVer, Bukkit.getVersion().split("-")[0], 0);

            logger.warning("IMPORTANT: We weren't able to fetch your plugin version from the strings: \""+Bukkit.getBukkitVersion()+"\" and \""+Bukkit.getVersion()+"\"");
            logger.warning("We'll use the latest supported version ("+latestVer.name()+") for now.");

			/*logger.warning("We only support unmodified variants of Spigot and PaperSpigot or your version of spigot isn't supported.");
			logger.warning("Please replace your server file with a supported build to fix the error");
			logger.warning("Stopping the plugin...");

			Bukkit.getPluginManager().disablePlugin(plugin);

			return false;*/
        }

        Version.current = instance;

        return true;
    }

    private static VersionInstance fetchVersion(String versionString) throws Exception{
        final String PATH = Bukkit.getServer().getClass().getPackage().getName();
        final String VERSION = PATH.substring(PATH.lastIndexOf(".")+1, PATH.length());
        final String[] vs = VERSION.split("_");
        final String enumName = vs[2]+"_"+vs[1];
        final Version ver = ofName(enumName);
        int subVersion = 0;

        final String[] parts = versionString.split("-")[0].split("\\.");

        if(parts.length == 1)
            throw new IllegalStateException("Unable to parse version number (1)");

        final int minor = Integer.valueOf(parts[1]);

        if(parts.length >= 3)
            subVersion = Integer.valueOf(parts[2]);

        if(ver.minor != minor)
            throw new IllegalStateException("Parsed minor number is different than expected (2)");

        return new VersionInstance(ver, Bukkit.getVersion().split("-")[0], subVersion);
    }

    public static @Nullable Version ofName(String name){
        for(Version ver : values()){
            if(ver.name().equals(name))
                return ver;
        }

        return null;
    }
}
