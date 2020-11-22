package de.marcely.ezgui;

public class GUI_Style {
	
	public static int getSlotCenter_Normal(int id, int used, int min, int max){
		return (int) ((double) (max-min)/2 - used/2 + id) + min;
	}
	
	public static int getSlotCenter_Beautiful(int id, int used, int min, int max){
		if(min != 0 || max != 9)
			return (int) ((double) id/used*(max-min) + 0.5) + min;
		
		switch(used){
		case 1:
			return 4;
		case 2:
			switch(id){
			case 0:
				return 3;
			case 1:
				return 5;
			}
		case 3:
			switch(id){
			case 0:
				return 1;
			case 1:
				return 4;
			case 2:
				return 7;
			}
		case 4:
			switch(id){
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
			switch(id){
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
			switch(id){
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
			switch(id){
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
			return id;
		default:
			return -1;
		}
	}
}
