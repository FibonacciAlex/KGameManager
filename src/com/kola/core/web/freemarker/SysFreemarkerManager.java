package com.kola.core.web.freemarker;

import javax.servlet.ServletContext;

import org.apache.struts2.views.freemarker.FreemarkerManager;

import com.kola.core.util.SpringUtil;
import com.kola.core.web.directive.MenuDirective;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class SysFreemarkerManager extends FreemarkerManager {
	@Override
	public synchronized Configuration getConfiguration(ServletContext servletContext) throws TemplateException{
		Configuration config = (Configuration)servletContext.getAttribute(FreemarkerManager.CONFIG_SERVLET_CONTEXT_KEY);
        if(config == null){
			config = createConfiguration(servletContext);
			MenuDirective menuDirective = (MenuDirective)SpringUtil.getBean("menuDirective");
			config.setSharedVariable("menuList", menuDirective);
			servletContext.setAttribute(FreemarkerManager.CONFIG_SERVLET_CONTEXT_KEY, config);
        }
        config.setWhitespaceStripping(true);
        return config;
	}
}
