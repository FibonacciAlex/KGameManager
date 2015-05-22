package data.reportdatas;

import java.util.List;

public class ChargeRankReport implements Report {

	public List<ChargeRankReportData> chargeRankReportDataList;

	public ChargeRankReportData totalData;

	public List<ChargeRankReportData> getChargeRankReportDataList() {
		return chargeRankReportDataList;
	}

	public void setChargeRankReportDataList(
			List<ChargeRankReportData> chargeRankReportDataList) {
		this.chargeRankReportDataList = chargeRankReportDataList;
	}

	public ChargeRankReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(ChargeRankReportData totalData) {
		this.totalData = totalData;
	}

}
