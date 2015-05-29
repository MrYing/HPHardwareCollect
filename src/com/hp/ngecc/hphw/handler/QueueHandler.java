package com.hp.ngecc.hphw.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.eventbus.CommandQueue;

public class QueueHandler implements Handler {

	private Log log = LogFactory.getLog(QueueHandler.class);
	
	
	@Override
	public void handle(Command cmd) {
		
		try {
			CommandQueue.getInstance().add(cmd);
			
			log.debug("cmd["+cmd+"] already put in queue. queue size:"+CommandQueue.getInstance().size());
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
