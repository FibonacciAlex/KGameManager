package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.reportdatas.FamilyWarReport;
import data.reportdatas.FamilyWarReportData;
import data.reportdatas.WorldBossReport;
import data.reportdatas.WorldBossReportData;
import reportdao.FamilyWarReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class FamilyWarReportDAOImpl implements FamilyWarReportDAO{
	private static final KGameLogger logger = KGameLogger
			.getLogger(FamilyWarReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	public FamilyWarReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
	}
	@Override
	public FamilyWarReport getFamilyWarReport(Map<String, String> condition) {
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

		FamilyWarReport report = getFamilyWarReportByServer(beginDate, endDate,
				serverId);

		if (report.getFamilyWarReportDataList().size() == 0) {
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}
	
	public FamilyWarReport getFamilyWarReportByServer(int beginDate, int endDate,
			int serverId){
		String selectSql = "select * from family_war_report where record_date >= ? and record_date <= ? and server_id = ?";

		FamilyWarReport report = new FamilyWarReport();

		List<FamilyWarReportData> list = new ArrayList<FamilyWarReportData>();

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
					FamilyWarReportData data = new FamilyWarReportData();

					int recordDate = rs.getInt("record_date");
					data.setReportDate(recordDate + "");

					int signUpCount = rs.getInt("sign_count");
					data.setSignUpCount(signUpCount + "");

					int t32Count = rs.getInt("t32_count");
					data.setT32Count(t32Count + "");

					int t16Count = rs.getInt("t16_count");
					data.setT16Count(t16Count + "");

					int t8Count = rs.getInt("t8_count");
					data.setT8Count(t8Count + "");

					int t4Count = rs.getInt("t4_count");
					data.setT4Count(t4Count + "");

					int t2Count = rs.getInt("t2_count");
					data.setT2Count(t2Count + "");

					String rankInfo = rs.getString("");
					data.setRankInfo(rankInfo);

					list.add(data);
				}

				report.setFamilyWarReportDataList(list);
				FamilyWarReportData totalData = new FamilyWarReportData();
				totalData.setReportDate("总计");
				totalData.setSignUpCount("－");
				totalData.setT32Count("－");
				totalData.setT16Count("－");
				totalData.setT8Count("－");
				totalData.setT4Count("－");
				totalData.setT2Count("－");
				totalData.setRankInfo("－");
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
		if (report.getFamilyWarReportDataList() == null) {
			report.setFamilyWarReportDataList(list);
		}

		return report;
	}
	
	private FamilyWarReportData processDefaultNullTotalData() {

		FamilyWarReportData totalData = new FamilyWarReportData();
		totalData.setReportDate("暂无数据");
		totalData.setSignUpCount("暂无数据");
		totalData.setT32Count("暂无数据");
		totalData.setT16Count("暂无数据");
		totalData.setT8Count("暂无数据");
		totalData.setT4Count("暂无数据");
		totalData.setT2Count("暂无数据");
		totalData.setRankInfo("暂无数据");

		return totalData;
	}

}
