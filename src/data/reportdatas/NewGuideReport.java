package data.reportdatas;

import java.util.List;

public class NewGuideReport {
	
	public List<NewGuideReportData> newGuideReportDataList;

	public NewGuideReportData totalData;

	public List<NewGuideReportData> getNewGuideReportDataList() {
		return newGuideReportDataList;
	}

	public void setNewGuideReportDataList(
			List<NewGuideReportData> newGuideReportDataList) {
		this.newGuideReportDataList = newGuideReportDataList;
	}

	public NewGuideReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(NewGuideReportData totalData) {
		this.totalData = totalData;
	}

}
