package com.hp.ngecc.hphw.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ngecc.hphw.domain.Command;

public class SSH2Executor extends RemoteCommandExcutor {

	private ch.ethz.ssh2.Connection conn;
	private ch.ethz.ssh2.Session session;
	private OutputStream out;
	private ch.ethz.ssh2.StreamGobbler in;
	private ch.ethz.ssh2.StreamGobbler err;

	private static Log log = LogFactory.getLog(Executor.class);

	public SSH2Executor(Command cmd, String host) {
		// 用于SSH无密码登陆,暂时不实现 .
		super(cmd, host);
	}
	

	public SSH2Executor(String host, String username, String password,String prompt,Command cmd) {
		super(cmd, host);
		log.debug("ssh :" + host + " -------" + username);
		this.username = username;
		this.password = password;
		
		if(StringUtils.isNotBlank(prompt))
		{
			this.prompt = prompt;
		}
	}

	private String username;

	private String password;

	@Override
	public Command run() {

		
		 try {
			 connect();
			 
			 String result = executeCommand(this.cmd.getCommand());
			
			 cmd.setExecResult(result);
				 
		} finally{
			disconnect();
		}
		
		return cmd;
	}

	public static void main(String[] args) throws IOException {
		System.setProperty("java.net.preferIPv4Stack", "true");

		// String host="16.159.216.55"; //String host = "192.168.153.51"; String
		//String host = "10.240.53.5";
		//String host ="16.159.216.55";
		//刀箱 OA  >
		String host="16.159.216.55";
		
		Command cmd = new Command();

		cmd.setCommand("ps -ef ");

		SSH2Executor executor = new SSH2Executor(host, "root", "password","#",cmd);
		
		executor.run();
		
	}

	@Override
	public boolean connect() {
		try {
			
			conn = new ch.ethz.ssh2.Connection(this.host);
			conn.connect(null, 1000000, 1000000);
			if (!conn.authenticateWithPassword(this.username, password))
				throw new IOException("Authentication failed.");

			session = conn.openSession();
			session.requestPTY("dumb", Integer.MAX_VALUE, 0, 0, 0, null);
			session.startShell();
			in = new ch.ethz.ssh2.StreamGobbler(session.getStdout());
			err = new ch.ethz.ssh2.StreamGobbler(session.getStderr());
			out = session.getStdin();
			super.reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			super.writer = new PrintWriter(out);
			String r = waitFor(this.prompt.toCharArray());
			log.error("登陆结果：" + r);
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("SSH连接异常,可能是SSH用户无法登陆导致。错误信息：" + e.getMessage());
			return false;
		}
	}

	@Override
	public List<String> executeCommand(List<String> commandList) {

		List<String> resultList = new ArrayList<String>();
		if (null == commandList || commandList.size() == 0) {
			return resultList;
		}
		for (String command : commandList) {
			if (StringUtils.isEmpty(command)) {
				continue;
			}
			
			String r = executeCommand(command);
			resultList.add(r);
		}
		return resultList;
	}

	/**/@Override
	public String executeCommand(String command) {
		String result = "";

		try {
			result = execute(command);
			if (log.isDebugEnabled()) {
				log.debug("执行命令: [" + command + "]，目标服务器: [" + this.host + "]");
				log.debug("命令 [" + command + "] 结果: \n{" + result + "}");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// closeSession();
		//String[] split = StringUtils.split(result, getLineSeparator());

		//System.out.println(split.length);
		
		return result;
		
	}

	@Override
	public boolean login() {
		return connect();
	}

	@Override
	public boolean disconnect() {

		closeSession();

		if (null != conn) {
			conn.close();
		}
		return false;
	}

	private void closeSession() {
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (err != null) {
				err.close();
			}
			if (session != null) {
				session.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
