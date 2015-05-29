package com.hp.ngecc.hphw.domain;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.collect.ExecutorType;
import com.hp.ngecc.hphw.executor.Executor;

public class Command implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(Command.class);
	

	public final static Map<String,List<Command>> cmdMap =new HashMap<String, List<Command>>();
	
	static{
		Command.init();
	}
	
	private static String handler;
	
	
	public static Map<String,List<Command>> init()
	{
		
			InputStream in = Executor.class.getResourceAsStream("/command.xml");
		
			SAXReader reader = new SAXReader();
	        
			try {
				Document document = reader.read(in);
				
				Element rootElement = document.getRootElement();
				
				if(rootElement.attribute("handler")!=null)
				{
					handler = rootElement.attribute("handler").getText();
				}
				
				List<Element> commandElement = rootElement.elements("command");
				
				List<Command> cmds=null;
				
				for(Element cmd:commandElement)
				{
					Map<String,String> a = new HashMap<String, String>();
					
					String deviceType = cmd.attribute("deviceType").getText();
					
					List<Attribute> attrs = cmd.attributes();
					
					for(Attribute attr:attrs)
					{
						a.put(attr.getName(), attr.getText());
					}
					
					
					DeviceType type =DeviceType.valueOf(deviceType);
					
					if(cmdMap.containsKey(deviceType))
					{
					
						cmds = cmdMap.get(deviceType);
						
					}
					else{
						
						
						cmds = new ArrayList<Command>();
						
					}
					
					String excuteor = cmd.attribute("excuteor").getText();
					String parser = cmd.attribute("parser").getText();
					
					List<Element> kpis = cmd.elements("kpi");
					
					for(Element kpiElement:kpis)
					{
						Command c = new Command();
						c.setType(type);
						c.setExcuteor(excuteor);
						c.setParser(parser);
						c.setKpi(kpiElement.attribute("name").getText());
						c.setCommand(kpiElement.getText());
						c.setAttributes(a);
						cmds.add(c);
					} 
					
					cmdMap.put(deviceType, cmds);					
				}
				
			} catch (DocumentException e) {
				log.error(e);
			}
		
	        
	        return cmdMap;
	        
	}
	
	public static String getHandler()
	{
		return Command.handler;
		
	}
	
	public  static List<Command> file(String deviceType,Map<String,String> attr)
	{
		Pattern p = Pattern.compile("\\$\\{\\w+\\}");
		
		  List<Command> commands = cmdMap.get(deviceType);
		  
		  
		  for(Command cmd:commands)
		  {
			  
			  String c = cmd.getCommand();
			  
			  if(c.contains("$") && c.contains("{") &&c.contains("}"))
			  {
				  Matcher m = p.matcher(c);
				  
				  while(m.find())
				  {
						String key = m.group().substring(2,m.group().length()-1);
						
						String value  = attr.get(key);
						
						c = c.replace(m.group(), value);
				  }
			  }
			  
			  cmd.setCommand(c);
		  }
		  
		  return commands;
				
	}
	
	
	
	
	
	
	
	public ExecutorType getExecutorByDeviceType(DeviceType deviceType)
	{
		
		Command.init();
		
		List<Command> cmds = cmdMap.get(deviceType.toString());
		
		if(cmds!=null &&cmds.size()!=0)
		{
			
			  return ExecutorType.valueOf(cmds.get(0).getExcuteor());
		}
		
		return null;
	}
	
 
	private Map<String,String> attributes; 
	
	private DeviceType type;
	
	private String excuteor;
	
	private String parser;
	
	/**
	 * 一个command 有可能对应多个kpi
	 */
	private String kpi;
 
	private String command;

	private String execResult;
	
	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public String getExcuteor() {
		return excuteor;
	}

	public void setExcuteor(String excuteor) {
		this.excuteor = excuteor;
	}

	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getExecResult() {
		return execResult;
	}

	public void setExecResult(String execResult) {
		this.execResult = execResult;
	}

	
	
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

 

	@Override
	public String toString() {
		return "Command [attributes=" + attributes + ", type=" + type
				+ ", excuteor=" + excuteor + ", parser=" + parser + ", kpi="
				+ kpi + ", command=" + command + ", execResult=" + execResult
				+ "]";
	}

	public String getKpi() {
		return kpi;
	}

	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kpi == null) ? 0 : kpi.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if (kpi == null) {
			if (other.kpi != null)
				return false;
		} else if (!kpi.equals(other.kpi))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public static void main(String[] args) {
		
		
		
		
		
		
	}
	
	
	
	 
	
}

 

