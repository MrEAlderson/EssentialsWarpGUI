package de.marcely.configmanager2.objects;

import lombok.Getter;

public class Description extends Config {
	
	@Getter private final boolean base;
	
	public Description(Tree rootTree, String name, String value){
		this(rootTree, name, value, false);
	}
	
	public Description(Tree rootTree, String name, String value, boolean base){
		super(name, rootTree, value);
		
		this.base = base;
	}
	
	@Override
	public byte getType(){
		return TYPE_DESCRIPTION;
	}
}