package com.kola.core.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class DataBaseKit {
	
	private static DataBaseKit instance;
	private String databasePath;
	private DataBase dataBase;
	
	public DataBaseKit(){
		URL url = getClass().getResource("/");
		databasePath = (url.getPath()+"../data/database.xml").replaceAll("%20", " ");
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(DataBase.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			dataBase = (DataBase) unmarshaller.unmarshal(new File(databasePath));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public String getS(){
		return "getS";
	}
	public List<User> getUsers(){
		List<User> users= new ArrayList();
		User user = new User();
		user.setUsername("user1");
		users.add(user);
		user = new User();
		user.setUsername("user2");
		users.add(user);
		return users;
	}
	static public DataBaseKit getInstance() {
		synchronized (DataBaseKit.class) {
			if (instance == null)
				instance = new DataBaseKit();
		}
		return instance;
	}

	public DataBase getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}
	
	public void updateDataBase(){
		try {
			FileOutputStream outPut = null;
			outPut = new FileOutputStream(databasePath);
			JAXBContext jc = JAXBContext.newInstance(DataBase.class);
			Marshaller m = jc.createMarshaller();
			m.marshal(dataBase, outPut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
}
