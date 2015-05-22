package reportdao;

import java.util.Map;

import data.reportdatas.ChargeRankReport;


public interface ChargeRankReportDAO {
	
	public ChargeRankReport getChargeRankReport(Map<String,String> condition);

}
