package reportdao.querycondition;

import reportdao.QueryConditionDAO;

public class QueryCondition {
	
	public static String DEFAULT_CONDITION_VALUE = "-1";
	
	/**
	 * <pre>
	 * 表示查询条件为开始日期的键值
	 * value的格式：String，举例:"20130101"，表示开始日期的值为2013年1月1日。
	 * </pre>
	 */
	public static final String QUERY_KEY_BEGIN_DATE = "beginDate";
	/**
	 * <pre>
	 * 表示查询条件为结束日期的键值
	 * value的格式：String，举例:"20130101"，表示开始日期的值为2013年1月1日。
	 * </pre>
	 */
	public static final String QUERY_KEY_END_DATE = "endDate";
	/**
	 * <pre>
	 * 表示查询条件为游戏区ID的键值。
	 * 注：游戏区ID的值列表从服务器获取，参考方法{@link QueryConditionDAO#getAllGameServerIDConditionData()}
	 * value的格式：String，举例:"1",表示游戏区ID为1
	 * </pre>
	 */
	public static final String QUERY_KEY_SERVER_ID = "server";
	
	
	/**
	 * <pre>
	 * 游戏渠道
	 * </pre>
	 */
	public static final String QUERY_KEY_PARENT_PROMO = "channel";
	
	/**
	 * <pre>
	 * 游戏渠道
	 * </pre>
	 */
	public static final String QUERY_KEY_SUB_PROMO = "subChannel";
	
	
	/**
	 * <pre>
	 * 游戏名
	 * </pre>
	 */
	public static final String QUERY_KEY_GAMES="games";
	
	
	/**
	 * <pre>
	 * 游戏状态
	 * </pre>
	 */
	public static final String QUERY_KEY_STATE="state";
	
	/**
	 * <pre>
	 * 游戏状态
	 * </pre>
	 */
	public static final String QUERY_KEY_USERTYPE="userType";

}
