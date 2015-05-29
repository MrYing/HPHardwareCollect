package com.hp.ngecc.hphw.executor;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.collect.ExecutorType;
import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.handler.Handler;
import com.hp.ngecc.hphw.handler.QueueHandler;

public  class Task implements Runnable {

	//ciID_ci类型_ci名称     
	//12345678_server_host12345
	public String name;
	
	public Map<String,String> attributes;
	
	private  Command cmd;
	
	private DeviceType type;
	
	
	public  Task(Command cmd)
	{
		this.cmd = cmd;
	}

	@Override
	public void run() {

		if(cmd.getExcuteor()!=null)
		{
			
			ExecutorType type = ExecutorType.valueOf(cmd.getExcuteor());
			
			Executor exec = null;
			
			switch (type) 
			{
			
				case SSH:
				
				SSHExecutor ssh = new SSHExecutor(attributes.get("host"), attributes.get("username"), attributes.get("password"),cmd);
				
				exec = ssh;
				
				break;
			
			
				case SSH2:
					
					SSH2Executor deviceSSH = new SSH2Executor(attributes.get("host"), attributes.get("username"), attributes.get("password"),attributes.get("prompt"),cmd);
					
					exec = deviceSSH;
					
					break;
	
				case TELNET:
					
					TelnetExecutor telnet = new TelnetExecutor(attributes.get("host"), cmd.getAttributes().get("port"), attributes.get("username"), attributes.get("password"), attributes.get("prompt"), attributes.get("incorrectStr"), cmd);

					exec = telnet;
					
					break;
					
				default:
					
					break;
			}
			
			
			Command result = null;
			
			Handler hander = null;
			
			if(exec!=null)
			{
				
				result = exec.run();
				
				String handler =Command.getHandler() ;
					
					if(StringUtils.isNotBlank(handler))
					{
						
						try {
							hander = (Handler) Class.forName(handler).newInstance();
						} catch (Exception e) {
							hander = null;
						}  
						if(handler==null)
						{
							hander = new QueueHandler();
						}
					}
					hander.handle(result);
			}
		}
		
		
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}
	
	
	public static void main(String[] args) {
		
		
		
		
	}
	
	
	
	
	
	
}
