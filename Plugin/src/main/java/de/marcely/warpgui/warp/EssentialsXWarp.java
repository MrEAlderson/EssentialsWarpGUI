package de.marcely.warpgui.warp;

import de.marcely.warpgui.warp.provider.EssentialsXWarpProvider;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Locale;

public class EssentialsXWarp implements Warp {

    @Getter
    private final String name;
    @Getter
    private final EssentialsXWarpProvider provider;

    public EssentialsXWarp(EssentialsXWarpProvider provider, String name) {
        this.provider = provider;
        this.name = name;
    }

    @Override
    public boolean hasPermission(Player player) {
        return !this.provider.getHook().getSettings().getPerWarpPermission() ||
                player.hasPermission("essentials.warps." + this.name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public void teleport(Player player) {
        // simulate a command execution
        this.provider.getWarpCommand().execute(
                player,
                this.provider.getWarpCommand().getLabel(),
                new String[] { this.name }
        );
    }
}
