package de.marcely.warpgui.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class ChestGUI implements ClickableGUI {
	
	private Inventory inv;
	private GUIItem[] items;
	private boolean ignoresCancelEvent = false;
	private String title;
	
	public ChestGUI(){
		this(1, "");
	}
	
	public ChestGUI(int height){
		this(height, "");
	}
	
	public ChestGUI(String title){
		this(1, title);
	}
	
	public ChestGUI(int height, String title){
		Objects.requireNonNull(title, "title");
		
		height = Math.max(1, Math.min(6, height));
		
		this.inv = Bukkit.createInventory(null, height*9, this.title = title);
		this.items = new GUIItem[height*9];
	}
	
	@Override
	public void open(Player player){
		player.openInventory(this.inv);
		GUIContainer.getInstance().openInventories.put(player, this);
	}

	@Override
	public void closeAll(){
		for(HumanEntity e:this.inv.getViewers())
			e.closeInventory();
	}

	@Override
	public void setTitle(String title){
		Objects.requireNonNull(title, "title");
		
		final Inventory prev = this.inv;
		final List<HumanEntity> players = new ArrayList<>(prev.getViewers());
		
		this.inv = Bukkit.createInventory(null, prev.getSize(), this.title = title);
		this.inv.setContents(prev.getContents());
		
		this.ignoresCancelEvent = true;
		players.forEach(player -> player.openInventory(this.inv));
		this.ignoresCancelEvent = false;
	}

	@Override
	public String getTitle(){
		return this.title;
	}

	@Override
	public boolean areItemsMoveable(){
		return false;
	}
	
	@Override
	public boolean ignoresCancelEvent(){
		return this.ignoresCancelEvent;
	}

	@Override
	public Collection<Player> getPlayers(){
		return this.inv.getViewers().stream()
				.map(human -> (Player) human)
				.collect(Collectors.toList());
	}

	@Override
	public int getWidth(){
		return 9;
	}

	@Override
	public int getHeight(){
		return this.inv.getSize()/9;
	}

	@Override
	public void setItem(GUIItem item, int slot){
		this.items[slot] = item;
		this.inv.setItem(slot, item != null ? item.getItem() : null);
	}

	@Override
	public @Nullable GUIItem getItem(int slot){
		return this.items[slot];
	}
	
	/**
	 * Resizes the inventory and tries to keep the content
	 * 
	 * @param height The new height
	 */
	public void setHeight(int height){
		if(getHeight() == height)
			return;
		
		final Inventory prev = this.inv;
		final List<HumanEntity> players = new ArrayList<>(prev.getViewers());
		final GUIItem[] prevItems = this.items;
		
		this.inv = Bukkit.createInventory(null, 9*height, getTitle());
		this.items = new GUIItem[9*height];
		
		this.inv.setContents(Arrays.copyOfRange(prev.getContents(), 0, Math.min(prevItems.length, this.items.length)));
		System.arraycopy(prevItems, 0, this.items, 0, Math.min(prevItems.length, this.items.length));
		
		this.ignoresCancelEvent = true;
		players.forEach(player -> player.openInventory(this.inv));
		this.ignoresCancelEvent = false;
	}

	@Override
	public void clear(){
		Arrays.fill(this.items, null);
		this.inv.setContents(new ItemStack[0]);
	}

	@Override
	public void fill(GUIItem item){
		for(int i=0; i<this.items.length; i++){
			setItem(item, i);
		}
	}

	@Override
	public void fillSpace(GUIItem item){
		for(int i=0; i<this.items.length; i++){
			if(this.items[i] == null)
				setItem(item, i);
		}
	}

	@Override
	public int getNextSpace(@Nullable AddItemCondition condition){
		int minX = 0;
		int maxX = 8;
		int minY = 0;
		int maxY = 5;
		
		if(condition != null){
			final int[] params = condition.getParameters();
			
			minX = params[0];
			maxX = params[1];
			minY = params[2];
			maxY = params[3];
		}
		
		final int maxSlot = maxY*9+maxX;
		int slot = minY*9+minX-1;
		
		// go slot by slot
		while((++slot) <= maxSlot){
			boolean issue = false;
			
			// go next row if reached max (or exit)
			do{
				final int x = slot%9;
				final int y = slot/9;
				
				issue = false;
				
				if(x > maxX){
					slot = (y+1)*9+minX;
					
					issue = true;
				}
				
				if(y > maxY)
					return -1;
			}while(issue);
			
			// check if slot is ok
			if(slot >= this.items.length)
				return -1;
			else if(this.items[slot] == null)
				return slot;
		}
		
		return -1;
	}
	/**
	 * Adds the item at the next available slot.
	 * <p>
	 * Keep in mind that items with the same materials don't get filled up.<br>
	 * Also tries to increase height of inventory to make space for items.
	 * 
	 * @param item The item you want to add
	 * @param condition You may specify the search at a specific area
	 * @return The slot to which it was added. -1 if the inventory is full
	 */
	@Override
	public int addItem(GUIItem item, @Nullable AddItemCondition condition){
		int slot = -1;
		
		while((slot = ClickableGUI.super.addItem(item, condition)) == -1 && getHeight() <= 5)
			setHeight(getHeight() + 1);
		
		return slot;
	}
}
