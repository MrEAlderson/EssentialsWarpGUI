package de.marcely.ezgui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GUIItem {
	
	protected GUI gui = null;
	private ItemStack is;
	
	public Object c = null;
	
	public GUIItem(ItemStack is){
		this.is = is;
	}
	
	public void setItemStack(ItemStack is){
		this.is = is;
		
		if(gui != null)
			gui.update();
	}
	
	public ItemStack getItemStack(){ return this.is; }
	
	public abstract void onClick(Player whoClicked, boolean leftClick, boolean shiftClick);
}