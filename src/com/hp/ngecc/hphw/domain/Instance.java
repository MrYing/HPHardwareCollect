package com.hp.ngecc.hphw.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class Instance implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name="";

	private String target;

	private long time;

	private String type;

	Map<String, String> kpis=Collections.EMPTY_MAP;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getKpis() {
		return kpis;
	}

	public void setKpis(Map<String, String> kpis) {
		this.kpis = kpis;
	}

	@Override
	public String toString() {
		return "Instance [name=" + name + ", target=" + target + ", time="
				+ time + ", type=" + type + ", kpis=" + kpis + "]";
	}

}
