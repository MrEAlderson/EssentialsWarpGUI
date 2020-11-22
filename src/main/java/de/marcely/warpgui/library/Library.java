package de.marcely.warpgui.library;

public interface Library {
	
	public LibraryType getType();
	
	public void load();
	
	public void unload();
}
