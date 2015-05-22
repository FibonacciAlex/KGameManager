package reportdao;

import java.util.Map;

import data.reportdatas.FirstChargeReport;
import data.reportdatas.FirstFunConsumeReport;

public interface FirstFunConsumeReportDAO {
	
   public final static String SORT_KEY_ROLE_COUNT = "roleCount";
	
	public final static String SORT_KEY_CONSUME_POINT= "consumePoint";
	
	public FirstFunConsumeReport getFirstFunConsumeReport(Map<String,String> condition);

}
