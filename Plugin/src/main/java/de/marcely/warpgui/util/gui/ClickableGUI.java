package de.marcely.warpgui.util.gui;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Extends GUI and adds methods for clickable items
 */
public interface ClickableGUI extends GUI {
	
	/**
	 * Returns the amount of items that fit in a row
	 * 
	 * @return The width of the GUI
	 */
	int getWidth();
	
	/**
	 * Returns the amount of items that fit in a column
	 * 
	 * @return The width of the GUI
	 */
	int getHeight();
	
	/**
	 * Returns the total amount of items/slots that fit into this GUI
	 * 
	 * @return The size of the GUI
	 */
	default int getSize(){
		return getWidth() * getHeight();
	}
	
	/**
	 * Set an item at a specific slot
	 * 
	 * @param item The item to be set
	 * @param slot The target slot
	 */
	void setItem(GUIItem item, int slot);
	
	/**
	 * Returns the item at the specific slot. May return null if there's none
	 * 
	 * @param slot The slot at which the item may be
	 * @return The placed item. May be null
	 */
	@Nullable GUIItem getItem(int slot);
	
	/**
	 * Replaces any slot with the given item
	 * 
	 * @param item The given item
	 */
	void fill(GUIItem item);
	
	/**
	 * Replaces any empty/air slot with the given item
	 * 
	 * @param item The given item
	 */
	void fillSpace(GUIItem item);
	
	/**
	 * Replaces any slot with the given item
	 * 
	 * @param is The given item
	 */
	default void fill(ItemStack is){
		fill(new GUIItem(is, ClickListener.Silent.INSTANCE));
	}
	
	/**
	 * Replaces any slot with the given item and listens for clicks
	 * 
	 * @param is The given item
	 * @param listener The click listener
	 */
	default void fill(ItemStack is, ClickListener listener){
		fill(new GUIItem(is, listener));
	}
	
	/**
	 * Replaces any empty/air slot with the given item
	 * 
	 * @param is The given item
	 */
	default void fillSpace(ItemStack is){
		fillSpace(new GUIItem(is, ClickListener.Silent.INSTANCE));
	}
	
	/**
	 * Replaces any empty/air slot with the given item and listens for clicks
	 * 
	 * @param is The given item
	 * @param listener The click listener
	 */
	default void fillSpace(ItemStack is, ClickListener listener){
		fillSpace(new GUIItem(is, listener));
	}
	
	default void setItem(GUIItem item, int x, int y){
		setItem(item, calcSlot(x, y));
	}
	
	/**
	 * Set an item at a specific slot and listen whenever someone clicks on it
	 * 
	 * @param is The item to be set
	 * @param slot The target slot
	 * @param listener Listens whenever someone clicks on the item
	 */
	default void setItem(ItemStack is, int slot, ClickListener listener){
		setItem(is != null ? new GUIItem(is, listener) : null, slot);
	}
	
	/**
	 * Set an item at a specific x and y coordinate and listen whenever someone clicks on it
	 * 
	 * @param is The item to be set
	 * @param x The target x coordinate
	 * @param y The target y coordinate
	 * @param listener Listens whenever someone clicks on the item
	 */
	default void setItem(ItemStack is, int x, int y, ClickListener listener){
		setItem(is != null ? is : null, calcSlot(x, y), listener);
	}
	
	/**
	 * Set an item at a specific slot
	 * 
	 * @param is The item to be set
	 * @param slot The target slot
	 */
	default void setItem(ItemStack is, int slot){
		setItem(is != null ? new GUIItem(is, ClickListener.Silent.INSTANCE) : null, slot);
	}
	
	/**
	 * Set an item at a specific x and y coordinate
	 * 
	 * @param is The item to be set
	 * @param x The target x coordinate
	 * @param y The target y coordinate
	 */
	default void setItem(ItemStack is, int x, int y){
		setItem(is != null ? is : null, calcSlot(x, y));
	}
	
	/**
	 * Returns the item at the specific x and y slot. May return null if there's none
	 * 
	 * @param x The target x coordinate
	 * @param y The target y coordinate
	 * @return The placed item. May be null
	 */
	default @Nullable GUIItem getItem(int x, int y){
		return getItem(calcSlot(x, y));
	}
	
	/**
	 * Starts at slot 0 and tries to find the next available/empty slot.<br>
	 * Returns -1 if it didn't find any
	 * 
	 * @param condition You may specify the search at a specific area
	 * @return The next available/empty slot. -1 if the inventory is full
	 */
	int getNextSpace(@Nullable AddItemCondition condition);
	
	/**
	 * Starts at slot 0 and tries to find the next available/empty slot.<br>
	 * Returns -1 if it didn't find any
	 * 
	 * @return The next available/empty slot. -1 if the inventory is full
	 */
	default int getNextSpace(){
		return getNextSpace(null);
	}
	
	/**
	 * Adds the item at the next available slot.
	 * <p>
	 * Keep in mind that items with the same materials don't get filled up.
	 * 
	 * @param item The item you want to add
	 * @param condition You may specify the search at a specific area
	 * @return The slot to which it was added. -1 if the inventory is full
	 */
	default int addItem(GUIItem item, @Nullable AddItemCondition condition){
		final int slot = getNextSpace(condition);
		
		if(slot != -1)
			setItem(item, slot);
		
		return slot;
	}
	
	default int addItem(GUIItem item){
		return addItem(item, null);
	}
	
	default int addItem(ItemStack is, ClickListener listener, @Nullable AddItemCondition condition){
		return addItem(new GUIItem(is, listener), condition);
	}
	
	default int addItem(ItemStack is, ClickListener listener){
		return addItem(new GUIItem(is, listener), null);
	}
	
	default int addItem(ItemStack is, @Nullable AddItemCondition condition){
		return addItem(new GUIItem(is, ClickListener.Silent.INSTANCE), condition);
	}
	
	default int addItem(ItemStack is){
		return addItem(new GUIItem(is, ClickListener.Silent.INSTANCE), null);
	}
	
	default void formatRow(int y, CenterFormat format, int min, int max){
		max += 1;
		
		if(max - min <= 0)
			return;
		
		// fetch previous items
		final GUIItem[] items = new GUIItem[max - min];
		int amount = 0;
		
		for(int i=0; i<items.length; i++){
			final int x = min + i;
			
			if((items[i] = getItem(x, y)) != null){
				setItem((GUIItem) null, x, y);
				
				amount++;
			}
		}
		
		// place them at other positions
		int i = 0;
		
		for(int i1=0; i1<items.length; i1++){
			final GUIItem item = items[i1];
			
			if(item == null)
				continue;
			
			setItem(item, format.calculate(i++, amount, min, max), y);
		}
	}
	
	default void formatRow(int y, CenterFormat format){
		formatRow(y, format, 0, getWidth()-1);
	}
	
	default void formatColumn(int x, CenterFormat format, int min, int max){
		max += 1;
		
		if(max - min <= 0)
			return;
		
		// fetch previous items
		final GUIItem[] items = new GUIItem[max - min];
		int amount = 0;
		
		for(int i=0; i<items.length; i++){
			final int y = min + i;
			
			if((items[i] = getItem(x, y)) != null){
				setItem((GUIItem) null, x, y);
				
				amount++;
			}
		}
		
		// place them at other positions
		int i = 0;
		
		for(int i1=0; i1<items.length; i1++){
			final GUIItem item = items[i1];
			
			if(item == null)
				continue;
			
			setItem(item, x, format.calculate(i++, amount, min, max));
		}
	}
	
	default void formatColumn(int x, CenterFormat format){
		formatColumn(x, format, 0, getHeight()-1);
	}
	
	default void formatAnyRow(CenterFormat format, int min, int max){
		for(int i=0; i<getHeight(); i++)
			formatRow(i, format, min, max);
	}
	
	default void formatAnyColumn(CenterFormat format, int min, int max){
		for(int i=0; i<getWidth(); i++)
			formatColumn(i, format, min, max);
	}
	
	default void formatAnyRow(CenterFormat format){
		for(int i=0; i<getHeight(); i++)
			formatRow(i, format);
	}
	
	default void formatAnyColumn(CenterFormat format){
		for(int i=0; i<getWidth(); i++)
			formatColumn(i, format);
	}
	
	default int calcSlot(int x, int y){
		return y*getWidth()+x;
	}
}
