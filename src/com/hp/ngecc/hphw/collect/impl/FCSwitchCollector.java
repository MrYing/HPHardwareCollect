package com.hp.ngecc.hphw.collect.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.executor.Task;


/**
 * FCSwitch光纤交换机采集线程.
 * @author hp-ngecc
 * @version 1.0
 */
public class FCSwitchCollector extends Collector {

	
	public FCSwitchCollector(String name) {
		
		super(name,DeviceType.FCSWITCH);
		
	}
	
	
	public void start()
	{
		
		
	}

	@Override
	protected List<Task> schedule() {
		
		List<Task> list = new ArrayList<Task>();
		
		HashMap<String,String> attr = new HashMap<String, String>();
		
		
		attr.put("host","192.168.151.170");
		attr.put("username","admin");
		attr.put("password","password");
		attr.put("prompt", ">");
		
		Map<String,List<Command>> cmdMap = Command.cmdMap;
		
		List<Command> cmds = cmdMap.get(deviceType.toString());
		
		for(Command cmd:cmds)
		{
			Task t = new Task(cmd);
			
			t.setAttributes(attr);
			
			list.add(t);
		}
		return list;
	}
	

 

 
	 

}
