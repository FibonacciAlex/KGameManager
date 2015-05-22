package com.kola.core.util.report;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class ReportKit {
	static private ReportKit reportKit;
	private final String DEVELOP = "DEVELOP";
	private Map<String, Report> reportMap;
	Configuration configuration;
	StringTemplateLoader stringLoader;
	
	public ReportKit() {
		configuration = new Configuration();
		configuration.setClassicCompatible(true);
		stringLoader = new StringTemplateLoader();
		configuration.setTemplateLoader(stringLoader);
		if(!"DEVELOP".equals(DEVELOP)){
			reportMap=getAllReoprtMap();
		}else{
			reportMap = new HashMap<String, Report>();
		}
	}

	private Map<String, Report> getAllReoprtMap() {
		URL url = getClass().getResource("/");
		String file_dir = (url.getPath()+"../data/chart/").replaceAll("%20", " ");
		File file = new File(file_dir);
		File[] files = file.listFiles();
		Map<String, Report> reportMap = new HashMap<String, Report>();
		try {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(".xml")) {
					JAXBContext jaxbContext = JAXBContext.newInstance(ReportBase.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					ReportBase reportBase = (ReportBase) unmarshaller.unmarshal(files[i]);
					for (Report report : reportBase.getReports()) {
						if (reportMap.containsKey(report.getName())) {
							System.out.println("ERROR:重复的报表名称：" + report.getName());
						} else {
							reportMap.put(report.getName(), report);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return reportMap;
	}

	static public ReportKit getInstance() {
		synchronized (ReportKit.class) {
			if (reportKit == null)
				reportKit = new ReportKit();
		}
		return reportKit;
	}


	public Report getReport(String reportName) {
		if (reportMap.containsKey(reportName)&& !"DEVELOP".equals(DEVELOP)) {
			return (Report) reportMap.get(reportName);
		} else {
			Report report = this.getAllReoprtMap().get(reportName);
			return report;
		}
	}

	@SuppressWarnings("rawtypes")
	public String getQueryValue(String name, String value, Map map) {
		stringLoader.putTemplate(name, value);
		StringWriter writer = new StringWriter();    
        try {
			Template template = configuration.getTemplate(name,"utf-8");
			template.process(map, writer);    
			return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}  
		return null;
	}
	
}
