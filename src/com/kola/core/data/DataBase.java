package com.kola.core.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "database")
public class DataBase {
	private String description = "earth";
	private MenuTable menus;
	private UserTable users;
	
	@XmlAttribute
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElementWrapper(name = "menus")
	@XmlElement(name = "menu")
	public MenuTable getMenus() {
		return menus;
	}

	public void setMenus(MenuTable menus) {
		this.menus = menus;
	}

	@XmlElementWrapper(name = "users")
	@XmlElement(name = "user")
	public UserTable getUsers() {
		return users;
	}

	public void setUsers(UserTable users) {
		this.users = users;
	}

}