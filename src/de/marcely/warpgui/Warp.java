/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.marcely.warpgui.components.WarpsContainer;

@Deprecated
public class Warp implements Serializable {
	private static final long serialVersionUID = 100042053024876811L;
	
	private String name;
	private String iconName;
	private short iconID;
	private String prefix = "";
	private List<String> lores = new ArrayList<String>();
	
	public String getName(){
		return this.name;
	}
	
	public de.marcely.warpgui.components.Warp convertToNew(){
		final de.marcely.warpgui.components.Warp warp = new de.marcely.warpgui.components.Warp(this.name){
			public WarpsContainer getContainer(){
				return EssentialsWarpGUI.instance.getContainer();
			}
		};
		
		warp.setIcon(new ItemStack(Material.getMaterial(this.iconName), 1, this.iconID));
		warp.setPrefix(this.prefix);
		
		for(String lore:this.lores)
			warp.addLore(lore);
		
		return warp;
	}
}