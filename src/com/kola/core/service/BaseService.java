package com.kola.core.service;

import java.io.Serializable;

import com.kola.core.util.page.Page;

public interface BaseService<T, PK extends Serializable> {
	
	/**
	 * @param id 
	 * @return 获取某个对象
	 */
	public T get(PK id);

	/**
	 * @param entity
	 * @return 保存某个对象
	 */
	public T save(T entity);

	/**
	 * @param entity
	 * @return 更新某个对像
	 */
	public T update(T entity);

	/**
	 * @param id
	 * @return 删除某个对象,并返回删除对象的值
	 */
	public T delete(PK id);
	
	/**
	 * @param page
	 * @return 
	 */
	public Page<T> getPageT(Page<T> page);
}
