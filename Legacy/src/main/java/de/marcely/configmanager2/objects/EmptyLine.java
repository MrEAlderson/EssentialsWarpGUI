package de.marcely.configmanager2.objects;

@Deprecated
public class EmptyLine extends Config {

	public EmptyLine(Tree parent){
		super(null, parent, (String) null);
	}
	
	@Override
	public byte getType(){
		return TYPE_EMPTYLINE;
	}
}
