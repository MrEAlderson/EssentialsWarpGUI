package de.marcely.warpgui.util.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GUIItem implements Cloneable {
	
	@Getter
	private final ItemStack item;
	@Getter
	private final ClickListener listener;
	
	@Getter @Setter
	private Object attachement;
	
	public GUIItem(ItemStack is, ClickListener listener){
		this(is, listener, null);
	}

	public GUIItem(ItemStack is, ClickListener listener, @Nullable Object attachement){
		Objects.requireNonNull(is, "is");
		Objects.requireNonNull(listener, "listener");

		this.item = is;
		this.listener = listener;
		this.attachement = attachement;
	}
	
	@Override
	public GUIItem clone(){
		final GUIItem item = new GUIItem(
				this.item.clone(),
				listener);
		
		item.attachement = this.attachement;
		
		return item;
	}
}