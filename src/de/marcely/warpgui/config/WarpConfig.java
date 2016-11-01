/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.marcely.warpgui.Util;
import de.marcely.warpgui.Warp;
import de.marcely.warpgui.main;

public class WarpConfig implements Serializable {
	private static final long serialVersionUID = -1266449831520034396L;
	public List<Warp> warps = new ArrayList<Warp>();
	
	public WarpConfig(){ }
	
	public void addLore(String warpname, String lore){
		getWarp(warpname).addLore(lore);
	}
	
	public void setIcon(String warpname, Material icon, int id){
		if(contains(warpname)){
			getWarp(warpname).setIcon(new ItemStack(icon, 1, (short) id));
		}else{
			Warp warp = new Warp(warpname, new ItemStack(icon, 1, (short) id));
			warps.add(warp);
		}
	}
	
	public void setPrefix(String warpname, String prefix){
		if(contains(warpname)){
			getWarp(warpname).setPrefix(prefix);
		}else{
			Warp warp = new Warp(warpname, new ItemStack(Material.CLAY_BALL), prefix);
			warps.add(warp);
		}
	}
	
	public Warp getWarp(String name){
		for(Warp warp:warps){
			if(warp.getName().equals(name))
				return warp;
		}
		
		if(Util.existsWarp(name)){
			Warp warp = new Warp(name, new ItemStack(Material.CLAY_BALL));
			warps.add(warp);
			return warp;
		}
		return null;
	}
	
	public List<Warp> getWarps(){
		List<Warp> list = new ArrayList<Warp>();
		
		for(String warp:main.es.getWarps().getList())
			list.add(getWarp(warp));
		
		return list;
	}
	
	public ItemStack getIcon(String warpname){
		return getWarp(warpname).getIcon();
	}
	
	public String getPrefix(String warpname){
		return getWarp(warpname).getPrefix();
	}
	
	public boolean contains(String warpname){
		return getWarp(warpname) != null;
	}
	
	public static boolean exists(){
		return new File("plugins/" + main.plugin.getName() + "/warps.cfg").exists();
	}
	
	public static WarpConfig load(){
		FileInputStream file = null;
		try {
			file = new FileInputStream("plugins/" + main.plugin.getName() + "/warps.cfg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ObjectInputStream load = null;
		try {
			load = new ObjectInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		WarpConfig config = null;
		try {
			config = (WarpConfig)load.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			load.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}
	
	public static void save(WarpConfig config){
		FileOutputStream file = null;
		try {
			file = new FileOutputStream("plugins/" + main.plugin.getName() + "/warps.cfg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ObjectOutputStream save = null;
		try {
			save = new ObjectOutputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			save.writeObject(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			save.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
