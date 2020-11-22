package de.marcely.ezgui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DecGUIItem extends GUIItem {

	public DecGUIItem(ItemStack is){
		super(is);
	}

	@Override
	public void onClick(Player whoClicked, boolean leftClick, boolean shiftClick){ }
}
