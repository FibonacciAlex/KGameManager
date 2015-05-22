package com.kola.core.web.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.kola.core.service.MenuService;
import com.kola.core.util.FreemarkerUtil;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class MenuDirective implements TemplateDirectiveModel {
//	private final HttpServletRequest request;
//	private final HttpServletResponse response;
//
//	public MenuDirective(HttpServletRequest request,
//			HttpServletResponse response) {
//		this.request = request;
//		this.response = response;
//	}

	@Resource
	private MenuService menuService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment environment, Map paramMap,
			TemplateModel[] templateModel,
			TemplateDirectiveBody templateDirectiveBody)
			throws TemplateException, IOException {
		String userId = FreemarkerUtil.getString("userId", paramMap);
		List localList = menuService.getUserMenu(userId);
		if ((templateDirectiveBody != null) && (localList != null)) {
			if (templateModel.length > 0)
				templateModel[0] = ObjectWrapper.BEANS_WRAPPER.wrap(localList);
			templateDirectiveBody.render(environment.getOut());
		}
	}
}