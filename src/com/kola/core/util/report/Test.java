package com.kola.core.util.report;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Test {
	public static String getT(){
		return "222";
	}
	public  String getT2(){
		return "111";
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		conf.setTemplateLoader(stringLoader);
		stringLoader.putTemplate("greetTemplate", "<#macro greet v>Hello ${v}</#macro>");
		stringLoader.putTemplate("myTemplate", "<#include \"greetTemplate\"><@greet v='${user}'/><#assign static = statics[\"com.kola.core.data.DataBaseKit\"]> <#list static.getInstance().getUsers() as list>${list.username}\r\n</#list> ${test.getT2()} ${user} World!");
		Map root = new HashMap();    
		root.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
        root.put("user", "lunzi");   
        root.put("test", new Test());
        StringWriter writer = new StringWriter();    
        try {
			Template template = conf.getTemplate("myTemplate","utf-8");
			template.process(root, writer);    
			System.out.println(writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}  
	}
}
