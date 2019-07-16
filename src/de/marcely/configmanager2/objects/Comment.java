package de.marcely.configmanager2.objects;

public class Comment extends Config {

	public Comment(Tree parent, String value){
		super(null, parent, value);
	}
	
	@Override
	public byte getType(){
		return TYPE_COMMENT;
	}
}
