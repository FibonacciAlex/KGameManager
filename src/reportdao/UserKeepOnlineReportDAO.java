package reportdao;

import java.util.Map;

import reportdao.querycondition.GameServerIDConditionData;
import reportdao.querycondition.QueryCondition;
import data.reportdatas.ShopSalesReport;
import data.reportdatas.UserKeepOnlineReport;
/**
 * <玩家留存率统计报表>的数据访问接口
 * 
 * @author zhaizl
 * 
 */
public interface UserKeepOnlineReportDAO {
	

	/**
	 * <pre>
	 * 查询游戏全部区或者某个区的玩家留存率计报表数据
	 * 有效的查询条件:
	 * {@link QueryCondition#QUERY_KEY_BEGIN_DATE}，表示开始日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_END_DATE}，结束日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_SERVER_ID}，游戏区ID，value格式：String,{@link GameServerIDConditionData#getServerId()}
	 * @param condition
	 * @return
	 * </pre>
	 */
	public UserKeepOnlineReport getUserKeepOnlineReport(Map<String,String> condition);

}
