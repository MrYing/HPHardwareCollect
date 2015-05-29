package com.hp.ngecc.hphw.executor;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ngecc.hphw.domain.Command;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


@Deprecated
public class SSHExecutor extends Executor {

	private static Log log = LogFactory.getLog(Executor.class);
	
	public SSHExecutor(Command cmd,String host) {
		//用于SSH无密码登陆,暂时不实现 . 
		super(cmd,host);
	}

	
	public SSHExecutor(String host,String username,String password,Command cmd) {
		super(cmd,host);
		log.debug("ssh :"+host+" -------"+username);
		this.username = username;
		this.password = password;
	}

	private String username;
	
	private String password;
	
	

	@Override
	public Command run() {
		Channel channel = null;
		
		Session session = null;
		
		String result = null;
		
	    try{
		      JSch jsch=new JSch();  

		       session=jsch.getSession(this.username, host, 22);
		      
		      SSHUserInfo ui = new SSHUserInfo();
		      
		      ui.setPasswd(this.password);
		      
		      session.setUserInfo(ui);
		      
		      session.connect(30000000);
		      
		      String command=this.cmd.getCommand();

		       channel=session.openChannel("exec");
		      
		      ((ChannelExec)channel).setCommand(command);
		      
		      channel.setInputStream(null);
		      
		      channel.setOutputStream(System.out);

		      ((ChannelExec)channel).setErrStream(System.err);

		      InputStream in=channel.getInputStream();
		      
		      channel.connect(1000*60*10);
		      
		      result = IOUtils.toString(in);
		      
		    }
		    catch(Exception e){
		    	System.out.println(e);
		    	log.error(e);
		    }finally{
		    	
		    	if(channel!=null)
		    	{
		    		channel.disconnect();
		    	}
		    	if(session!=null)
		    	{
		    		session.disconnect();
		    	}/**/
		    }
		
	    
	    cmd.setExecResult(result);
	    
		return cmd;
	}

	
	public static void main(String[] args) {
		
		System.setProperty("java.net.preferIPv4Stack", "true");
		
	     String host="10.240.53.3";
	     //String host = "192.168.153.51";
	     //String host="10.240.53.5";
	     
	     Command cmd = new Command();
	     
	     cmd.setCommand("ssss");
	     
	     
	     Executor executor = new SSHExecutor(host,"administrator","admin",cmd);
	     
	     
	    // System.out.println(Command.init().size());
	     
	     
	     
	     Command result = executor.run();
	     
	     
	     System.out.println(result.getExecResult());
	     
	     
	}


	
	
}




class SSHUserInfo implements UserInfo {

	private String passwd;
	
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	@Override
	public String getPassphrase(){ 
		return ">"; 
	}
	@Override
	 public String getPassword(){ 
		return this.passwd; 
	}
	
	@Override
	public boolean promptPassphrase(String message) {
		System.out.println("promptPassphrase-------------"+message);
		return true;
	}
	@Override
	public boolean promptPassword(String str) {
		System.out.println("promptPassword-------------"+str);
		return true;
	}
	@Override
	public boolean promptYesNo(String str) {
		System.out.println("promptYesNo-------------"+str);
		return true;
	}
	@Override
	public void showMessage(String message) {
		System.out.println("promptYesNo-------------"+message);
	}

}
 

