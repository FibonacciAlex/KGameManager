package com.kola.core.util.test;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement
public class Query {
 
    private String name;
 
    private String data;

    public Query(){
    	
    }
    
    public Query(String name,String data){
    	this.name = name;
    	this.data = data;
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
    
    
} 