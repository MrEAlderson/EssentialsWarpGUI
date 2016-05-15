/**
* Adds an GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.4
* @website http://marcely.de/ 
*/

package de.marcely.warpgui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Warp implements Serializable {
	private static final long serialVersionUID = 100042053024876811L;
	
	private String name;
	private String iconName;
	private short iconID;
	private String prefix = "";
	private List<String> lores = new ArrayList<String>();
	
	public Warp(String name, ItemStack icon){
		setName(name);
		setIcon(icon);
	}
	
	public Warp(String name, ItemStack icon, String prefix){
		setName(name);
		setIcon(icon);
		setPrefix(prefix);
	}
	
	public void addLore(String lore){
		this.lores.add(lore);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setIcon(ItemStack icon){
		this.iconName = icon.getType().name();
		this.iconID = icon.getDurability();
	}
	
	public void setPrefix(String prefix){
		this.prefix = prefix;
	}
	
	public String getName(){
		return this.name;
	}
	
	public ItemStack getIcon(){
		return new ItemStack(Material.getMaterial(this.iconName), 1, this.iconID);
	}
	
	public String getPrefix(){
		return this.prefix;
	}
	
	public List<String> getLores(){
		return this.lores;
	}
	
	public boolean removeLore(String lore){
		return this.lores.remove(lore);
	}
}
