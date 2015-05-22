package reportdao;

import java.util.Map;

import reportdao.querycondition.GameServerIDConditionData;
import reportdao.querycondition.QueryCondition;
import data.reportdatas.UserKeepOnlineReport;
import data.reportdatas.VIPReport;

public interface VIPReportDAO {
	

	/**
	 * <pre>
	 * 根据某天日期为条件查询游戏全部区的玩家VIP统计报表数据
	 * 有效的查询条件:
	 * {@link QueryCondition#QUERY_KEY_BEGIN_DATE}，表示某天日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_SERVER_ID}，游戏区ID，value格式：String,{@link GameServerIDConditionData#getServerId()}
	 * @param condition
	 * @return
	 * </pre>
	 */
	public VIPReport getVIPReport(Map<String,String> condition);


}
