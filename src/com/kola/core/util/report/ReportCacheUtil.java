package com.kola.core.util.report;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.dao.DataRetrievalFailureException;


/**
 * 工具类 - 缓存
 */

public class ReportCacheUtil {
	public static final String REPORT_CACHE_KEY = "ReportCache";
	
	public synchronized static void putCache(String key,Object object){
		Element element = new Element(key,object);
		System.out.println(CacheManager.getInstance().getCache(REPORT_CACHE_KEY));
		CacheManager.getInstance().getCache(REPORT_CACHE_KEY).put(element);
	}  

	public synchronized static Object getCache(String key){
		Element element = null;
		Cache cache=null;
		try {
			cache=CacheManager.getInstance().getCache(REPORT_CACHE_KEY);
			if(cache!=null)
			element = CacheManager.getInstance().getCache(REPORT_CACHE_KEY).get(key);
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("ResourceCache failure: " + cacheException.getMessage(), cacheException);
		}
		if (element == null) {
			return null;
		} else {
			try {
				return element.getObjectValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}  

	public synchronized static void removeCache(String key){
		if(CacheManager.getInstance().getCache(REPORT_CACHE_KEY).get(key)!=null)
			CacheManager.getInstance().getCache(REPORT_CACHE_KEY).remove(key);
	}  

}