package com.hp.ngecc.hphw.collect.impl;

import java.util.List;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.executor.Task;

/**
 * EVA存储采集线程.
 * @author hp-ngecc
 * @version 1.0
 */
public class EVACollector extends Collector  {

	 

	protected EVACollector(String name, DeviceType deviceType) {
		super(name, deviceType);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List<Task> schedule() {
		// TODO Auto-generated method stub
		return null;
	}
 
 
 
}
