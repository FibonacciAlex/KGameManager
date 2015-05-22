package com.kola.core.util;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;
import freemarker.template.utility.DeepUnwrap;

public class FreemarkerUtil {
	public static String getString(String paramString,
			Map<String, TemplateModel> paramMap) throws TemplateModelException {
		TemplateModel localTemplateModel = (TemplateModel) paramMap
				.get(paramString);
		if (localTemplateModel == null)
			return null;
		if (localTemplateModel instanceof TemplateScalarModel)
			return ((TemplateScalarModel) localTemplateModel).getAsString();
		if (localTemplateModel instanceof TemplateNumberModel)
			return ((TemplateNumberModel) localTemplateModel).getAsNumber()
					.toString();
		throw new TemplateModelException("The \"" + paramString
				+ "\" parameter " + "must be a string.");
	}

	public static Integer getInteger(String paramString,
			Map<String, TemplateModel> paramMap) throws TemplateModelException {
		TemplateModel localTemplateModel = (TemplateModel) paramMap
				.get(paramString);
		if (localTemplateModel == null)
			return null;
		if (localTemplateModel instanceof TemplateScalarModel) {
			String str = ((TemplateScalarModel) localTemplateModel)
					.getAsString();
			if (StringUtils.isEmpty(str))
				return null;
			return Integer.valueOf(Integer.parseInt(str));
		}
		if (localTemplateModel instanceof TemplateNumberModel)
			return Integer.valueOf(((TemplateNumberModel) localTemplateModel)
					.getAsNumber().intValue());
		throw new TemplateModelException("The \"" + paramString
				+ "\" parameter " + "must be a integer.");
	}

	public static Boolean getBoolean(String paramString,
			Map<String, TemplateModel> paramMap)  throws TemplateModelException {
		TemplateModel localTemplateModel = (TemplateModel) paramMap
				.get(paramString);
		if (localTemplateModel == null)
			return null;
		if (localTemplateModel instanceof TemplateScalarModel) {
			String str = ((TemplateScalarModel) localTemplateModel)
					.getAsString();
			if (StringUtils.isEmpty(str))
				return null;
			return Boolean.valueOf(str);
		}
		if (localTemplateModel instanceof TemplateBooleanModel)
			return Boolean.valueOf(((TemplateBooleanModel) localTemplateModel)
					.getAsBoolean());
		throw new TemplateModelException("The \"" + paramString
				+ "\" parameter " + "must be a boolean.");
	}

	public static Date IIIIIIll(String paramString,
			Map<String, TemplateModel> paramMap)  throws TemplateModelException {
		TemplateModel localTemplateModel = (TemplateModel) paramMap
				.get(paramString);
		if (localTemplateModel == null)
			return null;
		if (localTemplateModel instanceof TemplateScalarModel) {
			String str = ((TemplateScalarModel) localTemplateModel)
					.getAsString();
			if (StringUtils.isEmpty(str))
				return null;
			try {
				String[] arrayOfString = { "yyyy-MM", "yyyyMM", "yyyy/MM",
						"yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd",
						"yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss",
						"yyyy/MM/dd HH:mm:ss" };
				return DateUtils.parseDate(str, arrayOfString);
			} catch (ParseException localParseException) {
				localParseException.printStackTrace();
				return null;
			}
		}
		if (localTemplateModel instanceof TemplateDateModel)
			return ((TemplateDateModel) localTemplateModel).getAsDate();
		throw new TemplateModelException("The \"" + paramString
				+ "\" parameter " + "must be a date.");
	}

	public static Object getObject(String paramString,
			Map<String, TemplateModel> paramMap)  throws TemplateModelException {
		TemplateModel localTemplateModel = (TemplateModel) paramMap
				.get(paramString);
		if (localTemplateModel == null)
			return null;
		try {
			return DeepUnwrap.unwrap(localTemplateModel);
		} catch (TemplateModelException localTemplateModelException) {
			throw new TemplateModelException("The \"" + paramString
					+ "\" parameter " + "must be a object.");
		}
	}
}
