package com.kola.core.web.admin;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.kola.core.data.Menu;
import com.kola.core.data.MenuTable;
import com.kola.core.service.MenuService;
import com.kola.core.web.struts2.BaseAdminAction;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * 后台Action类 - 菜单
 */

@ParentPackage("admin")
public class MenuAction extends BaseAdminAction<Menu> {

	private Menu menu;
	private MenuTable menuTable;
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}


	public MenuTable getMenuTable() {
		menuTable = menuService.getAll();
		return menuTable;
	}

	public void setMenuTable(MenuTable menuTable) {
		this.menuTable = menuTable;
	}



	private static final long serialVersionUID = 6465259795235263493L;

	private String parentId;

	@Resource
	private MenuService menuService;

	// 添加
	public String add() {
		return INPUT;
	}

	// 编辑
	public String edit() {
		menu = menuService.get(id);
		return INPUT;
	}

	// 列表
	public String list() {
		this.setAttribute("menutable", JSONArray.fromObject(menuService.getAll()).toString());
		return LIST;
	}

	// 删除
	public String delete() {
		MenuTable menuTable  = menuService.getChildren(id);
		if (menuTable.size() > 0) {
			addActionError("此文章分类存在下级分类，删除失败!");
			return ERROR;
		}
		menuService.delete(id);
		return RELOAD;
	}

	// 保存
	@Validations(
		requiredStrings = {
			@RequiredStringValidator(fieldName = "menu.title", message = "导航名称不允许为空!"),
			@RequiredStringValidator(fieldName = "menu.url", message = "路径不允许为空!")
		},
		requiredFields = { 
			@RequiredFieldValidator(fieldName = "menu.orderList", message = "排序不允许为空!")
		},
		intRangeFields = {
			@IntRangeFieldValidator(fieldName = "menu.orderList", min = "0", message = "排序必须为零或正整数!")
		}
	)
	@InputConfig(resultName = "error")
	public String save() {
		if (StringUtils.isNotEmpty(parentId)) {
			menu.setParentId(parentId);
		}
		menuService.save(menu);
		this.updateMainFrame = true;
		redirectionUrl = "/admin/menu!add.action";
		return SUCCESS;
	}

	// 更新
	@Validations(
		requiredStrings = {
			@RequiredStringValidator(fieldName = "menu.title", message = "导航名称不允许为空!"),
			@RequiredStringValidator(fieldName = "menu.url", message = "路径不允许为空!")
		},
		requiredFields = { 
			@RequiredFieldValidator(fieldName = "menu.orderList", message = "排序不允许为空!")
		},
		intRangeFields = {
			@IntRangeFieldValidator(fieldName = "menu.orderList", min = "0", message = "排序必须为零或正整数!")
		}
	)
	@InputConfig(resultName = "error")
	public String update() {
		if (StringUtils.isNotEmpty(parentId)) {
			menu.setParentId(parentId);
		}
		menu.setId(id);
		menuService.update(menu);
		this.updateMainFrame = true;
		redirectionUrl = "/admin/menu!edit.action?id="+id;
		return SUCCESS;
	}


	public void grid(){
		pageT=menuService.getPageT(this.getPageT());
		this.ajaxJson(pageT.ajaxPage());
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