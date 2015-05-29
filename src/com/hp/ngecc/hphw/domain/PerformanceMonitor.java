package com.hp.ngecc.hphw.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.hp.ngecc.hphw.util.TimeUtil;

public class PerformanceMonitor implements Serializable {

	private static final long serialVersionUID = 1L;

	private String collector = "ngecc";

	private String collectorHost;

	private String ciType1="";

	private String ciType2="";
	
	private String targetIP;
	
	private List<Instance> instances;

	private String ciName="";
	
	public String getCollector() {
		return collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
	}

	public String getCollectorHost() {
		return collectorHost;
	}

	public void setCollectorHost(String collectorHost) {
		this.collectorHost = collectorHost;
	}

 
	public String getCiType1() {
		return ciType1;
	}

	public void setCiType1(String ciType1) {
		this.ciType1 = ciType1;
	}

	public String getCiType2() {
		return ciType2;
	}

	public void setCiType2(String ciType2) {
		this.ciType2 = ciType2;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	public String getCiName() {
		return ciName;
	}

	public void setCiName(String ciName) {
		this.ciName = ciName;
	}

	
	public String getTargetIP() {
		return targetIP;
	}

	public void setTargetIP(String targetIP) {
		this.targetIP = targetIP;
	}

	public String toXML() {
		String reuslt = null;

		Document document = DocumentHelper.createDocument();

		Element root = document.addElement("performanceMonitors");

		root.addAttribute("collector", collector);

		root.addAttribute("collectorHost", "collectorHost");

		Element group3 = root.addElement("group");

		group3.addAttribute("desc", collector);
		group3.addAttribute("name", this.ciType2);

		Element group2 = group3.addElement("group");
		group2.addAttribute("desc", "");
		group2.addAttribute("name", this.ciType1);

		Element group1 = group2.addElement("group");
		group1.addAttribute("desc", "");
		group1.addAttribute("name", ciName);

		if (instances != null) {

			for (Instance i : instances) {

				Element monitor = group1.addElement("monitor");

				monitor.addAttribute("name", i.getName());
				monitor.addAttribute("quality", "0");
				monitor.addAttribute("sourceTemplateName", "");
				monitor.addAttribute("target", i.getTarget());
				monitor.addAttribute("targetIP", i.getTarget());
				monitor.addAttribute("time",
						Long.toString(TimeUtil.currentTimeMillis()));
				monitor.addAttribute("type", "");

				Map<String, String> kpis = i.getKpis();

				for (String key : kpis.keySet()) {

					Element element = monitor.addElement("counter");

					element.addAttribute("name", key);

					element.addAttribute("value", kpis.get(key));

					element.addAttribute("quality", "0");

				}

			}
		}

		ByteArrayOutputStream out = null;

		XMLWriter writer = null;

		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			out = new ByteArrayOutputStream();

			writer = new XMLWriter(out, format);

			writer.write(document);

			reuslt = out.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IOUtils.closeQuietly(out);

		}

		return reuslt;

	}

	public static void main(String[] args) {

		PerformanceMonitor p = new PerformanceMonitor();

		p.setCiName("BJYQ-ITMON-TEST01_10.240.19.55");

		p.setCiType2("OS");

		p.setCiType1("Linux");

		p.setCollectorHost("BJYQ-ITMON-INRGATHER05");

		Instance instance = new Instance();

		instance.setName("cpu");

		instance.setTarget("10.240.19.55");

		instance.setTime(TimeUtil.currentTimeMillis());

		Map<String, String> kpis = new HashMap<String, String>();

		kpis.put("counters in error", "李强");

		instance.setKpis(kpis);

		List<Instance> instances = new ArrayList<Instance>();

		instances.add(instance);

		p.setInstances(instances);

		System.out.println(p.toXML());
	}

	@Override
	public String toString() {
		return "PerformanceMonitor [collector=" + collector
				+ ", collectorHost=" + collectorHost + ", ciType1=" + ciType1
				+ ", ciType2=" + ciType2 + ", instances=" + instances
				+ ", ciName=" + ciName + "]";
	}

 
	
	
	
 
	
	
	

}


