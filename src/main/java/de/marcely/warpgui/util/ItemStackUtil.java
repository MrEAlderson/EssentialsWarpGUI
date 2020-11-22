/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.util;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.marcely.warpgui.version.Version;

public class ItemStackUtil {
	
	public static ItemStack BLACK_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE, INK_SAC, PLAYER_HEAD;
	
	public static void init(){
		if(Version.getCurrent().getMinor() >= 13){
			BLACK_STAINED_GLASS_PANE = new ItemStack(Material.valueOf("BLACK_STAINED_GLASS_PANE"));
			LIME_STAINED_GLASS_PANE = new ItemStack(Material.valueOf("LIME_STAINED_GLASS_PANE"));
			INK_SAC = new ItemStack(Material.valueOf("INK_SAC"));
			PLAYER_HEAD = new ItemStack(Material.valueOf("PLAYER_HEAD"));
			
		}else{
			BLACK_STAINED_GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
			LIME_STAINED_GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			INK_SAC = new ItemStack(Material.valueOf("INK_SACK"));
			PLAYER_HEAD = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
		}
	}
	
	public static ItemStack rename(ItemStack is, String name){
		final ItemMeta im = is.getItemMeta();
		
		im.setDisplayName(name);
		is.setItemMeta(im);
		
		return is;
	}
	
	public static String getDisplayName(ItemStack is){
		if(is == null || is.getItemMeta() == null)
			return null;
		else
			return is.getItemMeta().getDisplayName();
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack ofString(String name){
		final String[] strs = name.split("\\:");
		final String n = strs[0];
		
		ItemStack is = new ItemStack(Material.STONE, 1);
		
		// first the type
		if(Version.getCurrent().getMinor() <= 12 && Util.isInteger(n)){
			final Material m = Material.getMaterial(Integer.valueOf(n));
			
			if(m != null)
				is.setType(m);
			else
				return null;
		
		}else{
			final Material m = MaterialUtil.ofString(n);
			
			if(m != null)
				is.setType(m);
			else
				return null;
		}
		
		// then the durability (like potion, mobspawner...)
		if(strs.length >= 2){
			switch(is.getType()){
				case POTION:
				{
					if(strs.length >= 4){
						if(Util.isInteger(strs[2]) && Util.isInteger(strs[3])){
							final PotionMeta meta = (PotionMeta) is.getItemMeta();
							PotionEffectType type = BukkitUtil.getPotionType(strs[1]);
							
							if(type != null)
								meta.setMainEffect(type);
							else
								return is;
							
							meta.addCustomEffect(type.createEffect(Integer.valueOf(strs[3]), Integer.valueOf(strs[2])), true);
							
							return is;
						}
					
					}else if(Util.isInteger(strs[1]))
						is.setDurability((short) (int)Integer.valueOf(strs[1]));
				}
				break;
				
				case LEATHER_HELMET:
				case LEATHER_CHESTPLATE:
				case LEATHER_LEGGINGS:
				case LEATHER_BOOTS:
				{
					final Color color = BukkitUtil.colorFromHex(strs[1]);
					
					if(color != null)
						is = ItemStackUtil.dye(is, DyeColor.getByColor(color));
					
					return is;
				}
				
				default:
					if(is.getType() == PLAYER_HEAD.getType()){
						if(strs[1].length() >= 3){
							final SkullMeta sm = (SkullMeta) is.getItemMeta();
							
							is.setDurability((short) 3);
							sm.setOwner(strs[1]);
							is.setItemMeta(sm);
						
						}
					}
					
					break;
			}
		}
		
		return is;
	}
	
	@SuppressWarnings("deprecation")
	public static String toString(ItemStack is){
		if(is == null)
			return "null";
		
		switch(is.getType()){
		case POTION:
		{
			final PotionMeta meta = (PotionMeta) is.getItemMeta();
			
			if(meta.getCustomEffects().size() == 1){
				final PotionEffect effect = meta.getCustomEffects().get(0);
				
				return is.getType().name().toLowerCase()
						+ ":" + effect.getType().getName().toLowerCase() + ":" + effect.getAmplifier() + ":" + effect.getDuration();
			
			}
		
		}
		break;
		
		case LEATHER_HELMET:
		case LEATHER_CHESTPLATE:
		case LEATHER_LEGGINGS:
		case LEATHER_BOOTS:
		{
			final LeatherArmorMeta lam = (LeatherArmorMeta) is.getItemMeta();
			
			if(!lam.getColor().equals(Bukkit.getServer().getItemFactory().getDefaultLeatherColor())){
				return is.getType().name().toLowerCase()
						+ ":" + BukkitUtil.colorToHex(lam.getColor());
			}
		}
		
		default:
			if(is.getType() == PLAYER_HEAD.getType()){
				return is.getType().name().toLowerCase()
						+ ":" + ((SkullMeta) is.getItemMeta()).getOwner();
			}
			
			break;
		}
		
		return is.getType().name().toLowerCase() + ":" + is.getDurability();
	}
	
	public static ItemStack dye(ItemStack is, DyeColor color){
		final Material type = is.getType();
		
		switch(type){
		case LEATHER_BOOTS:
		case LEATHER_LEGGINGS:
		case LEATHER_CHESTPLATE:
		case LEATHER_HELMET:
			{
				final LeatherArmorMeta lam = (LeatherArmorMeta) is.getItemMeta();
				
				lam.setColor(color.getColor());
				is.setItemMeta(lam);
				
				return is;
			}
		
		default:
			break;
		}
		return is;
	}
}
