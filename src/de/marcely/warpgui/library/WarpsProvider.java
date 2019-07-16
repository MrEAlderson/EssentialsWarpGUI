package de.marcely.warpgui.library;

import java.util.Collection;

import javax.annotation.Nullable;

import org.bukkit.Location;

public interface WarpsProvider {
	
	public Collection<String> getWarps();
	
	public @Nullable Location getWarpLocation(String name);
	
	public boolean existsWarp(String name);
	
	public void removeWarp(String name);
	
	public void addWarp(String name, Location loc);
}