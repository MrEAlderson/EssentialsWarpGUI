/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import de.marcely.warpgui.Util;
import de.marcely.warpgui.Warp;
import de.marcely.warpgui.Language;
import de.marcely.warpgui.main;
import de.marcely.warpgui.config.LanguageConfig;
import de.marcely.warpgui.config.WarpConfig;
import de.marcely.warpgui.config.Config;

public class warpcfg implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(Util.hasPermission(sender, "warpgui.cfg")){
			if(args.length >= 1){
				String subcommand = args[0];
				if(subcommand.equalsIgnoreCase("help")){
					sendCommands(sender);
				}else if(subcommand.equalsIgnoreCase("seticon")){
					if(args.length >= 3){
						String warpname = Util.getRealName(args[1]);
						String[] splits = args[2].split(":");
						Material icon = getMaterial(splits[0]);
						int id = 0;
						if(splits.length >= 2 && isNumber(splits[1]))
							id = Integer.valueOf(splits[1]);
						if(warpname != null){
							if(icon != null){
								setIcon(warpname, icon, id);
								sender.sendMessage(Language.Changed_Icon.getMessage().replace("{icon", icon.name().toLowerCase().replace("_", " ")));
							}else{
								sender.sendMessage(Language.Unkown_Material.getMessage().replace("{material}", splits[0]));
							}
						}else{
							sender.sendMessage(Language.DoesntExist_Warp.getMessage().replace("{warp}", warpname));
						}
					}else{
						sender.sendMessage(Language.Usage.getMessage().replace("{usage}", "/" + label + " seticon <warp name> <material>"));
					}
				}else if(subcommand.equalsIgnoreCase("prefix")){
					if(args.length >= 2){
						
						String warpname = Util.getRealName(args[1]);
						if(warpname != null){
							
							Warp warp = main.warps.getWarp(warpname);
							
							if(warp == null){
								warp = new Warp(warpname, new ItemStack(Material.CLAY_BALL));
								main.warps.warps.add(warp);
							}
							
							String bPrefix = null;
							
							if(warp != null && warp.getPrefix() != null)
								bPrefix = warp.getPrefix();
							
							// set the prefix
							if(args.length >= 4 && args[2].equalsIgnoreCase("set")){
								
								main.warps.setPrefix(warpname, args[3]);
								WarpConfig.save(main.warps);
								sender.sendMessage(Language.Changed_Prefix.getMessage().replace("{warp}", warpname));
							
							// remove the prefix
							}else if(args.length >= 3 && args[2].equalsIgnoreCase("remove")){
								
								main.warps.setPrefix(warpname, "");
								WarpConfig.save(main.warps);
								sender.sendMessage(Language.Removed_Prefix.getMessage().replace("{warp}", warpname));
								
							// informations
							}else{
								for(int i=0; i<7; i++)
									sender.sendMessage("");
								if(bPrefix != null)
									sender.sendMessage(ChatColor.GRAY + "Prefix: " + ChatColor.WHITE + bPrefix + warpname);
								else
									sender.sendMessage(Language.No_Prefix.getMessage().replace("{warp}", warpname));
								sender.sendMessage("");
								sender.sendMessage(Language.Usage_Change_Prefix.getMessage().replace("{usage}", "/" + label + " prefix <warp name> set <prefix>"));
								sender.sendMessage(Language.Usage_Remove_Prefix.getMessage().replace("{usage}", "/" + label + " prefix <warp name> remove"));
							}
						}else
							sender.sendMessage(Language.DoesntExist_Warp.getMessage().replace("{warp}", args[1]));
					}else
						sender.sendMessage(Language.Usage.getMessage().replace("{usage}", "/" + label + " prefix <warp name>"));
				}else if(subcommand.equalsIgnoreCase("lore")){
					
					if(args.length >= 2){
						
						String warpname = Util.getRealName(args[1]);
						
						if(warpname != null){
							
							Warp warp = main.warps.getWarp(warpname);
							
							if(warp == null){
								warp = new Warp(warpname, new ItemStack(Material.CLAY_BALL));
								main.warps.warps.add(warp);
							}
							
							// check if under 1.4
							try{
								warp.getLores();
							}catch(Exception e){
								sender.sendMessage(ChatColor.RED + "A error occured: Remove the file at " + ChatColor.DARK_RED + "plugins/" + main.plugin.getName() + "/warps.cfg" + '\n' + ChatColor.RED + "And reload this plugin");
								return true;
							}
							
							
							// add lore
							if(args.length >= 4 && args[2].equalsIgnoreCase("add")){
								
								String lore = "";
								
								for(int i=3; i<args.length; i++){
									lore += args[i];
									if(i - 1 < args.length) lore += " ";
								}
								
								warp.addLore(lore);
								WarpConfig.save(main.warps);
								sender.sendMessage(Language.Added_Lore.getMessage().replace("{warp}", warpname));
								
							// remove lore
							}else if(args.length >= 4 && args[2].equalsIgnoreCase("remove")){
								
								if(Util.isInteger(args[3])){
									
									int id = Integer.valueOf(args[3]);
									
									if(id >= 0 && id < warp.getLores().size()){
										
										warp.removeLore(warp.getLores().get(id));
										WarpConfig.save(main.warps);
										sender.sendMessage(Language.Removed_Lore.getMessage().replace("{id}", args[3]).replace("{warp}", warpname));
										
									}else
										sender.sendMessage(Language.Unkown_ID.getMessage().replace("{id}", args[3]));
									
								}else
									sender.sendMessage(Language.NotA_Number.getMessage().replace("{number}", args[3]));
								
							// informations
							}else{
								sender.sendMessage(ChatColor.DARK_AQUA + "Lores:");
								
								int i=0;
								for(String lore:warp.getLores()){
									sender.sendMessage("" + ChatColor.DARK_GREEN + i + ChatColor.GREEN + " " + lore);
									i++;
								}
								
								sender.sendMessage("");
								sender.sendMessage(Language.Usage_Add_Lore.getMessage().replace("{usage}", "/" + label + " lore <warp name> add <lore>"));
								sender.sendMessage(Language.Usage_Remove_Lore.getMessage().replace("{usage}", "/" + label + " lore <warp name> remove <id>"));
							}
						}else
							sender.sendMessage(Language.DoesntExist_Warp.getMessage().replace("{warp}", args[1]));
						
					}else
						sender.sendMessage(Language.Usage.getMessage().replace("{usage}", "/" + label + " lore <warp name>"));
				}else if(subcommand.equalsIgnoreCase("list")){
					String warps = "";
					
					List<Warp> list = main.warps.getWarps();
					
					for(int i=0; i<list.size(); i++){
						warps += list.get(i).getName();
						
						if(i != list.size() - 1) warps += ", ";
					}
					
					sender.sendMessage(Language.List_Warps.getMessage().replace("{warps}", warps));
					
				}else if(subcommand.equalsIgnoreCase("reload")){
					Config.load();
					LanguageConfig.load();
					sender.sendMessage(Language.Reloaded_Config.getMessage());
				}else{
					sender.sendMessage(Language.Unkown_Argument.getMessage().replace("{arg}", subcommand));
				}
			}else
				sendCommands(sender);
		}else{
			sender.sendMessage(Language.No_Permissions.getMessage());
		}
		return true;
	}
	
	public void sendCommands(CommandSender sender){
		sender.sendMessage(ChatColor.YELLOW + " ------------ " + ChatColor.GOLD + "Commands" + ChatColor.YELLOW + " ------------ ");
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg help");
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg seticon <warpName> <material>");
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg prefix <warpName>");
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg lore <warpName>");
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg list");
		sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg reload");
		sender.sendMessage("");
		sender.sendMessage(Language.Info_MadeBy.getMessage().replace("{info}", "Marcely1199"));
		sender.sendMessage(Language.Info_Version.getMessage().replace("{info}", "" + main.getVersion()));
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
