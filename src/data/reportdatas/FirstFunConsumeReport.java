package data.reportdatas;

import java.util.List;

public class FirstFunConsumeReport implements Report{
	/**
	 * 返回所有等级的首次使用钻石消耗统计数据的合集
	 * @return
	 */
	public List<FirstFunConsumeReportData> firstFunConsumeReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public FirstFunConsumeReportData totalData;

	public List<FirstFunConsumeReportData> getFirstFunConsumeReportDataList() {
		return firstFunConsumeReportDataList;
	}

	public void setFirstFunConsumeReportDataList(
			List<FirstFunConsumeReportData> firstFunConsumeReportDataList) {
		this.firstFunConsumeReportDataList = firstFunConsumeReportDataList;
	}

	public FirstFunConsumeReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(FirstFunConsumeReportData totalData) {
		this.totalData = totalData;
	}
	
	
}
