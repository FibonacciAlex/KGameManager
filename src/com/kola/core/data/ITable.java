package com.kola.core.data;

import java.io.Serializable;
import java.util.List;

import com.kola.core.util.page.Page;

public interface ITable<T, PK extends Serializable>{
	/**
	 * @param id 
	 * @return 获取某个对象
	 */
	public T getT(PK id);

	/**
	 * @param entity
	 * @return 保存某个对象
	 */
	public T saveT(T entity);

	/**
	 * @param entity
	 * @return 更新某个对像
	 */
	public T updateT(T entity,String ... excludeFilds);


	/**
	 * @param id 
	 * 删除某个对象
	 */
	public T deleteT(PK id);
	
	/**
	 * @param page
	 * @return 获取分页对象组
	 */
	public Page<T> getPageT(Page<T> page);
}
