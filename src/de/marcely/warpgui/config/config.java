package de.marcely.warpgui.config;

import de.marcely.warpgui.LanguageType;
import de.marcely.warpgui.main;

public class config {
	public static ConfigManager manager = new ConfigManager("Essentials_WarpGUI", "config.yml");
	
	public static void load(){
		manager.load();
		LanguageType language = LanguageType.getType(manager.getConfigString("language"));
		String version = manager.getConfigString("config-version");
		String invtitle = manager.getConfigString("inv-title");
		boolnull firstcharcaps = manager.getConfigBoolean("first-char-caps");
		
		if(invtitle != null)
			main.CONFIG_INVTITLE = invtitle;
		if(firstcharcaps != boolnull.NULL)
			main.CONFIG_FIRSTCHARCAPS = firstcharcaps.toBoolean();
		if(language != null){
			main.CONFIG_LANGUAGE = language;
			de.marcely.warpgui.language.updateLanguage();
		}
		if(version == null || version != null && !version.equals(main.version))
			save();
	}
	
	public static void save(){
		manager.clear();
		manager.addComment("Don't change this");
		manager.addConfig("config-version", main.version);
		
		manager.addEmptyLine();
		
		manager.addComment("The language from this plugin");
		manager.addConfig("language", main.CONFIG_LANGUAGE.name());
		
		manager.addComment("Set the title from the inventory");
		manager.addConfig("inv-title", main.CONFIG_INVTITLE);
		
		manager.addEmptyLine();
		
		manager.addComment("If it's enabled, the first character in the name of the warp is in caps");
		manager.addConfig("first-char-caps", main.CONFIG_FIRSTCHARCAPS);
		
		manager.save();
	}
}
