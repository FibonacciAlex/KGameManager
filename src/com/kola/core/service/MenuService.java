package com.kola.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kola.core.data.DataBaseKit;
import com.kola.core.data.Menu;
import com.kola.core.data.MenuTable;
import com.kola.core.data.User;
import com.kola.core.util.page.Page;
import com.kola.core.util.tree.ITreeCallback;

@Service
@Transactional
public class MenuService implements BaseService<Menu,String> {

	public Menu get(String id) {
		 return DataBaseKit.getInstance().getDataBase().getMenus().getT(id);
	}

	public MenuTable getAll(){
		return DataBaseKit.getInstance().getDataBase().getMenus();
	}
	
	public Menu save(Menu entity) {
		if(StringUtils.isNotEmpty(entity.getParentId())){
	    	Menu parent = this.get(entity.getParentId());
	    	entity.setPath(parent.getPath() + Menu.PATH_SEPARATOR + entity.getId());
	    }else{
	    	entity.setPath(entity.getId());
	    }
		return DataBaseKit.getInstance().getDataBase().getMenus().saveT(entity);
	}

	public Menu update(Menu entity) {
		if(StringUtils.isNotEmpty(entity.getParentId())){
	    	Menu parent = this.get(entity.getParentId());
	    	entity.setPath(parent.getPath() + Menu.PATH_SEPARATOR + entity.getId());
	    }
		return DataBaseKit.getInstance().getDataBase().getMenus().updateT(entity);
	}

	public Menu delete(String id) {
//		return DataBaseKit.getInstance().getDataBase().getMenus().deleteT(id);
		return null;
	}
    
	public MenuTable getChildren(String id){
		MenuTable menuTable = new MenuTable();
		MenuTable menus=DataBaseKit.getInstance().getDataBase().getMenus();
		for (Menu menu : menus) {
			if(StringUtils.equalsIgnoreCase(menu.getParentId(), id)){
				menuTable.add(menu);
			}
		}
		return menuTable;
	}
	
	public List<Menu> getMenuPathList(String id){
		List<Menu> menusResult = new ArrayList<Menu>();
		if(StringUtils.isEmpty(id)){
			return menusResult;
		}
		Menu menu = this.get(id);
		String [] ids = menu.getPath().split(Menu.PATH_SEPARATOR);
		MenuTable menus=DataBaseKit.getInstance().getDataBase().getMenus();
		for (int i = 0; i < ids.length; i++) {
			for (Menu t : menus) {
				if(StringUtils.equalsIgnoreCase(ids[i], t.getId())){
					menusResult.add(t);
					break;
				}
			}
		}
		return menusResult;
	}

	public String getHomeUrl(String userId){
		User user = DataBaseKit.getInstance().getDataBase().getUsers().getT(userId);
		String menuIds = user.getMenuIds();
		String [] ids = menuIds.split(",");
		MenuTable menus=DataBaseKit.getInstance().getDataBase().getMenus();
		for (int i = 0; i < ids.length; i++) {
			for (Menu t : menus) {
//				if(!StringUtils.startsWith(t.getUrl(), "###")&&t.getIsHome()){
//					 return t.getUrl();
//				}
				if(StringUtils.equalsIgnoreCase(ids[i], t.getId())&&!StringUtils.startsWith(t.getUrl(), "###")&&t.getIsHome()){
					 return t.getUrl();
				}

			}
		} 
		return "/admin/admin!empty.action";
	}
	
	public List<Menu> getUserMenu(String userId) {
		User user = DataBaseKit.getInstance().getDataBase().getUsers().getT(userId);
		String menuIds = user.getMenuIds();
		List<Menu> menusResult = new ArrayList<Menu>();
		if(StringUtils.isEmpty(menuIds)){
			return menusResult;
		}
		String [] ids = menuIds.split(",");
		MenuTable menus=DataBaseKit.getInstance().getDataBase().getMenus();
		for (int i = 0; i < ids.length; i++) {
			for (Menu t : menus) {
				if(StringUtils.equalsIgnoreCase(ids[i], t.getId())&&!t.getIsHome()){
					menusResult.add(t);
					break;
				}
			}
		}
		try {
			menusResult = getTreeMenu(menusResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menusResult;
	}

	private List<Menu> getTreeMenu(List<Menu> menusResult) throws Exception {
		menusResult = buildTree(menusResult, new ITreeCallback<Menu,String>(){
			public void init(Menu o) throws Exception {
				o.setText(o.getTitle());
				o.setChildren(null);
			}

			public String getId(Menu o) throws Exception {
				return o.getId();
			}

			public void addChild(Menu parent, Menu child) throws Exception {
				try {
					List<Menu> children = parent.getChildren();
					if (null == children) {
						children = new ArrayList<Menu>();
						parent.setChildren(children);
					}
					children.add(child);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public String getParentId(Menu o) throws Exception {
				return o.getParentId()!=null?o.getParentId():null;
			}
		});
		return menusResult;
	}
	
	public List<Menu> buildTree(List<Menu> list, ITreeCallback<Menu, String> callback) throws Exception {
		List<Menu> t = new ArrayList<Menu>();
		List<Menu> cl = new ArrayList<Menu>();
		Map<String, Menu> idx = new HashMap<String, Menu>();
		for (Menu o : list) {
			callback.init(o);
			if (null == callback.getParentId(o)) {
				t.add(o);
			} else {
				cl.add(o);
			}
			idx.put(callback.getId(o), o);
		}
		for (Menu o : cl) {
			Menu parent = idx.get(callback.getParentId(o));
			if (null == parent) {
				t.add(o);
			} else {
				callback.addChild(parent, o);
			}
		}
		return t;
	}

	@Override
	public Page<Menu> getPageT(Page<Menu> page) {
		return DataBaseKit.getInstance().getDataBase().getMenus().getPageT(page);
	}

	public List<Menu> getUserCheckMenu(String userId) {
		List<Menu> menusResult = new ArrayList<Menu>();
		String [] ids = null;
		if(StringUtils.isNotEmpty(userId)){
			User user = DataBaseKit.getInstance().getDataBase().getUsers().getT(userId);
			String menuIds = user.getMenuIds();
			if(!StringUtils.isEmpty(menuIds)){
				ids = menuIds.split(",");
			}
		}
		MenuTable menus=DataBaseKit.getInstance().getDataBase().getMenus();
		for (Menu t : menus) {
			if(ids!=null&&ids.length>0){
				boolean flag =false;
				for (int i = 0; i < ids.length; i++) {
					if(StringUtils.equalsIgnoreCase(ids[i], t.getId())){
						t.setIschecked(true);
						menusResult.add(t);
						flag=true;
						break;
					}
				}
				if(!flag){
					t.setIschecked(false);
					menusResult.add(t);
				}
			}else{
				t.setIschecked(false);
				menusResult.add(t);
			}
		}
		try {
			menusResult = getTreeMenu(menusResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menusResult;
	}

 }
