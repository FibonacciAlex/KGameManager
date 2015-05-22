package com.kola.core.service;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kola.core.data.DataBaseKit;
import com.kola.core.data.User;
import com.kola.core.data.UserTable;
import com.kola.core.util.page.Page;
/**
 * @service 只用此注解标记bean，不填value，则spring生成的bean名称是取类名，然后将首字母小写。
 * 使用@Service("XXX"),就相当于讲这个类定义为一个bean，bean名称为XXX;
 * @author Alex
 * @create 2015年1月22日 上午11:48:30
 */
@Service
@Transactional
public class UserService implements BaseService<User,String> {

	public User get(String id) {
		 return DataBaseKit.getInstance().getDataBase().getUsers().getT(id);
	}

	public UserTable getAll(){
		return DataBaseKit.getInstance().getDataBase().getUsers();
	}
	
	public User save(User entity) {
		entity.getId();//产生ID
		return DataBaseKit.getInstance().getDataBase().getUsers().saveT(entity);
	}

	public User update(User entity) {
		if(StringUtils.isEmpty(entity.getPassword())){
			//如果密码为空，则不进行更新原密码
			return DataBaseKit.getInstance().getDataBase().getUsers().updateT(entity,"password");
		}
		return DataBaseKit.getInstance().getDataBase().getUsers().updateT(entity);
	}

	public User delete(String id) {
		return DataBaseKit.getInstance().getDataBase().getUsers().deleteT(id);
	}
    
	public User checkLogin(String username,String password){
		UserTable userTable = DataBaseKit.getInstance().getDataBase().getUsers();
		for (User user : userTable) {
			if(StringUtils.equalsIgnoreCase(user.getUsername(), username)&&StringUtils.endsWithIgnoreCase(user.getPassword(), password)){
				return user;
			}
		}
		return null;
	}

	public boolean isExistByUsername(String username) {
		UserTable userTable = DataBaseKit.getInstance().getDataBase().getUsers();
		for (User user : userTable) {
			if(StringUtils.equalsIgnoreCase(user.getUsername(), username)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Page<User> getPageT(Page<User> page) {
		return DataBaseKit.getInstance().getDataBase().getUsers().getPageT(page);
	}

 }
