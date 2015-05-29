package com.hp.ngecc.hphw.collect.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.executor.Task;

/**
 * HeartSwitch心跳交换机线程.
 * @author hp-ngecc
 * @version 1.0
 */
public class HeartSwitchCollector extends Collector{


public HeartSwitchCollector(String name) {
		
		super(name,DeviceType.HEARTSWITCH);
	}
	
	
	public void start()
	{
		
		
	}

	@Override
	protected List<Task> schedule() {
		
		List<Task> list = new ArrayList<Task>();
		
		HashMap<String,String> attr = new HashMap<String, String>();
		
		
		attr.put("host","192.168.152.7");
		attr.put("username","admin");
		attr.put("password","123456");
		attr.put("prompt", "#");
		
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
