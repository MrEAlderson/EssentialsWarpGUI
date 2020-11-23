package de.marcely.warpgui.library;

import java.util.Collection;

import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.earth2me.essentials.commands.Commandwarp;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.commands.QuietAbortException;
import com.earth2me.essentials.commands.WarpNotFoundException;

import net.ess3.api.IEssentials;
import net.ess3.api.InvalidWorldException;

public class Essentials implements Library, WarpsProvider {
	
	protected IEssentials plugin;
	
	private Commandwarp warpCommand;
	
	@Override
	public LibraryType getType(){
		return LibraryType.ESSENTIALS;
	}

	@Override
	public void load(){
		this.plugin = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		
		init();
	}
	
	protected void init(){
		this.warpCommand = new Commandwarp();
		this.warpCommand.setEssentials(this.plugin);
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

	@Override
	public boolean hasUsePermission(CommandSender sender, String warpName){
		return !this.plugin.getSettings().getPerWarpPermission() || sender.hasPermission("essentials.warps." + warpName);
	}

	@Override
	public void warp(Player player, String warpName){
		try{
			this.warpCommand.run(Bukkit.getServer(), this.plugin.getUser(player), "warp", new String[]{ warpName });
        }catch(NoChargeException | QuietAbortException e){
        	// shh
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
}
