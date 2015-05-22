package com.kola.core.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.xwork.StringUtils;
@XmlRootElement(name = "user")
public class User extends Row implements IRow{
	private String username;
	private String menuIds;
	private String password;
	private String truename;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}
	
	@XmlTransient
	public String[] getMenus(){
		if(StringUtils.isNotEmpty(this.menuIds)){
			return this.menuIds.split(",");
		}
		return new String[]{};
	}

}
