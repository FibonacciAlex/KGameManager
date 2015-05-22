package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.reportdatas.GameCopperStockReport;
import data.reportdatas.GameCopperStockReportData;
import reportdao.GameCopperStockReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class GameCopperStockReportDAOImpl implements GameCopperStockReportDAO {


	private static final KGameLogger logger = KGameLogger
			.getLogger(GameCopperStockReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	// private DefineDataSourceManagerIF platformPool;

	public GameCopperStockReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		// platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
	}

	@Override
	public GameCopperStockReport getGameCopperStockReport(
			Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("GameCopperStockReportDAOImpl condition,key:" + key + ",value:"
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

		if (serverId == -1) {
			serverId = 188;
		}
		GameCopperStockReport report = getGameCopperStockReport(beginDate,
				endDate, serverId);
		if (report == null) {
			report = new GameCopperStockReport();
			report.setGameCopperStockReportList(new ArrayList<GameCopperStockReportData>());
		}
		if (report.getTotalData() == null) {
			report.setTotalData(processDefaultNullTotalData());
		}
		// logger.error("List<DailyReportData>:::size:"+result.size());
		// report.setGetDailyReportDataList(result);
		return report;
	}

	private GameCopperStockReport getGameCopperStockReport(int beginDate,
			int endDate, int serverId) {
		String selectSql = "select * from "
				+ "copper_stock_report where record_date>=? and record_date<=? and server_id=?";

		GameCopperStockReport report = new GameCopperStockReport();

		List<GameCopperStockReportData> list = new ArrayList<GameCopperStockReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long totalProduceCopper = 0;
		long totalConsumeCopper = 0;
		long totalStockCopper = 0;

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
					GameCopperStockReportData data = new GameCopperStockReportData();
					data.setReportDate(rs.getInt("record_date") + "");
					long produceCopper = rs.getLong("increased_copper");
					totalProduceCopper += produceCopper;
					data.setProduceCopper(produceCopper + "");

					long consumeCopper = rs.getLong("consume_copper");
					totalConsumeCopper += consumeCopper;
					data.setConsumeCopper(consumeCopper + "");

					long stockCopper = rs.getLong("stock_copper");
					totalStockCopper = stockCopper;
					data.setStockCopper(stockCopper + "");

					list.add(data);
				}

				report.setGameCopperStockReportList(list);
				GameCopperStockReportData totalData = new GameCopperStockReportData();
				totalData.setReportDate("总计");
				totalData.produceCopper = totalProduceCopper + "";
				totalData.consumeCopper = totalConsumeCopper + "";
				totalData.stockCopper = totalStockCopper + "";
				report.setTotalData(totalData);
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getGameCopperStockReportList() == null) {
			report.setGameCopperStockReportList(list);
		}

		return report;
	}

	private GameCopperStockReportData processDefaultNullTotalData() {
		GameCopperStockReportData data = new GameCopperStockReportData();
		data.setReportDate("暂无数据");
		data.setProduceCopper("暂无数据");
		data.setConsumeCopper("暂无数据");
		data.setStockCopper("暂无数据");
		data.setTotalData(true);
		return data;
	}

}
