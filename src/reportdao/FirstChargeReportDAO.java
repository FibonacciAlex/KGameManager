package reportdao;

import java.util.Map;

import data.reportdatas.FirstChargeReport;

public interface FirstChargeReportDAO {
	
    public final static String SORT_KEY_FIRST_CHARGE_USER_COUNT = "firstChargeUserCount";
	
	public final static String SORT_KEY_TOTAL_CHARGE_COUNT = "totalChargeCount";
	
	public final static String SORT_KEY_CHARGE_USER_RATE = "chargeUserRate";
	
    public final static String SORT_KEY_TOTAL_FIRST_CHARGE_MONEY = "totalFirstChargeMoney";
	
	public final static String SORT_KEY_FIRST_CHARGE_MONEY_RATE = "firstChargeMoneyRate";
	
	public FirstChargeReport getFirstChargeReport(Map<String,String> condition);

}
