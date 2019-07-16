package de.marcely.warpgui.util;

import javax.annotation.Nullable;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public class BukkitUtil {
	
	public static @Nullable Color colorFromHex(String hex){
		try{
			return Color.fromRGB(java.awt.Color.decode(hex).getRGB());
		}catch(Exception e){
			return null;
		}
	}
	
	public static String colorToHex(Color color){
		String hex = Integer.toHexString(color.asRGB() & 0xFFFFFF);
		
		if(hex.length() < 6)
			hex = "000000".substring(0,  6 - hex.length()) + hex;
		
		return "#" + hex;
	}
	
	@SuppressWarnings("deprecation")
	public static @Nullable PotionEffectType getPotionType(String name){
		Validate.checkNotNull(name);
		
		for(PotionEffectType type:PotionEffectType.values()){
			if(type.getName().equalsIgnoreCase(name) ||
			   (Util.isInteger(name) && type.getId() == Integer.valueOf(name)) ||
			   type.getName().replace("_", "").equalsIgnoreCase(name) ||
			   type.getName().replace("_", " ").equalsIgnoreCase(name))
				return type;
		}
		
		return null;
	}
}
