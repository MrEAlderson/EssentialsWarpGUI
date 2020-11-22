package de.marcely.configmanager2.objects;

public class EmptyLine extends Config {

	public EmptyLine(Tree parent){
		super(null, parent, null);
	}
	
	@Override
	public byte getType(){
		return TYPE_EMPTYLINE;
	}
}
