/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import de.marcely.warpgui.library.WarpsProvider;
import de.marcely.warpgui.util.Util;
import de.marcely.warpgui.util.Validate;
import lombok.Getter;

public class WarpsContainer {
	
	@Getter private WarpsProvider provider;
	private Map<String, Warp> warps;
	
	public WarpsContainer(WarpsProvider provider){
		this.provider = provider;
		this.warps = new HashMap<>();
	}
	
	public void setProvider(WarpsProvider provider){
		Validate.checkNotNull(provider);
		
		this.provider = provider;
	}
	
	public Collection<Warp> getWarps(){
		return this.warps.values();
	}
	
	public void removeWarp(Warp warp){
		this.warps.remove(warp.getName());
	}
	
	public void addWarp(Warp warp){
		this.warps.put(warp.getName().toLowerCase(), warp);
	}
	
	public Warp getWarp(String name){
		return this.warps.get(name.toLowerCase());
	}
	
	public void synchronizeWithProvider(){
		final Map<String, Warp> newWarps = new HashMap<>(this.warps.size()+4);
		
		for(String name:getProvider().getWarps()){
			Warp warp = this.getWarp(name);
			
			if(warp == null){
				warp = new Warp(name){
					public WarpsContainer getContainer(){
						return WarpsContainer.this;
					}
				};
			}
			
			newWarps.put(name.toLowerCase(), warp);
		}
		
		this.warps = newWarps;
	}
	
	public Collection<Warp> getWarps(Player player){
		if(Util.hasPermission(player, "essentials.warps.*"))
			return getWarps();
		
		final List<Warp> list = new ArrayList<Warp>(this.warps.size());
		
		for(Warp warp:getWarps()){
			if(!Util.hasPermission(player, "essentials.warps." + warp.getName().toLowerCase()))
				continue;
			
			list.add(warp);
		}
		
		return list;
	}
}