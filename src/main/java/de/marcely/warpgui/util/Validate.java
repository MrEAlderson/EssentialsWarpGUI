/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.util;

public class Validate {
	
	public static void checkNotNull(Object object){
		checkNotNull(object, (String) null);
	}

	public static void checkNotNull(Object object, String message){
		if(object == null)
			throw new NullPointerException(message == null ? "The input object is null. Please check the parameters!" : message);
	}
}
