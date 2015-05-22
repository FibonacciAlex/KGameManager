package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Collection;

import data.reportdatas.DailyReportData;
import data.reportdatas.FirstChargeReport;
import data.reportdatas.FirstChargeUserInfo;
import data.reportdatas.GameCopperStockReport;
import data.reportdatas.GamePointConsumeReport;
import data.reportdatas.GamePointConsumeReportData;
import data.reportdatas.Report;
import data.reportdatas.UserKeepOnlineReport;
import data.reportdatas.UserKeepOnlineReportData;
import reportdao.FirstChargeReportDAO;
import reportdao.GamePointConsumeReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.reportCache.ReportCacheManager;
import reportdao.impl.totalcounter.DailyReportTotalCounter;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class FirstChargeReportDAOImpl implements FirstChargeReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(FirstChargeReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	private static Map<String, Boolean> sortKeyMap;

	public FirstChargeReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();

		initSortKeyMap();
	}

	private static void initSortKeyMap() {
		if (sortKeyMap == null) {
			sortKeyMap = new HashMap<String, Boolean>();
			sortKeyMap
					.put(FirstChargeReportDAO.SORT_KEY_CHARGE_USER_RATE, true);
			sortKeyMap
					.put(FirstChargeReportDAO.SORT_KEY_FIRST_CHARGE_MONEY_RATE,
							true);
			sortKeyMap
					.put(FirstChargeReportDAO.SORT_KEY_FIRST_CHARGE_USER_COUNT,
							true);
			sortKeyMap.put(FirstChargeReportDAO.SORT_KEY_TOTAL_CHARGE_COUNT,
					true);
			sortKeyMap.put(
					FirstChargeReportDAO.SORT_KEY_TOTAL_FIRST_CHARGE_MONEY,
					true);
		}
	}

	@Override
	public FirstChargeReport getFirstChargeReport(Map<String, String> condition) {

		for (String key : condition.keySet()) {
			logger.error("getFirstChargeReport condition,key:" + key
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

		// if(ReportCacheManager.isReportExist(Report.REPORT_TYPE_FIRST_CHARGE,
		// condition)){
		// FirstChargeReport report =
		// (FirstChargeReport)(ReportCacheManager.getReport(Report.REPORT_TYPE_FIRST_CHARGE,
		// condition));
		// return sortReport(report, condition.get("sortname"),
		// condition.get("sortorder"));
		// }

		FirstChargeReport report = null;
		if (serverId == -1 && parentPromoId == -1) {
			report = getFirstChargeReport(beginDate, endDate);
		} else if (serverId > -1 && parentPromoId == -1) {
			report = getFirstChargeReportByServer(beginDate, endDate, serverId,
					-1, -1);

		} else if (serverId == -1 && parentPromoId > -1) {
			report = getFirstChargeReportByServer(beginDate, endDate, -1,
					parentPromoId, subPromoId);
		} else {
			report = getFirstChargeReportByServer(beginDate, endDate, serverId,
					parentPromoId, subPromoId);
		}
		if (report == null) {
			report = new FirstChargeReport();
			report.setFirstChargeUserInfoList(new ArrayList<FirstChargeUserInfo>());
			report.setTotalData(processDefaultNullTotalData());
		}
		if (report.totalData == null) {
			report.setTotalData(processDefaultNullTotalData());
		}

		ReportCacheManager.addReport(Report.REPORT_TYPE_FIRST_CHARGE,
				condition, report);

		return sortReport(report, condition.get("sortname"),
				condition.get("sortorder"));
	}

	public FirstChargeReport getFirstChargeReport(int beginDate, int endDate) {
		String selectSql = "select * from "
				+ "first_charge_report where record_date>=? and record_date<=?";

		FirstChargeReport report = new FirstChargeReport();

		List<FirstChargeUserInfo> list = new ArrayList<FirstChargeUserInfo>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			LinkedHashMap<Integer, FirstChargeUserData> dataMap = new LinkedHashMap<Integer, FirstChargeUserData>();
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

					String chargeInfo = rs.getString("charge_info");

					caculteFirstChargeUserInfo(dataMap, chargeInfo);
				}

				List<FirstChargeUserData> dataList = new ArrayList<FirstChargeReportDAOImpl.FirstChargeUserData>();

				FirstChargeUserData totalData = new FirstChargeUserData();

				for (FirstChargeUserData data : dataMap.values()) {
					dataList.add(data);
					totalData.firstChargeUserCount += data.firstChargeUserCount;
					totalData.totalChargeCount += data.totalChargeCount;
					totalData.totalFirstChargeMoney += data.totalFirstChargeMoney;
				}

				Collections.sort(dataList);

				for (FirstChargeUserData data : dataList) {
					FirstChargeUserInfo info = new FirstChargeUserInfo();
					info.isTotalData = false;
					info.roleLevel = data.roleLevel + "";
					info.firstChargeUserCount = data.firstChargeUserCount + "";
					info.totalChargeCount = data.totalChargeCount + "";
					info.chargeUserRate = getFloatString((((float) data.firstChargeUserCount) / totalData.firstChargeUserCount) * 100)
							+ "%";
					info.totalFirstChargeMoney = data.totalFirstChargeMoney
							+ "";
					info.firstChargeMoneyRate = getFloatString((((float) data.totalFirstChargeMoney) / totalData.totalFirstChargeMoney) * 100)
							+ "%";
					list.add(info);
				}

				report.setFirstChargeUserInfoList(list);
				report.setTotalData(processTotalData(totalData));

				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getFirstChargeUserInfoList() == null) {
			report.setFirstChargeUserInfoList(list);
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	public FirstChargeReport getFirstChargeReportByServer(int beginDate,
			int endDate, int serverId, int parentPromoId, int subPromoId) {
		String selectSql = "select * from "
				+ "first_charge_report_server where record_date>=? and record_date<=?";

		if (serverId > -1) {
			selectSql += " and server_id=?";
		}
		if (parentPromoId > -1) {
			selectSql += " and parent_promo_id=?";
		}
		if (subPromoId > -1) {
			selectSql += " and promo_id=?";
		}

		FirstChargeReport report = new FirstChargeReport();

		List<FirstChargeUserInfo> list = new ArrayList<FirstChargeUserInfo>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;

		try {
			LinkedHashMap<Integer, FirstChargeUserData> dataMap = new LinkedHashMap<Integer, FirstChargeUserData>();
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);
			if (serverId > -1) {
				ps.setInt(3, serverId);
				if (parentPromoId > -1) {
					ps.setInt(4, parentPromoId);
				}
				if (subPromoId > -1) {
					ps.setInt(5, subPromoId);
				}
			} else {
				if (parentPromoId > -1) {
					ps.setInt(3, parentPromoId);
				}
				if (subPromoId > -1) {
					ps.setInt(4, subPromoId);
				}
			}

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {

					String chargeInfo = rs.getString("charge_info");

					caculteFirstChargeUserInfo(dataMap, chargeInfo);
				}

				List<FirstChargeUserData> dataList = new ArrayList<FirstChargeReportDAOImpl.FirstChargeUserData>();

				FirstChargeUserData totalData = new FirstChargeUserData();

				for (FirstChargeUserData data : dataMap.values()) {
					dataList.add(data);
					totalData.firstChargeUserCount += data.firstChargeUserCount;
					totalData.totalChargeCount += data.totalChargeCount;
					totalData.totalFirstChargeMoney += data.totalFirstChargeMoney;
				}

				Collections.sort(dataList);

				for (FirstChargeUserData data : dataList) {
					FirstChargeUserInfo info = new FirstChargeUserInfo();
					info.isTotalData = false;
					info.roleLevel = data.roleLevel + "";
					info.firstChargeUserCount = data.firstChargeUserCount + "";
					info.totalChargeCount = data.totalChargeCount + "";
					info.chargeUserRate = getFloatString((((float) data.firstChargeUserCount) / totalData.firstChargeUserCount) * 100)
							+ "%";
					info.totalFirstChargeMoney = data.totalFirstChargeMoney
							+ "";
					info.firstChargeMoneyRate = getFloatString((((float) data.totalFirstChargeMoney) / totalData.totalFirstChargeMoney) * 100)
							+ "%";
					list.add(info);
				}

				report.setFirstChargeUserInfoList(list);
				report.setTotalData(processTotalData(totalData));

				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getFirstChargeUserInfoList() == null) {
			report.setFirstChargeUserInfoList(list);
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	public FirstChargeUserInfo processDefaultNullTotalData() {
		FirstChargeUserInfo info = new FirstChargeUserInfo();
		info.isTotalData = true;
		info.roleLevel = "合计";
		info.firstChargeUserCount = "暂无数据";
		info.totalChargeCount = "暂无数据";
		info.totalFirstChargeMoney = "暂无数据";
		info.chargeUserRate = "暂无数据";
		info.firstChargeMoneyRate = "暂无数据";
		return info;
	}

	private FirstChargeUserInfo processTotalData(FirstChargeUserData totalData) {
		FirstChargeUserInfo info = new FirstChargeUserInfo();
		info.isTotalData = true;
		info.roleLevel = "合计";
		info.firstChargeUserCount = totalData.firstChargeUserCount + "";
		info.totalChargeCount = totalData.totalChargeCount + "";
		info.totalFirstChargeMoney = totalData.totalFirstChargeMoney + "";
		info.chargeUserRate = "－";
		info.firstChargeMoneyRate = "－";
		return info;
	}

	private void caculteFirstChargeUserInfo(
			Map<Integer, FirstChargeUserData> infoMap, String info) {

		if (info != null && info.length() > 0) {
			if (info.split(",") != null && info.split(",").length > 0) {

				String[] infoStr = info.split(",");

				for (int i = 0; i < infoStr.length; i++) {
					String[] dataStr = infoStr[i].split(":");
					int roleLevel = Integer.parseInt(dataStr[0]);
					int firstChargeUserCount = Integer.parseInt(dataStr[1]);
					int chargeCount = Integer.parseInt(dataStr[2]);
					int chargeMoney = Integer.parseInt(dataStr[3]);

					if (infoMap.containsKey(roleLevel)) {
						infoMap.get(roleLevel).firstChargeUserCount += firstChargeUserCount;
						infoMap.get(roleLevel).totalChargeCount += chargeCount;
						infoMap.get(roleLevel).totalFirstChargeMoney += chargeMoney;
					} else {
						FirstChargeUserData data = new FirstChargeUserData();
						data.roleLevel = roleLevel;
						data.firstChargeUserCount = firstChargeUserCount;
						data.totalChargeCount = chargeCount;
						data.totalFirstChargeMoney = chargeMoney;
						infoMap.put(roleLevel, data);
					}
				}
			}
		}
	}

	public static class FirstChargeUserData implements
			Comparable<FirstChargeUserData> {
		/**
		 * 首充用户的角色等级
		 * 
		 * @return
		 */
		public int roleLevel;

		/**
		 * 该等级的首充用户人数
		 * 
		 * @return
		 */
		public int firstChargeUserCount;

		/**
		 * 该等级的首充用户的充值总次数
		 * 
		 * @return
		 */
		public int totalChargeCount;

		/**
		 * 该等级首充用户占比
		 * 
		 * @return
		 */
		public float chargeUserRate;

		/**
		 * 该等级首充金额
		 * 
		 * @return
		 */
		public int totalFirstChargeMoney;

		/**
		 * 该等级首充金额占比
		 * 
		 * @return
		 */
		public float firstChargeMoneyRate;

		@Override
		public int compareTo(FirstChargeUserData o) {
			int result = o.roleLevel > roleLevel ? -1
					: (o.roleLevel == roleLevel ? 0 : 1);
			return result;
		}
	}

	public static String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(value);
	}

	public FirstChargeReport sortReport(FirstChargeReport sourceReport,
			final String sortKey, final String sortType) {
		if (!sortKeyMap.containsKey(sortKey)) {
			return sourceReport;
		}

		// if(sortKey.equals(DailyReportDAO.SORT_KEY_CHARGE_MONEY)){
		List<FirstChargeUserInfo> list = new ArrayList<FirstChargeUserInfo>(
				sourceReport.firstChargeUserInfoList);
		Collections.sort(list, new Comparator<FirstChargeUserInfo>() {

			@Override
			public int compare(FirstChargeUserInfo o1, FirstChargeUserInfo o2) {
				double n1 = 0f;
				double n2 = 0f;

				if (sortKey
						.equals(FirstChargeReportDAO.SORT_KEY_FIRST_CHARGE_USER_COUNT)) {

					n1 = (o1.firstChargeUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.firstChargeUserCount);
					n2 = (o2.firstChargeUserCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.firstChargeUserCount);
				} else if (sortKey
						.equals(FirstChargeReportDAO.SORT_KEY_CHARGE_USER_RATE)) {
					n1 = (o1.chargeUserRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.chargeUserRate.substring(0,
									o1.chargeUserRate.indexOf("%")));
					n2 = (o2.chargeUserRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.chargeUserRate.substring(0,
									o2.chargeUserRate.indexOf("%")));
				} else if (sortKey
						.equals(FirstChargeReportDAO.SORT_KEY_FIRST_CHARGE_MONEY_RATE)) {
					n1 = (o1.firstChargeMoneyRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.firstChargeMoneyRate
									.substring(0, o1.firstChargeMoneyRate
											.indexOf("%")));
					n2 = (o2.firstChargeMoneyRate.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.firstChargeMoneyRate
									.substring(0, o2.firstChargeMoneyRate
											.indexOf("%")));
				} else if (sortKey
						.equals(FirstChargeReportDAO.SORT_KEY_TOTAL_CHARGE_COUNT)) {
					n1 = (o1.totalChargeCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.totalChargeCount);
					n2 = (o2.totalChargeCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.totalChargeCount);
				} else if (sortKey
						.equals(FirstChargeReportDAO.SORT_KEY_TOTAL_FIRST_CHARGE_MONEY)) {
					n1 = (o1.totalFirstChargeMoney.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.totalFirstChargeMoney);
					n2 = (o2.totalFirstChargeMoney.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.totalFirstChargeMoney);
				}

				if (sortType.equals("asc")) {
					if (n1 > n2)
						return 1;
					else if (n1 < n2)
						return -1;
					return 0;
				} else {
					if (n1 > n2)
						return -1;
					else if (n1 < n2)
						return 1;
					return 0;
				}
			}
		});

		FirstChargeReport report = new FirstChargeReport();
		report.setFirstChargeUserInfoList(list);
		report.setTotalData(sourceReport.getTotalData());
		return report;
	}
}
