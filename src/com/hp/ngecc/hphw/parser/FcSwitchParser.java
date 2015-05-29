package com.hp.ngecc.hphw.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.domain.Instance;


public class FcSwitchParser extends Parser {

	protected FcSwitchParser(Command cmd) {
		super(cmd);
	}

	@Override
	public String parse() {
		
		List<Instance> instances = new ArrayList<Instance>();
		
		String execResult = cmd.getCommand();
		
		
		if(cmd.getKpi().equals("02070006"))
		{
			
			switchLoginfo(instances, execResult);
		}
		
		else if(cmd.getKpi().equals("02070005"))
		{
			
			switchConfinfo(instances, execResult);
			
		}
		else{
			
			switchInfo(instances, execResult);
			
		}
		
		performanceMonitor.setInstances(instances);
		
		
		System.out.println(performanceMonitor.toXML());
		
		
		return performanceMonitor.toXML();
	}

	private void switchConfinfo(List<Instance> instances, String execResult) {
		
		Instance switchConfinfoInstance = new Instance();
		
		Map<String,String> switchConfinfoeKpis = new HashMap<String,String>();
		
		switchConfinfoeKpis.put("FCSwitch:ZoneConf",execResult);
		
		switchConfinfoInstance.setKpis(switchConfinfoeKpis);
		
		instances.add(switchConfinfoInstance);
		
		// zoneshow
		
	}

	private void switchLoginfo(List<Instance> instances, String execResult) {
		
		//fabriclog -s
		Instance switchLoginfoInstance = new Instance();
		
		Map<String,String> switchLoginfoKpis = new HashMap<String,String>();
		
		switchLoginfoKpis.put("FCSwitch:Log",execResult);
		
		switchLoginfoInstance.setKpis(switchLoginfoKpis);
		
		instances.add(switchLoginfoInstance);
		
	}

	private void switchInfo(List<Instance> instances, String execResult) {
		String []execResultLines = StringUtils.split(execResult, "\r\n");
		
		int index=Integer.MAX_VALUE;
		for(int i=0;i<execResultLines.length;i++)
		{
			if(i==0)
			{
				String switchName = StringUtils.remove(execResultLines[0],"switchName:").trim();
				
				Instance switchNameInstance = new Instance();
				
				Map<String,String> switchNameKpis = new HashMap<String,String>();
				
				switchNameKpis.put("FCSwitch:SwitchName", switchName);
				
				switchNameInstance.setKpis(switchNameKpis);
				
				instances.add(switchNameInstance);
				
			}
			
			if(execResultLines[i].contains("======"))
			{
				
				index=i;
			}
			
			if(i>index)
			{
				String s[] = StringUtils.split(execResultLines[i]," ");
				
				Instance portInstance = new Instance();
				
				portInstance.setName(s[1]);
				
				Map<String,String> portKpis = new HashMap<String, String>();
				
				portKpis.put("FCSwitch:Port", s[1]);
				portKpis.put("FCSwitch:Speed", s[4]);
				portKpis.put("FCSwitch:State", s[5]);
			 
 	  
				
			}
			
		}
		
	}

	public static void main(String[] args) throws Exception {
		
		Command cmd = new Command();
		
		String xml = IOUtils.toString(new FileInputStream("e://A_141.txt"));
		
		cmd.setCommand(xml);
		
		Parser p = new FcSwitchParser(cmd);
		
		p.parse();
		
		
		
		
	} 
	
	
	
	

}
