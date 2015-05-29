package com.hp.ngecc.hphw.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ngecc.hphw.domain.Command;

/**
 * @author liqiang
 *
 */
public abstract class Executor{
	
	private static Log log = LogFactory.getLog(Executor.class);
	
	/*protected static  Map<String,List<Command>> cmds;
	
	static{
		cmds = Command.init();
		
		log.info(cmds);
	}*/
	
	protected String host;
	
	protected Command cmd;
	
	public Executor(Command cmd,String host)
	{
		this.cmd = cmd;
		this.host = host;
	}
	
	public abstract Command run();

	public Command getCmd() {
		return cmd;
	}

	
	
	
}

 

