package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import data.reportdatas.GamePointConsumeReport;
import data.reportdatas.GamePointConsumeReportData;
import data.reportdatas.UserKeepOnlineReport;
import data.reportdatas.UserKeepOnlineReportData;
import reportdao.UserKeepOnlineReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;
import reportdao.util.DateUtil;

public class UserKeepOnlineReportDAOImpl implements UserKeepOnlineReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(UserKeepOnlineReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	public UserKeepOnlineReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
	}

	@Override
	public UserKeepOnlineReport getUserKeepOnlineReport(
			Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("getGamePointConsumeReport condition,key:" + key
					+ ",value:" + condition.get(key));
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

		UserKeepOnlineReport report = null;
		if (serverId == -1 && parentPromoId == -1) {
			report = getUserKeepOnlineReport(beginDate, endDate);
		} else if (serverId > -1 && parentPromoId == -1) {
			report = getUserKeepOnlineReportByServer(beginDate, endDate,
					serverId, -1, -1);

		} else if (serverId == -1 && parentPromoId > -1) {
			report = getUserKeepOnlineReportByPromo(beginDate, endDate,
					parentPromoId, subPromoId);
		} else {
			report = getUserKeepOnlineReportByServer(beginDate, endDate,
					serverId, parentPromoId, subPromoId);
		}
		if (report == null) {
			report = new UserKeepOnlineReport();
			report.setGetUserKeepOnlineReportDataList(new ArrayList<UserKeepOnlineReportData>());
			report.setTotalData(processDefaultNullTotalData());
		}
		if (report.totalData == null) {
			report.setTotalData(processDefaultAmountTotalData());
		}
		// logger.error("List<DailyReportData>:::size:"+result.size());
		// report.setGetDailyReportDataList(result);
		return report;
	}

	public UserKeepOnlineReport getUserKeepOnlineReport(int beginDate,
			int endDate) {
		String selectSql = "select * from "
				+ "user_keeping_report where record_date>=? and record_date<=?";

		UserKeepOnlineReport report = new UserKeepOnlineReport();

		List<UserKeepOnlineReportData> list = new ArrayList<UserKeepOnlineReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;

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
					UserKeepOnlineReportData data = new UserKeepOnlineReportData();

					int recordDate = rs.getInt("record_date");
					data.setReportDate(recordDate + "");

					String dateOfWeek = getDateOfWeek(recordDate);
					data.setDayOfweek(dateOfWeek);

					int regCount = rs.getInt("register_count");
					data.setCreateUserCount(regCount + "");

					String loginInfo = rs.getString("login_info");
					data.setLoginCountInfo(decodeLoginInfo(loginInfo));

					data.setTotalData(false);

					list.add(data);
				}

				report.setGetUserKeepOnlineReportDataList(list);

				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getGetUserKeepOnlineReportDataList() == null) {
			report.setGetUserKeepOnlineReportDataList(list);
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	public UserKeepOnlineReport getUserKeepOnlineReportByPromo(int beginDate,
			int endDate, int parentPromoId, int subPromoId) {
		String selectSql = "select * from "
				+ "user_keeping_report_promo where record_date>=? and record_date<=? and parent_promo_id = ?";

		if (subPromoId > 0) {
			selectSql += " and promo_id = ?";
		}

		UserKeepOnlineReport report = new UserKeepOnlineReport();

		List<UserKeepOnlineReportData> list = new ArrayList<UserKeepOnlineReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);
			ps.setInt(3, parentPromoId);
			if (subPromoId > 0) {
				ps.setInt(4, subPromoId);
			}

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					UserKeepOnlineReportData data = new UserKeepOnlineReportData();

					int recordDate = rs.getInt("record_date");
					data.setReportDate(recordDate + "");

					String dateOfWeek = getDateOfWeek(recordDate);
					data.setDayOfweek(dateOfWeek);

					int regCount = rs.getInt("register_count");
					data.setCreateUserCount(regCount + "");

					String loginInfo = rs.getString("login_info");
					data.setLoginCountInfo(decodeLoginInfo(loginInfo));

					data.setTotalData(false);

					list.add(data);
				}

				report.setGetUserKeepOnlineReportDataList(list);

				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getGetUserKeepOnlineReportDataList() == null) {
			report.setGetUserKeepOnlineReportDataList(list);
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	public UserKeepOnlineReport getUserKeepOnlineReportByServer(int beginDate,
			int endDate, int serverId, int parentPromoId, int subPromoId) {
		String selectSql = "select * from "
				+ "user_keeping_report_server where record_date>=? and record_date<=? and server_id = ?";

		UserKeepOnlineReport report = new UserKeepOnlineReport();

		List<UserKeepOnlineReportData> list = new ArrayList<UserKeepOnlineReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);
			ps.setInt(3, serverId);

			rs = bgPool.executeQuery(ps);

			LinkedHashMap<Integer, UserKeepOnlineData> dataMap = new LinkedHashMap<Integer, UserKeepOnlineData>();

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {

					int recordDate = rs.getInt("record_date");

					int regCount = rs.getInt("register_count");

					String loginInfo = rs.getString("login_info");
					int[] loginData = decodeLoginInfo(loginInfo);

					if (!dataMap.containsKey(recordDate)) {
						UserKeepOnlineData data = new UserKeepOnlineData();
						data.recordDate = recordDate;
						dataMap.put(recordDate, data);
					}
					UserKeepOnlineData data = dataMap.get(recordDate);
					data.regCount += regCount;
					if (data.loginInfo == null) {
						data.loginInfo = loginData;
					} else {
						for (int i = 0; i < data.loginInfo.length
								&& i < loginData.length; i++) {
							data.loginInfo[i] += loginData[i];
						}
					}
				}

				for (Integer recordDate : dataMap.keySet()) {
					UserKeepOnlineData data = dataMap.get(recordDate);

					UserKeepOnlineReportData reData = new UserKeepOnlineReportData();
					reData.setReportDate(recordDate + "");

					String dateOfWeek = getDateOfWeek(recordDate);
					reData.setDayOfweek(dateOfWeek);

					reData.setCreateUserCount(data.regCount + "");
					reData.setLoginCountInfo(data.loginInfo);

					reData.setTotalData(false);

					list.add(reData);
				}

				report.setGetUserKeepOnlineReportDataList(list);

				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getGetUserKeepOnlineReportDataList() == null) {
			report.setGetUserKeepOnlineReportDataList(list);
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}
	
	public UserKeepOnlineReport getUserKeepOnlineReportByServerPromo(int beginDate,
			int endDate, int serverId, int parentPromoId, int subPromoId) {
		String selectSql = "select * from "
				+ "user_keeping_report_server_promo where record_date>=? and record_date<=? and server_id = ?";
		if (parentPromoId > 0) {
			selectSql += " and parent_promo_id = ?";
		}

		if (subPromoId > 0) {
			selectSql += " and promo_id = ?";
		}

		UserKeepOnlineReport report = new UserKeepOnlineReport();

		List<UserKeepOnlineReportData> list = new ArrayList<UserKeepOnlineReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);
			ps.setInt(3, serverId);
			if (parentPromoId > 0) {
				ps.setInt(4, parentPromoId);
			}
			if (subPromoId > 0) {
				ps.setInt(5, subPromoId);
			}

			rs = bgPool.executeQuery(ps);

			LinkedHashMap<Integer, UserKeepOnlineData> dataMap = new LinkedHashMap<Integer, UserKeepOnlineData>();

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {

					int recordDate = rs.getInt("record_date");

					int regCount = rs.getInt("register_count");

					String loginInfo = rs.getString("login_info");
					int[] loginData = decodeLoginInfo(loginInfo);

					if (!dataMap.containsKey(recordDate)) {
						UserKeepOnlineData data = new UserKeepOnlineData();
						data.recordDate = recordDate;
						dataMap.put(recordDate, data);
					}
					UserKeepOnlineData data = dataMap.get(recordDate);
					data.regCount += regCount;
					if (data.loginInfo == null) {
						data.loginInfo = loginData;
					} else {
						for (int i = 0; i < data.loginInfo.length
								&& i < loginData.length; i++) {
							data.loginInfo[i] += loginData[i];
						}
					}
				}

				for (Integer recordDate : dataMap.keySet()) {
					UserKeepOnlineData data = dataMap.get(recordDate);

					UserKeepOnlineReportData reData = new UserKeepOnlineReportData();
					reData.setReportDate(recordDate + "");

					String dateOfWeek = getDateOfWeek(recordDate);
					reData.setDayOfweek(dateOfWeek);

					reData.setCreateUserCount(data.regCount + "");
					reData.setLoginCountInfo(data.loginInfo);

					reData.setTotalData(false);

					list.add(reData);
				}

				report.setGetUserKeepOnlineReportDataList(list);

				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getGetUserKeepOnlineReportDataList() == null) {
			report.setGetUserKeepOnlineReportDataList(list);
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	private UserKeepOnlineReportData processDefaultNullTotalData() {
		UserKeepOnlineReportData data = new UserKeepOnlineReportData();
		data.setReportDate("暂无数据");
		data.setDayOfweek("暂无数据");
		data.setLoginCountInfo(new int[0]);
		data.setCreateUserCount("暂无数据");
		data.setTotalData(true);
		return data;
	}

	private UserKeepOnlineReportData processDefaultAmountTotalData() {
		UserKeepOnlineReportData data = new UserKeepOnlineReportData();
		data.setReportDate("合计");
		data.setDayOfweek("－");
		data.setLoginCountInfo(new int[0]);
		data.setCreateUserCount("－");
		data.setTotalData(true);
		return data;
	}

	private String getDateOfWeek(int intDate) {
		Date date = DateUtil.intToDate(intDate);
		int weekday = DateUtil.getDayOfWeek(date);
		switch (weekday) {
		case 1:
			return "星期日";
		case 2:
			return "星期一";
		case 3:
			return "星期二";
		case 4:
			return "星期三";
		case 5:
			return "星期四";
		case 6:
			return "星期五";
		case 7:
			return "星期六";

		default:
			break;
		}
		return "-";
	}

	private int[] decodeLoginInfo(String loginInfo) {
		int[] result = new int[0];
		if (loginInfo != null && !loginInfo.equals("")) {
			String[] str = loginInfo.split(",");
			if (str.length > 0) {
				result = new int[str.length];
				for (int i = 0; i < str.length; i++) {
					String[] infoStr = str[i].split(":");
					result[i] = Integer.parseInt(infoStr[1]);
				}
			}
		}
		return result;
	}

	public static class UserKeepOnlineData {
		int recordDate;
		int regCount;
		int[] loginInfo;
	}

}
