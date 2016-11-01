/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6
* @website http://marcely.de/ 
*/

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
