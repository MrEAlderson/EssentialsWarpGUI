package de.marcely.warpgui;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Warp implements Serializable {
	private static final long serialVersionUID = 100042053024876811L;
	
	private String name;
	private String iconName;
	private short iconID;
	private String prefix = "";
	private LinkedHashMap<Integer, String> lore = new LinkedHashMap<Integer, String>();
	
	public Warp(String name, ItemStack icon){
		setName(name);
		setIcon(icon);
	}
	
	public Warp(String name, ItemStack icon, String prefix){
		setName(name);
		setIcon(icon);
		setPrefix(prefix);
	}
	
	public int addLore(String lore){
		if(this.lore.size() < 5){
			this.lore.put(this.lore.size(), lore);
			return this.lore.size() - 1;
		}else
			return -1;
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
	
	public String getLore(int line){
		if(this.lore.containsKey(line)){
			return this.lore.get(line);
		}else
			return null;
	}
	
	public int getLoreSize(){
		return this.lore.size();
	}
	
	public boolean removeLore(int line){
		if(this.lore.containsKey(line)){
			lore.remove(line);
			return true;
		}else
			return false;
	}
}
