package com.hp.ngecc.hphw.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.util.TimeUtil;

public class FileHandler implements Handler {

	private String path="";
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public void handle(Command cmd) {
		
		File file = new File(path+File.separator+cmd.getType().toString()+"_"+UUID.randomUUID().toString()+".kpi");
		log.debug("cmd result:"+cmd.getExecResult());
		log.debug("File handler: write file in ["+file.getAbsolutePath()+"]");
		
		FileOutputStream fileOut = null;
		ObjectOutputStream objOut = null;
		
		try {
			fileOut = new FileOutputStream(file);
			objOut = new ObjectOutputStream(fileOut);
			
			objOut.writeObject(cmd);
			
			//IOUtils.write(cmd.getExecResult(), objOut);
			
			objOut.flush();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(objOut);
			IOUtils.closeQuietly(fileOut);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
