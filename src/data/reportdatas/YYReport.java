package data.reportdatas;

import java.util.List;

public class YYReport {
	/**
	 * 返回每天报表统计数据的合集
	 * @return
	 */
	public List<YYReportData> yyReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public YYReportData totalData;

	public List<YYReportData> getYYReportDataList() {
		return yyReportDataList;
	}

	public void setYYReportDataList(
			List<YYReportData> yyReportDataList) {
		this.yyReportDataList = yyReportDataList;
	}

	public YYReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(YYReportData totalData) {
		this.totalData = totalData;
	}
}
