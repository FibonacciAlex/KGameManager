package com.kola.core.web.struts2;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.RequestUtils;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 拦截器 - 判断是否登录
 */

public class LoginVerifyInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = -86246303854807787L;

	protected String getUri(HttpServletRequest request) {
		String uri = (String) request
				.getAttribute("javax.servlet.include.servlet_path");
		if (uri != null)
			return uri;
		uri = RequestUtils.getServletPath(request);
		if (uri != null && !"".equals(uri)) {
			return uri;
		} else {
			uri = request.getRequestURI();
			return uri.substring(request.getContextPath().length());
		}
	}

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
		String uri = getUri(request);
		String loginId = (String) invocation.getInvocationContext().getSession().get("loginId");
		if (loginId == null && uri.indexOf("login.action")<=0) {
			return "login";
		}
		return invocation.invoke(); //退出当前拦截器
	}

}