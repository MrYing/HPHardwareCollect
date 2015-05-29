package com.hp.ngecc.hphw.executor;

import java.util.List;

import com.hp.ngecc.hphw.domain.Command;

public class LocalCommandExcutor  extends Executor implements CommandExecutor{

	public LocalCommandExcutor(Command cmd, String host) {
		super(cmd, host);
	}

	@Override
	public boolean connect() {
		return false;
	}

	@Override
	public List<String> executeCommand(List<String> commandList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean login() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Command run() {
		
		
		
		
		
		
		
		
		
		
		
		
		
		return null;
	}

}
