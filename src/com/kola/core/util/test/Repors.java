package com.kola.core.util.test;

import java.util.Vector;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "repors")
public class Repors {

	
	private String name;

	private Vector<Report> reports;
	
	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "report")
	public Vector<Report> getReports() {
		return reports;
	}

	public void setReports(Vector<Report> reports) {
		this.reports = reports;
	}

}