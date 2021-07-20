/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.components;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.marcely.warpgui.util.Validate;
import lombok.Getter;
import lombok.Setter;

public abstract class Warp {
	
	@Getter
	private String name;
	private ItemStack icon;
	@Getter @Setter
	private String prefix, suffix, displayName;
	@Getter @Setter
	private Integer forceSlot;
	private List<String> lores = new ArrayList<String>();
	
	public Warp(String name){
		this.name = name;
		this.icon = new ItemStack(Material.CLAY_BALL);
	}
	
	public abstract WarpsContainer getContainer();
	
	
	public void setName(String name){
		Validate.checkNotNull(name);
		
		getContainer().removeWarp(this);
		
		this.name = name;
		
		getContainer().addWarp(this);
	}
	
	public void setIcon(ItemStack icon){
		Validate.checkNotNull(icon);
		
		this.icon = icon.clone();
	}
	
	public ItemStack getIcon(){
		return this.icon.clone();
	}
	
	public void addLore(String lore){
		Validate.checkNotNull(lore);
		
		this.lores.add(lore);
	}
	
	public boolean removeLore(String lore){
		return this.lores.remove(lore);
	}
	
	public void removeLore(int line){
		this.lores.remove(line-1);
	}
	
	public void removeAllLore(){
		this.lores.clear();
	}
	
	public List<String> getLore(){
		return new ArrayList<>(this.lores);
	}
	
	public Location getTeleportLocation(){
		return getContainer().getProvider().getWarpLocation(getName());
	}
	
	public void warp(Player player){
		this.getContainer().getProvider().warp(player, getName());
	}
	
	public boolean hasUsePermission(CommandSender sender){
		return this.getContainer().getProvider().hasUsePermission(sender, this.getName());
	}
}
