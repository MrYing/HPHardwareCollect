package com.hp.ngecc.hphw;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hp.ngecc.hphw.collect.IMainCollector;
import com.hp.ngecc.hphw.collect.impl.Collector;
import com.hp.ngecc.hphw.collect.impl.ESL712ECollector;
import com.hp.ngecc.hphw.collect.impl.EnclosureOACollector;
import com.hp.ngecc.hphw.collect.impl.EnclosureVCCollector;
import com.hp.ngecc.hphw.collect.impl.FCSwitchCollector;
import com.hp.ngecc.hphw.collect.impl.HeartSwitchCollector;
import com.hp.ngecc.hphw.collect.impl.ILO3Collector;
import com.hp.ngecc.hphw.collect.impl.ILO4Collector;
import com.hp.ngecc.hphw.collect.impl.VLS9200Collector;
import com.hp.ngecc.hphw.urmpmgmt.URMPManage;

public class CollectMain {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="mainCollector")
	private IMainCollector mainCollector;
	
	public static void main(String[] args) {
		
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		/*Collector c = new ESL712ECollector("ESL712E Collect Server");
		//默认两分钟 可以不设置 
		c.setInterval(10);
		
		c.run();*/ 
		
/*		Collector c = new EnclosureOACollector("Enclosure OA Collect Server");
		//默认两分钟 可以不设置 
		c.setInterval(10);
		
		c.run();*/
 
/*		Collector c = new EnclosureVCCollector("Enclosure VC Collect Server");
		//默认两分钟 可以不设置 
		c.setInterval(10);
		
		c.run();*/
	 
		/*Collector c = new VLS9200Collector("VLS9200 Collect Server");
		
		c.setInterval(10);
		
		c.run();*/
		
		 /*Collector c = new FCSwitchCollector("FCSwitch Collect Server");
		
		c.setInterval(10);
		
		c.run();*/
		 
		/*Collector c = new HeartSwitchCollector("HeartSwitch Collect Server");
		
		c.setInterval(10);
		
		c.run();*/
		
		/**/
		Collector c = new ILO3Collector("test Collect Server");
		
		c.setInterval(10);
		
		c.run();
		/*
		Collector c = new ILO4Collector("test Collect Server");
		
		c.setInterval(10);
		
		c.run();*/
		
		
	}
	
	public void init(){
		try {
			log.debug("start init..............");
			//从URMP加载CI数据
			URMPManage.loadUrmp();
			
			log.debug("end init..............");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void start() throws Exception {
		try {
			log.debug("start start..............");
			
			mainCollector.start();
			
			log.debug("end start..............");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}
}
