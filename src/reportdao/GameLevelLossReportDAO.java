package reportdao;

import java.util.Map;

import reportdao.querycondition.GameServerIDConditionData;
import reportdao.querycondition.QueryCondition;
import data.reportdatas.GameLevelLossReport;

public interface GameLevelLossReportDAO {
	/**
	 * <pre>
	 * 查询游戏某个区的关卡流水数据报表
	 * 有效的查询条件:
	 * {@link QueryCondition#QUERY_KEY_BEGIN_DATE}，表示开始日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_SERVER_ID}，游戏区ID，value格式：String,{@link GameServerIDConditionData#getServerId()}
	 * @param condition
	 * @return
	 * </pre>
	 */	
	public GameLevelLossReport getGameLevelLossReport(Map<String,String> condition);
}
