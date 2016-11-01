/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.config;

import java.util.Map.Entry;

import de.marcely.warpgui.Language;
import de.marcely.warpgui.main;

public class LanguageConfig {
	public static ConfigManager cm = new ConfigManager(main.plugin.getName(), "messages.yml", false);
	
	public static void load(){
		if(cm.exists()){
			cm.load();
			
			for(Entry<String, Object> entry:cm.getInside(0).entrySet()){
				String key = entry.getKey();
				String value = (String) entry.getValue();
				
				Language l = Language.getLanguage(key);
				if(l != null)
					Language.setTranslation(l, Language.stringToChatColor(value));
			}
			
		}
		
		save();
	}
	
	public static void save(){
		cm.clear();
		
		for(Language l:Language.values())
			cm.addConfig(l.name(), Language.chatColorToString(l.getMessage()));
		
		cm.save();
	}
}