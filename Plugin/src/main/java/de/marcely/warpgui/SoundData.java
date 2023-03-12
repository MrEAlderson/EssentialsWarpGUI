package de.marcely.warpgui;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class SoundData {

    public static final SoundData SILENT = new SoundData("", false, false, 0, 1);

    @Getter
    private final String name;
    @Getter
    private final boolean isActive;
    @Getter
    private final boolean isCustomResourcePack; // 1.9+
    @Getter
    private final float volume, pitch;

    private Sound soundCache;
    @Getter
    private boolean soundCacheLoaded;

    public SoundData(String name, boolean isActive, boolean isCustomResourcePack, float volume, float pitch) {
        this.name = name;
        this.isActive = isActive;
        this.isCustomResourcePack = isCustomResourcePack;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundData(XSound sound, float volume, float pitch) {
        this(sound.name(), true, false, volume, pitch);
    }

    public void play(Player player) {
        if (this.isCustomResourcePack) {
            if (XMaterial.getVersion() < 9)
                return;

            player.playSound(
                    player.getLocation(),
                    this.name,
                    this.volume,
                    this.pitch
            );

            return;
        }

        final Sound sound = getAsBukkit();

        if (sound == null)
            return;

        player.playSound(
                player.getLocation(),
                sound,
                this.volume,
                this.pitch
        );
    }

    @Nullable
    public Sound getAsBukkit() {
        if (this.soundCacheLoaded)
            return this.soundCache;

        this.soundCacheLoaded = true;

        if (!this.isActive || this.isCustomResourcePack)
            return null;
        if (this.name.trim().isEmpty())
            return null;

        final XSound sound = XSound.matchXSound(this.name.toUpperCase()).orElse(null);

        if (sound == null || !sound.isSupported())
            return null;

        return this.soundCache = sound.parseSound();
    }

    public void serialize(Configuration config, String key) {
        config.set(key + ".name", this.name);
        config.set(key + ".is-custom-resource-pack", this.isCustomResourcePack);
        config.set(key + ".volume", this.volume);
        config.set(key + ".pitch", this.pitch);
    }

    public static SoundData parse(Configuration config, String key) {
        if (config == null || !config.contains(key + ".name"))
            return SILENT;

        return new SoundData(
                config.getString(key + ".name", ""),
                true,
                config.getBoolean(key + ".is-custom-resource-pack", false),
                (float) config.getDouble(key + ".volume", 1),
                (float) config.getDouble(key + ".pitch", 1)
        );
    }
}
