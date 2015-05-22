package reportdao;

import java.util.Map;

import reportdao.querycondition.GameServerIDConditionData;
import reportdao.querycondition.QueryCondition;
import data.reportdatas.GamePointConsumeReport;

/**
 * <全服元宝消耗明细>的数据访问接口
 * 
 * @author zhaizl
 * 
 */
public interface GamePointConsumeReportDAO {
	
    public final static String SORT_KEY_CONSUME_COUNT = "consumeCount";
	
	public final static String SORT_KEY_CONSUME_POINT = "consumePoint";
	
	public final static String SORT_KEY_CONSUME_RATE = "consumeRate";
	

	/**
	 * <pre>
	 * 查询游戏全部区或者某个区的元宝功能消耗报表数据
	 * 有效的查询条件:
	 * {@link QueryCondition#QUERY_KEY_BEGIN_DATE}，表示开始日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_END_DATE}，结束日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_SERVER_ID}，游戏区ID，value格式：String,{@link GameServerIDConditionData#getServerId()}
	 * @param condition
	 * @return
	 * </pre>
	 */	
	public GamePointConsumeReport getGamePointConsumeReport(Map<String,String> condition);



}
