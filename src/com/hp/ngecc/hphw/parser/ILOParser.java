package com.hp.ngecc.hphw.parser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.spi.XmlReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hp.ngecc.hphw.collect.DeviceType;
import com.hp.ngecc.hphw.domain.Command;
import com.hp.ngecc.hphw.domain.Instance;
import com.hp.ngecc.hphw.domain.PerformanceMonitor;

public class ILOParser extends Parser {

	int iloVersion = 3;

	public ILOParser(Command cmd) {
		super(cmd);
		iloVersion = cmd.getType().toString().equals("ILO3") ? 3 : 4;
	}

	@Override
	public String parse() {

		String xml = cmd.getExecResult();

		xml = format(xml);

		Document document = buildDocument(xml);

		List<Instance> instances = new ArrayList<Instance>();

		power(document, instances);

		cpu(document, instances);

		memory(document, instances);

		fan(document, instances);

		nic(document, instances);
		
		drive(document, instances);

		mainboard(document, instances);
		
		performanceMonitor.setInstances(instances);

		return performanceMonitor.toXML();
	}

	private void mainboard(Document document, List<Instance> instances) {

		
		 Element mainboardElement = (Element) document.selectSingleNode("//TEMP[LOCATION[@VALUE='System']][2]");
		
		 Instance mainboardInstance = new Instance();
		
		 Map<String, String> mainboardKpis = new HashMap<String, String>();
		
		 mainboardKpis.put("MAINBOARD:TEMP", mainboardElement.element("CURRENTREADING").attributeValue("VALUE"));
		 mainboardKpis.put("MAINBOARD:STATUS", mainboardElement.element("STATUS").attributeValue("VALUE"));
		 
		 
		 mainboardInstance.setKpis(mainboardKpis);
		 
		 instances.add(mainboardInstance);
		 
		 
		
	}

	private void drive(Document document, List<Instance> instances) {

		switch (iloVersion) {
		case 3:
			List<Element> logicalDriveElements = document.selectNodes("//BACKPLANE/DRIVE_BAY");
			
			List<Element> temp =  document.selectNodes("//TEMPERATURE/TEMP[LOCATION[@VALUE='System'] and LABEL[@VALUE='HDD'] ]");

			if(temp.size()==0)
			{
				temp =  document.selectNodes("//TEMPERATURE/TEMP[LOCATION[@VALUE='Storage']]");
				
				if(temp.size()==0)
				{
					 temp =  document.selectNodes("//TEMPERATURE/TEMP[LOCATION[@VALUE='System']]");
				}
			}
			
			for(Element logicalDriveElement:logicalDriveElements)
			{
				
				//System.out.println("DRIVE_BAY_"+logicalDriveElement.attributeValue("VALUE"));
				 Element logicalDriveStatusElements =  (Element) logicalDriveElement.selectSingleNode("following::STATUS");

				 String status = logicalDriveStatusElements.attributeValue("VALUE");
				
				 if(status.equalsIgnoreCase("ok"))
				 {
					 
					 
					 Map<String, String> deiverKpis = new HashMap<String, String>();
					 
					 Instance driveInstance = new Instance();
					 
					 driveInstance.setName("DRIVE_BAY_"+logicalDriveElement.attributeValue("VALUE"));
					 
					 deiverKpis.put("DRIVE:TEMP", temp.get(0).element("CURRENTREADING").attributeValue("VALUE"));
					 deiverKpis.put("DRIVE:STATUS", "Ok");
					 
					 driveInstance.setKpis(deiverKpis);
					 
					 instances.add(driveInstance);
				 }
				
			}
			break;
		case 4:
			
			List<Element> temphdd =  document.selectNodes("//TEMPERATURE/TEMP[LOCATION[@VALUE='System'] and LABEL[@VALUE='12-HD Max'] ]");
			
			List<Element> physicalDriveElements = document.selectNodes("//PHYSICAL_DRIVE");
			
			for(Element physicalDriveElement:physicalDriveElements)
			{
				 Map<String, String> physicalDriveKpis = new HashMap<String, String>();
				 
				 Instance physicalDriveInstance = new Instance();
				 
				 physicalDriveInstance.setName(physicalDriveElement.element("LABEL").attributeValue("VALUE"));

				 //physicalDriveKpis.put("DRIVE:TEMP", temp.get(0).element("CURRENTREADING").attributeValue("VALUE"));
				 physicalDriveKpis.put("DRIVE:STATUS", physicalDriveElement.element("STATUS").attributeValue("VALUE"));
				 
				 if(temphdd.size()>0)
				 {
					 physicalDriveKpis.put("DRIVE:TEMP", temphdd.get(0).element("CURRENTREADING").attributeValue("VALUE"));
				 }
				 
				 physicalDriveInstance.setKpis(physicalDriveKpis);
				 
				 instances.add(physicalDriveInstance);
				 
				 
				 
				 
			}/**/
			
			
			
			
			
			
			break;
 
		}
		
		
		
		
	}

	private void nic(Document document, List<Instance> instances) {
		Element  nicInfoElement = null;
		switch (iloVersion) {
		case 3:
			   nicInfoElement = (Element) document.selectSingleNode("//NIC_INFOMATION");
			break;
		case 4:
			 nicInfoElement = (Element) document.selectSingleNode("//NIC_INFORMATION");
			break;
		}
		
		 List<Element> nicElements = nicInfoElement.elements();
		 
		 for(Element nic:nicElements)
		 {
			 
			 Map<String, String> nicKpis = new HashMap<String, String>();
			 
			 nicKpis.put("NIC:TYPE", nic.getName());
			 nicKpis.put("MAC_ADDRESS",nic.element("MAC_ADDRESS").attributeValue("VALUE"));
			 
			 Instance nicInstance = new Instance();
			 
			 nicInstance.setName(nic.element("NETWORK_PORT").attributeValue("VALUE"));
			 nicInstance.setKpis(nicKpis);
			 
			 instances.add(nicInstance);
			 
		 }
		
		 
		 
		 
		 
		
		
		
	}

	/*
	 * <FAN> <ZONE VALUE = "System"/> <LABEL VALUE = "Fan 1"/> <STATUS VALUE =
	 * "OK"/> <SPEED VALUE = "13" UNIT="Percentage"/> </FAN>
	 */
	private void fan(Document document, List<Instance> instances) {

		List<Element> fanElements = document.selectNodes("//FANS/FAN");

		for (Element fan : fanElements) {

			Map<String, String> fanKpis = new HashMap<String, String>();

			Instance fanInstance = new Instance();

			fanInstance.setName(fan.element("LABEL").attributeValue("VALUE"));

			fanKpis.put("FAN:LABEL",
					fan.element("LABEL").attributeValue("VALUE"));
			fanKpis.put("FAN:SPEED",
					fan.element("SPEED").attributeValue("VALUE"));
			fanKpis.put("FAN:ZONE", fan.element("ZONE").attributeValue("VALUE"));
			fanKpis.put("FAN:STATUS",
					fan.element("STATUS").attributeValue("VALUE"));
			fanInstance.setKpis(fanKpis);
			instances.add(fanInstance);

		}

	}

	private void memory(Document document, List<Instance> instances) {

		switch (iloVersion) {
		
		case 3:
			
			//List<Element> memoryElements = document.selectNodes("//MEMORY_COMPONENTS/MEMORY_COMPONENT[MEMORY_SIZE[@VALUE!='Not Installed']]");
			List<Element> memoryElements = document.selectNodes("//MEMORY_COMPONENTS/MEMORY_COMPONENT");
			
			for(Element memoryElement:memoryElements)
			{
				
				Map<String, String> memoryKpis = new HashMap<String, String>();
				Instance memoryInstance = new Instance();
				memoryInstance.setName(memoryElement.element("MEMORY_LOCATION").attributeValue("VALUE"));
				memoryKpis.put("MEMORY:MEMORY_SIZE", memoryElement.element("MEMORY_SIZE").attributeValue("VALUE"));
				memoryKpis.put("MEMORY:MEMORY_STATUS",memoryElement.element("MEMORY_SIZE").attributeValue("VALUE").equals("Not Installed")?"Not Installed":"ok");
				memoryKpis.put("MEMORY:MEMORY_SPEED",memoryElement.element("MEMORY_SPEED").attributeValue("VALUE"));
				memoryInstance.setKpis(memoryKpis);
				
				instances.add(memoryInstance);
			}
			
			Instance memoryCount = new Instance();
			Map<String, String> memoryCountKpis = new HashMap<String, String>();
			memoryCountKpis.put("MEMORY:MEMORY_COUNT", Integer.toString(document.selectNodes("//MEMORY_COMPONENTS/MEMORY_COMPONENT[MEMORY_SIZE[@VALUE!='Not Installed']]").size()));
			memoryCount.setKpis(memoryCountKpis);
			instances.add(memoryCount);
			
			break;
			
		case 4:
			
			 Element memoryDetailsElements = (Element) document.selectSingleNode("//MEMORY_DETAILS");
			
			 List<Element> memoryCpuElements = memoryDetailsElements.elements();
			 
			 int memCount = 0;
			 
			for(Element memoryElement:memoryCpuElements)
			{
				String cpuName =  memoryElement.getName();
				
				Map<String, String> memKpis = new HashMap<String, String>();
				Instance memoInstance = new Instance();
				memoInstance.setName(cpuName+"_"+memoryElement.element("SOCKET").attributeValue("VALUE"));
				memKpis.put("MEMORY:MEMORY_LOCATION", memoInstance.getName());
				memKpis.put("MEMORY:MEMORY_SIZE", memoryElement.element("SIZE").attributeValue("VALUE"));
				memKpis.put("MEMORY:MEMORY_SPEED", memoryElement.element("FREQUENCY").attributeValue("VALUE"));
				String status = memoryElement.element("STATUS").attributeValue("VALUE");
				memKpis.put("MEMORY:MEMORY_STATUS", status);
				if(status.equals("Good, In Use"))
				{
					
					memCount++;
					
				}
				memoInstance.setKpis(memKpis);
				instances.add(memoInstance);
				
				
				
				
			}
			
			Map<String, String> memCountInstanceKpis = new HashMap<String, String>();
			Instance memCountInstance = new Instance();
			memCountInstanceKpis.put("MEMORY:MEMORY_COUNT", Integer.toString(memCount));
			memCountInstance.setKpis(memCountInstanceKpis);
			memCountInstance.setName("");
			instances.add(memCountInstance);
			
			
			
			
			
			break;
			
		default:
			break;
		}

	}

	private List<String> cpuTemp(Document document) {

		List<String> templist = new ArrayList<String>();

		List<Element> cpuTempElements = document.selectNodes("//TEMPERATURE/TEMP[LOCATION[@VALUE='CPU']]");

		for (Element cpuTemp : cpuTempElements) {
			templist.add(cpuTemp.element("CURRENTREADING").attribute("VALUE")
					.getText());
		}

		return templist;
	}

	private void cpu(Document document, List<Instance> instances) {

		List<String> cpuTemp = cpuTemp(document);

		List<Element> cpuElements = document
				.selectNodes("//PROCESSORS/PROCESSOR");

		int index = 0;
		for (Element cpu : cpuElements) {
			Map<String, String> cpuKpis = new HashMap<String, String>();

			Instance cpuInstance = new Instance();

			cpuInstance.setName(cpu.element("LABEL").attributeValue("VALUE"));

			cpuKpis.put("PROCESSOR:LABEL",
					cpu.element("LABEL").attributeValue("VALUE"));
			cpuKpis.put("PROCESSOR:SPEED",
					cpu.element("SPEED").attributeValue("VALUE"));
			cpuKpis.put("PROCESSOR:EXECUTION_TECHNOLOGY",
					cpu.element("EXECUTION_TECHNOLOGY").attributeValue("VALUE"));
			cpuKpis.put("PROCESSOR:MEMORY_TECHNOLOGY",
					cpu.element("MEMORY_TECHNOLOGY").attributeValue("VALUE"));
			cpuKpis.put("PROCESSOR:MEMORY_TECHNOLOGY",
					cpu.element("MEMORY_TECHNOLOGY").attributeValue("VALUE"));
			cpuKpis.put("PROCESSOR:INTERNAL_L1_CACHE",
					cpu.element("INTERNAL_L1_CACHE").attributeValue("VALUE"));
			cpuKpis.put("PROCESSOR:INTERNAL_L2_CACHE",
					cpu.element("INTERNAL_L2_CACHE").attributeValue("VALUE"));
			cpuKpis.put("PROCESSOR:INTERNAL_L3_CACHE",
					cpu.element("INTERNAL_L3_CACHE").attributeValue("VALUE"));
			cpuKpis.put("PROCESSOR:TEMP", cpuTemp.get(index));
			// TODO ilo3没有状态 cpu温度 无法直接获取
			// cpuKpis.put("PROCESSOR:STATUS",
			// cpu.element("STATUS").attributeValue("VALUE")); TODO
			cpuInstance.setKpis(cpuKpis);
			instances.add(cpuInstance);
			index++;
		}

	}

	private List<Instance> power(Document document, List<Instance> instances) {

		// 电源固件版本
		Instance powerManagementControllerFirmwareVersion = new Instance();

		Node powerManagementControllerFirmwareVersionElement = document
				.selectSingleNode("//POWER_MANAGEMENT_CONTROLLER_FIRMWARE_VERSION");

		String value = powerManagementControllerFirmwareVersionElement
				.valueOf("@VALUE");

		Map<String, String> powerManagementControllerFirmwareVersionKpis = new HashMap<String, String>();

		powerManagementControllerFirmwareVersionKpis.put(
				"POWER_SUPPLIES:POWER_MANAGEMENT_CONTROLLER_FIRMWARE_VERSION",
				value);

		powerManagementControllerFirmwareVersion
				.setKpis(powerManagementControllerFirmwareVersionKpis);

		instances.add(powerManagementControllerFirmwareVersion);

		List<Element> powerNameElements = document.selectNodes("//SUPPLY");

		for (Element e : powerNameElements) {
			// 电源名称
			Instance powerName = new Instance();
			Map<String, String> powerNameKpis = new HashMap<String, String>();
			powerName.setName(e.element("LABEL").attribute("VALUE").getText());
			powerNameKpis.put("POWER_SUPPLIES:LABEL", e.element("LABEL")
					.attribute("VALUE").getText());
			powerNameKpis.put("POWER_SUPPLIES:STATUS", e.element("STATUS")
					.attribute("VALUE").getText());

			powerName.setKpis(powerNameKpis);
			instances.add(powerName);
		}

		return instances;
	}

	private Document buildDocument(String xml) {
		Document document = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());

			SAXReader reader = new SAXReader();

			document = reader.read(in);

			return document;

		} catch (DocumentException e) {
			//
			e.printStackTrace();
		}
		return document;

	}

	public String format(String xml) {

		xml = "<?xml version='1.0'?>"
				+ "<GET_EMBEDDED_HEALTH_DATA>"
				+ StringUtils.substringBetween(xml,
						"<GET_EMBEDDED_HEALTH_DATA>",
						"</GET_EMBEDDED_HEALTH_DATA>")
				+ "</GET_EMBEDDED_HEALTH_DATA>";

		return xml;
	}

	public static void main(String[] args) throws Exception {

		Command cmd = new Command();

		try {
			 
			/*String xml =  IOUtils.toString(ILOParser.class.getResourceAsStream("/192.168.151.23_ilo3_182.xml")); 
			   
			   cmd.setType(DeviceType.ILO3); */
			 

			   String xml = IOUtils.toString(ILOParser.class.getResourceAsStream("/192.168.151.224_ilo4_203.xml"));

			 cmd.setType(DeviceType.ILO4);

			cmd.setExecResult(xml);

			cmd.setAttributes(new HashMap<String, String>());

			ILOParser p = new ILOParser(cmd);

			p.format(xml);

			p.parse();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
