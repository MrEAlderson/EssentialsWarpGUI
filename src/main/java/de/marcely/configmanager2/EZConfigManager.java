package de.marcely.configmanager2;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import de.marcely.configmanager2.objects.Config;
import de.marcely.configmanager2.objects.Description;
import de.marcely.configmanager2.objects.ListItem;
import de.marcely.configmanager2.objects.Tree;

/**
 * 
 * You may want to use this for loading (or manually writing) config files
 */
public class EZConfigManager extends ConfigFile {
	
	public EZConfigManager(File file){
		this(file, true);
	}
	
	public EZConfigManager(File file, boolean createNewFile){
		super(file);
		
		final File dir = getFile().getParentFile();
		
		if(!dir.exists())
			dir.mkdirs();
		if(createNewFile){
			if(!getFile().exists()){
				try{
					getFile().createNewFile();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public EZConfigManager(String path){
		this(path, true);
	}
	
	public EZConfigManager(String path, boolean createNewFile){
		this(new File(path));
	}
	
	public void addConfig(String name, Object value){
		this.getPicker().addConfig(name, value);
	}
	
	public void addComment(String value){
		this.getPicker().addComment(value);
	}
	
	public void addComment(String value, String path){
		this.getPicker().addComment(path, value);
	}
	
	public @Nullable String getConfigString(String name){
		final Config config = this.getPicker().getConfig(name);
		
		if(config != null)
			return config.getValue();
		else
			return null;
	}
	
	public @Nullable Boolean getConfigBoolean(String name){
		final String config = getConfigString(name);
		
		if(config != null && (config.equalsIgnoreCase("true") || config.equalsIgnoreCase("false")))
			return Boolean.valueOf(config);
		else
			return null;
	}
	
	private static boolean isDouble(String str){
		try{
			Double.valueOf(str);
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	private static boolean isInteger(String str){
		try{
			Integer.valueOf(str);
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public @Nullable Double getConfigDouble(String name){
		final String config = getConfigString(name);
		
		if(config != null && isDouble(config))
			return Double.valueOf(config);
		else
			return null;
	}
	
	public @Nullable Integer getConfigInt(String name){
		final String config = getConfigString(name);
		
		if(config != null && isInteger(config))
			return Integer.valueOf(config);
		else
			return null;
	}
	
	public void addEmptyLine(){
		this.getPicker().addEmptyLine();
	}
	
	public void addEmptyLine(String path){
		this.getPicker().addEmptyLine(path);
	}
	
	public List<Config> getConfigsWhichStartWith(String startsWith){
		return this.getPicker().getConfigsWhichStartWith(startsWith);
	}
	
	public List<Config> getConfigsWhichEndWith(String startsWith){
		return this.getPicker().getConfigsWhichEndWith(startsWith);
	}
	
	public List<Config> getConfigs(String equals){
		return this.getPicker().getConfigs(equals);
	}
	
	public @Nullable List<String> getListItems(String path){
		final Tree tree = this.getPicker().getTree(path, false);
		
		if(tree != null)
			return tree.getRawChilds();
		else
			return null;
	}
	
	public void addListItems(List<String> items, String path){
		final Tree tree = this.getPicker().getTree(path, true);
		
		for(String item:items){
			tree.addChild(new ListItem(item, tree));
			tree.getRawChilds().add(item);
		}
	}
	
	public @Nullable String getDescription(String name){
		final Description desc = this.getPicker().getDescription(name);
		
		return desc != null ? desc.getValue() : null;
	}
	
	public void addDescription(String name, String value){
		this.getPicker().setDescription(name, value);
	}
}