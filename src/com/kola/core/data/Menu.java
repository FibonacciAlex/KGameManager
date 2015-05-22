package com.kola.core.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "menu")
public class Menu extends Row implements IRow{
	public static final String PATH_SEPARATOR = ",";// 树路径分隔符
	private String description;
	private String title;
	private String path;
	private String url;
	private String remark;
	private Boolean isVisible;
	private Boolean isEnable;
	private Boolean isHome=false;
	private Integer orderList;
	private String parentId;
	private Integer level;
	private List<Menu> children;
	private Boolean ischecked;
	private String text;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Integer getOrderList() {
		return orderList;
	}

	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Boolean getIsHome() {
		return isHome;
	}

	public void setIsHome(Boolean isHome) {
		this.isHome = isHome;
	}

	@XmlTransient
	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLevel() {
		return path.split(PATH_SEPARATOR).length - 1;
	}

	@XmlTransient
	public Boolean getIschecked() {
		return ischecked;
	}

	public void setIschecked(Boolean ischecked) {
		this.ischecked = ischecked;
	}
	
	@XmlTransient
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
}