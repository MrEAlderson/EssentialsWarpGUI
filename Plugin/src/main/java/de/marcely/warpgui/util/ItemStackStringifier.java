package de.marcely.warpgui.util;

import com.cryptomorin.xseries.XMaterial;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Bogged;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@AllArgsConstructor
public class ItemStackStringifier {

    private static boolean SUPPORTS_NATIVE_ITEM_PARSE;
    private static boolean SUPPORTS_ITEM_COMPONENTS;
    private static final String KEY_ORIGINAL_STRING = "original-string";

    static {
        // can we use the internal item parser (1.19+)?
        try {
            Bukkit.getItemFactory().createItemStack("minecraft:stone");
            SUPPORTS_NATIVE_ITEM_PARSE = true;
        } catch (Error e) {
        }

        // do items now use components instead of NBT (1.20.5+)?
        try {
            Bogged.class.getName();
            SUPPORTS_ITEM_COMPONENTS = true;
        } catch (Error e) {
        }
    }

    private final Plugin plugin;


    @Nullable
    public ItemStack parse(@Nullable String str) {
        if (str == null)
            return null;

        // try to use build-in parser
        ItemStack is = nativeParse(str);

        if (is != null)
            return is;

        // fallbak to legacy parser
        return legacyParse(str);
    }

    @Nullable
    private ItemStack nativeParse(String str) {
        if (!SUPPORTS_NATIVE_ITEM_PARSE)
            return null;

        // remove spaces between material and NBT
        {
            final int nbtStart = str.indexOf('{');

            if (nbtStart != -1) {
                final StringBuilder builder = new StringBuilder(str);
                int spaceIndex;

                while ((spaceIndex = builder.indexOf(" ")) < nbtStart && spaceIndex != -1)
                    builder.deleteCharAt(spaceIndex);

                str = builder.toString();
            }
        }

        // parse :)
        ItemStack is;

        try {
            is = Bukkit.getItemFactory().createItemStack(str);
        } catch (IllegalArgumentException e) {
            // no print as user might not make use of this
            return null;
        }

        // add original string as PDC in case it case re-serialized
        final ItemMeta im = is.getItemMeta();

        if (im == null)
            return is;

        im.getPersistentDataContainer().set(
                new NamespacedKey(this.plugin, KEY_ORIGINAL_STRING),
                PersistentDataType.STRING,
                str
        );
        is.setItemMeta(im);

        return is;
    }

    @Nullable
    private ItemStack legacyParse(String str) {
        final String[] parts = str
                .replace("minecraft:", "")
                .split("\\:");
        ItemStack is = null;
        boolean parsedLegacy = false;

        if (parts[0].trim().isEmpty())
            return null;

        // fetch is base
        if (StringUtil.parseInt(parts[0]) != null) {
            Integer data = parts.length >= 2 ? StringUtil.parseInt(parts[1]) : 0;

            if (data == null)
                data = 0;

            final XMaterial xMat = XMaterial.matchXMaterial(
                            StringUtil.parseInt(parts[0]),
                            (byte) (int) data)
                    .orElse(null);
            parsedLegacy = true;

            if (xMat != null && xMat.isSupported())
                is = xMat.parseItem();

        } else {
            String name = parts[0].toUpperCase()
                    .replace("-", "_")
                    .replace(" ", "_");

            final XMaterial xMat = XMaterial.matchXMaterial(name).orElse(null);

            if (xMat != null && xMat.isSupported())
                is = xMat.parseItem();
            else {
                // deep search
                name = name.replace("_", "");

                for (Material mat : Material.values()) {
                    if (!mat.name().replace("_", "").equals(name))
                        continue;

                    is = new ItemStack(mat);
                    break;
                }
            }
        }

        if (is == null)
            return null;

        // work with data
        ItemMeta im = is.getItemMeta();

        if (im == null)
            return null;
        if (parts.length == 1)
            return is;

        if (im instanceof SkullMeta) {
            final String texture = parts[1];

            if (texture.length() <= 16) {
                final SkullMeta sm = (SkullMeta) im;

                sm.setOwner(texture);
            } else {
                applySkullTexture(is, texture);
                im = is.getItemMeta();
            }

        } else if (im instanceof LeatherArmorMeta) {
            final LeatherArmorMeta lam = (LeatherArmorMeta) im;

            try {
                lam.setColor(Color.fromRGB(Integer.decode(parts[1])));
            } catch (Exception e) {
            }

        } else if (!parsedLegacy && StringUtil.parseInt(parts[1]) != null) {
            is.setDurability((short) (int) StringUtil.parseInt(parts[1]));
        }

        is.setItemMeta(im);

        return is;
    }

    private static ItemStack applySkullTexture(ItemStack is, String texture) {
        final UUID uuid = UUID.randomUUID();

        if (SUPPORTS_ITEM_COMPONENTS) { // 1.20.5+
            return Bukkit.getUnsafe().modifyItemStack(
                    is,
                    "minecraft:player_head[minecraft:profile={properties:[{name:\"textures\",value:\"" + texture + "\"}]}]");
        }

        String id;

        if (XMaterial.getVersion() <= 15)
            id = "\"" + uuid + "\"";
        else {
            id = "[I;" + ((int) uuid.getMostSignificantBits()) + "," +
                    ((int) (uuid.getMostSignificantBits() >> 32L)) + "," +
                    ((int) uuid.getLeastSignificantBits()) + "," +
                    ((int) (uuid.getLeastSignificantBits() >> 32L)) + "]";
        }

        return Bukkit.getUnsafe().modifyItemStack(is, "{SkullOwner:{Id:" + id + ",Properties:{textures:[{Value:\"" + texture + "\"}]}}}");
    }

    public String serialize(@Nullable ItemStack is) {
        if (is == null)
            return "air";

        final ItemMeta im = is.getItemMeta();

        if (im == null)
            return "air";

        // native
        final String nativeStr = nativeSerialize(im);

        if (nativeStr != null)
            return nativeStr;

        // legacy
        return legacySerialize(is, im);
    }

    @Nullable
    private String nativeSerialize(ItemMeta im) {
        if (!SUPPORTS_NATIVE_ITEM_PARSE)
            return null;

        return im.getPersistentDataContainer().get(
                new NamespacedKey(this.plugin, KEY_ORIGINAL_STRING),
                PersistentDataType.STRING
        );
    }

    private String legacySerialize(ItemStack is, ItemMeta im) {
        final StringBuilder builder = new StringBuilder();

        builder.append(is.getType().name());

        if (im instanceof SkullMeta) {
            final SkullMeta sm = (SkullMeta) im;

            if (XMaterial.getVersion() >= 19) {
                if (sm.getOwnerProfile() != null) {
                    if (sm.getOwnerProfile().getTextures().getSkin() != null) {
                        builder.append(":" + Base64.getEncoder().encodeToString((
                                "{\"textures\":{\"SKIN\":{\"url\":\"" +
                                        sm.getOwnerProfile().getTextures().getSkin().toExternalForm() +
                                        "\"}}}"
                        ).getBytes(StandardCharsets.UTF_8)));
                    } else if (sm.getOwnerProfile().getName() != null)
                        builder.append(":" + sm.getOwnerProfile().getName());
                }

            } else if (sm.getOwner() != null)
                builder.append(":" + sm.getOwner());

        } else if (im instanceof LeatherArmorMeta) {
            final LeatherArmorMeta lam = (LeatherArmorMeta) im;

            builder.append(":#" +
                    Integer.toHexString(lam.getColor().getRed()) +
                    Integer.toHexString(lam.getColor().getGreen()) +
                    Integer.toHexString(lam.getColor().getBlue()));

        } else if (is.getDurability() != 0)
            builder.append(":" + is.getDurability());

        return builder.toString();
    }
}
