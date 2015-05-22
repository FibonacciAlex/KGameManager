package data.reportdatas;

/**
 * <日常数据报表>界面每日运营数据的数据类型定义接口
 * 
 * @author zhaizl
 * 
 */
public class DailyReportData {

	/**
	 * 表示是否合计的数据。返回true表示合计数据，返回false则表示其中某天的数据
	 * 
	 * @return
	 */
	public boolean isTotalData;
	
	public boolean isToday = false;

	/**
	 * 日报表统计的日期
	 * 
	 * @return
	 */
	public String reportDate;
	/**
	 * 下载客户端数
	 * 
	 * @return
	 */
	public String downloadCount;
	/**
	 * 激活用户数(首次连上服务器的客户端数)
	 * 
	 * @return
	 */
	public String firstConnectUserCount;
	/**
	 * 新增用户(当天新增的注册账号数)
	 * 
	 * @return
	 */
	public String registerUserCount;
	/**
	 * 新增有角用户(当天新增注册并有角色的用户数)
	 * 
	 * @return
	 */
	public String registerCreateRoleUserCount;
	/**
	 * 新增有效用户(有角色且等级>3的新增用户)
	 * 
	 * @return
	 */
	public String registerEffectUserCount;
	/**
	 * 登陆用户（当天所有登录过的用户数）
	 * 
	 * @return
	 */
	public String loginUserCount;
	/**
	 * 次日留存用户（昨天注册的用户今天再次登录）
	 */
	public String keepUserCount;
	/**
	 * 次日留存率
	 */
	public String keepUserRate;
	/**
	 * 首充用户（在游戏中第一次充值的用户）
	 * 
	 * @return
	 */
	public String firstChargeUserCount;
	/**
	 * 充值用户(当天充值的用户数)
	 * 
	 * @return
	 */
	public String todayChargeUser;
	/**
	 * 充值金额(当天充值总额)
	 * 
	 * @return
	 */
	public String todayChargeMoney;
	/**
	 * 付费ARPU(当天消费金额/当天充值人数)
	 * 
	 * @return
	 */
	public String arpu;
	/**
	 * DAU(日登陆用户数/月登录用户数)
	 * 
	 * @return
	 */
	public String DAU;
	/**
	 * 活跃用户(当天登陆一次的用户，不包括新增用户)
	 * 
	 * @return
	 */
	public String activeUserCount;
	/**
	 * 活跃用户ARPU
	 * 
	 * @return
	 */
	public String activeArpu;
	/**
	 * 忠诚用户(连续三天登录的用户数)
	 * 
	 * @return
	 */
	public String loyalUserCount;
	/**
	 * 回流用户（当天前3天未登陆过游戏的旧注册账号）
	 * 
	 * @return
	 */
	public String comebackUserCount;
	/**
	 * 最高在线（当天在线的人数峰值）
	 * 
	 * @return
	 */
	public String highestOnlineCount;
	/**
	 * 平均在线（平均当天各时间段的在线用户数）
	 * 
	 * @return
	 */
	public String avgOnlineCount;
	/**
	 * 人均登录次数（当天每人平均登陆次数）
	 * 
	 * @return
	 */
	public String perUserAvgLoginCount;
	/**
	 * 人均时长（当天每人平均在线时长(单位:小时)）
	 * 
	 * @return
	 */
	public String perUserAvgOnlineTime;
	/**
	 * 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
	 * 
	 * @return
	 */
	public String activeUserLossRate;
	/**
	 * 付费渗透率（当天充值人数/当天登陆人数）
	 * 
	 * @return
	 */
	public String payRate;
	/**
	 * 创角率（新增用户/激活用户数）
	 * 
	 * @return
	 */
	public String createRoleRate;
	
	/**
	 * 周活跃用户(最近7天内有2天登录或总登录时间>=2小时)
	 * 
	 * @return
	 */
	public String weekActiveUserCount;

	public boolean isTotalData() {
		return isTotalData;
	}

	public void setTotalData(boolean isTotalData) {
		this.isTotalData = isTotalData;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getFirstConnectUserCount() {
		return firstConnectUserCount;
	}

	public void setFirstConnectUserCount(String firstConnectUserCount) {
		this.firstConnectUserCount = firstConnectUserCount;
	}

	public String getRegisterUserCount() {
		return registerUserCount;
	}

	public void setRegisterUserCount(String registerUserCount) {
		this.registerUserCount = registerUserCount;
	}

	public String getRegisterCreateRoleUserCount() {
		return registerCreateRoleUserCount;
	}

	public void setRegisterCreateRoleUserCount(
			String registerCreateRoleUserCount) {
		this.registerCreateRoleUserCount = registerCreateRoleUserCount;
	}

	public String getRegisterEffectUserCount() {
		return registerEffectUserCount;
	}

	public void setRegisterEffectUserCount(String registerEffectUserCount) {
		this.registerEffectUserCount = registerEffectUserCount;
	}

	public String getLoginUserCount() {
		return loginUserCount;
	}

	public void setLoginUserCount(String loginUserCount) {
		this.loginUserCount = loginUserCount;
	}

	public String getFirstChargeUserCount() {
		return firstChargeUserCount;
	}

	public void setFirstChargeUserCount(String firstChargeUserCount) {
		this.firstChargeUserCount = firstChargeUserCount;
	}

	public String getTodayChargeUser() {
		return todayChargeUser;
	}

	public void setTodayChargeUser(String todayChargeUser) {
		this.todayChargeUser = todayChargeUser;
	}

	public String getTodayChargeMoney() {
		return todayChargeMoney;
	}

	public void setTodayChargeMoney(String todayChargeMoney) {
		this.todayChargeMoney = todayChargeMoney;
	}

	public String getArpu() {
		return arpu;
	}

	public void setArpu(String arpu) {
		this.arpu = arpu;
	}

	public String getDAU() {
		return DAU;
	}

	public void setDAU(String dAU) {
		DAU = dAU;
	}

	public String getActiveUserCount() {
		return activeUserCount;
	}

	public void setActiveUserCount(String activeUserCount) {
		this.activeUserCount = activeUserCount;
	}

	public String getActiveArpu() {
		return activeArpu;
	}

	public void setActiveArpu(String activeArpu) {
		this.activeArpu = activeArpu;
	}

	public String getLoyalUserCount() {
		return loyalUserCount;
	}

	public void setLoyalUserCount(String loyalUserCount) {
		this.loyalUserCount = loyalUserCount;
	}

	public String getComebackUserCount() {
		return comebackUserCount;
	}

	public void setComebackUserCount(String comebackUserCount) {
		this.comebackUserCount = comebackUserCount;
	}

	public String getHighestOnlineCount() {
		return highestOnlineCount;
	}

	public void setHighestOnlineCount(String highestOnlineCount) {
		this.highestOnlineCount = highestOnlineCount;
	}

	public String getAvgOnlineCount() {
		return avgOnlineCount;
	}

	public void setAvgOnlineCount(String avgOnlineCount) {
		this.avgOnlineCount = avgOnlineCount;
	}

	public String getPerUserAvgLoginCount() {
		return perUserAvgLoginCount;
	}

	public void setPerUserAvgLoginCount(String perUserAvgLoginCount) {
		this.perUserAvgLoginCount = perUserAvgLoginCount;
	}

	public String getPerUserAvgOnlineTime() {
		return perUserAvgOnlineTime;
	}

	public void setPerUserAvgOnlineTime(String perUserAvgOnlineTime) {
		this.perUserAvgOnlineTime = perUserAvgOnlineTime;
	}

	public String getActiveUserLossRate() {
		return activeUserLossRate;
	}

	public void setActiveUserLossRate(String activeUserLossRate) {
		this.activeUserLossRate = activeUserLossRate;
	}

	public String getPayRate() {
		return payRate;
	}

	public void setPayRate(String payRate) {
		this.payRate = payRate;
	}

	public String getCreateRoleRate() {
		return createRoleRate;
	}

	public void setCreateRoleRate(String createRoleRate) {
		this.createRoleRate = createRoleRate;
	}

	public String getKeepUserCount() {
		return keepUserCount;
	}

	public void setKeepUserCount(String keepUserCount) {
		this.keepUserCount = keepUserCount;
	}

	public String getKeepUserRate() {
		return keepUserRate;
	}

	public void setKeepUserRate(String keepUserRate) {
		this.keepUserRate = keepUserRate;
	}

	public String getWeekActiveUserCount() {
		return weekActiveUserCount;
	}

	public void setWeekActiveUserCount(String weekActiveUserCount) {
		this.weekActiveUserCount = weekActiveUserCount;
	}
	
	

}
