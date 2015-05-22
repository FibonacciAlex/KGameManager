package com.kola.core.web.admin;

import javax.annotation.Resource;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.kola.core.data.User;
import com.kola.core.service.MenuService;
import com.kola.core.service.UserService;
import com.kola.core.web.struts2.BaseAdminAction;

@SuppressWarnings("serial")
@ParentPackage("admin")
public class AdminAction extends BaseAdminAction<User> {
	public static final String SPRING_SECURITY_LAST_EXCEPTION = "SPRING_SECURITY_LAST_EXCEPTION";

	@Resource
	UserService userService;
	//Resource(name="xxx")，相当于为该属性注入一个名称为xxx的bean
	@Resource 
	MenuService menuService;
	public static User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 登录验证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		String username = this.getParameter("username");
		String password = this.getParameter("password");
		if (StringUtils.isEmpty(username)) {
			return "login";
		}
		User user = userService.checkLogin(username, password);
		if (user == null) {
			addActionError("您的用户名或密码错误!");
			return "login";
		} else {
//			if (user.getUsername().equals("cftgyy")) {
//				this.setSession("loginId", user.getId());
//				this.setSession("loginUsername", user.getUsername());
//				this.setSession("homeUrl", menuService.getHomeUrl(user.getId()));
//				this.getResponse().sendRedirect("/admin/report!index.action?name=channeldaily");
//			} else {
				setUser(user);
				this.setSession("loginId", user.getId());
				this.setSession("loginUsername", user.getUsername());
//				this.setSession("homeUrl", menuService.getHomeUrl(user.getId()));  //原来home 路径
//				this.getResponse().sendRedirect("/admin/admin!main.action");//原返回
				this.setSession("homeUrl", this.getRequest().getContextPath() + menuService.getHomeUrl(user.getId()));
				this.getResponse().sendRedirect(this.getRequest().getContextPath() + "/admin/admin!main.action");
//			}
			return null;
		}
	}

	/**
	 * 退出登录
	 * 
	 * @throws Exception
	 */
	public void logout() throws Exception {
		setUser(null);
		this.removeSession("loginId");
		this.removeSession("loginUsername");
		this.getResponse().sendRedirect("/admin/admin!login.action");
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

	public String empty() {
		return "empty";
	}
}
