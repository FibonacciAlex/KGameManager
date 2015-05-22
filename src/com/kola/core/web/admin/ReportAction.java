package com.kola.core.web.admin;

import java.io.File;
import java.net.URL;

import javax.annotation.Resource;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.kola.core.data.User;
import com.kola.core.service.ReportSerive;
import com.kola.core.web.struts2.BaseAdminAction;

@SuppressWarnings("serial")
@ParentPackage("admin")
public class ReportAction extends BaseAdminAction<User> {

	@Resource
	ReportSerive reportSerive;
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void report() {
		Object object = reportSerive.getResult(getRequest(), getResponse());
		if (object instanceof String) {
			this.ajaxText(object == null ? "" : object.toString());
		}
	}

	public String index() {
		URL url = getClass().getResource("/");
		String file_dir = (url.getPath() + "../template/admin/").replaceAll("%20",
				" ");
		File file = new File(file_dir+"report_"+name+".ftl");
		if (file.exists()) {
			return name;
		}
		return "empty";
	}
}
