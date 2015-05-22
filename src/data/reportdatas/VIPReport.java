package data.reportdatas;

import java.util.List;

/**
 * <VIP统计报表>界面的数据类型定义接口
 * @author zhaizl
 *
 */
public class VIPReport implements Report {
	
	/**
	 * 返回每天用户留存统计数据的合集
	 * @return
	 */
	public List<VIPReportData> getVIPReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public VIPReportData totalData;

	public List<VIPReportData> getGetVIPReportDataList() {
		return getVIPReportDataList;
	}

	public void setGetVIPReportDataList(List<VIPReportData> getVIPReportDataList) {
		this.getVIPReportDataList = getVIPReportDataList;
	}

	public VIPReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(VIPReportData totalData) {
		this.totalData = totalData;
	}
	
	

}
