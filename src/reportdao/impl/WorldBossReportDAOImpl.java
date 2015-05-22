package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.reportdatas.GamePointStockRankReport;
import data.reportdatas.GamePointStockRankReportData;
import data.reportdatas.WorldBossReport;
import data.reportdatas.WorldBossReportData;
import reportdao.WorldBossReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class WorldBossReportDAOImpl implements WorldBossReportDAO {
	private static final KGameLogger logger = KGameLogger
			.getLogger(WorldBossReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	public WorldBossReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
	}

	@Override
	public WorldBossReport getWorldBossReport(Map<String, String> condition) {
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

		WorldBossReport report = getWorldBossReportByServer(beginDate, endDate,
				serverId);

		if (report.getWorldBossReportDataList().size() == 0) {
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	public WorldBossReport getWorldBossReportByServer(int beginDate,
			int endDate, int serverId) {
		String selectSql = "select * from world_boss_report where record_date >= ? and record_date <= ? and server_id = ?";

		WorldBossReport report = new WorldBossReport();

		List<WorldBossReportData> list = new ArrayList<WorldBossReportData>();

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
			ps.setInt(3, serverId);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					WorldBossReportData data = new WorldBossReportData();

					int recordDate = rs.getInt("record_date");
					data.setReportDate(recordDate + "");

					int totalRoleCount = rs.getInt("total_role_count");
					data.setTotalRoleCount(totalRoleCount + "");

					int firstCount = rs.getInt("first_count");
					data.setFirstRoleCount(firstCount + "");

					int secondCount = rs.getInt("second_count");
					data.setSecondRoleCount(secondCount + "");

					int firstKill30 = rs.getInt("kill_first_30");
					data.setFirstKill30(firstKill30 + "");

					int firstKill40 = rs.getInt("kill_first_40");
					data.setFirstKill40(firstKill40 + "");

					int firstKill50 = rs.getInt("kill_first_50");
					data.setFirstKill50(firstKill50 + "");

					int firstKill60 = rs.getInt("kill_first_60");
					data.setFirstKill60(firstKill60 + "");

					int secondKill30 = rs.getInt("kill_second_30");
					data.setSecondKill30(secondKill30 + "");

					int secondKill40 = rs.getInt("kill_second_40");
					data.setSecondKill40(secondKill40 + "");

					int secondKill50 = rs.getInt("kill_second_50");
					data.setSecondKill50(secondKill50 + "");

					int secondKill60 = rs.getInt("kill_second_60");
					data.setSecondKill60(secondKill60 + "");

					float rate = rs.getFloat("join_rate");
					data.setSignUpRate(getFloatString(rate) + "%");

					list.add(data);
				}

				report.setWorldBossReportDataList(list);
				WorldBossReportData totalData = new WorldBossReportData();
				totalData.setReportDate("总计");
				totalData.setTotalRoleCount("－");
				totalData.setFirstRoleCount("－");
				totalData.setSecondRoleCount("－");
				totalData.setFirstKill30("－");
				totalData.setFirstKill40("－");
				totalData.setFirstKill50("－");
				totalData.setFirstKill60("－");
				totalData.setSecondKill30("－");
				totalData.setSecondKill40("－");
				totalData.setSecondKill50("－");
				totalData.setSecondKill60("－");
				totalData.setSignUpRate("－");
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
		if (report.getWorldBossReportDataList() == null) {
			report.setWorldBossReportDataList(list);
		}

		return report;
	}

	private WorldBossReportData processDefaultNullTotalData() {

		WorldBossReportData totalData = new WorldBossReportData();
		totalData.setReportDate("暂无数据");
		totalData.setTotalRoleCount("暂无数据");
		totalData.setFirstRoleCount("暂无数据");
		totalData.setSecondRoleCount("暂无数据");
		totalData.setFirstKill30("暂无数据");
		totalData.setFirstKill40("暂无数据");
		totalData.setFirstKill50("暂无数据");
		totalData.setFirstKill60("暂无数据");
		totalData.setSecondKill30("暂无数据");
		totalData.setSecondKill40("暂无数据");
		totalData.setSecondKill50("暂无数据");
		totalData.setSecondKill60("暂无数据");
		totalData.setSignUpRate("暂无数据");

		return totalData;
	}

	public static String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(value);
	}

}
