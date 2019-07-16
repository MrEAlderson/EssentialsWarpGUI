/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.config;

import de.marcely.configmanager2.EZConfigManager;
import de.marcely.configmanager2.objects.Config;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.util.StringUtil;
import de.marcely.warpgui.util.Util;

public class MessagesConfig {
	
	public static EZConfigManager cm = new EZConfigManager(Util.FILE_CONFIG_MESSAGES, false);
	
	public static void load(){
		if(!cm.exists()){
			for(Message msg:Message.values())
				msg.setValue(msg.getDefaultValue());
			
			save();
			return;
		}
		
		cm.load();
		
		for(Config config:cm.getRootTree().getChilds()){
			if(config.getType() != Config.TYPE_CONFIG)
				continue;
			
			final String key = config.getName();
			final String value = (String) config.getValue();
			final Message msg = Message.getMessage(key);
			
			if(msg != null)
				msg.setValue(StringUtil.readableStringToFormattedChatColor(value));
		}
	}
	
	public static void save(){
		cm.clear();
		
		for(Message msg:Message.values())
			cm.addConfig(msg.name(), StringUtil.formattedChatColorStringToReadable(msg.getValue()));
		
		cm.save();
	}
}