package de.marcely.configmanager2.objects;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

public class Config {
	
	public static final byte TYPE_TREE = 0x0;
	public static final byte TYPE_CONFIG = 0x1;
	public static final byte TYPE_COMMENT = 0x2;
	public static final byte TYPE_EMPTYLINE = 0x3;
	public static final byte TYPE_DESCRIPTION = 0x4;
	public static final byte TYPE_LISTITEM = 0x5;
	
	@Getter private final String name;
	@Getter private final Tree parent;
	
	@Getter @Setter String value;
	
	public Config(String name, Tree parent){
		this(name, parent, null);
	}
	
	public Config(String name, Tree parent, String value){
		this.name = name;
		this.parent = parent;
		this.value = value;
	}
	
	public byte getType(){
		return TYPE_CONFIG;
	}
	
	public String getAbsolutePath(){
		return parent != null ? parent.getAbsolutePath() + (!parent.getAbsolutePath().isEmpty() ? "." : "") + name : "";
	}
	
	public @Nullable Boolean getValueAsBoolean(){
		return null;
	}
	
	// Util
	private static @Nullable Boolean getBoolean(String str){
		return Boolean.valueOf(str);
	}
	
	public static List<String> valuesToString(List<Config> configs){
		final List<String> list = new ArrayList<String>();
		
		for(Config c:configs)
			list.add(c.value);
		
		return list;
	}
}