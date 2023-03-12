package de.marcely.warpgui.util;

import com.google.gson.*;
import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.WarpGUIPlugin;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Base64;

public class AdaptedGson {

    private static Gson instance = null;

    public static Gson get() {
        return instance;
    }

    public static void init(WarpGUIPlugin plugin) {
        instance = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(
                        GUIWarp.class,
                        (InstanceCreator<?>) type -> new GUIWarp(plugin.getContainer(), "error"))
                .registerTypeHierarchyAdapter(ConfigurationSerializable.class, new ConfigurationSerializableAdapter())
                .create();
    }

    private static class ConfigurationSerializableAdapter implements JsonSerializer<ConfigurationSerializable>, JsonDeserializer<ConfigurationSerializable> {

        @Override
        public ConfigurationSerializable deserialize(
                JsonElement json,
                Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {

            try {
                final ByteArrayInputStream array = new ByteArrayInputStream(Base64.getDecoder().decode(json.getAsString()));
                final BukkitObjectInputStream obj = new BukkitObjectInputStream(array);

                return (ConfigurationSerializable) obj.readObject();
            } catch(JsonParseException e) {
                throw e;
            } catch(Exception e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(
                ConfigurationSerializable src,
                Type typeOfSrc,
                JsonSerializationContext context) {

            try {
                final ByteArrayOutputStream array = new ByteArrayOutputStream();
                final BukkitObjectOutputStream obj = new BukkitObjectOutputStream(array);

                obj.writeObject(src);
                obj.flush();

                return new JsonPrimitive(Base64.getEncoder().encodeToString(array.toByteArray()));
            } catch(JsonParseException e) {
                throw e;
            } catch(Exception e) {
                throw new JsonParseException(e);
            }
        }
    }

}
