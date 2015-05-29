package com.hp.ngecc.hphw.executor;

import java.util.List;

public interface CommandExecutor {
	
	public boolean connect();  

	public List<String> executeCommand(final List<String> commandList);  

	public String executeCommand(final String command);  

	public boolean login(); 

	public boolean disconnect() ; 
}
