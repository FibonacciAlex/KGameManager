package com.kola.core.util.report;

import java.util.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "reports")
public class ReportBase {
	private Vector<Report> reports;

	@XmlElement(name = "report")
	public Vector<Report> getReports() {
		return reports;
	}

	public void setReports(Vector<Report> reports) {
		this.reports = reports;
	}
	
}
