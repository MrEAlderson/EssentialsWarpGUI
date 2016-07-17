/**
* Adds an GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.5.2
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;

import de.marcely.warpgui.Util;

public class ConfigManager {
	private File configFile = null;
	private MultiKeyMap<String, Object> configs = new MultiKeyMap<String, Object>();
	
	public ConfigManager(String pluginName, String configName){
		setPath("plugins/" + pluginName + "/" + configName, true);
	}
	
	public ConfigManager(String pluginName, String configName, boolean createNewFile){
		setPath("plugins/" + pluginName + "/" + configName, createNewFile);
	}
	
	public void addConfig(String name, Object value){
		configs.put(name, value);
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
		if(obj instanceof String && Util.isInteger(String.valueOf(obj)))
			return Integer.valueOf(String.valueOf(obj));
		
		return Integer.MAX_VALUE;
	}
	
	public void addEmptyLine(){
		addConfig("empty" + new Random().nextInt(), (String) null);
	}
	
	public HashMap<String, String> getKeysWhichStartWith(String startsWith){
		HashMap<String, String> list = new HashMap<String, String>();
		for(MultiKeyEntry<String, Object> MultiKeyEntry:configs.entrySet()){
			String name = MultiKeyEntry.getKey();
			if(name.startsWith(startsWith)){
				String value = MultiKeyEntry.getValue().toString();
				list.put(name, value);
				Bukkit.getConsoleSender().sendMessage(value);
			}
		}
		
		return list;
	}
	
	public Object getConfigObj(String name){
		return configs.getFirst(name);
	}
	
	public boolean update(){
		if(!configFile.exists()){
			save();
			return false;
		}
		
		return true;
	}
	
	public MultiKeyMap<String, Object> getInside(int insideLvl){
		MultiKeyMap<String, Object> list = new MultiKeyMap<String, Object>();
		
	    for(MultiKeyEntry<String, Object> MultiKeyEntry:configs.entrySet()){
	    	String name = MultiKeyEntry.getKey();
	    	Object value = MultiKeyEntry.getValue();
	    	if(!String.valueOf(name).startsWith("# ")){
	    		if(name.split("\\.").length - 1 == insideLvl){
	    			String str = "";
	    			int i = 1;
	    			int max = name.split("\\.").length;
	    			
	    			for(String s:name.split("\\.")){
	    				while(s.startsWith("	"))
	    					s = s.substring(1, s.length());
	    				if(i >= max)
	    					str += s;
	    				else{
	    					str += s + ".";
	    					i++;
	    				}
	    			}
	    			if(value instanceof String){
	    				String v = (String) value;
	    				while(v.startsWith("	"))
	    					v = v.substring(1, v.length());
	    				value = v;
	    			}
	    			list.put(str, value);
	    		}
	    	}
	    }
	    
	    return list;
	}
	
	public void save(){
		try {
			savee();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void savee() throws IOException {
		if(configFile.exists()) configFile.delete();
		
	    BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));
	    
	    List<String> doneInsideConfigs = new ArrayList<String>();
	    
	    for(MultiKeyEntry<String, Object> MultiKeyEntry:configs.entrySet()){
	    	String name = MultiKeyEntry.getKey();
	    	Object value = MultiKeyEntry.getValue();
	    	
	    	// create empty lines
	    	if(name.startsWith("empty") && value == null){
	    		bw.write("");
	    		bw.newLine();
	    	
	    	// isn't annotated
	    	}else if(!String.valueOf(name).startsWith("# ")){
	    		
	    		// prepare configs with multiple names
		    	if(name.split("\\.").length >= 2){
		    		String insideConfig = "";
		    		for(int i=0; i<name.split("\\.").length - 1; i++){
		    			if(i <= name.split("\\.").length - 2)
		    				insideConfig += name.split("\\.")[i];
		    			else
		    				insideConfig += name.split("\\.")[i] + ".";
		    		}
		    		
		    		// write object
		    		if(!doneInsideConfigs.contains(insideConfig)){
		    			bw.write(insideConfig + " {");
		    			bw.newLine();
		    			for(MultiKeyEntry<String, Object> MultiKeyEntry1:configs.entrySet()){
		    		    	String name1 = MultiKeyEntry1.getKey();
		    		    	Object value1 = MultiKeyEntry1.getValue();
		    		    	if(!String.valueOf(name1).startsWith("# ") && name1.split("\\.").length >= 2){
		    		    		String insideConfig1 = "";
		    		    		for(int i=0; i<name1.split("\\.").length - 1; i++){
		    		    			if(i <= name1.split("\\.").length - 2)
		    		    				insideConfig1 += name1.split("\\.")[i];
		    		    			else
		    		    				insideConfig1 += name1.split("\\.")[i] + ".";
		    		    		}
		    		    		// write config
		    		    		if(insideConfig.equals(insideConfig1)){
		    		    			String s = "";
		    		    			for(int i=0; i<name1.split("\\.").length - 1; i++)
		    		    				s += "	";
		    		    			bw.write(s + name1.replace(insideConfig, "").substring(1) + ": " + String.valueOf(value1));
		    		    			bw.newLine();
		    		    		}
		    		    	}
		    			}
		    			doneInsideConfigs.add(insideConfig);
		    			bw.write("}");
		    			bw.newLine();
		    		}
		    	}else{
		    		// write config
		    		bw.write(name + ": " + String.valueOf(value));
		    		bw.newLine();
		    	}
	    	}else{
		    	bw.write(name);
		    	bw.newLine();
	    	}
	    }
	    bw.close();
	}
	
	public void load(){
		// prepare
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF8"));
		}catch (FileNotFoundException | UnsupportedEncodingException e1){
			e1.printStackTrace();
		}
		String line = null;
		String currentLooking = "";
		
		// read
	    try{
		    clear();
			while((line = br.readLine()) != null){ 
				
				boolean b = false;
				
				// prepare configs inside objects and objects
				if(line.endsWith("{")){
					String z = line.substring(0, line.length() - 1);
					while(z.endsWith(" "))
						z = z.substring(0, z.length() - 1);
					if(currentLooking.equals(""))
						currentLooking += z;
					else
						currentLooking += "." + z;
					b = true;
				}else if(line.startsWith("}")){
					if(!line.equals("")){
						String[] strs = currentLooking.split("\\.");
						currentLooking = currentLooking.substring(0, currentLooking.length() - strs[strs.length - 1].length());
						b = true;
					}
				}
				
				// differentiate name and config
				if(b == false){
					String[] strs = line.split(":");
					String name = strs[0];
					String value = "";
					int i=1;
					if(strs.length >= 2){
						while(i < strs.length){
							if(i > 1)
								value += ":" + strs[i];
							else
								value += strs[i];
							i++;
						}
					}
					
					// fix spaces
					while(value.startsWith("	") || value.startsWith(" "))
						value = value.substring(1, value.length());
					while(name.startsWith(" "))
						name = name.substring(1, name.length());
					
					// save
					if(currentLooking.equals(""))
						configs.put(name, value);
					else
						configs.put(currentLooking + "." + name, value);
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}	
	}
	
	public void clear(){
		configs = new MultiKeyMap<String, Object>();
	}
	
	public boolean exists(){
		return configFile.exists();
	}
	
	public boolean isEmpty(){
		return configFile.length() == 0;
	}
	
	public void setPath(String path){
		setPath(path, true);
	}
	
	public void setPath(String path, boolean createNewFile){
		configFile = new File(path);
		File dir = configFile.getParentFile();
		
		if(!dir.exists())
			dir.mkdirs();
		if(createNewFile){
			if(!configFile.exists()){
				try{
					configFile.createNewFile();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getPath(){
		return this.configFile.getPath();
	}
	
	public File getFile(){
		return this.configFile;
	}
}
