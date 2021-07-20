package de.marcely.warpgui.command;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.marcely.warpgui.Message;
import de.marcely.warpgui.comand.warpcfg.*;
import de.marcely.warpgui.util.Util;
import lombok.Getter;

public class WarpcfgCommand implements CommandExecutor {
	
	@Getter private final SubCommand[] subCommands;
	
	public WarpcfgCommand(){
		this.subCommands = new SubCommand[]{
			new SubCommand("help", "", new HelpCommand(this)),
			new SubCommand("list", "", new ListCommand()),
			new SubCommand("reload", "", new ReloadCommand()),
			new SubCommand("seticon", "<warp name> <material>", new SetIconCommand()),
			new SubCommand("prefix", "<warp name> [...]", new PrefixCommand()),
			new SubCommand("suffix", "<warp name> [...]", new SuffixCommand()),
			new SubCommand("displayname", "<warp name> [...]", new DisplayNameCommand()),
			new SubCommand("lore", "<warp name> [...]", new LoreCommand())
		};
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg0, String label, String[] args){
		if(!Util.hasPermission(sender, "warpgui.cfg")){
			sender.sendMessage(Message.No_Permissions.getValue());
			
			return true;
		}
		
		if(args.length == 0){
			getSubCommand("help").getExecutor().onInvoke(sender, "help", new String[0]);
			
			return true;
		}
		
		final SubCommand cmd = getSubCommand(args[0]);
		
		if(cmd != null)
			cmd.getExecutor().onInvoke(sender, args[0], args.length >= 2 ? Arrays.copyOfRange(args, 1, args.length) : new String[0]);
		else
			sender.sendMessage(Message.Unkown_Argument.getValue().replace("{arg}", args[0].toLowerCase()));
		
		return true;
	}
	
	private @Nullable SubCommand getSubCommand(String name){
		for(SubCommand cmd:this.subCommands){
			if(cmd.getName().equalsIgnoreCase(name))
				return cmd;
		}
		
		return null;
	}
}
