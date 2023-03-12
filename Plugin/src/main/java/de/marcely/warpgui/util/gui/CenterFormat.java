package de.marcely.warpgui.util.gui;

/**
 * Helper for easily aligning items in a GUI
 */
public enum CenterFormat {
	
	/**
	 * Item: X, Air: O<br>
	 * Example with 3 items:<br>
	 * O O O X X X O O O
	 */
	CENTRALIZED,
	
	/**
	 * Item: X, Air: O<br>
	 * Example with 3 items:<br>
	 * O X O X O X O X O
	 */
	ALIGNED;
	
	public int calculate(int slot, int amount, int min, int max){
		switch(this){
		case CENTRALIZED:
			return getSlotCenter_Normal(slot, amount, min, max);
		case ALIGNED:
			return getSlotCenter_Beautiful(slot, amount, min, max);
		}
		
		return slot;
	}
	
	private static int getSlotCenter_Normal(int slot, int amount, int min, int max){
		return (int) ((double) (max-min)/2 - amount/2 + slot) + min;
	}
	
	private static int getSlotCenter_Beautiful(int slot, int amount, int min, int max){
		if(min == 0 && max == 9)
			return getSlotCenter_Beautiful_9(slot, amount);
		else if(max-min == 3)
			return getSlotCenter_Beautiful_3(slot, amount)+min;
		else
			return (int) ((double) slot/amount*(max-min) + 0.5D) + min;
	}
	
	private static int getSlotCenter_Beautiful_3(int slot, int amount){
		switch(amount){
		case 1:
			return 1;
		case 2:
			switch(slot){
			case 0:
				return 0;
			case 1:
				return 2;
			}
		default:
			return slot;
		}
	}
	
	private static int getSlotCenter_Beautiful_9(int slot, int amount){
		switch(amount){
		case 1:
			return 4;
		case 2:
			switch(slot){
			case 0:
				return 3;
			case 1:
				return 5;
			}
		case 3:
			switch(slot){
			case 0:
				return 1;
			case 1:
				return 4;
			case 2:
				return 7;
			}
		case 4:
			switch(slot){
			case 0:
				return 0;
			case 1:
				return 3;
			case 2:
				return 5;
			case 3:
				return 8;
			}
		case 5:
			switch(slot){
			case 0:
				return 0;
			case 1:
				return 2;
			case 2:
				return 4;
			case 3:
				return 6;
			case 4:
				return 8;
			}
		case 6:
			switch(slot){
			case 0:
				return 0;
			case 1:
				return 1;
			case 2:
				return 3;
			case 3:
				return 5;
			case 4:
				return 7;
			case 5:
				return 8;
			}
		case 7:
			switch(slot){
			case 0:
				return 0;
			case 1:
				return 2;
			case 2:
				return 3;
			case 3:
				return 4;
			case 4:
				return 5;
			case 5:
				return 6;
			case 6:
				return 8;
			}
		case 8:
		case 9:
			return slot;
		default:
			return -1;
		}
	}
}
