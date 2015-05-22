package reportdao;

import java.util.Map;

import reportdao.querycondition.GameServerIDConditionData;
import reportdao.querycondition.QueryCondition;
import data.reportdatas.RoleLossReport;

/**
 * <角色流失明细统计报表>的数据访问接口
 * 
 * @author zhaizl
 * 
 */
public interface RoleLossReportDAO {
	
	public final static String SORT_KEY_RANK = "rank";
	
	public final static String SORT_KEY_COMPLETE_COUNT = "completeCount";
	
	public final static String SORT_KEY_COMPLETE_RATE = "completeRate";
	
	public final static String SORT_KEY_MISSION_ID = "missionId";

	/**
	 * <pre>
	 * 查询游戏某个区的角色流失明细统计报表数据（此方法不能查询全部区的统计数据，只能查某个区）
	 * 有效的查询条件:
	 * {@link QueryCondition#QUERY_KEY_BEGIN_DATE}，表示开始日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_END_DATE}，结束日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_SERVER_ID}，游戏区ID，value格式：String,{@link GameServerIDConditionData#getServerId()}
	 * @param condition
	 * @return
	 * </pre>
	 */	
	public RoleLossReport getRoleLossReport(Map<String,String> condition);


}
