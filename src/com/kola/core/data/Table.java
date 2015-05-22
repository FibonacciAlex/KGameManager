package com.kola.core.data;

import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.kola.core.util.ReflectionUtils;
import com.kola.core.util.page.Page;

public class Table<T> extends Vector<T> implements ITable<T, String> {
	
	private static final long serialVersionUID = -7752169808146235723L;
	
	public T getT(String id) {
		for (T t : this) {
			if(StringUtils.equalsIgnoreCase(ReflectionUtils.invokeGetterMethod(t, "id").toString(), id)){
				return t;
			}
		}
		return null;
	} 

	public T deleteT(String id) {
		T t=this.getT(id);
		this.remove(t);
		return t;
	}

	public T saveT(T entity) {
		this.add(entity);
		return entity;
	}

	public T updateT(T entity,String ... excludeFilds) {
		for (T t : this) {
			if(StringUtils.equalsIgnoreCase(ReflectionUtils.invokeGetterMethod(t, "id").toString(), ReflectionUtils.invokeGetterMethod(entity, "id").toString())){
				try {
					BeanUtils.copyProperties(entity, t , excludeFilds);
					return t;
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return entity;
	}
	
	public Page<T> getPageT(Page<T> page){
		page.setTotal(this.size());
		if(this.size()>page.getPagesize()*page.getPage()){
			page.setRows(this.subList((page.getPage()-1)*page.getPagesize(), page.getPage()*page.getPagesize()));
		}else{
			page.setRows(this.subList((page.getPage()-1)*page.getPagesize(), this.size()));
		}
		return page;
	}
}
