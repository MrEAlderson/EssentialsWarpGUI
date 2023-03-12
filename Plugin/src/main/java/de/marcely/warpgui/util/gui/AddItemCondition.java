package de.marcely.warpgui.util.gui;

import lombok.Getter;

/**
 * Optional parameter when adding items into a GUI to force them to be in a specific area
 */
public class AddItemCondition {
	
	public static final byte TYPE_WITHIN_X = 0;
	public static final byte TYPE_WITHIN_Y = 1;
	public static final byte TYPE_WITHIN = 2;
	
	@Getter private final byte type;
	@Getter private final int[] parameters;
	
	public AddItemCondition(byte type, int[] params){
		this.type = type;
		this.parameters = params;
	}
	
	public static AddItemCondition withinX(int xMin, int xMax){
		final int[] params = new int[4];
		
		params[0] = xMin;
		params[1] = xMax;
		params[2] = 0;
		params[3] = 5;
		
		return new AddItemCondition(TYPE_WITHIN_X, params);
	}
	
	public static AddItemCondition withinY(int yMin, int yMax){
		final int[] params = new int[4];
		
		params[0] = 0;
		params[1] = 8;
		params[2] = yMin;
		params[3] = yMax;
		
		return new AddItemCondition(TYPE_WITHIN_Y, params);
	}
	
	public static AddItemCondition within(int xMin, int xMax, int yMin, int yMax){
		final int[] params = new int[4];
		
		params[0] = xMin;
		params[1] = xMax;
		params[2] = yMin;
		params[3] = yMax;
		
		return new AddItemCondition(TYPE_WITHIN, params);
	}
}
