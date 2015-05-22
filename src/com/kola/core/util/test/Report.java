package com.kola.core.util.test;

import java.math.BigDecimal;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement
public class Report {
 
    private String name;
    
    private Vector<Query> querys;
    
    private String resultType;

    public Report(){
    	
    }
    
    public Report(String name,String resultType){
    	this.name = name;
    	this.resultType = resultType;
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	@XmlElementWrapper(name = "querys")
	@XmlElement(name = "query")
	public Vector<Query> getQuerys() {
		return querys;
	}

	public void setQuerys(Vector<Query> querys) {
		this.querys = querys;
	}
    
    
} 