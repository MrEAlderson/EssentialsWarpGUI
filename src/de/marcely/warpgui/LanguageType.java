package de.marcely.warpgui;

public enum LanguageType {
	English,
	German;
	
	public static LanguageType getType(String str){
		if(str == null)
			return LanguageType.English;
		
		if(str.equalsIgnoreCase("english") ||
		   str.equalsIgnoreCase("englisch"))
			return LanguageType.English;
		else if(str.equalsIgnoreCase("german") ||
				str.equalsIgnoreCase("deutsch"))
			return LanguageType.German;
		
		return LanguageType.German;
	}
}
