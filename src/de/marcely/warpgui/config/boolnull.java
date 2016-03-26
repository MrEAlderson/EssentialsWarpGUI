package de.marcely.warpgui.config;

public enum boolnull {
	True,
	False,
	NULL;
	
	public boolean toBoolean(){
		if(this == True)
			return true;
		
		return false;
	}
	
	public static boolnull valueOf(boolean bool){
		if(bool == true)
			return True;
		else if(bool == false)
			return False;
		return NULL;
	}
}
