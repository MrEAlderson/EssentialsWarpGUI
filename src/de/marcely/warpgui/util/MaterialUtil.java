/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.util;

import org.bukkit.Material;

public class MaterialUtil {
	
	public static Material ofString(String str){
		if(str.equalsIgnoreCase("dye")) return Material.INK_SACK;
		
		for(Material m:Material.values()){
			if(m.name().equalsIgnoreCase(str) ||
			   m.name().replace("_", "").equalsIgnoreCase(str) ||
			   m.name().replace("_item", "").equalsIgnoreCase(str) ||
			   m.name().replace("_item", "").replace("_", "").equalsIgnoreCase(str))
				return m;
		}
		
		return null;
	}
	
	public static String toString(Material mat){
		final StringBuilder name = new StringBuilder(mat.name().toLowerCase().replace("_", " "));
		
		boolean firstChar = true;
		for(int i=0; i<name.length(); i++){
			char c = name.charAt(i);
			
			if(firstChar) {
				name.setCharAt(i, Character.toUpperCase(c));
				firstChar = false;
			}else if(c == ' ')
				firstChar = true;
		}
		
		return name.toString();
	}
}
