package com.kola.core.web.admin;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.ParentPackage;

import com.kola.core.data.Menu;
import com.kola.core.data.User;
import com.kola.core.data.UserTable;
import com.kola.core.service.MenuService;
import com.kola.core.service.UserService;
import com.kola.core.web.struts2.BaseAdminAction;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * 后台Action类 - 菜单
 */

@ParentPackage("admin")
public class UserAction extends BaseAdminAction<User> {

	private User user;
	private UserTable userTable;
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public UserTable getUserTable() {
		userTable = userService.getAll();
		return userTable;
	}

	public void setUserTable(UserTable userTable) {
		this.userTable = userTable;
	}



	private static final long serialVersionUID = 6465259795235263493L;

	private String parentId;

	@Resource
	private UserService userService;
	@Resource
	private MenuService menuService;
	// 是否已存在 ajax验证
	public String checkUsername() {
		String username = user.getUsername();
		if (userService.isExistByUsername(username)) {
			return ajaxText("false");
		} else {
			return ajaxText("true");
		}
	}
	
	// 添加
	public String add() {
		return INPUT;
	}

	// 编辑
	public String edit() {
		user = userService.get(id);
		return INPUT;
	}

	// 列表
	public String list() {
		return LIST;
	}

	// 删除
	public String delete() {
		userService.delete(id);
		return ajaxJsonSuccessMessage("删除成功！");
	}

	// 保存
	@Validations(
		requiredStrings = {
			@RequiredStringValidator(fieldName = "user.username", message = "用户名不允许为空!"),
			@RequiredStringValidator(fieldName = "user.password", message = "密码不允许为空!"),
			@RequiredStringValidator(fieldName = "user.truename", message = "真实名不允许为空!")
		}, 
		stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "user.username", minLength = "2", maxLength = "20", message = "用户名长度必须在${minLength}到${maxLength}之间!"),
			@StringLengthFieldValidator(fieldName = "user.password", minLength = "4", maxLength = "20", message = "密码长度必须在${minLength}到${maxLength}之间!") 
		}
	)
	@InputConfig(resultName = "error")
	public String save() {
		userService.save(user);
		this.setUpdateMainFrame(true);
		redirectionUrl = "/admin/user!add.action";
		return SUCCESS;
	}

	// 更新
	@Validations(
			requiredStrings = { 
					@RequiredStringValidator(fieldName = "user.username", message = "用户名不允许为空!"),
					@RequiredStringValidator(fieldName = "user.truename", message = "真实名不允许为空!")
			}, 
			stringLengthFields = {
					@StringLengthFieldValidator(fieldName = "user.username", minLength = "2", maxLength = "20", message = "用户名长度必须在${minLength}到${maxLength}之间!"),
					@StringLengthFieldValidator(fieldName = "user.password", minLength = "4", maxLength = "20", message = "密码长度必须在${minLength}到${maxLength}之间!") 
			}
	)
	@InputConfig(resultName = "error")
	public String update() {
		user.setId(id);
		userService.update(user);
		this.updateMainFrame = true;
		redirectionUrl = "/admin/user!edit.action?id="+id;
		return SUCCESS;
	}

	public void grid(){
		pageT=userService.getPageT(this.getPageT());
		this.ajaxJson(pageT.ajaxPage());
	}
	
	public void getUserCheckMenu(){
		List<Menu> menusResult =  menuService.getUserCheckMenu(this.id);
		this.ajaxJson(JSONArray.fromObject(menusResult).toString());
	}
	public String user(){
		return "user";
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}