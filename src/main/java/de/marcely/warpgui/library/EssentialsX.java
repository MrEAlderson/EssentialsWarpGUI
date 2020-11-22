package de.marcely.warpgui.library;

import org.bukkit.Bukkit;

import com.earth2me.essentials.IEssentials;

public class EssentialsX extends Essentials {
	
	@Override
	public LibraryType getType(){
		return LibraryType.ESSENTIALS_X;
	}

	@Override
	public void load(){
		this.plugin = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("EssentialsX");
	}
}
