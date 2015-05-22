package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.reportdatas.GamePointConsumeReport;
import data.reportdatas.GamePointConsumeReportData;
import data.reportdatas.RoleOccupationReport;
import data.reportdatas.RoleOccupationReportData;
import reportdao.RoleOccupationReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class RoleOccupationReportDAOImpl implements RoleOccupationReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(RoleOccupationReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	// private DefineDataSourceManagerIF platformPool;

	public RoleOccupationReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		// platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
	}

	@Override
	public RoleOccupationReport getRoleOccupationReport(
			Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("getRoleOccupationReport condition,key:" + key
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

		RoleOccupationReport report = null;
		if (serverId == -1) {
			report = getRoleOccupationReport(beginDate, endDate);
		} else {
			report = getRoleOccupationReportByServer(beginDate, endDate,
					serverId);
		}
		if (report == null) {
			report = new RoleOccupationReport();
			report.setRoleOccupationDataList(new ArrayList<RoleOccupationReportData>());
		}
		if (report.getTotalData() == null) {
			report.setTotalData(processDefaultNullTotalData());
		}
		if (report.getRoleOccupationDataList().size() == 0) {
			report.getRoleOccupationDataList().add(
					processDefaultNullTotalData());
		}
		// logger.error("List<DailyReportData>:::size:"+result.size());
		// report.setGetDailyReportDataList(result);
		return report;
	}

	public RoleOccupationReport getRoleOccupationReport(int beginDate,
			int endDate) {

		String selectSql = "select sum(warrior) sw,sum(magician) sm,sum(bowman) sb from "
				+ "role_occupation_report where record_date>=? and record_date<=?";

		RoleOccupationReport report = new RoleOccupationReport();

		List<RoleOccupationReportData> list = new ArrayList<RoleOccupationReportData>();

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
				int warrior = rs.getInt("sw");
				int magician = rs.getInt("sm");
				int bowman = rs.getInt("sb");
				int total = warrior + magician + bowman;

				RoleOccupationReportData warriorData = new RoleOccupationReportData();
				warriorData.setOccName("突击战士");
				warriorData.setOccCount(warrior + "");
				if (total == 0) {
					warriorData.setOccRate("0%");
				} else {
					warriorData.setOccRate(getFloatString((float) warrior * 100
							/ total)
							+ "%");
				}
				list.add(warriorData);

				RoleOccupationReportData magicianData = new RoleOccupationReportData();
				magicianData.setOccName("暗影特工");
				magicianData.setOccCount(magician + "");
				if (total == 0) {
					magicianData.setOccRate("0%");
				} else {
					magicianData.setOccRate(getFloatString((float) magician
							* 100 / total)
							+ "%");
				}
				list.add(magicianData);

				RoleOccupationReportData bowmanData = new RoleOccupationReportData();
				bowmanData.setOccName("枪械师");
				bowmanData.setOccCount(bowman + "");
				if (total == 0) {
					bowmanData.setOccRate("0%");
				} else {
					bowmanData.setOccRate(getFloatString((float) bowman * 100
							/ total)
							+ "%");
				}
				list.add(bowmanData);

				RoleOccupationReportData totalData = new RoleOccupationReportData();
				totalData.setOccName("合计");
				totalData.setOccCount(total + "");
				totalData.setOccRate("－");

				report.setRoleOccupationDataList(list);
				report.setTotalData(totalData);

				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getRoleOccupationDataList() == null) {
			report.setRoleOccupationDataList(list);
		}

		return report;
	}

	public RoleOccupationReport getRoleOccupationReportByServer(int beginDate,
			int endDate, int serverId) {
		String selectSql = "select sum(warrior) sw,sum(magician) sm,sum(bowman) sb from "
				+ "role_occupation_report where record_date>=? and record_date<=? and server_id = ?";

		RoleOccupationReport report = new RoleOccupationReport();

		List<RoleOccupationReportData> list = new ArrayList<RoleOccupationReportData>();

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
			if (rs != null && rs.next()) {
				int warrior = rs.getInt("sw");
				int magician = rs.getInt("sm");
				int bowman = rs.getInt("sb");
				int total = warrior + magician + bowman;

				RoleOccupationReportData warriorData = new RoleOccupationReportData();
				warriorData.setOccName("突击战士");
				warriorData.setOccCount(warrior + "");
				if (total == 0) {
					warriorData.setOccRate("0%");
				} else {
					warriorData.setOccRate(getFloatString((float) warrior * 100
							/ total)
							+ "%");
				}
				list.add(warriorData);

				RoleOccupationReportData magicianData = new RoleOccupationReportData();
				magicianData.setOccName("暗影特工");
				magicianData.setOccCount(magician + "");
				if (total == 0) {
					magicianData.setOccRate("0%");
				} else {
					magicianData.setOccRate(getFloatString((float) magician
							* 100 / total)
							+ "%");
				}
				list.add(magicianData);

				RoleOccupationReportData bowmanData = new RoleOccupationReportData();
				bowmanData.setOccName("枪械师");
				bowmanData.setOccCount(bowman + "");
				if (total == 0) {
					bowmanData.setOccRate("0%");
				} else {
					bowmanData.setOccRate(getFloatString((float) bowman * 100
							/ total)
							+ "%");
				}
				list.add(bowmanData);

				RoleOccupationReportData totalData = new RoleOccupationReportData();
				totalData.setOccName("合计");
				totalData.setOccCount(total + "");
				totalData.setOccRate("－");

				report.setRoleOccupationDataList(list);
				report.setTotalData(totalData);

				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getRoleOccupationDataList() == null) {
			report.setRoleOccupationDataList(list);
		}

		return report;
	}

	private String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.000");
		return fnum.format(value);
	}

	private RoleOccupationReportData processDefaultNullTotalData() {
		RoleOccupationReportData totalData = new RoleOccupationReportData();
		totalData.setOccName("暂无数据");
		totalData.setOccCount("暂无数据");
		totalData.setOccRate("暂无数据");

		return totalData;
	}

}
