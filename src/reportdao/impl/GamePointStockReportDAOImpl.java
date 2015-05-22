package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.reportdatas.GamePointConsumeReportData;
import data.reportdatas.GamePointStockReport;
import data.reportdatas.GamePointStockReportData;
import data.reportdatas.Report;
import data.reportdatas.RoleLevelReport;
import data.reportdatas.RoleLevelReportData;
import reportdao.GamePointConsumeReportDAO;
import reportdao.GamePointStockReportDAO;
import reportdao.ShopSalesReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.reportCache.ReportCacheManager;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class GamePointStockReportDAOImpl implements GamePointStockReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(GamePointStockReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	// private DefineDataSourceManagerIF platformPool;
	
	private static Map<String, Boolean> sortKeyMap;

	public GamePointStockReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		// platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
	}
	
	

	@Override
	public GamePointStockReport getGamePointStockReport(
			Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("getRoleLevelReport condition,key:" + key + ",value:"
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

		
		
		GamePointStockReport report = getGamePointStockReport(beginDate,endDate,serverId);
		if (report == null) {
			report = new GamePointStockReport();
			report.setGamePointStockReportDataList(new ArrayList<GamePointStockReportData>());
		}
		if (report.getTotalData() == null) {
			report.setTotalData(processDefaultNullTotalData());
		}

		
		return report;
	}
	
	private GamePointStockReport getGamePointStockReport(int beginDate,int endDate,int serverId){
		String selectSql = "select * from "
			+ "game_point_stock_report where record_date>=? and record_date<=? and server_id=?";
		
		GamePointStockReport report = new GamePointStockReport();

		List<GamePointStockReportData> list = new ArrayList<GamePointStockReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long totalChargePoint = 0;
		long totalPresentPoint = 0;
		long totalConsumePoint = 0;
		long totalStockPoint = 0;

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
				while(rs.next()){
					GamePointStockReportData data = new GamePointStockReportData();
					data.setReportDate(rs.getInt("record_date")+"");
					long chargePoint = rs.getLong("increased_charge_point");
					totalChargePoint+=chargePoint;
					data.setChargePoint(chargePoint+"");
					
					long presentPoint = rs.getLong("increased_present_point");
					totalPresentPoint+=presentPoint;
					data.setPresentPoint(presentPoint+"");
					
					long consumePoint = rs.getLong("consume_point");
					totalConsumePoint+=consumePoint;
					data.setConsumePoint(consumePoint+"");
					
					long stockPoint = rs.getLong("stock_point");
					totalStockPoint=stockPoint;
					data.setStockPoint(stockPoint+"");
					
					list.add(data);
				}
				
				report.setGamePointStockReportDataList(list);
				GamePointStockReportData totalData = new GamePointStockReportData();
				totalData.setReportDate("总计");
				totalData.chargePoint = totalChargePoint+"";
				totalData.presentPoint = totalPresentPoint+"";
				totalData.consumePoint = totalConsumePoint + "";
				totalData.stockPoint = totalStockPoint+"";
				report.setTotalData(totalData);
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getGamePointStockReportDataList() == null) {
			report.setGamePointStockReportDataList(list);
		}

		return report;
	}
	
	private GamePointStockReportData processDefaultNullTotalData(){
		GamePointStockReportData data = new GamePointStockReportData();
		data.setReportDate("暂无数据");
		data.setChargePoint("暂无数据");
		data.setPresentPoint("暂无数据");
		data.setConsumePoint("暂无数据");
		data.setStockPoint("暂无数据");
		data.setTotalData(true);
		return data;
	}

}
