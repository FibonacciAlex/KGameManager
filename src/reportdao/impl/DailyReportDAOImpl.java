package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import data.reportdatas.DailyReport;
import data.reportdatas.DailyReportData;
import data.reportdatas.Report;
import reportdao.DailyReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.reportCache.ReportCacheManager;
import reportdao.impl.totalcounter.DailyReportTotalCounter;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;
import reportdao.util.DateUtil;

public class DailyReportDAOImpl implements DailyReportDAO {
	private static final KGameLogger logger = KGameLogger
			.getLogger(DailyReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;
	private DefineDataSourceManagerIF platformPool;

	private static Map<String, Boolean> sortKeyMap;

	public DailyReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
		initSortKeyMap();

	}

	private static void initSortKeyMap() {
		if (sortKeyMap == null) {
			sortKeyMap = new HashMap<String, Boolean>();
			sortKeyMap.put(DailyReportDAO.SORT_KEY_ACTIVE_ARPU, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_ACTIVE_LOSS_RATE, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_ACTIVE_USER, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_ARPU, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_AVG_LOGIN, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_AVG_ONLINE, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_AVG_ONLINE_TIME, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_CHARGE_MONEY, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_CHARGE_USER, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_COMBACK_USER, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_CREATE_ROLE_RATE, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_DAU, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_FIRST_CHARGE_USER, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_HIGHEST_ONLINE, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_KEEP_USER, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_KEEP_USER_RATE, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_LOGIN_USER, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_LOYAL_USER, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_PAY_RATE, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_REGISER_CREATE_ROLE_USER,
					true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_REGISER_EFFECT_USER, true);
			sortKeyMap.put(DailyReportDAO.SORT_KEY_REGISER_USER, true);
		}
	}

	// @Override
	// public DailyReport getDailyReport(Map<String, String> condition) {
	// // TODO Auto-generated method stub
	//
	// // 以下是参考的假数据做法
	//
	// // 解释condition条件的开始日期
	// String beginDate = condition.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
	// // 解释condition条件的结束日期
	// String endDate = condition.get(QueryCondition.QUERY_KEY_END_DATE);
	// // 解释condition条件的游戏区ID
	// int serverId = 0;
	// if (condition.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
	// serverId = Integer.parseInt(condition
	// .get(QueryCondition.QUERY_KEY_SERVER_ID));
	// }
	//
	// // 根据endDate与beginDate的差值计算需要查询的天数（如果endDate与beginDate相同，即查询天数为1）
	// int dateCount = countDays(beginDate, endDate) + 1;
	//
	// if (dateCount > 0) {
	// DailyReport report = new DailyReport();
	// report.setGetDailyReportDataList(new ArrayList<DailyReportData>());
	// for (int i = 0; i < dateCount; i++) {
	// DailyReportData data = new DailyReportData();
	// data.setTotalData(false);
	// data.setReportDate(getNextDates(beginDate,i));
	// // 下载量
	// int downloadCount = random(8000, 18000);
	// data.setDownloadCount(downloadCount + "");
	// // 激活用户数(首次连上服务器的客户端数)
	// int firstConnectUserCount = random((downloadCount - 2000),
	// downloadCount);
	// data.setFirstConnectUserCount(firstConnectUserCount + "");
	// // 新增用户(当天新增的注册账号数)
	// int registerUserCount = random((firstConnectUserCount - 1000),
	// firstConnectUserCount);
	// data.setRegisterUserCount(registerUserCount + "");
	// // 新增有角用户(当天新增注册并有角色的用户数)
	// int registerCreateRoleUserCount = random(
	// (registerUserCount - 500), registerUserCount);
	// data.setRegisterCreateRoleUserCount(registerCreateRoleUserCount
	// + "");
	// // 新增有效用户(有角色且等级>3的新增用户)
	// int registerEffectUserCount = random(
	// (registerCreateRoleUserCount - 300),
	// registerCreateRoleUserCount);
	// data.setRegisterEffectUserCount(registerEffectUserCount + "");
	// // 登陆用户（当天所有登录过的用户数）
	// int loginUserCount = random(40000, 60000);
	// data.setLoginUserCount(loginUserCount + "");
	// // 首充用户（在游戏中第一次充值的用户）
	// int firstChargeUserCount = random(200, 500);
	// data.setFirstChargeUserCount(firstChargeUserCount + "");
	// // 充值用户(当天充值的用户数)
	// int todayChargeUser = random(800, 1200);
	// data.setTodayChargeUser(todayChargeUser + "");
	// // 付费ARPU(当天消费金额/当天充值人数)
	// float arpu = (float) random(150, 250);
	// data.setArpu(getFloatString(arpu));
	// // 充值金额(当天充值总额)
	// float todayChargeMoney = todayChargeUser * arpu;
	// data.setTodayChargeMoney(getFloatString(todayChargeMoney));
	// // DAU(日登陆用户数/月登录用户数)
	// float DAU = ((float) random(1000, 2000)) / 100;
	// data.setDAU(getFloatString(DAU) + "%");
	// // 活跃用户(当天登陆一次的用户，不包括新增用户)
	// int activeUserCount = random((loginUserCount
	// - registerCreateRoleUserCount - 300), loginUserCount
	// - registerCreateRoleUserCount);
	// data.setActiveUserCount(activeUserCount + "");
	// // 活跃用户ARPU
	// float activeArpu = (float) random(200, 300);
	// data.setActiveArpu(getFloatString(activeArpu));
	// // 忠诚用户(连续三天登录的用户数)
	// int loyalUserCount = random((loginUserCount / 2 - 2000),
	// loginUserCount / 2);
	// data.setLoyalUserCount(loyalUserCount + "");
	// // 回流用户（当天前3天未登陆过游戏的旧注册账号）
	// int comebackUserCount = random((loginUserCount / 2 - 2000),
	// loginUserCount / 2);
	// data.setLoyalUserCount(loyalUserCount + "");
	// data.setComebackUserCount(comebackUserCount + "");
	// // 最高在线（当天在线的人数峰值）
	// int highestOnlineCount = random(10000, 15000);
	// data.setHighestOnlineCount(highestOnlineCount + "");
	// // 平均在线（平均当天各时间段的在线用户数）
	// int avgOnlineCount = random((highestOnlineCount / 4 * 3 - 500),
	// highestOnlineCount / 4 * 3);
	// data.setAvgOnlineCount(avgOnlineCount + "");
	// // 人均登录次数（当天每人平均登陆次数）
	// float perUserAvgLoginCount = ((float) random(250, 450)) / 100;
	// data.setPerUserAvgLoginCount(getFloatString(perUserAvgLoginCount));
	// // 人均时长（当天每人平均在线时长(单位:小时)）
	// float perUserAvgOnlineTime = ((float) random(30, 100)) / 100;
	// data.setPerUserAvgOnlineTime(getFloatString(perUserAvgOnlineTime));
	// // 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
	// float activeUserLossRate = ((float) random(1500, 3000)) / 100;
	// data.setActiveUserLossRate(getFloatString(activeUserLossRate)
	// + "%");
	// // 付费渗透率（当天充值人数/当天登陆人数）
	// float payRate = ((float) random(200, 350)) / 100;
	// data.setPayRate(getFloatString(payRate) + "%");
	// // 创角率（新增用户/激活用户数）
	// float createRoleRate = ((float) registerCreateRoleUserCount / (float)
	// firstConnectUserCount) * 100;
	// data.setCreateRoleRate(getFloatString(createRoleRate) + "%");
	//
	// report.getDailyReportDataList.add(data);
	// }
	//
	// return report;
	// } else {
	// return null;
	// }
	//
	// }

	@Override
	public DailyReport getDailyReport(Map<String, String> condition) {

		for (String key : condition.keySet()) {
			logger.error("getDailyReport condition,key:" + key + ",value:"
					+ condition.get(key));
		}

		// 解释condition条件的开始日期
		String beginDateStr = condition
				.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		// 解释condition条件的结束日期
		String endDateStr = condition.get(QueryCondition.QUERY_KEY_END_DATE);
		// 解释condition条件的游戏区ID
		int serverId = -1;
		if (condition.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
			serverId = Integer.parseInt(condition
					.get(QueryCondition.QUERY_KEY_SERVER_ID));
		}
		int beginDate = Integer.parseInt(beginDateStr);
		int endDate = Integer.parseInt(endDateStr);

		// 解释condition条件的推广渠道ID
		int parentPromoId = -1;
		if (condition.containsKey(QueryCondition.QUERY_KEY_PARENT_PROMO)) {
			parentPromoId = Integer.parseInt(condition
					.get(QueryCondition.QUERY_KEY_PARENT_PROMO));
		}

		// 解释condition条件的推广子渠道ID
		int subPromoId = -1;
		if (condition.containsKey(QueryCondition.QUERY_KEY_SUB_PROMO)) {
			subPromoId = Integer.parseInt(condition
					.get(QueryCondition.QUERY_KEY_SUB_PROMO));
		}

		// if(ReportCacheManager.isReportExist(Report.REPORT_TYPE_DAILY,
		// condition)){
		// DailyReport report =
		// (DailyReport)(ReportCacheManager.getReport(Report.REPORT_TYPE_DAILY,
		// condition));
		// return sortReport(report, condition.get("sortname"),
		// condition.get("sortorder"));
		// }

		// 根据endDate与beginDate的差值计算需要查询的天数（如果endDate与beginDate相同，即查询天数为1）
		int dateCount = DateUtil.countDays(beginDate, endDate) + 1;

		if (dateCount > 0) {
			//
			// serverId = 188;
			// promoId = 1002;
			//
			DailyReport report = null;
			if (serverId == -1 && parentPromoId == -1) {
				report = getDailyReportData(beginDate, endDate);
			} else if (serverId > -1 && parentPromoId == -1) {
				report = getDailyReportDataByServer(beginDate, endDate,
						serverId);
			} else if (serverId == -1 && parentPromoId > -1) {
				report = getDailyReportDataByPromo(beginDate, endDate,
						parentPromoId, subPromoId);
			} else {
				report = getDailyReportDataByServerAndPromo(beginDate, endDate,
						serverId, parentPromoId, subPromoId);
			}
			// logger.error("List<DailyReportData>:::size:"+result.size());
			// report.setGetDailyReportDataList(result);
			if (report.getGetTotalData() == null) {
				report.setGetTotalData(processDefaultNullTotalData());
			}

			// return report;

			ReportCacheManager.addReport(Report.REPORT_TYPE_DAILY, condition,
					report);

			return sortReport(report, condition.get("sortname"),
					condition.get("sortorder"));
		}

		return null;
	}

	private DailyReport getDailyReportData(int beginDate, int endDate) {
		String selectSql = "select * from daily_report where record_date>=? and record_date<=? order by record_date";

		DailyReport report = new DailyReport();

		List<DailyReportData> list = new ArrayList<DailyReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				DailyReportTotalCounter counter = new DailyReportTotalCounter();
				rs.beforeFirst();
				while (rs.next()) {
					counter.dayCount++;
					DailyReportData data = new DailyReportData();
					data.setTotalData(false);
					data.setReportDate(rs.getInt("record_date") + "");
					// 下载量
					int download_count = rs.getInt("download_count");
					counter.downloadCount += download_count;
					data.setDownloadCount(download_count + "");
					// 激活用户数(首次连上服务器的客户端数)
					int first_connect_count = rs.getInt("first_connect_count");
					counter.firstConnectUserCount += first_connect_count;
					data.setFirstConnectUserCount(first_connect_count + "");
					// 新增用户(当天新增的注册账号数)
					int register_count = rs.getInt("register_count");
					counter.registerUserCount += register_count;
					data.setRegisterUserCount(register_count + "");
					// 新增有角用户(当天新增注册并有角色的用户数)
					int register_role_count = rs.getInt("register_role_count");
					counter.registerCreateRoleUserCount += register_role_count;
					data.setRegisterCreateRoleUserCount(register_role_count
							+ "");
					// 新增有效用户(有角色且等级>3的新增用户)
					int register_effect_count = rs
							.getInt("register_effect_count");
					counter.registerEffectUserCount += register_effect_count;
					data.setRegisterEffectUserCount(register_effect_count + "");
					// 登陆用户（当天所有登录过的用户数）
					data.setLoginUserCount(rs.getInt("login_count") + "");
					// 次日留存用户（昨天注册的用户今天再次登录）
					int keep_user_count = rs.getInt("keep_user_count");
					counter.keepUserCount += keep_user_count;
					data.setKeepUserCount(keep_user_count + "");
					// 次日留存率
					data.setKeepUserRate(getFloatString(rs
							.getFloat("keep_user_rate")) + "%");
					// 首充用户（在游戏中第一次充值的用户）
					int first_charge_user_count = rs
							.getInt("first_charge_user_count");
					counter.firstChargeUserCount += first_charge_user_count;
					data.setFirstChargeUserCount(first_charge_user_count + "");
					// 充值用户(当天充值的用户数)
					int charge_user_count = rs.getInt("charge_user_count");
					data.setTodayChargeUser(charge_user_count + "");
					// 付费ARPU(当天消费金额/当天充值人数)
					data.setArpu(rs.getFloat("arpu") + "");
					// 充值金额(当天充值总额)
					int charge_money = rs.getInt("charge_money");
					counter.todayChargeMoney += charge_money;
					data.setTodayChargeMoney(getFloatString((float) (charge_money))
							+ "");
					// DAU(日登陆用户数/月登录用户数)
					data.setDAU(getFloatString(0f) + "%");
					// 活跃用户(当天登陆一次的用户，不包括新增用户)
					int active_user_count = rs.getInt("active_user_count");
					counter.activeUserCount += active_user_count;
					data.setActiveUserCount(active_user_count + "");
					// 活跃用户ARPU
					data.setActiveArpu(getFloatString(rs
							.getFloat("active_arpu")));
					// 忠诚用户(连续三天登录的用户数)
					int loyal_user_count = rs.getInt("loyal_user_count");
					counter.loyalUserCount += loyal_user_count;
					data.setLoyalUserCount(loyal_user_count + "");
					// 回流用户（当天前3天未登陆过游戏的旧注册账号）
					int come_back_user_count = rs
							.getInt("come_back_user_count");
					counter.comebackUserCount += come_back_user_count;
					data.setComebackUserCount(rs.getInt("come_back_user_count")
							+ "");
					// 最高在线（当天在线的人数峰值）
					int highest_online_count = rs
							.getInt("highest_online_count");
					if (counter.highestOnlineCount < highest_online_count) {
						counter.highestOnlineCount = highest_online_count;
					}
					data.setHighestOnlineCount(highest_online_count + "");
					// 平均在线（平均当天各时间段的在线用户数）
					int avg_online_count = rs.getInt("avg_online_count");
					counter.avgOnlineCount += avg_online_count;
					data.setAvgOnlineCount(avg_online_count + "");
					// 人均登录次数（当天每人平均登陆次数）
					float avg_login_count = rs.getFloat("avg_login_count");
					counter.perUserAvgLoginCount += avg_login_count;
					data.setPerUserAvgLoginCount(getFloatString(avg_login_count));
					// 人均时长（当天每人平均在线时长(单位:小时)）
					float avg_online_time = rs.getFloat("avg_online_time");
					counter.perUserAvgOnlineTime += avg_online_time;
					data.setPerUserAvgOnlineTime(getFloatString(avg_online_time));
					// 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
					float active_user_loss_rate = rs
							.getFloat("active_user_loss_rate");
					counter.activeUserLossRate += active_user_loss_rate;
					data.setActiveUserLossRate(getFloatString(active_user_loss_rate)
							+ "%");
					// 付费渗透率（当天充值人数/当天登陆人数）
					data.setPayRate(getFloatString(rs.getFloat("pay_rate"))
							+ "%");
					// 创角率（新增用户/激活用户数）
					data.setCreateRoleRate(getFloatString(rs
							.getFloat("create_role_rate")) + "%");

					// 周活跃用户数：最近7天内有2天登录或总登录时间>=2小时
					data.setWeekActiveUserCount(rs
							.getInt("week_active_user_count") + "");

					logger.error(
							"record_date:{},register_count:{},login_count:()",
							data.reportDate, data.registerUserCount,
							data.loginUserCount);

					list.add(data);
				}

				processDailyReportCounter(counter, beginDate, endDate, -1, -1,
						-1);

				processDAU(counter, list);

				int nowDate = DateUtil.getNowDate();
				if (beginDate <= nowDate && nowDate <= endDate) {
					list.add(getTodayData(-1, -1, -1));
				}

				DailyReportData totalData = processTotalData(counter);
				report.setGetTotalData(totalData);
				report.setGetDailyReportDataList(list);
			} else {
				int nowDate = DateUtil.getNowDate();
				if (beginDate <= nowDate && nowDate <= endDate) {
					list.add(getTodayData1(-1, -1, -1));
					DailyReportData totalData = processTotalData(new DailyReportTotalCounter());
					report.setGetTotalData(totalData);
					report.setGetDailyReportDataList(list);
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getDailyReportDataList == null) {
			report.setGetDailyReportDataList(list);
		}

		return report;
	}

	private DailyReport getDailyReportDataByServerAndPromo(int beginDate,
			int endDate, int serverId, int parentPromoId, int subPromoId) {
		String selectSql = "select * from daily_report_server_promo where record_date>=? and record_date<=?";

		selectSql += " and server_id=" + serverId;

		selectSql += " and parent_promo_id=" + parentPromoId;

		if (subPromoId != -1) {
			selectSql += " and promo_id=" + subPromoId;
		}

		DailyReport report = new DailyReport();

		List<DailyReportData> list = new ArrayList<DailyReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				DailyReportTotalCounter counter = new DailyReportTotalCounter();
				rs.beforeFirst();
				while (rs.next()) {
					counter.dayCount++;
					DailyReportData data = new DailyReportData();
					data.setTotalData(false);
					data.setReportDate(rs.getInt("record_date") + "");
					// 下载量
					int download_count = rs.getInt("download_count");
					counter.downloadCount += download_count;
					data.setDownloadCount(download_count + "");
					// 激活用户数(首次连上服务器的客户端数)
					int first_connect_count = rs.getInt("first_connect_count");
					counter.firstConnectUserCount += first_connect_count;
					data.setFirstConnectUserCount(first_connect_count + "");
					// 新增用户(当天新增的注册账号数)
					int register_count = rs.getInt("register_count");
					counter.registerUserCount += register_count;
					data.setRegisterUserCount(register_count + "");
					// 新增有角用户(当天新增注册并有角色的用户数)
					int register_role_count = rs.getInt("register_role_count");
					counter.registerCreateRoleUserCount += register_role_count;
					data.setRegisterCreateRoleUserCount(register_role_count
							+ "");
					// 新增有效用户(有角色且等级>3的新增用户)
					int register_effect_count = rs
							.getInt("register_effect_count");
					counter.registerEffectUserCount += register_effect_count;
					data.setRegisterEffectUserCount(register_effect_count + "");
					// 登陆用户（当天所有登录过的用户数）
					data.setLoginUserCount(rs.getInt("login_count") + "");
					// 次日留存用户（昨天注册的用户今天再次登录）
					int keep_user_count = rs.getInt("keep_user_count");
					counter.keepUserCount += keep_user_count;
					data.setKeepUserCount(keep_user_count + "");
					// 次日留存率
					data.setKeepUserRate(getFloatString(rs
							.getFloat("keep_user_rate")) + "%");
					// 首充用户（在游戏中第一次充值的用户）
					int first_charge_user_count = rs
							.getInt("first_charge_user_count");
					counter.firstChargeUserCount += first_charge_user_count;
					data.setFirstChargeUserCount(first_charge_user_count + "");
					// 充值用户(当天充值的用户数)
					int charge_user_count = rs.getInt("charge_user_count");
					data.setTodayChargeUser(charge_user_count + "");
					// 付费ARPU(当天消费金额/当天充值人数)
					data.setArpu(rs.getFloat("arpu") + "");
					// 充值金额(当天充值总额)
					int charge_money = rs.getInt("charge_money");
					counter.todayChargeMoney += charge_money;
					data.setTodayChargeMoney(getFloatString((float) (charge_money))
							+ "");
					// DAU(日登陆用户数/月登录用户数)
					data.setDAU(getFloatString(0f) + "%");
					// 活跃用户(当天登陆一次的用户，不包括新增用户)
					int active_user_count = rs.getInt("active_user_count");
					counter.activeUserCount += active_user_count;
					data.setActiveUserCount(active_user_count + "");
					// 活跃用户ARPU
					data.setActiveArpu(getFloatString(rs
							.getFloat("active_arpu")));
					// 忠诚用户(连续三天登录的用户数)
					int loyal_user_count = rs.getInt("loyal_user_count");
					counter.loyalUserCount += loyal_user_count;
					data.setLoyalUserCount(loyal_user_count + "");
					// 回流用户（当天前3天未登陆过游戏的旧注册账号）
					int come_back_user_count = rs
							.getInt("come_back_user_count");
					counter.comebackUserCount += come_back_user_count;
					data.setComebackUserCount(rs.getInt("come_back_user_count")
							+ "");
					// 最高在线（当天在线的人数峰值）
					int highest_online_count = rs
							.getInt("highest_online_count");
					if (counter.highestOnlineCount < highest_online_count) {
						counter.highestOnlineCount = highest_online_count;
					}
					data.setHighestOnlineCount(highest_online_count + "");
					// 平均在线（平均当天各时间段的在线用户数）
					int avg_online_count = rs.getInt("avg_online_count");
					counter.avgOnlineCount += avg_online_count;
					data.setAvgOnlineCount(avg_online_count + "");
					// 人均登录次数（当天每人平均登陆次数）
					float avg_login_count = rs.getFloat("avg_login_count");
					counter.perUserAvgLoginCount += avg_login_count;
					data.setPerUserAvgLoginCount(getFloatString(avg_login_count));
					// 人均时长（当天每人平均在线时长(单位:小时)）
					float avg_online_time = rs.getFloat("avg_online_time");
					counter.perUserAvgOnlineTime += avg_online_time;
					data.setPerUserAvgOnlineTime(getFloatString(avg_online_time));
					// 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
					float active_user_loss_rate = rs
							.getFloat("active_user_loss_rate");
					counter.activeUserLossRate += active_user_loss_rate;
					data.setActiveUserLossRate(getFloatString(active_user_loss_rate)
							+ "%");
					// 付费渗透率（当天充值人数/当天登陆人数）
					data.setPayRate(getFloatString(rs.getFloat("pay_rate"))
							+ "%");
					// 创角率（新增用户/激活用户数）
					data.setCreateRoleRate(getFloatString(rs
							.getFloat("create_role_rate")) + "%");

					// 周活跃用户数：最近7天内有2天登录或总登录时间>=2小时
					int weekActiveUserCount = rs
							.getInt("week_active_user_count");
					counter.weekActiveUserCount += weekActiveUserCount;
					data.setWeekActiveUserCount(weekActiveUserCount + "");

					logger.error(
							"record_date:{},register_count:{},login_count:()",
							data.reportDate, data.registerUserCount,
							data.loginUserCount);

					list.add(data);
				}

				processDailyReportCounter(counter, beginDate, endDate,
						serverId, parentPromoId, subPromoId);

				processDAU(counter, list);

				int nowDate = DateUtil.getNowDate();
				if (beginDate <= nowDate && nowDate <= endDate) {
					list.add(getTodayData(parentPromoId, subPromoId, serverId));
				}

				DailyReportData totalData = processTotalData(counter);
				report.setGetTotalData(totalData);
				report.setGetDailyReportDataList(list);
			} else {
				int nowDate = DateUtil.getNowDate();
				if (beginDate <= nowDate && nowDate <= endDate) {
					list.add(getTodayData1(parentPromoId, subPromoId, serverId));
					DailyReportData totalData = processTotalData(new DailyReportTotalCounter());
					report.setGetTotalData(totalData);
					report.setGetDailyReportDataList(list);
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getDailyReportDataList == null) {
			report.setGetDailyReportDataList(list);
		}
		return report;
	}

	private DailyReport getDailyReportDataByServerAndPromo1(int beginDate,
			int endDate, int serverId, int parentPromoId, int subPromoId) {
		String selectSql = "select * from daily_report_server where record_date>=? and record_date<=?";

		selectSql += " and server_id=" + serverId;

		selectSql += " and parent_promo_id=" + parentPromoId;

		if (subPromoId != -1) {
			selectSql += " and promo_id=" + subPromoId;
		}

		DailyReport report = new DailyReport();

		List<DailyReportData> list = new ArrayList<DailyReportData>();

		Map<String, DailyReportDataStruct> map = new LinkedHashMap<String, DailyReportDAOImpl.DailyReportDataStruct>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DailyReportTotalCounter counter = new DailyReportTotalCounter();

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {

				rs.beforeFirst();
				while (rs.next()) {

					// DailyReportData data = new DailyReportData();
					// data.setTotalData(false);
					// data.setReportDate(rs.getInt("record_date") + "");
					int record_date = rs.getInt("record_date");
					String key = record_date + "-" + parentPromoId;
					if (subPromoId != -1) {
						key += "-" + subPromoId;
					}
					DailyReportDataStruct data = null;
					if (!map.containsKey(key)) {
						data = new DailyReportDataStruct(record_date, serverId,
								parentPromoId, subPromoId);
						map.put(key, data);
						counter.dayCount++;
					} else {
						data = map.get(key);
					}
					// 下载量
					int download_count = rs.getInt("download_count");
					data.downloadCount += download_count;
					// 激活用户数(首次连上服务器的客户端数)
					int first_connect_count = rs.getInt("first_connect_count");
					data.firstChargeUserCount += first_connect_count;
					// 新增用户(当天新增的注册账号数)
					int register_count = rs.getInt("register_count");
					data.registerUserCount += register_count;
					// 新增有角用户(当天新增注册并有角色的用户数)
					int register_role_count = rs.getInt("register_role_count");
					data.registerCreateRoleUserCount += register_role_count;

					// 新增有效用户(有角色且等级>3的新增用户)
					int register_effect_count = rs
							.getInt("register_effect_count");
					data.registerEffectUserCount += register_effect_count;
					// 登陆用户（当天所有登录过的用户数）
					int login_count = rs.getInt("login_count");
					data.loginUserCount += login_count;
					// 次日留存用户（昨天注册的用户今天再次登录）
					int keep_user_count = rs.getInt("keep_user_count");
					data.keepUserCount += keep_user_count;
					// 次日留存率
					float keepUserRate = rs.getFloat("keep_user_rate");
					data.keepUserRate += keepUserRate;
					data.keepUserRateCounter++;

					// 首充用户（在游戏中第一次充值的用户）
					int first_charge_user_count = rs
							.getInt("first_charge_user_count");
					data.firstChargeUserCount += first_charge_user_count;
					// 充值用户(当天充值的用户数)
					int charge_user_count = rs.getInt("charge_user_count");
					data.todayChargeUser += charge_user_count;
					// 充值金额(当天充值总额)
					int charge_money = rs.getInt("charge_money");
					data.todayChargeMoney += charge_money;
					// 活跃用户(当天登陆一次的用户，不包括新增用户)
					int active_user_count = rs.getInt("active_user_count");
					data.activeUserCount += active_user_count;
					// 忠诚用户(连续三天登录的用户数)
					int loyal_user_count = rs.getInt("loyal_user_count");
					data.loyalUserCount += loyal_user_count;
					// 回流用户（当天前3天未登陆过游戏的旧注册账号）
					int come_back_user_count = rs
							.getInt("come_back_user_count");
					data.comebackUserCount += come_back_user_count;
					// 最高在线（当天在线的人数峰值）
					int highest_online_count = rs
							.getInt("highest_online_count");
					if (data.highestOnlineCount < highest_online_count) {
						data.highestOnlineCount = highest_online_count;
					}
					// 平均在线（平均当天各时间段的在线用户数）
					int avg_online_count = rs.getInt("avg_online_count");
					if (data.avgOnlineCount < avg_online_count) {
						data.avgOnlineCount = avg_online_count;
					}

					// 人均登录次数（当天每人平均登陆次数）
					float avg_login_count = rs.getFloat("avg_login_count");
					counter.perUserAvgLoginCount += avg_login_count;
					if (data.perUserAvgLoginCount < avg_login_count) {
						data.perUserAvgLoginCount = avg_login_count;
					}
					// 人均时长（当天每人平均在线时长(单位:小时)）
					float avg_online_time = rs.getFloat("avg_online_time");
					if (data.perUserAvgOnlineTime < avg_online_time) {
						data.perUserAvgOnlineTime = avg_online_time;
					}
					// 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
					float active_user_loss_rate = rs
							.getFloat("active_user_loss_rate");
					data.active_user_loss_rate_counter++;
					data.activeUserLossRate += active_user_loss_rate;
					// // 付费渗透率（当天充值人数/当天登陆人数）
					// data.setPayRate(getFloatString(rs.getFloat("pay_rate"))
					// + "%");
					// // 创角率（新增用户/激活用户数）
					// data.setCreateRoleRate(getFloatString(rs
					// .getFloat("create_role_rate")) + "%");

					logger.error(
							"record_date:{},register_count:{},login_count:()",
							data.reportDate, data.registerUserCount,
							data.loginUserCount);

					// list.add(data);
				}

			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getDailyReportDataList == null) {
			report.setGetDailyReportDataList(list);
		}

		processDailyReportDataStruct(map, list, counter);

		processDailyReportCounter(counter, beginDate, endDate, serverId,
				parentPromoId, subPromoId);

		processDAU(counter, list);

		int nowDate = DateUtil.getNowDate();
		if (beginDate <= nowDate && nowDate <= endDate) {
			list.add(getTodayData(parentPromoId, subPromoId, serverId));
		}

		DailyReportData totalData = processTotalData(counter);
		report.setGetTotalData(totalData);
		report.setGetDailyReportDataList(list);
		return report;
	}

	private void processDailyReportDataStruct(
			Map<String, DailyReportDataStruct> map, List<DailyReportData> list,
			DailyReportTotalCounter counter) {
		for (DailyReportDataStruct dataStruct : map.values()) {
			counter.dayCount++;
			DailyReportData data = new DailyReportData();
			data.setTotalData(false);
			data.setReportDate(dataStruct.reportDate + "");
			// 下载量
			counter.downloadCount += dataStruct.downloadCount;
			data.setDownloadCount(dataStruct.downloadCount + "");
			// 激活用户数(首次连上服务器的客户端数)
			counter.firstConnectUserCount += dataStruct.firstConnectUserCount;
			data.setFirstConnectUserCount(dataStruct.firstConnectUserCount + "");
			// 新增用户(当天新增的注册账号数)
			counter.registerUserCount += dataStruct.registerUserCount;
			data.setRegisterUserCount(dataStruct.registerUserCount + "");
			// 新增有角用户(当天新增注册并有角色的用户数)
			counter.registerCreateRoleUserCount += dataStruct.registerCreateRoleUserCount;
			data.setRegisterCreateRoleUserCount(dataStruct.registerCreateRoleUserCount
					+ "");
			// 新增有效用户(有角色且等级>3的新增用户)
			counter.registerEffectUserCount += dataStruct.registerEffectUserCount;
			data.setRegisterEffectUserCount(dataStruct.registerEffectUserCount
					+ "");
			// 登陆用户（当天所有登录过的用户数）
			data.setLoginUserCount(dataStruct.loginUserCount + "");
			// 次日留存用户（昨天注册的用户今天再次登录）
			counter.keepUserCount += dataStruct.keepUserCount;
			data.setKeepUserCount(dataStruct.keepUserCount + "");
			// 次日留存率
			float keepUserRate = dataStruct.keepUserRate
					/ dataStruct.keepUserRateCounter;
			data.setKeepUserRate(getFloatString(keepUserRate) + "%");
			// 首充用户（在游戏中第一次充值的用户）
			counter.firstChargeUserCount += dataStruct.firstChargeUserCount;
			data.setFirstChargeUserCount(dataStruct.firstChargeUserCount + "");
			// 充值用户(当天充值的用户数)
			data.setTodayChargeUser(dataStruct.todayChargeUser + "");
			// 付费ARPU(当天消费金额/当天充值人数)
			data.setArpu(getFloatString((float) ((float) dataStruct.todayChargeMoney)
					/ dataStruct.todayChargeUser));
			// 充值金额(当天充值总额)
			counter.todayChargeMoney += dataStruct.todayChargeMoney;
			data.setTodayChargeMoney(getFloatString((float) (dataStruct.todayChargeMoney))
					+ "");
			// DAU(日登陆用户数/月登录用户数)
			data.setDAU(getFloatString(0f) + "%");
			// 活跃用户(当天登陆一次的用户，不包括新增用户)
			counter.activeUserCount += dataStruct.activeUserCount;
			data.setActiveUserCount(dataStruct.activeUserCount + "");
			// 活跃用户ARPU
			data.setActiveArpu(getFloatString((float) ((float) dataStruct.todayChargeMoney)
					/ dataStruct.activeUserCount));
			// 忠诚用户(连续三天登录的用户数)
			counter.loyalUserCount += dataStruct.loyalUserCount;
			data.setLoyalUserCount(dataStruct.loyalUserCount + "");
			// 回流用户（当天前3天未登陆过游戏的旧注册账号）
			counter.comebackUserCount += dataStruct.comebackUserCount;
			data.setComebackUserCount(dataStruct.comebackUserCount + "");
			// 最高在线（当天在线的人数峰值）
			int highest_online_count = dataStruct.highestOnlineCount;
			if (counter.highestOnlineCount < highest_online_count) {
				counter.highestOnlineCount = highest_online_count;
			}
			data.setHighestOnlineCount(highest_online_count + "");
			// 平均在线（平均当天各时间段的在线用户数）
			int avg_online_count = dataStruct.avgOnlineCount;
			counter.avgOnlineCount += avg_online_count;
			data.setAvgOnlineCount(avg_online_count + "");
			// 人均登录次数（当天每人平均登陆次数）
			float avg_login_count = dataStruct.perUserAvgLoginCount;
			counter.perUserAvgLoginCount += avg_login_count;
			data.setPerUserAvgLoginCount(getFloatString(avg_login_count));
			// 人均时长（当天每人平均在线时长(单位:小时)）
			float avg_online_time = dataStruct.perUserAvgOnlineTime;
			counter.perUserAvgOnlineTime += avg_online_time;
			data.setPerUserAvgOnlineTime(getFloatString(avg_online_time));
			// 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
			float active_user_loss_rate = dataStruct.activeUserLossRate
					/ dataStruct.active_user_loss_rate_counter;
			counter.activeUserLossRate += active_user_loss_rate;
			data.setActiveUserLossRate(getFloatString(active_user_loss_rate)
					+ "%");
			// 付费渗透率（当天充值人数/当天登陆人数）
			data.setPayRate(getFloatString((float) ((float) dataStruct.todayChargeUser)
					/ dataStruct.loginUserCount)
					+ "%");
			// 创角率（新增用户/激活用户数）
			data.setCreateRoleRate(getFloatString((float) ((float) dataStruct.registerCreateRoleUserCount)
					/ dataStruct.registerUserCount)
					+ "%");

			logger.error("record_date:{},register_count:{},login_count:()",
					data.reportDate, data.registerUserCount,
					data.loginUserCount);

			list.add(data);
		}
	}

	private Map<Integer, Integer> getYesterdayRegisterUser(int beginDate,
			int endDate, int serverId, int parentPromoId, int subPromoId) {
		Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		int bDate = DateUtil.getBeforeOrNextDates(beginDate, -1);
		int eDate = DateUtil.getBeforeOrNextDates(endDate, -1);
		String selectSql = "select record_date rDAte,sum(register_count) cc from daily_report_server where record_date>=? and record_date<=?";

		selectSql += " and server_id=" + serverId;

		selectSql += " and parent_promo_id=" + parentPromoId;

		if (subPromoId != -1) {
			selectSql += " and promo_id=" + subPromoId;
		}

		selectSql += " group by record_date order by record_date";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, bDate);
			ps.setInt(2, eDate);
			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					int record_date = rs.getInt("rDate");
					int date = DateUtil.getBeforeOrNextDates(record_date, 1);
					int regCount = rs.getInt("cc");
					if (!map.containsKey(record_date)) {
						map.put(date, regCount);
					}
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		return map;
	}

	private DailyReport getDailyReportDataByServer(int beginDate, int endDate,
			int serverId) {
		String selectPSql = "select distinct(record_date) rDate,sum(register_count) regCount,sum(active_user_count) activeCount"
				+ " from daily_report_server where record_date>=? and record_date<=? and server_id="
				+ serverId + " group by server_id,record_date";

		String selectSql = "select distinct(record_date) rDate,sum(download_count) dlCount,sum(first_connect_count) firCount,"
				+ "sum(register_count) regCount,sum(register_role_count) regRCount,sum(register_effect_count) regECount,"
				+ "sum(login_count) lnCount,sum(keep_user_count) keepCount,sum(first_charge_user_count) fChargeUserCount,"
				+ "sum(charge_money) chargeMoney,sum(charge_user_count) chargeUserCount,sum(active_user_count) activeCount,"
				+ "sum(loyal_user_count) loyal,sum(come_back_user_count) comeback,max(highest_online_count) hOnline,"
				+ "max(avg_online_count) avgOnline,max(avg_login_count) avgLogin,max(avg_online_time) avgOT,sum(week_active_user_count) weekActiveCount"
				+ " from daily_report_server where record_date>=? and record_date<=?";

		if (serverId != -1) {
			selectSql += " and server_id=" + serverId
					+ " group by server_id,record_date";
		}

		int yesterday = DateUtil.getBeforeOrNextDates(beginDate, -1);

		DailyReport report = new DailyReport();

		List<DailyReportData> list = new ArrayList<DailyReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = bgPool.getConnection();

			ps = con.prepareStatement(selectPSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, yesterday);
			ps.setInt(2, endDate);

			rs = bgPool.executeQuery(ps);

			HashMap<Integer, Integer> yesterdayRegMap = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> yesterdayActiveMap = new HashMap<Integer, Integer>();

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					int date = rs.getInt("rDate");
					int regCount = rs.getInt("regCount");
					int activeCount = rs.getInt("activeCount");
					yesterdayRegMap.put(date, regCount);
					yesterdayActiveMap.put(date, activeCount);
				}
			}
			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);

			Map<Integer, LoginInfo> loginMap = new HashMap<Integer, DailyReportDAOImpl.LoginInfo>();

			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				DailyReportTotalCounter counter = new DailyReportTotalCounter();
				rs.beforeFirst();
				while (rs.next()) {
					counter.dayCount++;
					DailyReportData data = new DailyReportData();
					data.setTotalData(false);
					data.setReportDate(rs.getInt("rDate") + "");
					// 下载量
					int download_count = rs.getInt("dlCount");
					counter.downloadCount += download_count;
					data.setDownloadCount(download_count + "");
					// 激活用户数(首次连上服务器的客户端数)
					int first_connect_count = rs.getInt("firCount");
					counter.firstConnectUserCount += first_connect_count;
					data.setFirstConnectUserCount(first_connect_count + "");
					// 新增用户(当天新增的注册账号数)
					int register_count = rs.getInt("regCount");
					counter.registerUserCount += register_count;
					data.setRegisterUserCount(register_count + "");
					// 新增有角用户(当天新增注册并有角色的用户数)
					int register_role_count = rs.getInt("regRCount");
					counter.registerCreateRoleUserCount += register_role_count;
					data.setRegisterCreateRoleUserCount(register_role_count
							+ "");
					// 新增有效用户(有角色且等级>3的新增用户)
					int register_effect_count = rs.getInt("regECount");
					counter.registerEffectUserCount += register_effect_count;
					data.setRegisterEffectUserCount(register_effect_count + "");
					// 登陆用户（当天所有登录过的用户数）
					int login_count = rs.getInt("lnCount");
					data.setLoginUserCount(login_count + "");
					// 次日留存用户（昨天注册的用户今天再次登录）
					int keepUserCount = rs.getInt("keepCount");
					counter.keepUserCount += keepUserCount;
					data.setKeepUserCount(keepUserCount + "");
					// 次日留存率
					int beforeDate = DateUtil.getBeforeOrNextDates(
							Integer.parseInt(data.reportDate), -1);
					if (yesterdayRegMap.containsKey(beforeDate)) {
						int beforeDateRegCount = yesterdayRegMap
								.get(beforeDate);
						float keepUserRate = 0f;
						if(beforeDateRegCount>0){
							keepUserRate = (float) (keepUserCount * 100)
							/ beforeDateRegCount;
						}
						data.setKeepUserRate(getFloatString(keepUserRate)
								+ "%");
					} else {
						data.setKeepUserRate(getFloatString(0f) + "%");
					}

					// 首充用户（在游戏中第一次充值的用户）
					int first_charge_user_count = rs.getInt("fChargeUserCount");
					counter.firstChargeUserCount += first_charge_user_count;
					data.setFirstChargeUserCount(first_charge_user_count + "");
					// 充值用户(当天充值的用户数)
					int chargeUserCount = rs.getInt("chargeUserCount");
					data.setTodayChargeUser(chargeUserCount + "");

					// 充值金额(当天充值总额)
					int chargeMoney = rs.getInt("chargeMoney");
					counter.todayChargeMoney += chargeMoney;
					data.setTodayChargeMoney(getFloatString((float) (chargeMoney))
							+ "");
					// 付费ARPU(当天消费金额/当天充值人数)
					if (chargeMoney != 0 && chargeUserCount != 0) {
						data.setArpu(getFloatString(((float) (chargeMoney))
								/ chargeUserCount));
					} else {
						data.setArpu(getFloatString(0f));
					}
					// DAU(日登陆用户数/月登录用户数)
					data.setDAU(getFloatString(0f) + "%");
					// 活跃用户(当天登陆一次的用户，不包括新增用户)
					int activeCount = rs.getInt("activeCount");
					counter.activeUserCount += activeCount;
					data.setActiveUserCount(activeCount + "");
					// 活跃用户ARPU
					if (chargeMoney != 0 && activeCount != 0) {
						data.setActiveArpu(getFloatString(((float) (chargeMoney))
								/ activeCount));
					} else {
						data.setActiveArpu(getFloatString(0f));
					}
					// 忠诚用户(连续三天登录的用户数)
					int loyalCount = rs.getInt("loyal");
					counter.loyalUserCount += loyalCount;
					data.setLoyalUserCount(loyalCount + "");
					// 回流用户（当天前3天未登陆过游戏的旧注册账号）
					int come_back_user_count = rs.getInt("comeback");
					counter.comebackUserCount += come_back_user_count;
					data.setComebackUserCount(come_back_user_count + "");
					// 最高在线（当天在线的人数峰值）
					int highest_online_count = rs.getInt("hOnline");
					if (counter.highestOnlineCount < highest_online_count) {
						counter.highestOnlineCount = highest_online_count;
					}
					data.setHighestOnlineCount(highest_online_count + "");
					// 平均在线（平均当天各时间段的在线用户数）
					int avg_online_count = rs.getInt("avgOnline");
					counter.avgOnlineCount += avg_online_count;
					data.setAvgOnlineCount(avg_online_count + "");
					// 人均登录次数（当天每人平均登陆次数）
					float avg_login_count = rs.getFloat("avgLogin");
					counter.perUserAvgLoginCount += avg_login_count;
					data.setPerUserAvgLoginCount(getFloatString(avg_login_count));
					// 人均时长（当天每人平均在线时长(单位:小时)）
					float avg_online_time = rs.getFloat("avgOT");
					counter.perUserAvgOnlineTime += avg_online_time;
					data.setPerUserAvgOnlineTime(getFloatString(avg_online_time));
					// 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
					if (yesterdayActiveMap.containsKey(beforeDate)
							&& yesterdayActiveMap.get(beforeDate) > 0) {
						int yesterdayActive = yesterdayActiveMap
								.get(beforeDate);
						float active_user_loss_rate = 0f;
						if(yesterdayActive>0){
							active_user_loss_rate = ((float) (yesterdayActive - loyalCount) * 100)
								/ yesterdayActive;
						}
						data.setActiveUserLossRate(getFloatString(active_user_loss_rate)
								+ "%");
						counter.activeUserLossRate += active_user_loss_rate;
					} else {
						data.setActiveUserLossRate(getFloatString(0f) + "%");
					}

					// 付费渗透率（当天充值人数/当天登陆人数）
					float payRate = 0f;
					if(login_count>0){
						payRate = ((float) (chargeUserCount * 100))
								/ login_count;
					}
					data.setPayRate(getFloatString(payRate)
							+ "%");
					// 创角率（新增用户/激活用户数）
					float createRoleRate = 0f;
					if(register_count>0){
						createRoleRate = ((float) (register_role_count * 100))
								/ register_count;
					}
					data.setCreateRoleRate(getFloatString(createRoleRate)
							+ "%");

					// 周活跃用户数：最近7天内有2天登录或总登录时间>=2小时
					int weekActiveUserCount = rs.getInt("weekActiveCount");
					counter.weekActiveUserCount += weekActiveUserCount;
					data.setWeekActiveUserCount(weekActiveUserCount + "");

					logger.error(
							"record_date:{},register_count:{},login_count:()",
							data.reportDate, data.registerUserCount,
							data.loginUserCount);

					list.add(data);
				}

				processDailyReportCounter(counter, beginDate, endDate,
						serverId, -1, -1);

				processDAU(counter, list);

				int nowDate = DateUtil.getNowDate();
				if (beginDate <= nowDate && nowDate <= endDate) {
					list.add(getTodayData(-1, -1, serverId));
				}

				DailyReportData totalData = processTotalData(counter);
				report.setGetTotalData(totalData);
				report.setGetDailyReportDataList(list);
			} else {
				int nowDate = DateUtil.getNowDate();
				if (beginDate <= nowDate && nowDate <= endDate) {
					list.add(getTodayData1(-1, -1, serverId));
					DailyReportData totalData = processTotalData(new DailyReportTotalCounter());
					report.setGetTotalData(totalData);
					report.setGetDailyReportDataList(list);
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getDailyReportDataList == null) {
			report.setGetDailyReportDataList(list);
		}
		return report;
	}

	private DailyReport getDailyReportDataByPromo(int beginDate, int endDate,
			int parentPromoId, int subPromoId) {
		String selectPSql = "select distinct(record_date) rDate,sum(register_count) regCount,sum(active_user_count) activeCount"
				+ " from daily_report_promo where record_date>=? and record_date<=?";
		if (subPromoId == -1) {
			selectPSql += " and parent_promo_id=" + parentPromoId
					+ " group by parent_promo_id,record_date";
		} else {
			selectPSql += " and parent_promo_id=" + parentPromoId
					+ " and promo_id=" + subPromoId
					+ " group by promo_id,record_date";
		}

		logger.error("##### selectPSql:" + selectPSql);

		String selectSql = "select distinct(record_date) rDate,sum(download_count) dlCount,sum(first_connect_count) firCount,"
				+ "sum(register_count) regCount,sum(register_role_count) regRCount,sum(register_effect_count) regECount,"
				+ "sum(login_count) lnCount,sum(keep_user_count) keepCount,sum(first_charge_user_count) fChargeUserCount,"
				+ "sum(charge_money) chargeMoney,sum(charge_user_count) chargeUserCount,sum(active_user_count) activeCount,"
				+ "sum(loyal_user_count) loyal,sum(come_back_user_count) comeback,max(highest_online_count) hOnline,"
				+ "max(avg_online_count) avgOnline,max(avg_login_count) avgLogin,max(avg_online_time) avgOT,sum(week_active_user_count) weekActiveCount"
				+ " from daily_report_promo where record_date>=? and record_date<=?";

		if (subPromoId == -1) {
			selectSql += " and parent_promo_id=" + parentPromoId
					+ " group by parent_promo_id,record_date";
		} else {
			selectSql += " and parent_promo_id=" + parentPromoId
					+ " and promo_id=" + subPromoId
					+ " group by promo_id,record_date";
		}

		int yesterday = DateUtil.getBeforeOrNextDates(beginDate, -1);

		DailyReport report = new DailyReport();

		List<DailyReportData> list = new ArrayList<DailyReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = bgPool.getConnection();

			ps = con.prepareStatement(selectPSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, yesterday);
			ps.setInt(2, endDate);

			rs = bgPool.executeQuery(ps);

			HashMap<Integer, Integer> yesterdayRegMap = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> yesterdayActiveMap = new HashMap<Integer, Integer>();

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					int date = rs.getInt("rDate");
					int regCount = rs.getInt("regCount");
					int activeCount = rs.getInt("activeCount");
					yesterdayRegMap.put(date, regCount);
					yesterdayActiveMap.put(date, activeCount);
				}
			}
			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);

			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				DailyReportTotalCounter counter = new DailyReportTotalCounter();
				rs.beforeFirst();
				while (rs.next()) {
					counter.dayCount++;
					DailyReportData data = new DailyReportData();
					data.setTotalData(false);
					data.setReportDate(rs.getInt("rDate") + "");
					// 下载量
					int download_count = rs.getInt("dlCount");
					counter.downloadCount += download_count;
					data.setDownloadCount(download_count + "");
					// 激活用户数(首次连上服务器的客户端数)
					int first_connect_count = rs.getInt("firCount");
					counter.firstConnectUserCount += first_connect_count;
					data.setFirstConnectUserCount(first_connect_count + "");
					// 新增用户(当天新增的注册账号数)
					int register_count = rs.getInt("regCount");
					counter.registerUserCount += register_count;
					data.setRegisterUserCount(register_count + "");
					// 新增有角用户(当天新增注册并有角色的用户数)
					int register_role_count = rs.getInt("regRCount");
					counter.registerCreateRoleUserCount += register_role_count;
					data.setRegisterCreateRoleUserCount(register_role_count
							+ "");
					// 新增有效用户(有角色且等级>3的新增用户)
					int register_effect_count = rs.getInt("regECount");
					counter.registerEffectUserCount += register_effect_count;
					data.setRegisterEffectUserCount(register_effect_count + "");
					// 登陆用户（当天所有登录过的用户数）
					int login_count = rs.getInt("lnCount");
					data.setLoginUserCount(login_count + "");
					// 次日留存用户（昨天注册的用户今天再次登录）
					int keepUserCount = rs.getInt("keepCount");
					counter.keepUserCount += keepUserCount;
					data.setKeepUserCount(keepUserCount + "");
					// 次日留存率
					int beforeDate = DateUtil.getBeforeOrNextDates(
							Integer.parseInt(data.reportDate), -1);
					if (yesterdayRegMap.containsKey(beforeDate)) {
						int beforeDateRegCount = yesterdayRegMap
								.get(beforeDate);
						float keepUserRate = 0f;
						if(beforeDateRegCount>0){
							keepUserRate = (float) (keepUserCount * 100)
							/ beforeDateRegCount;
						}
						data.setKeepUserRate(getFloatString(keepUserRate)
								+ "%");
					} else {
						data.setKeepUserRate(getFloatString(0f) + "%");
					}

					// 首充用户（在游戏中第一次充值的用户）
					int first_charge_user_count = rs.getInt("fChargeUserCount");
					counter.firstChargeUserCount += first_charge_user_count;
					data.setFirstChargeUserCount(first_charge_user_count + "");
					// 充值用户(当天充值的用户数)
					int chargeUserCount = rs.getInt("chargeUserCount");
					data.setTodayChargeUser(chargeUserCount + "");

					// 充值金额(当天充值总额)
					int chargeMoney = rs.getInt("chargeMoney");
					counter.todayChargeMoney += chargeMoney;
					data.setTodayChargeMoney(getFloatString((float) (chargeMoney))
							+ "");
					// 付费ARPU(当天消费金额/当天充值人数)
					if (chargeMoney != 0 && chargeUserCount != 0) {
						data.setArpu(getFloatString(((float) (chargeMoney))
								/ chargeUserCount));
					} else {
						data.setArpu(getFloatString(0f));
					}
					// DAU(日登陆用户数/月登录用户数)
					data.setDAU(getFloatString(0f) + "%");
					// 活跃用户(当天登陆一次的用户，不包括新增用户)
					int activeCount = rs.getInt("activeCount");
					counter.activeUserCount += activeCount;
					data.setActiveUserCount(activeCount + "");
					// 活跃用户ARPU
					if (chargeMoney != 0 && activeCount != 0) {
						data.setActiveArpu(getFloatString(((float) (chargeMoney))
								/ activeCount));
					} else {
						data.setActiveArpu(getFloatString(0f));
					}
					// 忠诚用户(连续三天登录的用户数)
					int loyalCount = rs.getInt("loyal");
					counter.loyalUserCount += loyalCount;
					data.setLoyalUserCount(loyalCount + "");
					// 回流用户（当天前3天未登陆过游戏的旧注册账号）
					int come_back_user_count = rs.getInt("comeback");
					counter.comebackUserCount += come_back_user_count;
					data.setComebackUserCount(come_back_user_count + "");
					// 最高在线（当天在线的人数峰值）
					int highest_online_count = rs.getInt("hOnline");
					if (counter.highestOnlineCount < highest_online_count) {
						counter.highestOnlineCount = highest_online_count;
					}
					data.setHighestOnlineCount(highest_online_count + "");
					// 平均在线（平均当天各时间段的在线用户数）
					int avg_online_count = rs.getInt("avgOnline");
					counter.avgOnlineCount += avg_online_count;
					data.setAvgOnlineCount(avg_online_count + "");
					// 人均登录次数（当天每人平均登陆次数）
					float avg_login_count = rs.getFloat("avgLogin");
					counter.perUserAvgLoginCount += avg_login_count;
					data.setPerUserAvgLoginCount(getFloatString(avg_login_count));
					// 人均时长（当天每人平均在线时长(单位:小时)）
					float avg_online_time = rs.getFloat("avgOT");
					counter.perUserAvgOnlineTime += avg_online_time;
					data.setPerUserAvgOnlineTime(getFloatString(avg_online_time));
					// 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
					if (yesterdayActiveMap.containsKey(beforeDate)
							&& yesterdayActiveMap.get(beforeDate) > 0) {
						int yesterdayActive = yesterdayActiveMap
								.get(beforeDate);
						float active_user_loss_rate = 0f;
						if(yesterdayActive>0){
							active_user_loss_rate = ((float) (yesterdayActive - loyalCount) * 100)
								/ yesterdayActive;
						}
						data.setActiveUserLossRate(getFloatString(active_user_loss_rate)
								+ "%");
						counter.activeUserLossRate += active_user_loss_rate;
					} else {
						data.setActiveUserLossRate(getFloatString(0f) + "%");
					}

					// 付费渗透率（当天充值人数/当天登陆人数）
					float payRate = 0f;
					if(login_count>0){
						payRate = ((float) (chargeUserCount * 100))
								/ login_count;
					}
					data.setPayRate(getFloatString(payRate)
							+ "%");
					// 创角率（新增用户/激活用户数）
					float createRoleRate = 0f;
					if(register_count>0){
						createRoleRate = ((float) (register_role_count * 100))
								/ register_count;
					}
					data.setCreateRoleRate(getFloatString(createRoleRate)
							+ "%");
					// 周活跃用户数：最近7天内有2天登录或总登录时间>=2小时
					int weekActiveUserCount = rs.getInt("weekActiveCount");
					counter.weekActiveUserCount += weekActiveUserCount;
					data.setWeekActiveUserCount(weekActiveUserCount + "");

					logger.error(
							"record_date:{},register_count:{},login_count:()",
							data.reportDate, data.registerUserCount,
							data.loginUserCount);

					list.add(data);
				}

				processDailyReportCounter(counter, beginDate, endDate, -1,
						parentPromoId, subPromoId);

				processDAU(counter, list);

				int nowDate = DateUtil.getNowDate();
				if (beginDate <= nowDate && nowDate <= endDate) {
					list.add(getTodayData(parentPromoId, subPromoId, -1));
				}

				DailyReportData totalData = processTotalData(counter);
				report.setGetTotalData(totalData);
				report.setGetDailyReportDataList(list);
			} else {
				int nowDate = DateUtil.getNowDate();
				if (beginDate <= nowDate && nowDate <= endDate) {
					list.add(getTodayData1(parentPromoId, subPromoId, -1));
					DailyReportData totalData = processTotalData(new DailyReportTotalCounter());
					report.setGetTotalData(totalData);
					report.setGetDailyReportDataList(list);
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getDailyReportDataList == null) {
			report.setGetDailyReportDataList(list);
		}
		return report;
	}

	private DailyReportData processTotalData(DailyReportTotalCounter counter) {
		DailyReportData data = new DailyReportData();
		data.setReportDate("合计");
		data.setDownloadCount(counter.downloadCount + "");
		data.setFirstConnectUserCount(counter.firstConnectUserCount + "");
		data.setRegisterUserCount(counter.registerUserCount + "");
		data.setRegisterCreateRoleUserCount(counter.registerCreateRoleUserCount
				+ "");
		data.setRegisterEffectUserCount(counter.registerEffectUserCount + "");
		data.setLoginUserCount(counter.loginUserCount + "");
		data.setKeepUserCount(counter.keepUserCount + "");
		data.setKeepUserRate(getFloatString(counter.keepUserRate) + "%");
		data.setActiveUserCount("－");
		data.setFirstChargeUserCount(counter.firstChargeUserCount + "");
		data.setTodayChargeMoney(getFloatString(((float) counter.todayChargeMoney)));
		data.setTodayChargeUser(counter.todayChargeUser + "");
		data.setArpu(getFloatString(counter.arpu));
		data.setActiveArpu("－");
		data.setLoyalUserCount("－");
		data.setComebackUserCount("－");
		data.setHighestOnlineCount(counter.highestOnlineCount + "");
		data.setAvgOnlineCount(counter.avgOnlineCount + "");
		data.setPerUserAvgLoginCount(getFloatString(counter.perUserAvgLoginCount));
		data.setPerUserAvgOnlineTime(getFloatString(counter.perUserAvgOnlineTime));
		data.setActiveUserLossRate(getFloatString(counter.activeUserLossRate)
				+ "%");
		data.setDAU(getFloatString(counter.DAU) + "%");
		data.setCreateRoleRate(getFloatString(counter.createRoleRate) + "%");
		data.setPayRate(getFloatString(counter.payRate) + "%");

		data.setTotalData(true);

		return data;
	}

	private void processDailyReportCounter(DailyReportTotalCounter counter,
			int beginDate, int endDate, int serverId, int parentPromoId,
			int subPromoId) {
		counter.loginUserCount = getTotalLoginCount(beginDate, endDate,
				serverId, parentPromoId, subPromoId);
		counter.todayChargeUser = getTotalChargeUserCount(beginDate, endDate,
				serverId, parentPromoId, subPromoId);
		counter.keepUserRate = 0f;
		if (counter.registerUserCount > 0) {
			counter.keepUserRate = (float) counter.keepUserCount * 100
					/ counter.registerUserCount;
		}
		if (counter.todayChargeMoney > 0 && counter.todayChargeUser > 0) {
			counter.arpu = (float) counter.todayChargeMoney
					/ counter.todayChargeUser;
		} else {
			counter.arpu = 0f;
		}
		if (counter.dayCount > 0) {
			counter.avgOnlineCount = counter.avgOnlineCount / counter.dayCount;
			counter.perUserAvgLoginCount = counter.perUserAvgLoginCount
					/ counter.dayCount;
			counter.perUserAvgOnlineTime = counter.perUserAvgOnlineTime
					/ counter.dayCount;
			counter.activeUserLossRate = counter.activeUserLossRate
					/ counter.dayCount;
		} else {
			counter.avgOnlineCount = 0;
			counter.perUserAvgLoginCount = 0;
			counter.perUserAvgOnlineTime = 0;
			counter.activeUserLossRate = 0;
		}

		if (counter.todayChargeUser > 0 && counter.loginUserCount > 0) {
			counter.payRate = (float) counter.todayChargeUser * 100
					/ counter.loginUserCount;
		} else {
			counter.payRate = 0f;
		}
		counter.createRoleRate = 0f;
		if (counter.registerUserCount > 0) {
			counter.createRoleRate = (float) counter.registerCreateRoleUserCount
					* 100 / counter.registerUserCount;
		}

	}

	private int getTotalLoginCount(int beginDate, int endDate, int serverId,
			int parentPromoId, int subPromoId) {
		String selectMinLoginId = "select min(min_login_record_id) minid from last_daily_record_collect_id where record_date >= "
				+ beginDate;
		String selectMaxLoginId = "select max(max_login_record_id) maxid from last_daily_record_collect_id where record_date <= "
				+ endDate;

		String selectLoginSql = "select count(distinct player_id) countId from login_record "
				+ "where id>=? and id<=?";
		if (serverId != -1) {
			selectLoginSql += " and server_id=" + serverId;
		}
		if (parentPromoId != -1) {
			selectLoginSql += " and parent_promo_id=" + parentPromoId;
		}

		if (subPromoId != -1) {
			selectLoginSql += " and promo_id=" + subPromoId;
		}
		logger.error("####getTotalLoginCount selectLoginSql:" + selectLoginSql);

		long minId = 0;
		long maxId = 0;
		int loginCount = 0;

		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			bgCon = bgPool.getConnection();

			ps = bgCon.prepareStatement(selectMinLoginId,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = bgPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				minId = rs.getLong("minid");
			}
			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);

			ps = bgCon.prepareStatement(selectMaxLoginId,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = bgPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				maxId = rs.getLong("maxid");
			}
			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);
			// /////////////////////////////////////////////
			if (maxId > 0) {
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectLoginSql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps.setLong(1, minId);
				ps.setLong(2, maxId);

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					loginCount = rs.getInt("countId");
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return loginCount;
	}

	private int getTotalChargeUserCount(int beginDate, int endDate,
			int serverId, int parentPromoId, int subPromoId) {
		String selectMinLoginId = "select min(min_charge_record_id) minid from last_daily_record_collect_id where record_date >= "
				+ beginDate;
		String selectMaxLoginId = "select max(max_charge_record_id) maxid from last_daily_record_collect_id where record_date <= "
				+ endDate;

		String selectLoginSql = "select count(distinct player_id) countId from charge_record "
				+ "where id>=? and id<=?";
		if (serverId != -1) {
			selectLoginSql += " and server_id=" + serverId;
		}
		if (parentPromoId != -1) {
			selectLoginSql += " and parent_promo_id=" + parentPromoId;
		}

		if (subPromoId != -1) {
			selectLoginSql += " and promo_id=" + subPromoId;
		}
		long minId = 0;
		long maxId = 0;
		int loginCount = 0;

		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			bgCon = bgPool.getConnection();

			ps = bgCon.prepareStatement(selectMinLoginId,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = bgPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				minId = rs.getLong("minid");
			}
			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);

			ps = bgCon.prepareStatement(selectMaxLoginId,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = bgPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				maxId = rs.getLong("maxid");
			}
			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);
			// /////////////////////////////////////////////
			if (maxId > 0) {
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectLoginSql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps.setLong(1, minId);
				ps.setLong(2, maxId);

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					loginCount = rs.getInt("countId");
				}
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return loginCount;
	}

	private DailyReportData getTodayData(int parentPromoId, int promoId,
			int serverId) {
		int nowDate = DateUtil.getNowDate();
		int endDate = DateUtil.getBeforeOrNextDates(nowDate, 1);
		int yesterday = DateUtil.getBeforeOrNextDates(nowDate, -1);

		long lastMinPlayerId = 0;
		long lastMaxPlayerId = 0;
		long lastLoginId = 0;
		long lastChargeId = 0;

		int todayRegisterCount = 0;
		int todayLoginCount = 0;
		int todayChargeUserCount = 0;
		int todayChargeMoney = 0;

		String selectIdSql = "select * from last_daily_record_collect_id where record_date="
				+ yesterday;

		// String selectGsIdSql =
		// "select * last_gs_collect_id where record_date="
		// + yesterday + " and server_id = " + serverId;

		String selectRegisterSql = "select count(player_id) cpid from player where player_id>? and register_time<"
				+ endDate;
		if (parentPromoId > -1) {
			selectRegisterSql += " and parent_promo_id = " + parentPromoId;
		}
		if (promoId > -1) {
			selectRegisterSql += " and promo_id = " + promoId;
		}
		if (serverId > -1) {
			selectRegisterSql += " and attribute like ?";
		}

		String selectLoginSql = "select count(distinct player_id) cpid from login_record where id>? and login_time<"
				+ endDate;
		if (parentPromoId > -1) {
			selectLoginSql += " and parent_promo_id = " + parentPromoId;
		}
		if (promoId > -1) {
			selectLoginSql += " and promo_id = " + promoId;
		}
		if (serverId > -1) {
			selectLoginSql += " and server_id = " + serverId;
		}

		String selectChargeSql = "select (sum(rmb)/100) srmb,count(distinct player_id) cpid from charge_record where id>? and charge_time<"
				+ endDate;
		if (parentPromoId > -1) {
			selectChargeSql += " and parent_promo_id = " + parentPromoId;
		}
		if (promoId > -1) {
			selectChargeSql += " and promo_id = " + promoId;
		}
		if (serverId > -1) {
			selectChargeSql += " and server_id = " + serverId;
		}

		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isSelectOK = false;

		try {
			bgCon = bgPool.getConnection();

			ps = bgCon.prepareStatement(selectIdSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = bgPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				lastMinPlayerId = rs.getLong("min_player_id");
				lastMaxPlayerId = rs.getLong("max_player_id");
				lastLoginId = rs.getLong("max_login_record_id");
				lastChargeId = rs.getLong("max_charge_record_id");
				isSelectOK = true;
				// logger.error("#######getTodayData::::isSelectOK:{}:{}:{}:{}:{}",isSelectOK,lastMinPlayerId,lastMaxPlayerId,lastLoginId,lastChargeId);
			}
			logger.error("#######getTodayData::::isSelectOK:{}:{}:{}:{}:{}",
					isSelectOK, lastMinPlayerId, lastMaxPlayerId, lastLoginId,
					lastChargeId);

			if (isSelectOK) {
				ps.clearParameters();
				ps.close();
				bgPool.closeResultSet(rs);

				// if (serverId > -1) {
				//
				// ps = bgCon.prepareStatement(selectGsIdSql,
				// ResultSet.TYPE_SCROLL_INSENSITIVE,
				// ResultSet.CONCUR_READ_ONLY);
				//
				// rs = bgPool.executeQuery(ps);
				//
				// if (rs != null && rs.next()) {
				// lastMaxRoleId = rs.getLong("max_role_id");
				// }
				// ps.clearParameters();
				// ps.close();
				// bgPool.closeResultSet(rs);
				// }
				// /////////////////////////////////////////////

				logger.error("#######getTodayData::::selectRegisterSql:::::"
						+ selectRegisterSql);
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectRegisterSql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps.setLong(1, lastMaxPlayerId);
				if (serverId > -1) {
					String attr = "%\"K2\":" + serverId + "%";
					logger.error("#######getTodayData:::::::::" + attr);
					ps.setString(2, attr);
				}

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					todayRegisterCount = rs.getInt("cpid");
					// logger.error("#######getTodayData:::::::::"+todayRegisterCount);
				}

				ps.clearParameters();
				ps.close();
				platformPool.closeResultSet(rs);

				// //////////////////////////////
				logger.error("#######getTodayData::::selectLoginSql:::::"
						+ selectLoginSql);
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectLoginSql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps.setLong(1, lastLoginId);

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					todayLoginCount = rs.getInt("cpid");
				}

				ps.clearParameters();
				ps.close();
				platformPool.closeResultSet(rs);
				// ///////////////////////////////

				logger.error("#######getTodayData::::selectChargeSql:::::"
						+ selectChargeSql);
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectChargeSql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps.setLong(1, lastChargeId);

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					todayChargeUserCount = rs.getInt("cpid");
					todayChargeMoney = rs.getInt("srmb");
				}
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}

		DailyReportData data = new DailyReportData();
		data.setReportDate("今天即时");
		data.isToday = true;
		data.setDownloadCount("－");
		data.setFirstConnectUserCount("－");
		data.setRegisterUserCount(todayRegisterCount + "");
		data.setRegisterCreateRoleUserCount("－");
		data.setRegisterEffectUserCount("－");
		data.setLoginUserCount(todayLoginCount + "");
		data.setKeepUserCount("－");
		data.setKeepUserRate("－");
		data.setActiveUserCount("－");
		data.setFirstChargeUserCount("－");
		data.setTodayChargeMoney(todayChargeMoney + "");
		data.setTodayChargeUser(todayChargeUserCount + "");
		data.setArpu("－");
		data.setActiveArpu("－");
		data.setLoyalUserCount("－");
		data.setComebackUserCount("－");
		data.setHighestOnlineCount("－");
		data.setAvgOnlineCount("－");
		data.setPerUserAvgLoginCount("－");
		data.setPerUserAvgOnlineTime("－");
		data.setActiveUserLossRate("－");
		data.setDAU("－");
		data.setCreateRoleRate("－");
		data.setPayRate("－");

		data.setTotalData(true);

		return data;

	}

	private DailyReportData getTodayData1(int parentPromoId, int promoId,
			int serverId) {
		int nowDate = DateUtil.getNowDate();
		int endDate = DateUtil.getBeforeOrNextDates(nowDate, 1);
		int yesterday = DateUtil.getBeforeOrNextDates(nowDate, -1);

		int todayRegisterCount = 0;
		int todayLoginCount = 0;
		int todayChargeUserCount = 0;
		int todayChargeMoney = 0;

		// String selectIdSql =
		// "select * from last_daily_record_collect_id where record_date="
		// + yesterday;

		// String selectGsIdSql =
		// "select * last_gs_collect_id where record_date="
		// + yesterday + " and server_id = " + serverId;

		String selectRegisterSql = "select count(player_id) cpid from player where register_time>"
				+ nowDate + " and register_time<" + endDate;
		if (parentPromoId > -1) {
			selectRegisterSql += " and parent_promo_id = " + parentPromoId;
		}
		if (promoId > -1) {
			selectRegisterSql += " and promo_id = " + promoId;
		}
		if (serverId > -1) {
			selectRegisterSql += " and attribute like ?";
		}

		String selectLoginSql = "select count(distinct player_id) cpid from login_record where login_time>"
				+ nowDate + " and login_time<" + endDate;
		if (parentPromoId > -1) {
			selectLoginSql += " and parent_promo_id = " + parentPromoId;
		}
		if (promoId > -1) {
			selectLoginSql += " and promo_id = " + promoId;
		}
		if (serverId > -1) {
			selectLoginSql += " and server_id = " + serverId;
		}

		String selectChargeSql = "select (sum(rmb)/100) srmb,count(distinct player_id) cpid from charge_record where charge_time>"
				+ nowDate + " and charge_time<" + endDate;
		if (parentPromoId > -1) {
			selectChargeSql += " and parent_promo_id = " + parentPromoId;
		}
		if (promoId > -1) {
			selectChargeSql += " and promo_id = " + promoId;
		}
		if (serverId > -1) {
			selectChargeSql += " and server_id = " + serverId;
		}

		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// boolean isSelectOK = false;

		try {
			bgCon = bgPool.getConnection();

			logger.error("#######getTodayData::::selectRegisterSql:::::"
					+ selectRegisterSql);
			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectRegisterSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			if (serverId > -1) {
				String attr = "%\"K2\":" + serverId + "%";
				logger.error("#######getTodayData:::::::::" + attr);
				ps.setString(1, attr);
			}

			rs = platformPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				todayRegisterCount = rs.getInt("cpid");
				// logger.error("#######getTodayData:::::::::"+todayRegisterCount);
			}

			ps.clearParameters();
			ps.close();
			platformPool.closeResultSet(rs);

			// //////////////////////////////
			logger.error("#######getTodayData::::selectLoginSql:::::"
					+ selectLoginSql);
			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectLoginSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = platformPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				todayLoginCount = rs.getInt("cpid");
			}

			ps.clearParameters();
			ps.close();
			platformPool.closeResultSet(rs);
			// ///////////////////////////////

			logger.error("#######getTodayData::::selectChargeSql:::::"
					+ selectChargeSql);
			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectChargeSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = platformPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				todayChargeUserCount = rs.getInt("cpid");
				todayChargeMoney = rs.getInt("srmb");
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}

		DailyReportData data = new DailyReportData();
		data.setReportDate("今天即时");
		data.isToday = true;
		data.setDownloadCount("－");
		data.setFirstConnectUserCount("－");
		data.setRegisterUserCount(todayRegisterCount + "");
		data.setRegisterCreateRoleUserCount("－");
		data.setRegisterEffectUserCount("－");
		data.setLoginUserCount(todayLoginCount + "");
		data.setKeepUserCount("－");
		data.setKeepUserRate("－");
		data.setActiveUserCount("－");
		data.setFirstChargeUserCount("－");
		data.setTodayChargeMoney(todayChargeMoney + "");
		data.setTodayChargeUser(todayChargeUserCount + "");
		data.setArpu("－");
		data.setActiveArpu("－");
		data.setLoyalUserCount("－");
		data.setComebackUserCount("－");
		data.setHighestOnlineCount("－");
		data.setAvgOnlineCount("－");
		data.setPerUserAvgLoginCount("－");
		data.setPerUserAvgOnlineTime("－");
		data.setActiveUserLossRate("－");
		data.setDAU("－");
		data.setCreateRoleRate("－");
		data.setPayRate("－");

		data.setTotalData(true);

		return data;

	}

	private DailyReportData processDefaultNullTotalData() {
		DailyReportData data = new DailyReportData();
		data.setReportDate("暂无数据");
		data.setDownloadCount("暂无数据");
		data.setFirstConnectUserCount("暂无数据");
		data.setRegisterUserCount("暂无数据");
		data.setRegisterCreateRoleUserCount("暂无数据");
		data.setRegisterEffectUserCount("暂无数据");
		data.setLoginUserCount("暂无数据");
		data.setKeepUserCount("暂无数据");
		data.setKeepUserRate("暂无数据");
		data.setActiveUserCount("暂无数据");
		data.setFirstChargeUserCount("暂无数据");
		data.setTodayChargeMoney("暂无数据");
		data.setTodayChargeUser("暂无数据");
		data.setArpu("暂无数据");
		data.setActiveArpu("暂无数据");
		data.setLoyalUserCount("暂无数据");
		data.setComebackUserCount("暂无数据");
		data.setHighestOnlineCount("暂无数据");
		data.setAvgOnlineCount("暂无数据");
		data.setPerUserAvgLoginCount("暂无数据");
		data.setPerUserAvgOnlineTime("暂无数据");
		data.setActiveUserLossRate("暂无数据");
		data.setDAU("暂无数据");
		data.setCreateRoleRate("暂无数据");
		data.setPayRate("暂无数据");

		data.setTotalData(true);

		return data;
	}

	private void processDAU(DailyReportTotalCounter counter,
			List<DailyReportData> list) {
		float totalDau = 0f;
		if (list != null && list.size() > 0) {
			for (DailyReportData data : list) {
				int loginCount = Integer.parseInt(data.loginUserCount);
				float dau = ((float) loginCount / counter.loginUserCount) * 100;
				totalDau += dau;
				data.setDAU(getFloatString(dau) + "%");
			}

			counter.DAU = totalDau / list.size();

		}

	}

	/**
	 * 返回min到max之间的随机数，不包含max这个边界值
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomWithoutMax(int min, int max) {
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		return new Random().nextInt(max - min) + min;
	}

	/**
	 * 返回0到max之间的随机数，不包含max
	 * 
	 * @param max
	 * @return
	 */
	public static int random(int max) {
		if (max < 0) {
			return random(max, 0);
		} else {
			return new Random().nextInt(max);
		}
	}

	/**
	 * 返回min到max之间的随机数，包含min和max两个边界值
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max) {
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		return new Random().nextInt((max + 1) - min) + min;
	}

	public static String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(value);
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyyMMdd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 计算两个日期之间相差的天数 日期格式：yyyyMMdd
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int countDays(String begin, String end) {
		int days = 0;

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar c_b = Calendar.getInstance();
		Calendar c_e = Calendar.getInstance();

		try {
			c_b.setTime(df.parse(begin));
			c_e.setTime(df.parse(end));

			while (c_b.before(c_e)) {
				days++;
				c_b.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException pe) {
			System.out.println("日期格式必须为：yyyyMMdd；如：20130404.");
		}

		return days;
	}

	/**
	 * 根据日期条件计算N天后的日期，日期格式：yyyyMMdd
	 * 
	 * @param beginDate
	 * @param dateCount
	 * @return
	 */
	public static String getNextDates(String beginDate, int dateCount) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar c_b = Calendar.getInstance();
		try {
			c_b.setTime(df.parse(beginDate));
			c_b.add(Calendar.DAY_OF_YEAR, dateCount);
		} catch (ParseException e) {
			System.out.println("日期格式必须为：yyyyMMdd；如：20130404.");
		}
		return df.format(c_b.getTime());
	}

	public static void main(String[] args) {
		DailyReportDAOImpl impl = new DailyReportDAOImpl();
		Map<String, String> condition = new HashMap<String, String>();
		condition.put(QueryCondition.QUERY_KEY_BEGIN_DATE, "20130430");
		condition.put(QueryCondition.QUERY_KEY_END_DATE, "20130502");
		condition.put(QueryCondition.QUERY_KEY_SERVER_ID, "1");

		DailyReport report = impl.getDailyReport(condition);

		for (DailyReportData data : report.getDailyReportDataList) {
			StringBuilder builder = new StringBuilder();
			builder.append("***************************" + "\n");
			builder.append("getReportDate:" + data.getReportDate() + "\n");
			builder.append("getDownloadCount:" + data.getDownloadCount() + "\n");
			builder.append("getFirstConnectUserCount:"
					+ data.getFirstConnectUserCount() + "\n");
			builder.append("getRegisterUserCount:"
					+ data.getRegisterUserCount() + "\n");
			builder.append("getRegisterCreateRoleUserCount:"
					+ data.getRegisterCreateRoleUserCount() + "\n");
			builder.append("getRegisterEffectUserCount:"
					+ data.getRegisterEffectUserCount() + "\n");
			builder.append("getLoginUserCount:" + data.getLoginUserCount()
					+ "\n");
			builder.append("getFirstChargeUserCount:"
					+ data.getFirstChargeUserCount() + "\n");
			builder.append("getTodayChargeUser:" + data.getTodayChargeUser()
					+ "\n");
			builder.append("getTodayChargeMoney:" + data.getTodayChargeMoney()
					+ "\n");
			builder.append("getArpu:" + data.getArpu() + "\n");
			builder.append("getDAU:" + data.getDAU() + "\n");
			builder.append("getActiveUserCount:" + data.getActiveUserCount()
					+ "\n");
			builder.append("getActiveArpu:" + data.getActiveArpu() + "\n");
			builder.append("getLoyalUserCount:" + data.getLoyalUserCount()
					+ "\n");
			builder.append("getComebackUserCount:"
					+ data.getComebackUserCount() + "\n");
			builder.append("getHighestOnlineCount:"
					+ data.getHighestOnlineCount() + "\n");
			builder.append("getAvgOnlineCount:" + data.getAvgOnlineCount()
					+ "\n");
			builder.append("getPerUserAvgLoginCount:"
					+ data.getPerUserAvgLoginCount() + "\n");
			builder.append("getPerUserAvgOnlineTime:"
					+ data.getPerUserAvgOnlineTime() + "\n");
			builder.append("getActiveUserLossRate:"
					+ data.getActiveUserLossRate() + "\n");
			builder.append("getPayRate:" + data.getPayRate() + "\n");
			builder.append("getCreateRoleRate:" + data.getCreateRoleRate()
					+ "\n");
			builder.append("***************************" + "\n");
			System.out.println(builder.toString());
		}

		// System.out.println(strToDate("20130501"));
		// System.out.println(getNextDates("20130501",10));
	}

	public DailyReport sortReport(DailyReport sourceReport,
			final String sortKey, final String sortType) {
		if (!sortKeyMap.containsKey(sortKey)) {
			return sourceReport;
		}

		// if(sortKey.equals(DailyReportDAO.SORT_KEY_CHARGE_MONEY)){
		List<DailyReportData> list = new ArrayList<DailyReportData>(
				sourceReport.getDailyReportDataList);
		Collections.sort(list, new Comparator<DailyReportData>() {

			@Override
			public int compare(DailyReportData o1, DailyReportData o2) {
				double n1 = 0f;
				double n2 = 0f;

				if (sortKey.equals(DailyReportDAO.SORT_KEY_ACTIVE_ARPU)) {

					n1 = (o1.activeArpu.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.activeArpu);
					n2 = (o2.activeArpu.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.activeArpu);
				} else if (sortKey
						.equals(DailyReportDAO.SORT_KEY_ACTIVE_LOSS_RATE)) {
					n1 = (o1.activeUserLossRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.activeUserLossRate
									.substring(0,
											o1.activeUserLossRate.indexOf("%")));
					n2 = (o2.activeUserLossRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.activeUserLossRate
									.substring(0,
											o2.activeUserLossRate.indexOf("%")));
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_ACTIVE_USER)) {
					n1 = (o1.activeUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.activeUserCount);
					n2 = (o2.activeUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.activeUserCount);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_ARPU)) {
					n1 = (o1.arpu.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o1.arpu);
					n2 = (o2.arpu.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o2.arpu);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_AVG_LOGIN)) {
					n1 = (o1.perUserAvgLoginCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.perUserAvgLoginCount);
					n2 = (o2.perUserAvgLoginCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.perUserAvgLoginCount);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_AVG_ONLINE)) {
					n1 = (o1.avgOnlineCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.avgOnlineCount);
					n2 = (o2.avgOnlineCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.avgOnlineCount);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_CHARGE_MONEY)) {
					n1 = (o1.todayChargeMoney.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.todayChargeMoney);
					n2 = (o2.todayChargeMoney.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.todayChargeMoney);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_CHARGE_USER)) {
					n1 = (o1.todayChargeUser.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.todayChargeUser);
					n2 = (o2.todayChargeUser.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.todayChargeUser);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_COMBACK_USER)) {
					n1 = (o1.comebackUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.comebackUserCount);
					n2 = (o2.comebackUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.comebackUserCount);
				} else if (sortKey
						.equals(DailyReportDAO.SORT_KEY_CREATE_ROLE_RATE)) {
					n1 = (o1.createRoleRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.createRoleRate.substring(0,
									o1.createRoleRate.indexOf("%")));
					n2 = (o2.createRoleRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.createRoleRate.substring(0,
									o2.createRoleRate.indexOf("%")));
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_DAU)) {
					n1 = (o1.DAU.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o1.DAU.substring(0,
									o1.DAU.indexOf("%")));
					n2 = (o2.DAU.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o2.DAU.substring(0,
									o2.DAU.indexOf("%")));
				} else if (sortKey
						.equals(DailyReportDAO.SORT_KEY_FIRST_CHARGE_USER)) {
					n1 = (o1.firstChargeUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.firstChargeUserCount);
					n2 = (o2.firstChargeUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.firstChargeUserCount);
				} else if (sortKey
						.equals(DailyReportDAO.SORT_KEY_HIGHEST_ONLINE)) {
					n1 = (o1.highestOnlineCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.highestOnlineCount);
					n2 = (o2.highestOnlineCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.highestOnlineCount);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_KEEP_USER)) {
					n1 = (o1.keepUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.keepUserCount);
					n2 = (o2.keepUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.keepUserCount);
				} else if (sortKey
						.equals(DailyReportDAO.SORT_KEY_KEEP_USER_RATE)) {
					n1 = (o1.keepUserRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.keepUserRate.substring(0,
									o1.keepUserRate.indexOf("%")));
					n2 = (o2.keepUserRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.keepUserRate.substring(0,
									o2.keepUserRate.indexOf("%")));
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_LOGIN_USER)) {
					n1 = (o1.loginUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.loginUserCount);
					n2 = (o2.loginUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.loginUserCount);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_LOYAL_USER)) {
					n1 = (o1.loyalUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.loyalUserCount);
					n2 = (o2.loyalUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.loyalUserCount);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_PAY_RATE)) {
					n1 = (o1.payRate.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o1.payRate.substring(0,
									o1.payRate.indexOf("%")));
					n2 = (o2.payRate.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o2.payRate.substring(0,
									o2.payRate.indexOf("%")));
				} else if (sortKey
						.equals(DailyReportDAO.SORT_KEY_REGISER_CREATE_ROLE_USER)) {
					n1 = (o1.registerCreateRoleUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.registerCreateRoleUserCount);
					n2 = (o2.registerCreateRoleUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.registerCreateRoleUserCount);
				} else if (sortKey
						.equals(DailyReportDAO.SORT_KEY_REGISER_EFFECT_USER)) {
					n1 = (o1.registerEffectUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.registerEffectUserCount);
					n2 = (o2.registerEffectUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.registerEffectUserCount);
				} else if (sortKey.equals(DailyReportDAO.SORT_KEY_REGISER_USER)) {
					n1 = (o1.registerUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.registerUserCount);
					n2 = (o2.registerUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.registerUserCount);
				}

				if (sortType.equals("asc")) {
					if (o1.isToday) {
						n1 = Double.MAX_VALUE;
					}
					if (o2.isToday) {
						n2 = Double.MAX_VALUE;
					}
					if (n1 > n2)
						return 1;
					else if (n1 < n2)
						return -1;
					return 0;
				} else {
					if (o1.isToday) {
						n1 = -Double.MAX_VALUE;
					}
					if (o2.isToday) {
						n2 = -Double.MAX_VALUE;
					}
					if (n1 > n2)
						return -1;
					else if (n1 < n2)
						return 1;
					return 0;
				}
			}
		});

		DailyReport report = new DailyReport();
		report.setGetDailyReportDataList(list);
		report.setGetTotalData(sourceReport.getTotalData);
		return report;
		// }

		// return sourceReport;
	}

	public static class DailyReportDataStruct {

		/**
		 * 日报表统计的日期
		 * 
		 * @return
		 */
		public int reportDate;

		public int serverId;

		public int parentPromoId;
		public int subPromoId;

		public int active_user_loss_rate_counter = 0;
		public int keepUserRateCounter = 0;

		public DailyReportDataStruct(int reportDate, int serverId,
				int parentPromoId, int subPromoId) {
			super();
			this.reportDate = reportDate;
			this.serverId = serverId;
			this.parentPromoId = parentPromoId;
			this.subPromoId = subPromoId;
		}

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
	}

	public static class LoginInfo {
		public int date;
		public int loginCount;
		public int totalOnlineTime;
	}

}
