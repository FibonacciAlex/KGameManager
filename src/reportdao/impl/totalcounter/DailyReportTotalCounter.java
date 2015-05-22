package reportdao.impl.totalcounter;

public class DailyReportTotalCounter {
	
	public int dayCount = 0;
 	
	/**
	 * 下载客户端数
	 * 
	 * @return
	 */
	public int downloadCount;
	/**
	 * 激活用户数(首次连上服务器的客户端数)
	 * 
	 * @return
	 */
	public int firstConnectUserCount;
	/**
	 * 新增用户(当天新增的注册账号数)
	 * 
	 * @return
	 */
	public int registerUserCount;
	/**
	 * 新增有角用户(当天新增注册并有角色的用户数)
	 * 
	 * @return
	 */
	public int registerCreateRoleUserCount;
	/**
	 * 新增有效用户(有角色且等级>3的新增用户)
	 * 
	 * @return
	 */
	public int registerEffectUserCount;
	/**
	 * 登陆用户（当天所有登录过的用户数）
	 * 
	 * @return
	 */
	public int loginUserCount;
	/**
	 * 次日留存用户（昨天注册的用户今天再次登录）
	 */
	public int keepUserCount;
	/**
	 * 次日留存率
	 */
	public float keepUserRate;
	/**
	 * 首充用户（在游戏中第一次充值的用户）
	 * 
	 * @return
	 */
	public int firstChargeUserCount;
	/**
	 * 充值用户(当天充值的用户数)
	 * 
	 * @return
	 */
	public int todayChargeUser;
	/**
	 * 充值金额(当天充值总额)
	 * 
	 * @return
	 */
	public int todayChargeMoney;
	/**
	 * 付费ARPU(当天消费金额/当天充值人数)
	 * 
	 * @return
	 */
	public float arpu;
	/**
	 * DAU(日登陆用户数/月登录用户数)
	 * 
	 * @return
	 */
	public float DAU;
	/**
	 * 活跃用户(当天登陆一次的用户，不包括新增用户)
	 * 
	 * @return
	 */
	public int activeUserCount;
	/**
	 * 活跃用户ARPU
	 * 
	 * @return
	 */
	public float activeArpu;
	/**
	 * 忠诚用户(连续三天登录的用户数)
	 * 
	 * @return
	 */
	public int loyalUserCount;
	/**
	 * 回流用户（当天前3天未登陆过游戏的旧注册账号）
	 * 
	 * @return
	 */
	public int comebackUserCount;
	/**
	 * 最高在线（当天在线的人数峰值）
	 * 
	 * @return
	 */
	public int highestOnlineCount;
	/**
	 * 平均在线（平均当天各时间段的在线用户数）
	 * 
	 * @return
	 */
	public int avgOnlineCount;
	/**
	 * 人均登录次数（当天每人平均登陆次数）
	 * 
	 * @return
	 */
	public float perUserAvgLoginCount;
	/**
	 * 人均时长（当天每人平均在线时长(单位:小时)）
	 * 
	 * @return
	 */
	public float perUserAvgOnlineTime;
	/**
	 * 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
	 * 
	 * @return
	 */
	public float activeUserLossRate;
	/**
	 * 付费渗透率（当天充值人数/当天登陆人数）
	 * 
	 * @return
	 */
	public float payRate;
	/**
	 * 创角率（新增用户/激活用户数）
	 * 
	 * @return
	 */
	public float createRoleRate;
	/**
	 * 周活跃用户(当天登陆一次的用户，不包括新增用户)
	 * 
	 * @return
	 */
	public int weekActiveUserCount;

}
