/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.3
* @website http://marcely.de/ 
*/

package de.marcely.kitgui.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.marcely.kitgui.Kit;
import de.marcely.kitgui.Language;
import de.marcely.kitgui.main;
import de.marcely.kitgui.config.KitConfig;
import de.marcely.kitgui.config.Config;

public class kitcfg implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("kitgui.cfg")){
			if(args.length >= 1){
				String subcommand = args[0];
				if(subcommand.equalsIgnoreCase("help")){
					sendCommands(sender);
				}else if(subcommand.equalsIgnoreCase("seticon")){
					if(args.length >= 3){
						String kitname = args[1];
						String[] splits = args[2].split(":");
						Material icon = getMaterial(splits[0]);
						int id = 0;
						if(splits.length >= 2 && isNumber(splits[1]))
							id = Integer.valueOf(splits[1]);
						if(main.getKit(kitname.toLowerCase()) != null){
							if(icon != null){
								setIcon(kitname, icon, id);
								sender.sendMessage(Language.Changed_Icon.getMessage().replace("{icon}", icon.name().toLowerCase().replace("_", " ")));
							}else{
								sender.sendMessage(Language.Unkown_Material.getMessage().replace("{material}", args[2]));
							}
						}else{
							sender.sendMessage(Language.DoesntExist_Kit.getMessage().replace("{kit}", kitname));
						}
					}else{
						sender.sendMessage(Language.Usage.getMessage().replace("{usage}", "/kitcfg seticon <kit name> <material>"));
					}
				}else if(subcommand.equalsIgnoreCase("prefix")){
					
					if(args.length >= 2){
						
						Kit kit = main.kits.getKit(args[1]);
						
						if(kit != null){
							String kitname = main.getKit(args[1].toLowerCase()).getName();
							String bPrefix = null;
							
							if(kit != null && kit.getPrefix() != null)
								bPrefix = kit.getPrefix();
							
							if(args.length >= 4 && args[2].equalsIgnoreCase("set")){
								
								main.kits.setPrefix(kitname, args[3]);
								KitConfig.save(main.kits);
								sender.sendMessage(Language.Changed_Prefix.getMessage().replace("{kit}", kitname));
								
							}else if(args.length >= 3 && args[2].equalsIgnoreCase("remove")){
								
								main.kits.setPrefix(kitname, "");
								KitConfig.save(main.kits);
								sender.sendMessage(Language.Changed_Prefix.getMessage().replace("{kit}", kitname));
							
							}else{
								for(int i=0; i<7; i++)
									sender.sendMessage("");
								if(bPrefix != null)
									sender.sendMessage(ChatColor.GRAY + "Prefix: " + ChatColor.WHITE + bPrefix + kitname);
								else
									sender.sendMessage(Language.No_Prefix.getMessage().replace("{kit}", kitname));
								sender.sendMessage("");
								sender.sendMessage(Language.Usage_Change_Prefix.getMessage().replace("{usage}", "/" + label + " prefix <kit name> set <prefix>"));
								sender.sendMessage(Language.Usage_Remove_Prefix.getMessage().replace("{usage}", "/" + label + " prefix <kit name> remove"));
							}
						}else
							sender.sendMessage(Language.DoesntExist_Kit.getMessage().replace("{kit}", args[1]));
					}else{
						sender.sendMessage(Language.Usage.getMessage().replace("{usage}", "/" + label + " prefix <kit name>"));
					}
				}else if(subcommand.equalsIgnoreCase("lore")){

					if(args.length >= 2){
						
						Kit kit = main.kits.getKit(args[1]);
						
						if(kit != null){
							
							// check if under 1.2
							try{
								kit.getLores();
							}catch(Exception e){
								sender.sendMessage(ChatColor.RED + "A error occured: Remove the file at " + ChatColor.DARK_RED + "plugins/" + main.plugin.getName() + "/kits.cfg" + '\n' + ChatColor.RED + "And reload this plugin");
								return true;
							}
							
							
							// add lore
							if(args.length >= 4 && args[2].equalsIgnoreCase("add")){
								
								kit.addLore(args[3]);
								KitConfig.save(main.kits);
								sender.sendMessage(Language.Added_Lore.getMessage().replace("{kit}", kit.getName()));
								
							// remove lore
							}else if(args.length >= 4 && args[2].equalsIgnoreCase("remove")){
								
								if(main.isInteger(args[3])){
									
									int id = Integer.valueOf(args[3]);
									
									if(id >= 0 && id < kit.getLores().size()){
										
										kit.removeLore(kit.getLores().get(id));
										KitConfig.save(main.kits);
										sender.sendMessage(Language.Removed_Lore.getMessage().replace("{id}", args[3]).replace("{kit}", kit.getName()));
										
									}else
										sender.sendMessage(Language.Unkown_ID.getMessage().replace("{id}", args[3]));
									
								}else
									sender.sendMessage(Language.NotA_Number.getMessage().replace("{number}", args[3]));
								
							// informations
							}else{
								sender.sendMessage(ChatColor.DARK_AQUA + "Lores:");
								
								int i=0;
								for(String lore:kit.getLores()){
									sender.sendMessage("" + ChatColor.DARK_GREEN + i + ChatColor.GREEN + " " + lore);
									i++;
								}
								
								sender.sendMessage("");
								sender.sendMessage(Language.Usage_Add_Lore.getMessage().replace("{usage}", "/" + label + " lore <kit name> add <lore>"));
								sender.sendMessage(Language.Usage_Remove_Lore.getMessage().replace("{usage}", "/" + label + " lore <kit name> remove <id>"));
							}
						}else
							sender.sendMessage(Language.DoesntExist_Kit.getMessage().replace("{kit}", args[1]));
						
					}else
						sender.sendMessage(Language.Usage.getMessage().replace("{usage}", "/" + label + " lore <kit name>"));
				}else if(subcommand.equalsIgnoreCase("reload")){
					Config.load();
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
		sender.sendMessage("");
		sender.sendMessage(ChatColor.YELLOW + " ------------ " + ChatColor.GOLD + "Commands" + ChatColor.YELLOW + " ------------ ");
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg help");
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg seticon <kitName> <material>");
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg prefix <kitName>");
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg lore <kitname>");
		sender.sendMessage(ChatColor.DARK_AQUA + "/kitcfg reload");
		sender.sendMessage("");
		sender.sendMessage(Language.Info_MadeBy.getMessage().replace("{info}", "Marcely1199"));
		sender.sendMessage(Language.Info_Version.getMessage().replace("{info}", "" + main.getVersion()));
	}
	
	public void setIcon(String kitname, Material icon, int id){
		main.kits.setIcon(kitname, icon, (short) id);
		de.marcely.kitgui.config.KitConfig.save(main.kits);
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
