package com.kola.core.util.report;

import java.util.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "report")
public class Report {
	private String name;

	private Vector<Query> querys;

	private String resultType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElementWrapper(name = "querys")
	@XmlElement(name = "query")
	public Vector<Query> getQuerys() {
		return querys;
	}

	public void setQuerys(Vector<Query> querys) {
		this.querys = querys;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	
}
