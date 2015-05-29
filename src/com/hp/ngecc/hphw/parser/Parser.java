package com.hp.ngecc.hphw.parser;

import java.util.Map;

import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.domain.PerformanceMonitor;



public abstract class Parser {

	
	protected Command cmd;
	
	protected PerformanceMonitor performanceMonitor;
	

	protected Parser(Command cmd)
	{
		this.cmd = cmd;
		
		performanceMonitor = new PerformanceMonitor();
		
		Map<String,String> attr =  cmd.getAttributes();
		
		if(attr!=null)
		
		{
			
			performanceMonitor.setCiName(attr.get("ciName")==null?"":attr.get("ciName"));
			
			performanceMonitor.setCollectorHost(attr.get("collectorHost"));
			
			performanceMonitor.setTargetIP("");
			
			performanceMonitor.setCiType1(attr.get("ciType1")==null?"":attr.get("ciType1"));
			
			performanceMonitor.setCiType2(attr.get("ciType2")==null?"":attr.get("ciType2"));
			
			
		}	

		
	}
	
	public abstract String parse();
	
	
}
