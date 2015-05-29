package com.hp.ngecc.hphw.executor;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ngecc.hphw.domain.Command;


public abstract class RemoteCommandExcutor extends Executor implements CommandExecutor
{
	
	public RemoteCommandExcutor(Command cmd, String host) {
		
		super(cmd, host);
	}

	private  static Log	 log = LogFactory.getLog(RemoteCommandExcutor.class);
	
	private static final String MORE_REGEX = "[\n\r]*.*-{2,5}\\s*[Mm]ore\\s*-{2,5}\\s*$";

	private static final String PROMPT_CHAR = ">";
	
	private Pattern morePattern = Pattern.compile(MORE_REGEX);
	
	protected BufferedReader reader;

	protected PrintWriter writer;

	protected String prompt = ">#$:]";
	
	protected boolean onlyResult = true;
	
	private String lineSeparator;
	
	public String execute(String command) throws IOException
	{
		writer.print(command + "\n");
		writer.flush();

		String result = waitFor(this.prompt.toCharArray());

		//IOUtils.write(result, new FileOutputStream("c://test.txt"));
		
	 	if (onlyResult)
		{
	 		String pattern = "^" + command+ "[\\r\\n]*(.+)[\\r\\n].*["+prompt+"]\\s*$";
			Pattern p = Pattern.compile(pattern,Pattern.DOTALL);
			Matcher m = p.matcher(result);
			if (m.find())
			{
				return m.group(1);
			}
		}  
		
		
		return result;
	}
	
	
	
	public String batchExcute(List<String> commands) throws IOException {
		String result = "";
		for (int i = 0; i < commands.size(); i++) {
			if(result.length()>0)
			{
				result+="\n";
			}
			result += execute(commands.get(i));
		}
		return result;
	}
	
	protected String waitFor(String sign) throws IOException
	{
		return waitFor(new String[] { sign });
	}
	
	protected String waitFor(String[] signs) throws IOException
	{
		List<String> list = new ArrayList<String>();
		for (String sign : signs)
		{
			list.add(sign);
		}
		return waitFor(list);
	}
	
	protected String waitFor(char[] signs) throws IOException
	{
		List<String> list = new ArrayList<String>();
		for (char sign : signs)
		{
			list.add("" + sign);
		}
		return waitFor(list);
	}
	
	protected String waitFor(Collection<String> signs) throws IOException
	{
		
		char[] buf = new char[1024];
		StringBuffer sb = new StringBuffer();
		String result = null;
		outter: while (true)
		{
			int read = reader.read(buf);
			if (read == -1)
			{
				throw new IOException();
			}
			sb.append(buf, 0, read);   /**/
			
			result=sb.toString().replaceAll("[\\x08]+", "");
			result=result.replaceAll("[\\x1B][\\x5B][4][2][D]\\s*[\\x1B][\\x5B][4][2][D]\\s*[^a-zA-Z_0-9]", "");
			sb=new StringBuffer(result);
			result=sb.toString();
			
			String moreFlag=getMoreFlagString(result);
			if (moreFlag!=null)
			{
				writer.print(" ");
				writer.flush();
				sb.delete(sb.length() - moreFlag.length(), sb.length());
				sb.append("\n");
			}else
			{
				for (String sign : signs)
				{
					if (result.replaceFirst("\\s+$", "").endsWith(sign))
					{
						break outter;
					}
				}
			}
			
			
		}
		
		result = sb.toString();
		return result;
	}
	
	public String getMoreFlagString(String result)
	{
		 Matcher m= morePattern.matcher(result); 
         while (m.find()) {
        	 return result.substring(m.start(),m.end());
        }
        return null;
	}
	
	protected String getLineSeparator() {
		if (StringUtils.isEmpty(lineSeparator)) {
			lineSeparator = System.getProperty("line.separator", "\n");
		}
		return lineSeparator;
	}
	
	
	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}
	
	
}
