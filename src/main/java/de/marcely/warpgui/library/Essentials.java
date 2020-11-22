package de.marcely.warpgui.library;

import java.util.Collection;

import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.commands.WarpNotFoundException;

import net.ess3.api.InvalidWorldException;

public class Essentials implements Library, WarpsProvider {
	
	protected IEssentials plugin;
	
	@Override
	public LibraryType getType(){
		return LibraryType.ESSENTIALS;
	}

	@Override
	public void load(){
		this.plugin = ((IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials"));
	}

	@Override
	public void unload(){ }

	@Override
	public Collection<String> getWarps(){
		return this.plugin.getWarps().getList();
	}

	@Override
	public @Nullable Location getWarpLocation(String name){
		try{
			return this.plugin.getWarps().getWarp(name);
		}catch(WarpNotFoundException | InvalidWorldException e){
			return null;
		}
	}

	@Override
	public boolean existsWarp(String name){
		return getWarpLocation(name) != null;
	}

	@Override
	public void addWarp(String name, Location loc){
		try{
			this.plugin.getWarps().setWarp(name, loc);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void removeWarp(String name){
		try {
			this.plugin.getWarps().removeWarp(name);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
