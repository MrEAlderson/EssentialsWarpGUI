package de.marcely.warpgui.library;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.marcely.warpgui.EssentialsWarpGUI;
import lombok.Getter;

public enum LibraryType {
	
	VAULT("Vault", Vault.class),
	
	ESSENTIALS("Essentials", Essentials.class),
	ESSENTIALS_X("EssentialsX", EssentialsX.class);
	
	@Getter private final String pluginName;
	@Getter private final Class<? extends Library> instanceClass;
	
	@Getter private Library instance;
	
	private LibraryType(String pluginName, Class<? extends Library> clazz){
		this.pluginName = pluginName;
		this.instanceClass = clazz;
	}
	
	@SuppressWarnings("unchecked")
	public static @Nullable <T>T findInstanceWithInterface(Class<? extends T> clazz){
		for(LibraryType type:values()){
			if(type.instance == null)
				continue;
			
			for(Class<?> c:type.instanceClass.getInterfaces()){
				if(c == clazz)
					return (T) type.instance;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static @Nullable <T>T get(Class<? extends T> clazz){
		for(LibraryType type:values()){
			if(type.instanceClass == clazz)
				return (T) type.instance;
		}
		
		return null;
	}
	
	public static void initAll(){
		for(LibraryType type:values()){
			if(type.getInstance() != null)
				continue;
			
			final Plugin plugin = Bukkit.getPluginManager().getPlugin(type.getPluginName());
			
			if(plugin != null){
				EssentialsWarpGUI.instance.getLogger().info("Loading library '" + type.pluginName + "'...");
				
				try{
					type.instance = type.instanceClass.newInstance();
					type.instance.load();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
