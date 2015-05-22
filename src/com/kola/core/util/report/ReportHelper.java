package com.kola.core.util.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

public class ReportHelper {

	/**
	 * @param reportName
	 * @param request
	 * @param params
	 * @return 判断传进来的参数值是否有变化
	 */
	@SuppressWarnings("unchecked")
	public static boolean getIsParamsChange(String reportName,
			Map<String, String> params) {
		Object object = ReportCacheUtil.getCache(reportName + "_params");
		if (object == null) {
			return true;
		}
		if (object != null && object instanceof Map) {
			Map<String, String> sessionParam = (Map<String, String>) object;
			for (String key : sessionParam.keySet()) {
				if (key.equals("page") || key.equals("pagesize")) {
					continue;
				}
				if (!params.containsKey(key)) {
					return true;
				}
				if (!StringUtils.equalsIgnoreCase(params.get(key),
						sessionParam.get(key))) {
					return true;
				}
			}
		}
		if (ReportHelper.getObject(reportName) == null) {
			return true;
		}
		return false;
	}

	/**
	 * @param reportName
	 * @return 获取对象
	 */
	public static Object getObject(String reportName) {
		return ReportCacheUtil.getCache(reportName);
	}

	/**
	 * @param reportName
	 * @param object
	 * @param params
	 *            设置缓存
	 */
	public static void setObject(String reportName, Object object,
			Map<String, String> params) {
		ReportCacheUtil.putCache(reportName + "_params", params);
		ReportCacheUtil.putCache(reportName, object);
	}

	/**
	 * @param reportName
	 * @param object
	 * @param params
	 *            设置缓存
	 */
	public static void setObject(String reportName, Object object,
			Object totalObject, Map<String, String> params) {
		ReportCacheUtil.putCache(reportName + "_params", params);
		ReportCacheUtil.putCache(reportName, object);
		if (totalObject != null) {
			ReportCacheUtil.putCache(reportName + "_total", totalObject);
		}
	}

	/**
	 * @param list
	 * @param params
	 * @return 获取分页对象
	 */
	@SuppressWarnings("rawtypes")
	public static String getPageResult(List list, Map<String, String> params) {
		JSONObject json = new JSONObject();
		if (list == null) {
			json.put("Total", 0);
			json.put("Rows", "[]");
			return json.toString();
		}
		json.put("Total", list.size());
		int page = Integer.parseInt(params.get("page"));
		int pagesize = Integer.parseInt(params.get("pagesize"));
		if (list.size() > page * pagesize) {
			json.put("Rows",
					list.subList((page - 1) * pagesize, page * pagesize));
		} else {
			json.put("Rows", list.subList((page - 1) * pagesize, list.size()));
		}
		return json.toString();
	}

	/**
	 * @param list
	 * @param params
	 * @return 获取分页对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getPageResult(List list, Object totalObject,
			Map<String, String> params) {
		JSONObject json = new JSONObject();
		if (list == null) {
			json.put("Total", 0);
			json.put("Rows", "[]");
			return json.toString();
		}
		json.put("Total", list.size());
		int page = Integer.parseInt(params.get("page"));
		int pagesize = Integer.parseInt(params.get("pagesize"));
		List subList = new ArrayList();
		int end = 0;
		if (list.size() > page * pagesize) {
			end = page * pagesize;
		} else {
			end = list.size();
		}
		subList.addAll(list.subList((page - 1) * pagesize, end));
		if (totalObject != null) {
			subList.add(totalObject);
		}
		json.put("Rows", subList);
		return json.toString();
	}
}
