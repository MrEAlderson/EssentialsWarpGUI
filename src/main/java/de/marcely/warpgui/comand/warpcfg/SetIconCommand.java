package de.marcely.warpgui.comand.warpcfg;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.SubCommandExecutor;
import de.marcely.warpgui.components.Warp;
import de.marcely.warpgui.components.WarpsContainer;
import de.marcely.warpgui.config.WarpsConfig;
import de.marcely.warpgui.util.ItemStackUtil;

public class SetIconCommand implements SubCommandExecutor {

	@Override
	public void onInvoke(CommandSender sender, String label, String[] args){
		if(args.length <= 1){
			sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg seticon <warp name> <material>"));
			
			return;
		}
		
		final WarpsContainer container = EssentialsWarpGUI.instance.getContainer();
		
		container.synchronizeWithProvider();
		
		final Warp warp = container.getWarp(args[0]);
		
		if(warp != null){
			final ItemStack icon = ItemStackUtil.ofString(args[1]);
			
			if(icon != null && icon.getType() != Material.AIR){
				warp.setIcon(icon);
				WarpsConfig.save();
				
				sender.sendMessage(Message.Changed_Icon.getValue().replace("{icon}", ItemStackUtil.toString(icon)));
				
			}else
				sender.sendMessage(Message.Unkown_Material.getValue().replace("{material}", args[1]));
		}else
			sender.sendMessage(Message.DoesntExist_Warp.getValue().replace("{warp}", args[0]));
	}
}
