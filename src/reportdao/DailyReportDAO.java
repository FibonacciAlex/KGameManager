package reportdao;

import java.util.Map;

import reportdao.querycondition.GameServerIDConditionData;
import reportdao.querycondition.QueryCondition;

import data.reportdatas.DailyReport;

/**
 * <日常数据报表>的数据访问接口
 * 
 * @author zhaizl
 * 
 */
public interface DailyReportDAO {
	
	public final static String SORT_KEY_REGISER_USER = "registerUserCount";
	
	public final static String SORT_KEY_REGISER_CREATE_ROLE_USER = "registerCreateRoleUserCount";
	
	public final static String SORT_KEY_REGISER_EFFECT_USER = "registerEffectUserCount";
	
	public final static String SORT_KEY_LOGIN_USER = "loginUserCount";
	
	public final static String SORT_KEY_KEEP_USER = "keepUserCount";
	
	public final static String SORT_KEY_KEEP_USER_RATE = "keepUserRate";
	
	public final static String SORT_KEY_FIRST_CHARGE_USER = "firstChargeUserCount";
	
	public final static String SORT_KEY_CHARGE_USER = "todayChargeUser";
	
	public final static String SORT_KEY_CHARGE_MONEY = "todayChargeMoney";
	
	public final static String SORT_KEY_ARPU = "arpu";
	
	public final static String SORT_KEY_ACTIVE_USER = "activeUserCount";
	
	public final static String SORT_KEY_ACTIVE_ARPU = "activeArpu";
	
	public final static String SORT_KEY_LOYAL_USER = "loyalUserCount";
	
	public final static String SORT_KEY_DAU = "DAU";
	
	public final static String SORT_KEY_COMBACK_USER = "comebackUserCount";
	
	public final static String SORT_KEY_HIGHEST_ONLINE = "highestOnlineCount";
	
	public final static String SORT_KEY_AVG_ONLINE = "avgOnlineCount";
	
	public final static String SORT_KEY_AVG_LOGIN = "perUserAvgLoginCount";
	
	public final static String SORT_KEY_AVG_ONLINE_TIME = "perUserAvgOnlineTime";
	
	public final static String SORT_KEY_ACTIVE_LOSS_RATE = "activeUserLossRate";
	
	public final static String SORT_KEY_PAY_RATE = "payRate";
	
	public final static String SORT_KEY_CREATE_ROLE_RATE = "createRoleRate";
	
	public final static String SORT_KEY_WEEK_ACTIVE_USER = "weekActiveUserCount";


	/**
	 * <pre>
	 * 查询游戏的每日总报表数据
	 * 有效的查询条件:
	 * {@link QueryCondition#QUERY_KEY_BEGIN_DATE}，表示开始日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_END_DATE}，结束日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_SERVER_ID}，游戏区ID，value格式：String,{@link GameServerIDConditionData#getServerId()}
	 * @param condition
	 * @return
	 * </pre>
	 */
	public DailyReport getDailyReport(Map<String,String> condition);
	
	


}
