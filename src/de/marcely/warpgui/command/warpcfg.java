package de.marcely.warpgui.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.marcely.warpgui.Warp;
import de.marcely.warpgui.language;
import de.marcely.warpgui.main;
import de.marcely.warpgui.config.WarpConfig;
import de.marcely.warpgui.config.config;

public class warpcfg implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("warpgui.cfg")){
			if(args.length >= 1){
				String subcommand = args[0];
				if(subcommand.equalsIgnoreCase("help")){
					sendCommands(sender);
				}else if(subcommand.equalsIgnoreCase("seticon")){
					if(args.length >= 3){
						String warpname = main.getRealName(args[1]);
						String[] splits = args[2].split(":");
						Material icon = getMaterial(splits[0]);
						int id = 0;
						if(splits.length >= 2 && isNumber(splits[1]))
							id = Integer.valueOf(splits[1]);
						if(warpname != null){
							if(icon != null){
								setIcon(warpname, icon, id);
								sender.sendMessage(ChatColor.GREEN + language.iconChangedTo + " " + ChatColor.DARK_GREEN + icon.name().toLowerCase().replace("_", " ") + ChatColor.GREEN + "!");
							}else{
								sender.sendMessage(ChatColor.DARK_RED + language.unkownMaterial + " " + ChatColor.RED + args[2] + ChatColor.DARK_RED + "!");
							}
						}else{
							sender.sendMessage(ChatColor.DARK_RED + language.theWarp + " " + ChatColor.RED + warpname + ChatColor.DARK_RED + " " + language.doesntExists);
						}
					}else{
						sender.sendMessage(ChatColor.YELLOW + language.howToUseThisCommand + ": " + ChatColor.GOLD + "/warpcfg seticon <warp name> <material>");
					}
				}else if(subcommand.equalsIgnoreCase("prefix")){
					if(args.length >= 2){
						String warpname = main.getRealName(args[1]);
						if(warpname != null){
							Warp warp = main.warps.getWarp(warpname);
							String bPrefix = null;
							
							if(warp != null && warp.getPrefix() != null)
								bPrefix = warp.getPrefix();
							
							if(args.length >= 4 && args[2].equalsIgnoreCase("set")){
								String aPrefix = args[3];
								main.warps.setPrefix(warpname, aPrefix);
								WarpConfig.save(main.warps);
								sender.sendMessage(ChatColor.GREEN + language.thePrefixByTheWarp + " " + ChatColor.DARK_GREEN + warpname + ChatColor.GREEN + " " + language.hasBeenSuccessfullyChanged);
							}else{
								for(int i=0; i<7; i++)
									sender.sendMessage("");
								if(bPrefix != null)
									sender.sendMessage(ChatColor.GRAY + "Prefix: " + ChatColor.WHITE + bPrefix + warpname);
								else
									sender.sendMessage(ChatColor.RED + warpname + " " + ChatColor.DARK_RED + language.gotNoPrefix);
								sender.sendMessage("");
								sender.sendMessage(ChatColor.GOLD + language.write + ChatColor.YELLOW + "/warpcfg prefix <warp name> set <prefix> " + ChatColor.GOLD + language.toChangeThePrefix);
							}
						}else{
							sender.sendMessage(ChatColor.DARK_RED + language.theWarp + " " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " " + language.doesntExists);
						}
					}else{
						sender.sendMessage(ChatColor.YELLOW + language.howToUseThisCommand + ": " + ChatColor.GOLD + "/warpcfg prefix <warp name>");
					}
				}else if(subcommand.equalsIgnoreCase("lore")){
					sender.sendMessage(ChatColor.RED + "Currently not supported!");
					/*if(args.length >= 2){
						if(main.existsWarp(args[1])){
							if(args.length >= 4 && args[2].equalsIgnoreCase("add")){
								
							}else if(args.length >= 4 && args[2].equalsIgnoreCase("remove")){
								
							}else{
								
							}
						}else{
							sender.sendMessage(ChatColor.DARK_RED + language.theWarp + " " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " " + language.doesntExists);
						}
					}else{
						sender.sendMessage(ChatColor.YELLOW + language.howToUseThisCommand + ": " + ChatColor.GOLD + "/warpcfg lore <warp name>");
					}*/
				}else if(subcommand.equalsIgnoreCase("reload")){
					config.load();
					sender.sendMessage(ChatColor.GREEN + language.reloadedConfig);
				}else{
					sender.sendMessage(ChatColor.DARK_RED + language.unkownSubcommand + " " + ChatColor.RED + subcommand + ChatColor.DARK_RED + "!");
				}
			}else
				sendCommands(sender);
		}else{
			sender.sendMessage(ChatColor.DARK_RED + language.noPermissions);
		}
		return true;
	}
	
	public void sendCommands(CommandSender sender){
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + " ------------ " + ChatColor.GOLD + "Commands" + ChatColor.YELLOW + " ------------ ");
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg help " + ChatColor.AQUA + language.saysYouTheCommandForWarpcfg);
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg seticon <warpName> <material> " + ChatColor.AQUA + language.changeTheIconFromAWarp);
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg prefix <warpName> " + ChatColor.AQUA + language.changeThePrefixFromAWarp);
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg lore <warpName> " + ChatColor.AQUA + language.addOrRemoveLoresFromAWarp);
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg reload " + ChatColor.AQUA + language.reloadTheConfig);
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GREEN + "WarpGUI " + language.ressourceBy + " " + ChatColor.DARK_GREEN + "Marcely1199");
		sender.sendMessage(ChatColor.GREEN + "WarpGUI " + language.version + " " + ChatColor.DARK_GREEN + main.version);
	}
	
	public void setIcon(String warpname, Material icon, int id){
		main.warps.setIcon(warpname, icon, (short) id);
		de.marcely.warpgui.config.WarpConfig.save(main.warps);
	}
	
	public boolean isNumber(String str){
		try{
			Integer.valueOf(str);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public Material getMaterial(String str){
		for(Material m:Material.values()){
			if(str.equalsIgnoreCase(m.name()) ||
			   str.equalsIgnoreCase(m.name().replace("_", "")) ||
			   m.name().equalsIgnoreCase(str + "_item") ||
			   isNumber(str) && Integer.valueOf(str) == m.getId())
				return m;
		}
		return null;
	}
}
