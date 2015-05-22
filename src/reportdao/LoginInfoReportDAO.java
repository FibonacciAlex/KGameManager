package reportdao;

import java.util.Map;

import data.reportdatas.LoginInfoReport;

public interface LoginInfoReportDAO {
	
	public LoginInfoReport getLoginInfoReport(Map<String, String> condition);

}
