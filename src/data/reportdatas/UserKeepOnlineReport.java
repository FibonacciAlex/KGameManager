package data.reportdatas;

import java.util.List;

/**
 * <玩家留存率统计报表>界面的数据类型定义接口
 * @author zhaizl
 *
 */
public class UserKeepOnlineReport implements Report {
	
	/**
	 * 返回每天用户留存统计数据的合集
	 * @return
	 */
	public List<UserKeepOnlineReportData> getUserKeepOnlineReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public UserKeepOnlineReportData totalData;

	public List<UserKeepOnlineReportData> getGetUserKeepOnlineReportDataList() {
		return getUserKeepOnlineReportDataList;
	}

	public void setGetUserKeepOnlineReportDataList(
			List<UserKeepOnlineReportData> getUserKeepOnlineReportDataList) {
		this.getUserKeepOnlineReportDataList = getUserKeepOnlineReportDataList;
	}

	public UserKeepOnlineReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(UserKeepOnlineReportData totalData) {
		this.totalData = totalData;
	}
	
	

}
