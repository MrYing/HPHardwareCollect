package com.hp.ngecc.hphw.collect.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.hp.ngecc.hphw.collect.IMainCollector;


/**
 * 采集主线程.
 * @author hp-ngecc
 * @version 1.0
 */
public class MainCollector implements IMainCollector{

	
	private int iloInterval;

	private int enclosureInterval;

	private int evaInterval;

	private int esl712Interval;

	private int vls9200Interval;

	private int fcInterval;

	private int heartInterval;
	
	public void start() {
		
		// 定时启动ILO采集
		
		
		// 定时启动Enclosure刀箱采集
		
		
		// 定时启动EVA存储采集
		
		
		// 定时启动ESL712磁带库采集
		
		
		// 定时启动VLS9200虚拟带库采集
		
		
		// 定时启动FCSwitch光纤交换机采集
		
		
		// 定时启动HeartSwitch心跳交换机采集
		
	}

	public  void setIloInterval(int iloInterval) {
		this.iloInterval=iloInterval;
	}


	public  void setEnclosureInterval(int enclosureInterval) {
		this.enclosureInterval=enclosureInterval;
	}

	public  void setEvaInterval(int evaInterval) {
		this.evaInterval=evaInterval;
	}

	public  void setEsl712Interval(int esl712Interval) {
		this.esl712Interval=esl712Interval;
	}

	public  void setVls9200Interval(int vls9200Interval) {
		this.vls9200Interval=vls9200Interval;
	}

	public  void setFcInterval(int fcInterval) {
		this.fcInterval=fcInterval;
	}

	public  void setHeartInterval(int heartInterval) {
		this.heartInterval=heartInterval;
	}
}
