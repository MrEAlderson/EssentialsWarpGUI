package de.marcely.configmanager2;

public class IOResult {
	
	public static final int RESULT_SUCCESS = 0x0;
	
	public static final int RESULT_FAILED_UNKOWN = 0x1;
	
	public static final int RESULT_FAILED_LOAD_MISSINGFILE = 0x2;
	public static final int RESULT_FAILED_LOAD_NOPERMS = 0x3;
	public static final int RESULT_FAILED_LOAD_NOTVALID = 0x4;
	
	public static final int RESULT_FAILED_SAVE_NOPERMS = 0x2;
}
