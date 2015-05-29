package com.hp.ngecc.hphw.collect.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.executor.Task;


/**
 * PC服务器和刀片服务器的ILO采集.
 * @author hp
 * @version 1.0ss
 * 变更内容：
 */
public class ILO4Collector extends Collector {

	public ILO4Collector(String name) {
		super(name,DeviceType.ILO4);
	}


	protected List<Task> schedule() {
		
		List<Task> list = new ArrayList<Task>();
		
		HashMap<String,String> attr = new HashMap<String, String>();
		
		
		attr.put("host","16.159.216.55");
		attr.put("username","root");
		attr.put("password","password");
		attr.put("prompt", "#");
		
		attr.put("iloFilePath", "Get_Embedded_Health.xml");
		
		attr.put("iloFilePath", "Get_Embedded_Health.xml");
		
		attr.put("iloVersion", "ilo4");
		
		//Map<String,List<Command>> cmdMap = Command.cmdMap;
		
		List<Command> cmds = Command.file(deviceType.toString(), attr);

		System.out.println(cmds);
		
		/*for(Command cmd:cmds)
		{
			Task t = new Task(cmd);
			
			t.setAttributes(attr);
			
			list.add(t);
		}*/
		
		
		
		return list;
	}

	 
 

}
