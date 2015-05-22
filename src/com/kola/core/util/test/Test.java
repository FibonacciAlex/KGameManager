package com.kola.core.util.test;

import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Test {
	public static void main(String[] args) {
		Vector<Report> orders = new Vector<Report>();
        
        Report order1 = new Report("Mart", "LH59900");
         
        Report order2 = new Report("Mart", "LH59800");
         
        orders.add(order1);
        orders.add(order2);
         
        Repors shop = new Repors();
        shop.setReports(orders);
         
         
       
        try {
        	JAXBContext context = JAXBContext.newInstance(Repors.class);
            Marshaller marshal = context.createMarshaller();
            marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshal.marshal(shop, System.out);
//            StringWriter writer = new StringWriter();
//            marshal.marshal(shop, writer);
//            System.out.println(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
         
	}
}
