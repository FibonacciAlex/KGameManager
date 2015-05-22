package reportdao;

import java.util.Map;

import data.reportdatas.FamilyWarReport;

public interface FamilyWarReportDAO {
	
	public FamilyWarReport getFamilyWarReport(Map<String,String> condition);
}
