package com.kola.core.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bsh.EvalError;
import bsh.Interpreter;

import com.kola.core.util.report.Query;
import com.kola.core.util.report.Report;
import com.kola.core.util.report.ReportKit;

import freemarker.ext.beans.BeansWrapper;
@Service
@Transactional
public class ReportSerive {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getResult(HttpServletRequest request,
			HttpServletResponse response) {
		Map paramsMap = new HashMap();
		try {
			getRequestParameters(request, paramsMap);
			String reportName = paramsMap.get("reportName").toString();
			Report report =ReportKit.getInstance().getReport(reportName);
			Map reportMap = new HashMap();
			reportMap.put("request", request);
			reportMap.put("response", response);
			reportMap.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
			reportMap.put("params", paramsMap);
			if(report.getQuerys()!=null){
				for (Query query : report.getQuerys()) {
					String sql = getResult(this.getValue(report.getName()+"-"+query.getName(),query.getData(), reportMap));
					if("output".equals(query.getType())){
						return sql;
					}else if("execute".equals(query.getType())){
						try {
							Object resultObject = getExecuteObject(request,
									response, reportMap, sql);
							if(resultObject!=null)
							reportMap.put(query.getName(), resultObject);
						} catch (EvalError e) {
							e.printStackTrace();
						}
					}else if("executeAndOutput".equals(query.getType())){
						Object resultObject = getExecuteObject(request,
								response, reportMap, sql);
						if(resultObject!=null)
						return getResult(resultObject.toString());
						else
							return "";
					}else{
						reportMap.put(query.getName(), sql);
					}
				}
			}
			reportMap.remove("request");
			reportMap.remove("response");
			reportMap.remove("statics");
			if(report.getResultType()!=null){
				if(report.getResultType().equals("data")){
					try {
						return reportMap;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(report.getResultType().equals("output")){
					try {
						return JSONObject.fromObject(reportMap).toString();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}

	private void getRequestParameters(HttpServletRequest request, Map paramsMap) {
		if(request!=null){
			Map map = request.getParameterMap();
			for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
				Map.Entry element = (Map.Entry) iter.next();
				String paramName = (String) element.getKey();
				String[] values = (String[]) element.getValue();
				if (values != null && values.length != 0){
					if (values.length > 1){
						paramsMap.put(paramName, values);
					}else{
						paramsMap.put(paramName, values[0]);
					}
				}
			}
		}
	}
	
	private String getResult(String result) {
		result=result.replaceAll("\r", "");
		result=result.replaceAll("\n", "");
		result=result.replaceAll("\t", "");
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private Object getExecuteObject(HttpServletRequest request,
			HttpServletResponse response, Map paramsMap, String sql)
			throws EvalError {
		Interpreter inter = new Interpreter();
		inter.set("request", request);
		inter.set("response", response);
		inter.set("paramsMap", paramsMap);
		inter.eval(sql);
		Object resultObject = inter.get("resultObject");
		return resultObject;
	}
	
	public String getValue(String name,String value, Map map) {
		return ReportKit.getInstance().getQueryValue(name, value, map);
	}
	// public ReportBo getObject(String reportName) {
	// if (reportMap.containsKey(reportName)
	// && !"DEVELOP".equals(this.getPropertiesByKey("MODULE").trim())) {
	//
	// return (ReportBo) reportMap.get(reportName);
	// } else {
	// ReadChartXml xml = new
	// ReadChartXml(this.getPropertiesByKey("SQLTYPE").trim());
	// ReportBo bo = new ReportBo();
	// try {
	// Map map = xml.getObject();
	// bo = (ReportBo) map.get(reportName);
	// this.setObject(reportName, bo);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return bo;
	// }
	// }
}
