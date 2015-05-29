package com.hp.ngecc.hphw.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.domain.Instance;

public class HeartSwitchParser extends Parser {

	protected HeartSwitchParser(Command cmd) {
		super(cmd);
	}

	@Override
	public String parse() {
		
		
		Instance heartswitchStateInstance = new Instance();
		
		List<Instance> instances = new ArrayList<Instance>();
		
		Map<String,String> heartswitchStateKpis = new HashMap<String,String>();
		
		String execResult = this.cmd.getExecResult();
		
		heartswitchStateKpis.put("HeartSwitch", execResult);
		
		String s[] = StringUtils.split(execResult,"\r\n");
		
		
		for(String str:s)
		{
			if(str.contains("State"))
			{
				heartswitchStateInstance.setName("");
				heartswitchStateKpis.put("HeartSwitch:FrontShow", str.split(":")[1]);
				heartswitchStateInstance.setKpis(heartswitchStateKpis);
				instances.add(heartswitchStateInstance);
				break;
			}
		
		}
		
		
		performanceMonitor.setInstances(instances);
		
		System.out.println(performanceMonitor.toXML());
		
		return performanceMonitor.toXML();
	}

	
	
	public static void main(String[] args) throws Exception {
		
		
		
		
		Command c = new Command();
		
		String r = IOUtils.toString(new FileInputStream("e://A_147.txt"));
		
		c.setExecResult(r);
		
		Parser p = new HeartSwitchParser(c);
		
		p.parse();
		
		
		
		
	}
	
	
	
	
	
	
	
	
}
