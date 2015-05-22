package reportdao;

import java.util.Map;

import data.reportdatas.ChargeRankReport;
import data.reportdatas.NewGuideReport;

public interface NewGuideReportDAO {
	
	public NewGuideReport getNewGuideReport(Map<String,String> condition);

}
