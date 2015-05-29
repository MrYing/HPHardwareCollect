package com.hp.ngecc.hphw.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.parser.ILOParser;
import com.hp.ngecc.hphw.parser.Parser;

public class FileProcessor extends Processor {

	private String path = "E:\\devenv\\workspace\\ngecc\\HPHardwareCollect\\Target\\kpis\\";

	@Override
	public void process() {

		File f = new File(path);

		while (true) {

			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Collection<File> list = FileUtils.listFiles(f,new String[] { "kpi" }, false);

			if (list.size() > 0) {

				final File file = list.iterator().next();

				new Thread(new Runnable() {
					@Override
					public void run() {
						Parser parser = null;
						FileInputStream in = null;
						ObjectInputStream obj = null;
						try {

							in = new FileInputStream(file);

							obj = new ObjectInputStream(in);

							Command cmd = (Command) obj.readObject();

							DeviceType type =  cmd.getType();
							
							switch (type) 
							{
								case ILO3:
									
									parser = new ILOParser(cmd); 
									break;
									
								case ILO4:
									parser = new ILOParser(cmd); 
									break;	
								case FCSWITCH:
									//parser = new ILOParser(cmd); 
									break;	
								default:
									
									break;
							}
							
							String xml = parser.parse();
							
							send(xml);
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {

							IOUtils.closeQuietly(obj);
							IOUtils.closeQuietly(in);
						}

						file.delete();

					}

				}).start();
				;

			}

			System.out.println(list.size());
		}

	}

	void sendToCollector()
	{
		
		
		
		
		
	}
	
	
	
	
	public static void main(String[] args) {

		FileProcessor f = new FileProcessor();

		f.process();

	}

}
