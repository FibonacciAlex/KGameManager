package data.reportdatas;

import java.util.List;

public class LoginInfoReport {
	/**
	 * 返回每天报表统计数据的合集
	 * @return
	 */
	public List<LoginInfoReportData> loginInfoReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public LoginInfoReportData totalData;

	public List<LoginInfoReportData> getLoginInfoReportDataList() {
		return loginInfoReportDataList;
	}

	public void setLoginInfoReportDataList(
			List<LoginInfoReportData> loginInfoReportDataList) {
		this.loginInfoReportDataList = loginInfoReportDataList;
	}

	public LoginInfoReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(LoginInfoReportData totalData) {
		this.totalData = totalData;
	}
}
