package com.hp.ngecc.hphw.util;

import com.hp.ngecc.hphw.collect.impl.Collector;

public class ShutdownInterceptor extends Thread{

	private Collector app;
	
	
	public ShutdownInterceptor(Collector app) {
		this.app = app;
	}
	
	
	public void run() {
		this.app.shutDown();
	}
	
	 
}
