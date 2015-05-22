package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.reportdatas.GameLevelLossReport;
import data.reportdatas.GameLevelLossReportData;
import data.reportdatas.LoginInfoReport;
import data.reportdatas.LoginInfoReportData;
import reportdao.LoginInfoReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class LoginInfoReportDAOImpl implements LoginInfoReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(LoginInfoReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	public LoginInfoReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
	}

	@Override
	public LoginInfoReport getLoginInfoReport(Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("GameLevelLossReport condition,key:" + key + ",value:"
					+ condition.get(key));
		}

		// 解释condition条件的开始日期
		String beginDateStr = condition
				.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		// 解释condition条件的开始日期
		String endDateStr = condition.get(QueryCondition.QUERY_KEY_END_DATE);

		int serverId = -1;
		if (condition.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
			serverId = Integer.parseInt(condition
					.get(QueryCondition.QUERY_KEY_SERVER_ID));
		}
		int beginDate = Integer.parseInt(beginDateStr);
		int endDate = Integer.parseInt(endDateStr);

		LoginInfoReport report = null;

		if (serverId != -1) {
			report = getLoginInfoReportByServer(beginDate, endDate, serverId);
		} else {
			report = getLoginInfoReport(beginDate, endDate);
		}

		if (report.getLoginInfoReportDataList().size() == 0) {
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	private LoginInfoReport getLoginInfoReportByServer(int beginDate,
			int endDate, int serverId) {
		String selectSql = "select * from login_report_server where record_date >= ? and record_date <= ? and server_id = ?";

		LoginInfoReport report = new LoginInfoReport();

		List<LoginInfoReportData> list = new ArrayList<LoginInfoReportData>();

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
			int totalCount1 = 0, totalCount2 = 0, totalCountN = 0;

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					LoginInfoReportData data = new LoginInfoReportData();

					int date = rs.getInt("record_date");
					data.setReportDate(date + "");

					int login1 = rs.getInt("count_1");
					data.setLogin1Count(login1 + "");
					totalCount1 += login1;

					int login2 = rs.getInt("count_2");
					data.setLogin2Count(login2 + "");
					totalCount2 += login2;

					int loginN = rs.getInt("count_n");
					data.setLoginNCount(loginN + "");
					totalCountN += loginN;

					list.add(data);
				}

				report.setLoginInfoReportDataList(list);
				LoginInfoReportData totalData = new LoginInfoReportData();
				totalData.setReportDate("总计");
				totalData.setLogin1Count(totalCount1 + "");
				totalData.setLogin2Count(totalCount2 + "");
				totalData.setLoginNCount(totalCountN + "");
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
		if (report.getLoginInfoReportDataList() == null) {
			report.setLoginInfoReportDataList(list);
		}

		return report;
	}

	private LoginInfoReport getLoginInfoReport(int beginDate, int endDate) {
		String selectSql = "select * from login_report where record_date >= ? and record_date <= ?";

		LoginInfoReport report = new LoginInfoReport();

		List<LoginInfoReportData> list = new ArrayList<LoginInfoReportData>();

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
			int totalCount1 = 0, totalCount2 = 0, totalCountN = 0;

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					LoginInfoReportData data = new LoginInfoReportData();

					int date = rs.getInt("record_date");
					data.setReportDate(date + "");

					int login1 = rs.getInt("count_1");
					data.setLogin1Count(login1 + "");
					totalCount1 += login1;

					int login2 = rs.getInt("count_2");
					data.setLogin2Count(login2 + "");
					totalCount2 += login2;

					int loginN = rs.getInt("count_n");
					data.setLoginNCount(loginN + "");
					totalCountN += loginN;

					list.add(data);
				}

				report.setLoginInfoReportDataList(list);
				LoginInfoReportData totalData = new LoginInfoReportData();
				totalData.setReportDate("总计");
				totalData.setLogin1Count(totalCount1 + "");
				totalData.setLogin2Count(totalCount2 + "");
				totalData.setLoginNCount(totalCountN + "");
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
		if (report.getLoginInfoReportDataList() == null) {
			report.setLoginInfoReportDataList(list);
		}

		return report;
	}

	private LoginInfoReportData processDefaultNullTotalData() {
		LoginInfoReportData data = new LoginInfoReportData();
		data.setReportDate("暂无数据");
		data.setLogin1Count("暂无数据");
		data.setLogin2Count("暂无数据");
		data.setLoginNCount("暂无数据");
		return data;
	}

}
