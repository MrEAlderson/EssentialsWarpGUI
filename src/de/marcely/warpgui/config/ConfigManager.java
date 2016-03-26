package de.marcely.warpgui.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;

public class ConfigManager {
	private String configName = null;
	private File configFile = null;
	private LinkedHashMap<String, Object> configs = new LinkedHashMap<String, Object>();
	
	public ConfigManager(String pluginName, String configName){
		this.configName = configName;
		configFile = new File("plugins/" + pluginName + "/" + configName);
		
		File dir = new File("plugins/" + pluginName);
		
		if(!dir.exists())
			dir.mkdir();
		if(!configFile.exists())
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public String getConfigName(){
		return this.configName;
	}
	
	public void addConfig(String name, String value){
		configs.put(name, value);
	}
	
	public void addConfig(String name, boolean value){
		configs.put(name, value);
	}
	
	public void addConfig(String name, Double value){
		configs.put(name, value);
	}
	
	public void addConfig(String name, int value){
		configs.put(name, value);
	}
	
	public void addComment(String comment){
		addConfig("# " + comment, "");
	}
	
	public String getConfigString(String name){
		Object obj = getConfigObj(name);
		if(obj instanceof String)
			return (String) obj;
		
		return null;
	}
	
	public boolnull getConfigBoolean(String name){
		Object obj = getConfigObj(name);
		if(obj instanceof Boolean)
			return boolnull.valueOf((boolean) obj);
		else if(obj instanceof String){
			String str = String.valueOf(obj);
			if(str.equalsIgnoreCase("true") ||
			   str.equalsIgnoreCase("false"))
				return boolnull.valueOf(Boolean.valueOf(str));
		}
		return boolnull.NULL;
	}
	
	public double getConfigDouble(String name){
		Object obj = getConfigObj(name);
		if(obj instanceof Double)
			return (double) obj;
		else if(obj instanceof Float)
			return (double)((float)obj);
		else if(obj instanceof String){
			return Double.valueOf((String) obj);
		}
		
		return Double.MAX_VALUE;
	}
	
	public int getConfigInt(String name){
		Object obj = getConfigObj(name);
		if(obj instanceof Integer)
			return (int) obj;
		
		return Integer.MAX_VALUE;
	}
	
	public void addEmptyLine(){
		addConfig("empty" + new Random().nextInt(), (String) null);
	}
	
	public HashMap<String, String> getKeysWhichStartWith(String startsWith){
		HashMap<String, String> list = new HashMap<String, String>();
		for(Entry<String, Object> entry:configs.entrySet()){
			String name = entry.getKey();
			if(name.startsWith(startsWith)){
				String value = entry.getValue().toString();
				list.put(name, value);
				Bukkit.getConsoleSender().sendMessage(value);
			}
		}
		
		return list;
	}
	
	public Object getConfigObj(String name){
		return configs.get(name);
	}
	
	public boolean update(){
		if(!configFile.exists()){
			save();
			return false;
		}
		
		return true;
	}
	
	public void save(){
		try {
			savee();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void savee() throws IOException{
		if(configFile.exists()) configFile.delete();
		
	    FileWriter fw = new FileWriter(configFile);
	    BufferedWriter bw = new BufferedWriter(fw);
	    for(Entry<String, Object> entry:configs.entrySet()){
	    	String name = entry.getKey();
	    	Object value = entry.getValue();
	    	if(name.startsWith("empty") && value == null)
	    		bw.write("");
	    	else if(!String.valueOf(name).startsWith("# "))
		    	bw.write(name + ": " + String.valueOf(value));
		    else
		    	bw.write(name);
	    	bw.newLine();
	    }
	    bw.close();
	}
	
	public void load(){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(configFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String zeile = null;
	    try {
		    clear();
			while((zeile = br.readLine()) != null){ 
				String[] strs = zeile.split(":");
				String name = strs[0];
				String value = "";
				if(strs.length >= 2){
					value = strs[1];
				}
				while(value.startsWith(" ")){
					value = value.substring(1, value.length());
				}
				configs.put(name, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void clear(){
		configs = new LinkedHashMap<String, Object>();
	}
	
	public boolean exists(){
		return configFile.exists();
	}
}
