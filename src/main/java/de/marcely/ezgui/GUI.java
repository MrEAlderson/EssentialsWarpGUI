package de.marcely.ezgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;

public class GUI extends BaseGUI implements Clickable {
	
	private String title;
	private int height;
	
	private GUIItem items[][];
	
	public boolean autoRefresh = true;
	
	public static List<Player> cachePlayers = new ArrayList<Player>();
	
	/**
	 * 
	 * @param title the title of the GUI
	 * @param height the height of the GUI (maximum is 6)
	 */
	public GUI(String title, int height){
		this.title = title;
		this.height = height;
		
		this.items = new GUIItem[9][height];
	}
	
	@Override
	public boolean isCancellable(){
		return true;
	}
	
	@Override
	public boolean hasAntiDrop(){
		return true;
	}
	
	
	/**
	 * 
	 * @param i the item which should be added
	 * @return returns true if it was successful (if there's even space in the gui)
	 */
	public boolean addItem(GUIItem i){
		return addItem(i, null);
	}
	
	/**
	 * 
	 * @param i the item which should be added
	 * @return returns true if it was successful (if there's even space in the gui)
	 */
	public boolean addItem(GUIItem i, @Nullable AddItemFlag flag){
		i.gui = this;
		
		// search free slot
		if(height > 0){
			
			int slot = -1;
			while((slot = getNextSpace(slot + 1)) != -1){
					int y = slot / 9;
					int x = slot - y*9;
					
					// flag check
					if(flag != null){
						if(flag.type == AddItemFlag.WITHIN_X){
							final int min = (int) flag.param[0], max = (int) flag.param[1];
							
							if(x < min || x > max)
								continue;
						}else if(flag.type == AddItemFlag.WITHIN_Y){
							final int min = (int) flag.param[0], max = (int) flag.param[1];
							
							if(y < min || y > max)
								continue;
						}else if(flag.type == AddItemFlag.WITHIN){
							final int xMin = (int) flag.param[0], xMax = (int) flag.param[1],
									yMin = (int) flag.param[2], yMax = (int) flag.param[3];
							
							if(x < xMin || x > xMax || y < yMin || y > yMax)
								continue;
							
						}else if(flag.type == AddItemFlag.CENTERIZED_HORIZONTAL){
							x = (4 + (x%2 == 0 ? -x : x +1)/2);
						}
					}
					
					// set item
					items[x][y] = i;
					onSetItem(x, y, i);
					
					// update
					if(autoRefresh)
						update();
					
					return true;
			}
		}
		
		// else resize and add again
		if(height < 6){
			setHeight(height + 1);
			addItem(i, flag);
		}
		
		return false;
	}
	
	@Override
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setHeight(int height){ 
		GUIItem[][] oldItems = items.clone();
		items = new GUIItem[9][height];
		
		// resize
		for(int ix=0; ix<9; ix++){
			for(int iy=0; iy<this.height; iy++){
				if(iy<height)
					items[ix][iy] = oldItems[ix][iy];
			}
		}
		
		this.height = height;
		
		// update
		if(autoRefresh)
			update();
	}
	
	/**
	 * 
	 * @param i which item should be added
	 * @param x the x position of the item (starts at 0)
	 * @param y the y position of the item (starts at 0)
	 */
	public void setItemAt(GUIItem i, int x, int y){
		// remove gui from old item
		if(items[x][y] != null)
			items[x][y].gui = null;
		
		// set item
		items[x][y] = i;
		onSetItem(x, y, i);
		
		// set gui to new item
		i.gui = this;
		
		// update
		if(autoRefresh)
			update();
	}
	
	/**
	 * 
	 * @param i which item should be added
	 * @param slot at which slot it should be added (from 0 to gui-height*9)
	 */
	@Override
	public void setItemAt(GUIItem i, int slot){
		final int y = slot / 9;
		
		setItemAt(i, slot - y*9, y);
	}
	
	@Override
	public String getTitle(){
		return this.title;
	}
	
	public int getHeight(){ return this.height; }
	
	@Override
	public GUIItem getItemAt(int slot){
		int y = slot / 9;
		return getItemAt(slot - y * 9, y);
	}
	public GUIItem getItemAt(int x, int y){
		if(x >= 0 && y >= 0 && x < 9 && y < height)
			return this.items[x][y];
		else
			return null;
	}
	
	/**
	 * 
	 * @param player for which player it should open
	 */
	@Override
	public void open(Player player){
		String newTitle = title;
		if(newTitle.length() >= 32){
			newTitle = newTitle.substring(0, 29);
			newTitle += "...";
		}
			
		final Inventory inv = Bukkit.createInventory(player, height * 9, newTitle);
		
		// set items
		for(int ix=0; ix<9; ix++){
			for(int iy=0; iy<height; iy++){
				if(items[ix][iy] != null)
					inv.setItem(iy * 9 + ix, items[ix][iy].getItemStack());
				else
					inv.setItem(iy * 9 + ix, null);
			}
		}
		
		// open inventory
		player.openInventory(inv);
		openInventories.put(player, this);
	}
	
	/**
	 * 
	 * updates the gui for every player. normally this plugin will do it automaticly
	 */
	public void update(){
		for(Player player:getPlayersWhoUseThisGUI()){
			cachePlayers.add(player);
			open(player);
		}
	}
	
	/**
	 * 
	 * @return returns a list of players who have this GUI opend
	 */
	public List<Player> getPlayersWhoUseThisGUI(){
		List<Player> list = new ArrayList<Player>();
		
		for(Entry<Player, BaseGUI> e:openInventories.entrySet()){
			if(e.getValue().equals(this))
				list.add(e.getKey());
		}
		
		return list;
	}
	
	/**
	 * 
	 * removes any item from the gui
	 */
	public void clear(){
		for(int ix=0; ix<9; ix++){
			for(int iy=0; iy<height; iy++){
				items[ix][iy] = null;
			}
		}
		
		// update
		update();
	}
	
	/**
	 * 
	 * @return looks for the next empty slot. returns -1 if there's none
	 */
	public int getNextSpace(){
		return getNextSpace(0, 0);
	}
	
	/**
	 * 
	 * @param start at which it should start
	 * @return looks for the next empty slot. returns -1 if there's none
	 */
	public int getNextSpace(int start){
		final int y = start/9;
		final int x = start - y*9;
		
		return getNextSpace(x, y);
	}
	
	/**
	 * 
	 * @param startX at which x slot it should start
	 * @param startY at which y slot it should start
	 * @return looks for the next empty slot. returns -1 if there's none
	 */
	public int getNextSpace(int startX, int startY){
		for(int iy=startY; iy<height; iy++){
			for(int ix=startX; ix<9; ix++){
				if(items[ix][iy] == null || items[ix][iy].getItemStack().getType() == Material.AIR)
					return iy * 9 + ix;
			}
		}
		
		return -1;
	}
	
	/**
	 * 
	 * centers all items at the specified y position
	 * @param y which row it should center
	 */
	public void centerAtY(int y, CenterFormatType type, int min, int max){
		final List<GUIItem> array = new ArrayList<GUIItem>();
		
		for(int i=min; i<max; i++){
			if(items[i][y] != null && items[i][y].getItemStack().getType() != Material.AIR)
				array.add(items[i][y]);
			
			items[i][y] = null;
		}
		
		for(int i=0; i<array.size(); i++){
			final GUIItem gi = array.get(i);
			
			int x = -1;
			
			if(type == CenterFormatType.Normal)
				x = GUI_Style.getSlotCenter_Normal(i, array.size(), min, max);
			else if(type == CenterFormatType.Beautiful)
				x = GUI_Style.getSlotCenter_Beautiful(i, array.size(), min, max);
			
			items[x][y] = gi;
		}
	}
	
	/**
	 * 
	 * centers all items at the specified x position
	 * @param x which row it should center
	 */
	public void centerAtX(int x, CenterFormatType type, int min, int max){
		final List<GUIItem> array = new ArrayList<GUIItem>();
		
		for(int i=min; i<max; i++){
			if(items[x][i] != null && items[x][i].getItemStack().getType() != Material.AIR)
				array.add(items[x][i]);
			
			items[x][i] = null;
		}
		
		for(int i=0; i<array.size(); i++){
			final GUIItem gi = array.get(i);
			
			int y = -1;
			
			if(type == CenterFormatType.Normal)
				y = GUI_Style.getSlotCenter_Normal(i, array.size(), min, max);
			else if(type == CenterFormatType.Beautiful)
				y = GUI_Style.getSlotCenter_Beautiful(i, array.size(), min, max);
			
			items[x][y] = gi;
		}
	}
	
	/**
	 * 
	 * centers all items at the specified y position
	 * @param y which row it should center
	 */
	public void centerAtY(int y, CenterFormatType type){
		centerAtY(y, type, 0, 9);
	}
	
	/**
	 * 
	 * centers any y row
	 */
	public void centerAtYAll(CenterFormatType type, int min, int max){
		for(int y=0; y<getHeight(); y++)
			centerAtY(y, type, min, max);
	}
	
	/**
	 * 
	 * centers any y row
	 */
	public void centerAtYAll(CenterFormatType type){
		for(int y=0; y<getHeight(); y++)
			centerAtY(y, type);
	}
	
	
	/**
	 * 
	 * centers all items at the specified x position
	 * @param x which row it should center
	 */
	public void centerAtX(int x, CenterFormatType type){
		centerAtX(x, type, 0, getHeight());
	}
	
	/**
	 * 
	 * centers any x row
	 */
	public void centerAtXAll(CenterFormatType type, int min, int max){
		for(int x=0; x<9; x++)
			centerAtX(x, type, min, max);
	}
	
	/**
	 * 
	 * centers any y row
	 */
	public void centerAtXAll(CenterFormatType type){
		for(int x=0; x<9; x++)
			centerAtY(x, type);
	}
	
	/**
	 * 
	 * replaces air with the item
	 * @param i which item should get set
	 * @return returns amount of replaced items
	 */
	public int setBackground(GUIItem i){
		int a = 0;
		
		i.gui = this;
		
		for(int ix=0; ix<9; ix++){
			for(int iy=0; iy<height; iy++){
				if(items[ix][iy] == null || items[ix][iy].getItemStack().getType() == Material.AIR){
					items[ix][iy] = i;
					a++;
				}
			}
		}
		
		return a;
	}
	
	/**
	 * 
	 * replaces anything in the gui with the item
	 * @param i which item should get set
	 * @return returns amount of replaced items
	 */
	public int fill(GUIItem i){
		for(int ix=0; ix<9; ix++){
			for(int iy=0; iy<height; iy++){
				items[ix][iy] = i;
			}
		}
		
		return height*9;
	}
	
	/**
	 * 
	 * make sure to override this when creating the gui (not a reqiurement)
	 * @param player which player closed the inventory
	 */
	@Override
	public void onClose(Player player){ }
	
	/**
	 * 
	 * make sure to override this when creating the gui (not a reqiurement)
	 */
	protected void onSetItem(int x, int y,  GUIItem item){ }
	
	public static class AddItemFlag {
		
		public static final int WITHIN_X = 0;
		public static final int WITHIN_Y = 1;
		public static final int WITHIN = 2;
		public static final int CENTERIZED_HORIZONTAL = 3;
		
		@Getter private int type;
		private Object[] param;
		
		public static AddItemFlag createWithinX(int xMin, int xMax){
			AddItemFlag flag = new AddItemFlag();
			
			flag.type = WITHIN_X;
			flag.param = new Object[2];
			flag.param[0] = xMin;
			flag.param[1] = xMax;
			
			return flag;
		}
		
		public static AddItemFlag createWithinY(int yMin, int yMax){
			AddItemFlag flag = new AddItemFlag();
			
			flag.type = WITHIN_Y;
			flag.param = new Object[2];
			flag.param[0] = yMin;
			flag.param[1] = yMax;
			
			return flag;
		}
		
		public static AddItemFlag createWithin(int xMin, int xMax, int yMin, int yMax){
			AddItemFlag flag = new AddItemFlag();
			
			flag.type = WITHIN;
			flag.param = new Object[4];
			flag.param[0] = xMin;
			flag.param[1] = xMax;
			flag.param[2] = yMin;
			flag.param[3] = yMax;
			
			return flag;
		}
		
		public static AddItemFlag createCenterizedHorizontal(){
			AddItemFlag flag = new AddItemFlag();
			
			flag.type = CENTERIZED_HORIZONTAL;
			
			return flag;
		}
	}
	
	public static enum CenterFormatType {
		Normal,
		Beautiful;
	}
}
