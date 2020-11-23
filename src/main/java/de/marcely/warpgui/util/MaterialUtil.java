/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.util;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import de.marcely.warpgui.version.Version;

public class MaterialUtil {
	
	public static @Nullable Material ofString(String str){
		if(Version.getCurrent().getMinor() <= 12 && str.equalsIgnoreCase("dye"))
			return ItemStackUtil.INK_SAC.getType();
		
		str = str.replaceAll("[_ ]", "");
		
		for(Material m:Material.values()){
			if(m.name().replaceAll("[_ ]", "").equalsIgnoreCase(str))
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
	
	public static Material getItemVariant(Material mat){
		if(Version.getCurrent().getMinor() >= 13)
			return mat;
		
		final Material result = ofString(mat.name() + "_ITEM");
		
		return result != null ? result : mat;
	}
}
