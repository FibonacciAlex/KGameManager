package data.reportdatas;

import java.util.List;

public class FamilyWarReport {
	/**
	 * 返回所有等级的首次使用钻石消耗统计数据的合集
	 * @return
	 */
	public List<FamilyWarReportData> familyWarReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public FamilyWarReportData totalData;

	public List<FamilyWarReportData> getFamilyWarReportDataList() {
		return familyWarReportDataList;
	}

	public void setFamilyWarReportDataList(
			List<FamilyWarReportData> familyWarReportDataList) {
		this.familyWarReportDataList = familyWarReportDataList;
	}

	public FamilyWarReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(FamilyWarReportData totalData) {
		this.totalData = totalData;
	}
}
