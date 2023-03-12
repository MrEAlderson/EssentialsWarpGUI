package de.marcely.warpgui.storage;

import de.marcely.warpgui.Message;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Level;

public class MessagesConfig {

    public static void loadAndUpdate(Plugin plugin) {
        final File file = getFile(plugin);

        if (!file.exists()) {
            save(plugin);
            return;
        }

        final YamlConfiguration root = YamlConfiguration.loadConfiguration(file);
        int missingEntries = 0;

        for (String key : root.getKeys(false)) {
            final Message msg = Message.getByKey(key);

            if (msg == null || !root.isString(key) && !root.isList(key)) {
                missingEntries++;
                continue;
            }

            if (root.isString(key))
                msg.setConfigMessage(root.getString(key));
            else
                msg.setConfigMessage(String.join("\n", root.getStringList(key)));
        }

        if (missingEntries >= 1)
            save(plugin);
    }

    private static void save(Plugin plugin) {
        final YamlConfiguration root = new YamlConfiguration();

        for (Message msg : Message.values()) {
            root.set(
                    msg.getKey(),
                    msg.getConfigMessage()
            );
        }

        try {
            root.save(getFile(plugin));
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save " + getFile(plugin).getName() +
                    " (Possible out of storage or missing permissions?)", e);
        }
    }

    private static File getFile(Plugin plugin) {
        return new File(plugin.getDataFolder(), "messages.yml");
    }
}