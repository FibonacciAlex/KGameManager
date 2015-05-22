package com.kola.core.util.report;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "query")
public class Query {
	private String name;

	private String data;
	
	private String type;
	
	private String isLowerCase;
	
	private String isPage;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIsLowerCase() {
		return isLowerCase;
	}

	public void setIsLowerCase(String isLowerCase) {
		this.isLowerCase = isLowerCase;
	}

	public String getIsPage() {
		return isPage;
	}

	public void setIsPage(String isPage) {
		this.isPage = isPage;
	}
}
