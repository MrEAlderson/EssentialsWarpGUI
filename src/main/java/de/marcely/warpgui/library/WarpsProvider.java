package de.marcely.warpgui.library;

import java.util.Collection;

import org.jetbrains.annotations.Nullable;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface WarpsProvider {
	
	public Collection<String> getWarps();
	
	public @Nullable Location getWarpLocation(String warpName);
	
	public boolean existsWarp(String warpName);
	
	public void removeWarp(String warpName);
	
	public void addWarp(String warpName, Location loc);
	
	public boolean hasUsePermission(CommandSender sender, String warpName);
	
	public void warp(Player player, String warpName);
}