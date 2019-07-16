package de.marcely.warpgui.command;

import lombok.Getter;

public class SubCommand {
	
	@Getter private final String name;
	@Getter private final String parameter;
	@Getter private final SubCommandExecutor executor;
	
	SubCommand(String name, String parameter, SubCommandExecutor executor){
		this.name = name;
		this.parameter = parameter;
		this.executor = executor;
	}
}