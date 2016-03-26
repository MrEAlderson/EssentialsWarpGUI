package de.marcely.warpgui;

import org.bukkit.ChatColor;

public class language {
	public static String noPermissions = "You have got no permissions for this command!";
	public static String theWarp = "The warp";
	public static String doesntExists = "doesn't exists!";
	public static String notAPlayer = "You are not a player!";
	public static String givingYouThe = "Giving you the";
	public static String warp = "warp";
	public static String iconChangedTo = "The icon has been changed to";
	public static String unkownMaterial = "Unkown material";
	public static String howToUseThisCommand = "How to use this command";
	public static String reloadedConfig = "The config has been successfully reloaded!";
	public static String unkownSubcommand = "Unkown subcommand";
	public static String ressourceBy = "ressource by";
	public static String version = "version";
	public static String reloadTheConfig = "Reload the config";
	public static String changeTheIconFromAWarp = "Change the icon from a warp";
	public static String changeThePrefixFromAWarp = "Change the prefix from a warp";
	public static String addOrRemoveLoresFromAWarp = "Add or remove lores from a warp";
	public static String saysYouTheCommandForWarpcfg = "Says you the commands for /warpcfg";
	public static String gotNoPrefix = "got no prefix.";
	public static String write = "Write";
	public static String toChangeThePrefix = "to change the prefix";
	public static String thePrefixByTheWarp = "The prefix by the warp";
	public static String hasBeenSuccessfullyChanged = "has been successfully changed!";
	public static String teleportingTo = ChatColor.GOLD + "Teleporting to " + ChatColor.YELLOW + "{warp}" + ChatColor.GOLD + "...";
	
	public static void updateLanguage(){
		if(main.CONFIG_LANGUAGE == LanguageType.English){
			noPermissions = "You have got no permissions for this command!";
			theWarp = "The warp";
			doesntExists = "doesn't exists!";
			notAPlayer = "You are not a player!";
			givingYouThe = "Giving you the";
			warp = "warp";
			iconChangedTo = "The icon has been changed to";
			unkownMaterial = "Unkown material";
			howToUseThisCommand = "How to use this command";
			reloadedConfig = "The config has been successfully reloaded!";
			unkownSubcommand = "Unkown subcommand";
			ressourceBy = "ressource by";
			version = "version";
			reloadTheConfig = "Reload the config";
			changeTheIconFromAWarp = "Change the icon from a warp";
			changeThePrefixFromAWarp = "Change the prefix from a warp";
			addOrRemoveLoresFromAWarp = "Add or remove lores from a warp";
			saysYouTheCommandForWarpcfg = "Says you the commands for /warpcfg";	
			gotNoPrefix = "got no prefix.";
			write = "Write";
			toChangeThePrefix = "to change the prefix";
			thePrefixByTheWarp = "The prefix by the warp";
			hasBeenSuccessfullyChanged = "has been successfully changed!";
			teleportingTo = ChatColor.GOLD + "Teleporting to " + ChatColor.YELLOW + "{warp}" + ChatColor.GOLD + "...";
		}else if(main.CONFIG_LANGUAGE == LanguageType.German){
			noPermissions = "Du hast keine Rechte für diesen Befehl";
			theWarp = "Den Warp";
			doesntExists = "gibt es nicht!";
			notAPlayer = "Du bist kein Spieler!";
			givingYouThe = "Gebe dir das";
			warp = "Warp";
			iconChangedTo = "Das Icon wurde geändert zu";
			unkownMaterial = "Unbekanntes Material";
			howToUseThisCommand = "Wie man den Befehl benutzt";
			reloadedConfig = "Die Einstellungen wurden erfolgreich aktualisiert!";
			unkownSubcommand = "Unbekanntes subbefehl";
			ressourceBy = "ressource von";
			version = "version";
			reloadTheConfig = "Einstellungen neuladen";
			changeTheIconFromAWarp = "Das Icon vom Warp ändern";
			changeThePrefixFromAWarp = "Den prefix von einem Warp ändern";
			addOrRemoveLoresFromAWarp = "Die Beschreibung von einem Warp ändern";
			saysYouTheCommandForWarpcfg = "Sagt dir die Befehle für /warpcfg";	
			gotNoPrefix = "hat keinen Prefix.";
			write = "Schreibe";
			toChangeThePrefix = "um den Prefix zu ändern";
			thePrefixByTheWarp = "Das Prefix von dem Warp";
			hasBeenSuccessfullyChanged = "wurde erfolgreich geändert!";
			teleportingTo = ChatColor.GOLD + "Teleportiere zu " + ChatColor.YELLOW + "{warp}" + ChatColor.GOLD + "...";
		}
	}
}
