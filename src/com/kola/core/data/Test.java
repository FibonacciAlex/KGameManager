package com.kola.core.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.kola.core.util.report.Query;
import com.kola.core.util.report.Report;
import com.kola.core.util.report.ReportBase;

public class Test {
	public static void main(String[] args) {
//		MenuTable countrys = new MenuTable();
//		
//		Menu country = new Menu();
//		country.setDescription("首页");
//		country.setTitle("首页");
//		country.setUrl("###");
//		country.setDescription("首页");
//		country.setId("1000");
//		country.setPath("1000");
//		country.setIsVisible(false);
//		country.setIsEnable(true);
//		country.setIsHome(true);
//		countrys.add(country);
//		
//		country = new Menu();
//		country.setDescription("KLOA后台数据");
//		country.setTitle("首页");
//		country.setUrl("/admin/admin!index.action");
//		country.setDescription("首页");
//		country.setId("10000001");
//		country.setPath("1000,10000001");
//		country.setIsVisible(false);
//		country.setIsEnable(true);
//		country.setIsHome(true);
//		countrys.add(country);
//		
//		country = new Menu();
//		country.setDescription("系统管理 ");
//		country.setTitle("系统管理 ");
//		country.setUrl("###");
//		country.setDescription("系统管理 ");
//		country.setId("2000");
//		country.setPath("2000");
//		country.setIsVisible(false);
//		country.setIsEnable(true);
//		countrys.add(country);
//		
//		country = new Menu();
//		country.setDescription("菜单管理");
//		country.setTitle("菜单管理");
//		country.setUrl("/admin/menu!list.action");
//		country.setDescription("菜单管理");
//		country.setId("20000001");
//		country.setPath("2000,20000001");
//		country.setIsVisible(false);
//		country.setIsEnable(true);
//		countrys.add(country);
//		
//		DataBase earth = new DataBase();
//		earth.setDescription("KLOA后台数据");
//		earth.setMenus(countrys);
//		
//		UserTable users = new UserTable();
//		User user = new User();
//		user.setId("3000");
//		user.setUsername("admin");
//		user.setMenuIds("1000,10000001,2000,20000001");
//		users.add(user);
//		earth.setUsers(users);
		
		
		ReportBase reportBase = new ReportBase();
		
		Vector<Report> reports = new Vector<Report>(); 
		Report report = new Report();
		report.setName("name");
		report.setResultType("data");
		
		
		Vector<Query> querys = new Vector<Query>(); 
		Query query = new Query();
		query.setName("query");
		query.setData("data");
		querys.add(query);
		
		query = new Query();
		query.setName("query1");
		query.setData("data1");
		querys.add(query);
		report.setQuerys(querys);
		
		reports.add(report);
		
		reportBase.setReports(reports);
		FileOutputStream outPut = null;
		try {
			outPut = new FileOutputStream("c:/database.xml");
			StringWriter sw = new StringWriter();
			JAXBContext jc = JAXBContext.newInstance(ReportBase.class);
			Marshaller m = jc.createMarshaller();
			m.marshal(reportBase, sw);
//			m.marshal(earth, outPut);
			System.out.println(sw.toString());
			
			
			JAXBContext jaxbContext = JAXBContext.newInstance(ReportBase.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			reportBase = (ReportBase) unmarshaller.unmarshal(new File("D:/working/kola/WebRoot/WEB-INF/data/chart/test.xml"));
			System.out.println(reportBase.getReports().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
