package com.hp.ngecc.hphw.collect;

/**
 * @author liqiang
 *
 */

public enum DeviceType {
	
	//Ilo3 1.82版本的采集,G7系列PC服务器和刀片
	 ILO3,
	//Ilo4 2.03版本的采集,G8系列PC服务器和刀片
	ILO4,
	//刀箱的采集
	ENCLOSURE_OA,
	
	ENCLOSURE_VC,
	
	//存储EVA8400采集
	EVA8400,
	//存储EVA6500/EVA6550采集
	EVA6500,
	//磁带库 ESL712E 采集
	ESL712E,
	//虚拟带库 VLS9200 采集
	VLS9200,
	//光纤交换机采集
	FCSWITCH,
	//心跳交换机采集
	HEARTSWITCH,
	//带内SiteScope采集
	SITESCOPE;
 
	
	 
	 
	 
	 
	 
	 
	
}
