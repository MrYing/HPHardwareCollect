package com.hp.ngecc.hphw.collect.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.executor.Task;

/**
 * Enclosure刀箱采集线程.
 * @author hp-ngecc
 * @version 1.0
 */
public class EnclosureOACollector extends Collector{
 


	public EnclosureOACollector(String name) {
		super(name,DeviceType.ENCLOSURE_OA);
	}
	
	
	public void start()
	{
		
		
	}

	@Override
	protected List<Task> schedule() {
		List<Task> list = new ArrayList<Task>();
		
		HashMap<String,String> attr = new HashMap<String, String>();
		
		attr.put("host","192.168.153.26");
		attr.put("username","zhoubotao");
		attr.put("password","123456789");
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
