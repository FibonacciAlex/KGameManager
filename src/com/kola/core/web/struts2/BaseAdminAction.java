package com.kola.core.web.struts2;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.ParentPackage;

import com.kola.core.data.Menu;
import com.kola.core.service.MenuService;
import com.kola.core.util.page.Page;

/**
 * 后台Action类 - 管理中心基类
 */
@ParentPackage("admin")
public class BaseAdminAction<T> extends BaseAction {

	private static final long serialVersionUID = 6718838822334455667L;
	@Resource
	private MenuService menuService;
	protected String menuId;
	protected String id;
	protected String[] ids;
	protected Page<T> pageT = new Page<T>();
	protected String logInfo;// 日志记录信息
	protected String redirectionUrl;// 操作提示后的跳转URL,为null则返回前一页
	protected Integer page;
	protected Integer pagesize;
	protected Boolean updateMainFrame=false;
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	
	public String input() throws Exception {
		return INPUT;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getRedirectionUrl() {
		return redirectionUrl;
	}

	public void setRedirectionUrl(String redirectionUrl) {
		this.redirectionUrl = redirectionUrl;
	}

	public List<Menu> getMenuPathList(){
		return menuService.getMenuPathList(menuId);
	}

	public Page<T> getPageT() {
		pageT.setPage(page);
		pageT.setPagesize(pagesize);
		return pageT;
	}

	public void setPageT(Page<T> pageT) {
		this.pageT = pageT;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public Boolean getUpdateMainFrame() {
		return updateMainFrame;
	}

	public void setUpdateMainFrame(Boolean updateMainFrame) {
		this.updateMainFrame = updateMainFrame;
	}
	
}