package reportdao;

import java.util.Map;

import reportdao.querycondition.GameServerIDConditionData;
import reportdao.querycondition.QueryCondition;
import data.reportdatas.GamePointConsumeRankReport;
import data.reportdatas.GamePointStockRankReport;

public interface GamePointStockRankReportDAO {
	/**
	 * <pre>
	 * 查询游戏全部区或者某个区的元宝功能库存排行报表数据
	 * 有效的查询条件:
	 * {@link QueryCondition#QUERY_KEY_BEGIN_DATE}，表示开始日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_SERVER_ID}，游戏区ID，value格式：String,{@link GameServerIDConditionData#getServerId()}
	 * @param condition
	 * @return
	 * </pre>
	 */	
	public GamePointStockRankReport getGamePointStockRankReport(Map<String,String> condition);
}
