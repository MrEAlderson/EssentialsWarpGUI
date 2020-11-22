package de.marcely.warpgui.version;

import lombok.Getter;

public class VersionInstance {
	
	@Getter
	private final Version version;
	@Getter
	private final String raw;
	@Getter
	private final int subVersionNumber;
	
	public VersionInstance(Version ver, String raw, int subVersionNumber){
		this.version = ver;
		this.raw = raw;
		this.subVersionNumber = subVersionNumber;
	}
	
	public int getMinor(){
		return this.version.getMinor();
	}
	
	public int getRevision(){
		return this.version.getRevision();
	}
}
