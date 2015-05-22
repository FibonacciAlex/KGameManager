package data.reportdatas;

import java.util.List;

/**
 * <日常数据报表>界面的数据类型定义接口
 * @author zhaizl
 *
 */
public class DailyReport implements Report {
	
	/**
	 * 返回每天报表统计数据的合集
	 * @return
	 */
	public List<DailyReportData> getDailyReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public DailyReportData getTotalData;

	public List<DailyReportData> getGetDailyReportDataList() {
		return getDailyReportDataList;
	}

	public void setGetDailyReportDataList(
			List<DailyReportData> getDailyReportDataList) {
		this.getDailyReportDataList = getDailyReportDataList;
	}

	public DailyReportData getGetTotalData() {
		return getTotalData;
	}

	public void setGetTotalData(DailyReportData getTotalData) {
		this.getTotalData = getTotalData;
	}
	
	

}
