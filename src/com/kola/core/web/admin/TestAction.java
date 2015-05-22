package com.kola.core.web.admin;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.security.core.userdetails.User;

import com.kola.core.web.struts2.BaseAdminAction;

@SuppressWarnings("serial")
@ParentPackage("admin")
public class TestAction extends BaseAdminAction<User>{
	public static final String SPRING_SECURITY_LAST_EXCEPTION = "SPRING_SECURITY_LAST_EXCEPTION";

	public String login() throws Exception{
		String username = this.getParameter("username");
		String password = this.getParameter("password");
		if(StringUtils.isEmpty(username)){
			return "login";
		}
		if(!StringUtils.equalsIgnoreCase(username, "1")||!StringUtils.endsWithIgnoreCase(password, "1")){
			addActionError("您的用户名或密码错误!");
			return "login";
		}else{
			this.setSession("loginUserName", username);
			this.getResponse().sendRedirect("/admin/admin!main.action");
			return null;
		}
	}
	
	// 后台主页面
	public String main() {
		return "main";
	}
	
	// 后台Header
	public String header() {
		return "header";
	}
	
	// 后台菜单
	public String menu() {
		return "menu";
	}
	
	// 后台中间(显示/隐藏菜单)
	public String middle() {
		return "middle";
	}
	
	// 后台首页
	public String index() {
		return "index";
	}
	 
}
