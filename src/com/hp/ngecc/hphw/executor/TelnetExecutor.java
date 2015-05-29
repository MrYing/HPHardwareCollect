package com.hp.ngecc.hphw.executor;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.telnet.TelnetClient;

import com.hp.ngecc.hphw.domain.Command;


public class TelnetExecutor extends Executor {  
	
	
	private Log log = LogFactory.getLog(this.getClass());
    private TelnetClient telnet = new TelnetClient();  
    private InputStream in;  
    private PrintStream out;  
  
    private boolean loginIncorrect=false;
    //--------------------------------------------
    private String ip;
    private int port;
    private String user;
    private String password;
    private String prompt="/>";  
    private String incorrectStr="Login incorrect";
    private Command cmd;
    
    
    
    public TelnetExecutor(String ip, String port, String user, String password,String prompt,String incorrectStr,Command cmd) {  
    	
    	super(cmd,ip);
    	this.ip = ip;
    	this.port = Integer.valueOf(port);
    	this.user = user;
    	this.password = password;
    	this.prompt = prompt==null?(user.equals("root") ? "#" : "$"):prompt;
        this.incorrectStr = incorrectStr;
    	this.cmd = cmd;
        
    }  
  
    public void login(String user, String password) {  
        readUntil("login:");  
    	write(user);  
        readUntil("Password:");
        write(password);  
        String info =  readUntil(prompt + "");  
        if(info.contains("faild"))
        {
        	loginIncorrect = true;
        	
        }  
    }  
  
    public String readUntil(String pattern)  {  
       /* try {  
            char lastChar = pattern.charAt(pattern.length() - 1);  
            StringBuffer sb = new StringBuffer();  
            char ch = (char) in.read();  
            while (true) {  
                sb.append(ch);  
                if (ch == '\n') {  
            
                	
                	if(incorrectStr!=null && sb.toString().contains(incorrectStr))
                	{
                		return "faild+["+sb.toString()+"]";
                	}
                	
                    if (sb.toString().endsWith(pattern)) { 
                    	System.out.println(sb.toString());
                        return sb.toString();
                    }
                    
                }  
                ch = (char) in.read();  
            }  
        } catch (Exception e) {  
        	log.error(e);
        }  */
    	
    	try {
			List<String> line = IOUtils.readLines(in);
			
			for(String l:line)
			{
				if(StringUtils.isNotBlank(l))
				{
					if(l.contains(pattern))
					{
						return l;
					}
				}
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
        return null;  
    }  
  
    public void write(String value) {  
    	System.out.println(value);
        try {  
            out.println(value);  
            out.flush();  
        } catch (Exception e) {  
        	log.error(e);
        }  
    }  
  
    private String execCommand(String command) {  
        try {
        	
            write(command);  
            return readUntil(prompt);  
        } catch (Exception e) {  
        	log.error(e);
        }  
        return null;  
    }  
  
    public void disconnect() {  
        try {  
        	out.flush();
            out.close();
            in.close();
        } catch (Exception e) {  
        	log.error(e);
        }  
    }  
  
    public static void main(String[] args) {  
    	System.setProperty("java.net.preferIPv4Stack", "true");
        try {  
            String ip = "10.240.53.5";  
            String port = "23";  
            String user = "admin";  
            String password = "admin";   
            //String cmd="";
            
            Command cmd = new Command();
            
            cmd.setCommand("help");
            
            TelnetExecutor telnet = new TelnetExecutor(ip, port, user, password,null,null,cmd);  
            //telnet.sendCommand("export LANG=en");  
            
           // String r3 = telnet.sendCommand(cmd);  
            
            //System.out.println(r3);  
            telnet.run();
            
           String r = cmd.getExecResult();
           System.out.println(r);
       /*    String s[] = r.split("\r\n");
           
         for(String str:s)
         {
        	   
        	   System.out.println(str);
         }  */ 
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

	@Override
	public Command run() {
		try {  
            telnet.connect(ip, port);  
          
            in = telnet.getInputStream();  
            
            out = new PrintStream(telnet.getOutputStream());  
            
            login(user, password); 
            
            if(loginIncorrect)
            {
            	  log.error("ip:["+ip+"] port:["+port+"] user:["+user+"]" +"login loginIncorrect");
            }else{
            	
            	String result = execCommand(cmd.getCommand());
            	
            	this.cmd.setExecResult(result);
            }
            
            
        } catch (Exception e) {  
        	log.error(e);
        } finally
        {
        	if(in!=null &&out!=null)
        	{
        		disconnect();  
        	}
        }
		
		return this.cmd;
	}  
    
    
    
    
    
    
} 


