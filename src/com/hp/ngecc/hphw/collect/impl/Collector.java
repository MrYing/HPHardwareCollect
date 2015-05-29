package com.hp.ngecc.hphw.collect.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.executor.Task;
import com.hp.ngecc.hphw.util.ShutdownInterceptor;

 
/**
 * 采集线程池 通用基类 
 * @author qli6
 *
 */
public abstract class Collector {

	
	private String name;
	
	private static Log log = LogFactory.getLog(Collector.class);

	protected  int corePoolSize = 5;
	protected  int maximumPoolSize = 40;
	protected  int keepAliveTime = 2;
	protected  int workQueue = 50;

    ThreadPoolExecutor executorThreadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,new ArrayBlockingQueue(workQueue),new ThreadPoolExecutor.AbortPolicy());

	private static long beginTime = 0L;

	static final int MAXTRYTIMES = 10;

	static final int MAXGRACEWAITINGTIME = 5;

	static final int EVERYTIMEDELAY = 200;

	protected int interval=2;

	private List<Task> tasks;
	
	protected DeviceType deviceType;

	protected Collector(String name,DeviceType deviceType) {
		ShutdownInterceptor shutdownInterceptor = new ShutdownInterceptor(this);
		Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
		beginTime = System.currentTimeMillis();
		// servicename = servicename;

		// port = port;
		this.name = name;
		
		this.deviceType = deviceType;
		
		this.tasks = schedule();
		
		
		if (!(log.isDebugEnabled()))
			return;
		log.debug("线程池维护线程的最少数量 corePoolSize :" + corePoolSize);
		log.debug("线程池维护线程的最大数量 maximumPoolSize :" + maximumPoolSize);
		log.debug("线程池维护线程所允许的空闲时间 keepAliveTime :" + keepAliveTime);
		log.debug("线程池所使用的缓冲队列 workQueue :" + workQueue);
	}

	
	protected abstract List<Task> schedule();
	
	
	public void run() {
		
		if(tasks==null || tasks.size()==0)
		{
			
			log.error("没有需要执行的采集作业,程序退出。");
			System.exit(0);
		}
		
		
		log.info("" + "开始启动服务，最大的工作线程是" + 300);


		while (!(this.executorThreadPool.isShutdown())) {
			
			try {
				log.debug(name+"is running.");
				
				execTask();
				
				log.debug(name+"is sleeping.");
				
				TimeUnit.MINUTES.sleep(interval);
				
			} catch (Exception e) {
				if (e instanceof IOException) {
					log.error(e.getMessage());
				} else if (e instanceof RejectedExecutionException) {
					log.error("系统在收到关闭服务的命令后仍然有请求过来");
				}
			}
			
		
		}
	}
	
	
	private void execTask() throws IOException, RejectedExecutionException, Exception {
 
		if(tasks!=null ||tasks.size()>0)
		{
			for(Task task:tasks)
			{
				this.executorThreadPool.execute(task);
			}
		}
		
		
		
		
		
		
		
		
	}
	
	
	
	
	public void shutDown() {
		
		this.executorThreadPool.shutdown();

		try {
			this.executorThreadPool.awaitTermination(5L, TimeUnit.SECONDS);
		} catch (InterruptedException localInterruptedException) {
			//忽略....
		}
		log.error("在规定的等待时间中主线程完全退出情况为" + this.executorThreadPool.isTerminated());

		beginTime = 0L;
		log.error("服务成功优雅退出");
	}


	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	
	
	

}
