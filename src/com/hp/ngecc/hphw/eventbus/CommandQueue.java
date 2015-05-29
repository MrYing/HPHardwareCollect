package com.hp.ngecc.hphw.eventbus;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import org.apache.log4j.Logger;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.domain.Command;

public class CommandQueue {

	private static CommandQueue instance = new CommandQueue();
	
	private  static Logger log = Logger.getLogger(CommandQueue.class);
	
	private  BlockingQueue<Command> queue = new LinkedBlockingQueue<Command>();
	
	private CommandQueue() {}
	
	public static CommandQueue getInstance()
	{
		if(instance==null)
		{
			
			instance = new CommandQueue();
		}
		return instance;
	}
	
	
	public void add(Command cmd) throws InterruptedException
	{
		
		log.debug("add:" + cmd+"size:"+queue.size());
		queue.put(cmd);
		
	}
	
	
	public void remove() throws InterruptedException
	{
		log.debug("remove:" + queue.take()+"size:"+queue.size());
	}
	
	
	
	public void clear()
	{
		
		
		queue.clear();
		
		log.debug("clear all ....");
		
	}
	
	public int size()
	{
		return queue.size();
	}
	
	
	
public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				
				
				for(;;)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					new Thread(new Runnable(
							
							) {
						
						@Override
						public void run() {
							
							
							Command cmd = new Command();
							
							cmd.setType(DeviceType.ILO3);
							cmd.setKpi(UUID.randomUUID().toString());
							try {
								CommandQueue.getInstance().add(cmd);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
					
					
					
					
				}	
				
				
				
				
				
				
			}
		}).start();
		
		
new Thread(new Runnable() {
			
			@Override
			public void run() {

				
				while(true)
				{
					
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					new Thread(new Runnable(
							
							) {
						
						@Override
						public void run() {

							
							try {
								CommandQueue.getInstance().remove();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
					
					
				}
				
			}
			
		}).start();
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
}
