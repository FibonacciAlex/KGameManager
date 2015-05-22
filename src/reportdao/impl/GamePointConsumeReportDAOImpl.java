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
import java.util.List;
import java.util.Map;

import data.reportdatas.DailyReport;
import data.reportdatas.DailyReportData;
import data.reportdatas.GamePointConsumeReport;
import data.reportdatas.GamePointConsumeReportData;
import data.reportdatas.Report;
import reportdao.DailyReportDAO;
import reportdao.GamePointConsumeReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.reportCache.ReportCacheManager;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;
import reportdao.util.DateUtil;

public class GamePointConsumeReportDAOImpl implements GamePointConsumeReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(GamePointConsumeReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;
	
	private static Map<String, Boolean> sortKeyMap;

	public GamePointConsumeReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		initSortKeyMap();
	}

	@Override
	public GamePointConsumeReport getGamePointConsumeReport(
			Map<String, String> condition) {

		for (String key : condition.keySet()) {
			logger.error("getGamePointConsumeReport condition,key:" + key + ",value:"
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
		
		if(ReportCacheManager.isReportExist(Report.REPORT_TYPE_GAME_POINT_CONSUME, condition)){
			GamePointConsumeReport report = (GamePointConsumeReport)(ReportCacheManager.getReport(Report.REPORT_TYPE_GAME_POINT_CONSUME, condition));
			return sortReport(report, condition.get("sortname"), condition.get("sortorder"));
		}
		

		GamePointConsumeReport report = null;
		if (serverId == -1 && parentPromoId == -1) {
			report = getGamePointConsumeReport(beginDate,endDate);
		} else if (serverId > -1 && parentPromoId == -1) {
			report = getGamePointConsumeReportByServer(beginDate,endDate,
					serverId);
		} else if (serverId == -1 && parentPromoId > -1) {
			report = getGamePointConsumeReportByPromo(beginDate,endDate,
					parentPromoId);
		} else {
			report = getGamePointConsumeReportByServerAndPromo(beginDate,endDate,
					serverId, parentPromoId);
		}
		if(report == null){
			report = new GamePointConsumeReport();
			report.setGetGamePointConsumeReportList(new ArrayList<GamePointConsumeReportData>());
		}
		if(report.getTotalData()==null){
			report.setTotalData(processDefaultNullTotalData());
		}
		
		ReportCacheManager.addReport(Report.REPORT_TYPE_GAME_POINT_CONSUME, condition, report);
		
		return sortReport(report, condition.get("sortname"), condition.get("sortorder"));
	}
	
	private static void initSortKeyMap() {
		if (sortKeyMap == null) {
			sortKeyMap = new HashMap<String, Boolean>();
			sortKeyMap.put(GamePointConsumeReportDAO.SORT_KEY_CONSUME_COUNT, true);
			sortKeyMap.put(GamePointConsumeReportDAO.SORT_KEY_CONSUME_POINT, true);
			sortKeyMap.put(GamePointConsumeReportDAO.SORT_KEY_CONSUME_RATE, true);
		}
	}

	private GamePointConsumeReport getGamePointConsumeReport(int beginDate,int endDate) {

		String selectSql = "select fun_name,sum(total_consume_count) sc,sum(total_consume_point) sp," +
				"sum(role_count) cu from " +
				"fun_point_consume_report where record_date>=? and record_date<=? group by fun_type";

		GamePointConsumeReport report = new GamePointConsumeReport();

		List<GamePointConsumeReportData> list = new ArrayList<GamePointConsumeReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;
		int totalRoleCount = 0;

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
					GamePointConsumeReportData data = new GamePointConsumeReportData();

					String funName = rs.getString("fun_name");
					data.setConsumeTypeName(funName);

					int totalConsumeCount = rs.getInt("sc");
					data.setConsumeCount(totalConsumeCount + "");
					totalCount += totalConsumeCount;

					int totalConsumePoint = rs.getInt("sp");
					data.setConsumePoint(totalConsumePoint + "");
					totalPoint += totalConsumePoint;
					
					int role = rs.getInt("cu");
					data.setRoleCount(role + "");
					totalRoleCount += role;

					list.add(data);
				}
				
				
				report.setGetGamePointConsumeReportList(list);
				GamePointConsumeReportData totalData = new GamePointConsumeReportData();
				totalData.setConsumeTypeName("总计");
				totalData.consumeCount = totalCount+"";
				totalData.consumePoint = totalPoint + "";
				totalData.consumeRate = "－";
				totalData.roleCount = totalRoleCount +"";
				report.setTotalData(totalData);
				
				for (GamePointConsumeReportData data:list) {
					float rate = (float)(Integer.parseInt(data.consumePoint))*100/(Integer.parseInt(totalData.consumePoint));
					data.setConsumeRate(getFloatString(rate)+"%");
				}
				
				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if(report.getGetGamePointConsumeReportList()==null){
			report.setGetGamePointConsumeReportList(list);
		}

		return report;
	}

	private GamePointConsumeReport getGamePointConsumeReportByServerAndPromo(
			int beginDate,int endDate, int serverId, int parentPromoId) {
		String selectSql = "select fun_name,sum(total_consume_count) sc,sum(total_consume_point) sp," +
				"sum(role_count) cu from " +
				"fun_point_consume_report_server where record_date>=? and record_date<=? "
				+ "and server_id=? and parent_promo_id=? group by fun_type";

		GamePointConsumeReport report = new GamePointConsumeReport();

		List<GamePointConsumeReportData> list = new ArrayList<GamePointConsumeReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;
		int totalRoleCount = 0;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);
			ps.setInt(3, serverId);
			ps.setInt(4, parentPromoId);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					GamePointConsumeReportData data = new GamePointConsumeReportData();

					String funName = rs.getString("fun_name");
					data.setConsumeTypeName(funName);

					int totalConsumeCount = rs.getInt("total_consume_count");
					data.setConsumeCount(totalConsumeCount + "");
					totalCount += totalConsumeCount;

					int totalConsumePoint = rs.getInt("total_consume_point");
					data.setConsumePoint(totalConsumePoint + "");
					totalPoint += totalConsumePoint;
					
					int role = rs.getInt("cu");
					data.setRoleCount(role + "");
					totalRoleCount += role;

					list.add(data);
				}
				report.setGetGamePointConsumeReportList(list);
				GamePointConsumeReportData totalData = new GamePointConsumeReportData();
				totalData.setConsumeTypeName("总计");
				totalData.consumeCount = totalCount+"";
				totalData.consumePoint = totalPoint + "";
				totalData.consumeRate = "－";
				totalData.roleCount = totalRoleCount +"";
				report.setTotalData(totalData);
				
				for (GamePointConsumeReportData data:list) {
					float rate = (float)(Integer.parseInt(data.consumePoint))*100/(Integer.parseInt(totalData.consumePoint));
					data.setConsumeRate(getFloatString(rate)+"%");
				}
				
				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if(report.getGetGamePointConsumeReportList()==null){
			report.setGetGamePointConsumeReportList(list);
		}

		return report;
	}

	private GamePointConsumeReport getGamePointConsumeReportByServer(
			int beginDate,int endDate, int serverId) {
		String selectSql = "select fun_name,sum(total_consume_count) sc,sum(total_consume_point) sp," +
				"sum(role_count) cu from "
				+ "fun_point_consume_report_server where record_date>=? and record_date<=? "
				+ "and server_id=? group by fun_type";

		GamePointConsumeReport report = new GamePointConsumeReport();

		List<GamePointConsumeReportData> list = new ArrayList<GamePointConsumeReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;
		int totalRoleCount = 0;

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
					GamePointConsumeReportData data = new GamePointConsumeReportData();

					String funName = rs.getString("fun_name");
					data.setConsumeTypeName(funName);

					int totalConsumeCount = rs.getInt("sc");
					data.setConsumeCount(totalConsumeCount + "");
					totalCount += totalConsumeCount;

					int totalConsumePoint = rs.getInt("sp");
					data.setConsumePoint(totalConsumePoint + "");
					totalPoint += totalConsumePoint;
					
					int role = rs.getInt("cu");
					data.setRoleCount(role + "");
					totalRoleCount += role;

					list.add(data);
				}
				report.setGetGamePointConsumeReportList(list);
				GamePointConsumeReportData totalData = new GamePointConsumeReportData();
				totalData.setConsumeTypeName("总计");
				totalData.consumeCount = totalCount+"";
				totalData.consumePoint = totalPoint + "";
				totalData.consumeRate = "－";
				totalData.roleCount = totalRoleCount +"";
				report.setTotalData(totalData);
				
				for (GamePointConsumeReportData data:list) {
					float rate = (float)(Integer.parseInt(data.consumePoint))*100/(Integer.parseInt(totalData.consumePoint));
					data.setConsumeRate(getFloatString(rate)+"%");
				}
				
				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if(report.getGetGamePointConsumeReportList()==null){
			report.setGetGamePointConsumeReportList(list);
		}

		return report;
	}

	private GamePointConsumeReport getGamePointConsumeReportByPromo(
			int beginDate,int endDate, int parentPromoId) {
		String selectSql = "select fun_name,sum(total_consume_count) sc,sum(total_consume_point) sp," +
				"sum(role_count) cu from" +
				" fun_point_consume_report_server where record_date>=? and record_date<=? "
				+ "and parent_promo_id=? group by fun_type";

		GamePointConsumeReport report = new GamePointConsumeReport();

		List<GamePointConsumeReportData> list = new ArrayList<GamePointConsumeReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;
		int totalRoleCount = 0;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);
			ps.setInt(3, parentPromoId);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					GamePointConsumeReportData data = new GamePointConsumeReportData();

					String funName = rs.getString("fun_name");
					data.setConsumeTypeName(funName);

					int totalConsumeCount = rs.getInt("sc");
					data.setConsumeCount(totalConsumeCount + "");
					totalCount += totalConsumeCount;

					int totalConsumePoint = rs.getInt("sp");
					data.setConsumePoint(totalConsumePoint + "");
					totalPoint += totalConsumePoint;
					
					int role = rs.getInt("cu");
					data.setRoleCount(role + "");
					totalRoleCount += role;

					list.add(data);
				}
				report.setGetGamePointConsumeReportList(list);
				GamePointConsumeReportData totalData = new GamePointConsumeReportData();
				totalData.setConsumeTypeName("总计");
				totalData.consumeCount = totalCount+"";
				totalData.consumePoint = totalPoint + "";
				totalData.consumeRate = "－";
				totalData.roleCount = totalRoleCount +"";
				report.setTotalData(totalData);
				
				for (GamePointConsumeReportData data:list) {
					float rate = (float)(Integer.parseInt(data.consumePoint))*100/(Integer.parseInt(totalData.consumePoint));
					data.setConsumeRate(getFloatString(rate)+"%");
				}
				
				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if(report.getGetGamePointConsumeReportList()==null){
			report.setGetGamePointConsumeReportList(list);
		}

		return report;
	}
	
	private GamePointConsumeReportData processDefaultNullTotalData() {
		GamePointConsumeReportData data = new GamePointConsumeReportData();
		data.setConsumeTypeName("暂无数据");
		data.setConsumeCount("暂无数据");
		data.setConsumePoint("暂无数据");
		data.setConsumeRate("暂无数据");

		return data;
	}
	
	private String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.000");
		return fnum.format(value);
	}
	
	public GamePointConsumeReport sortReport(GamePointConsumeReport sourceReport,
			final String sortKey, final String sortType) {
		if (!sortKeyMap.containsKey(sortKey)) {
			return sourceReport;
		}

		// if(sortKey.equals(DailyReportDAO.SORT_KEY_CHARGE_MONEY)){
		List<GamePointConsumeReportData> list = new ArrayList<GamePointConsumeReportData>(
				sourceReport.getGamePointConsumeReportList);
		Collections.sort(list, new Comparator<GamePointConsumeReportData>() {

			@Override
			public int compare(GamePointConsumeReportData o1, GamePointConsumeReportData o2) {
				double n1 = 0f;
				double n2 = 0f;			
				

				if (sortKey.equals(GamePointConsumeReportDAO.SORT_KEY_CONSUME_COUNT)) {
					
					n1 = (o1.consumeCount.equals("－"))?Double.MAX_VALUE:Double.parseDouble(o1.consumeCount);
					n2 = (o2.consumeCount.equals("－"))?Double.MAX_VALUE:Double.parseDouble(o2.consumeCount);
				} else if (sortKey
						.equals(GamePointConsumeReportDAO.SORT_KEY_CONSUME_RATE)) {
					n1 = (o1.consumeRate.equals("－"))?Double.MAX_VALUE:Double.parseDouble(o1.consumeRate.substring(0,
							o1.consumeRate.indexOf("%")));
					n2 = (o2.consumeRate.equals("－"))?Double.MAX_VALUE:Double.parseDouble(o2.consumeRate.substring(0,
							o2.consumeRate.indexOf("%")));
				} else if (sortKey.equals(GamePointConsumeReportDAO.SORT_KEY_CONSUME_POINT)) {
					n1 = (o1.consumePoint.equals("－"))?Double.MAX_VALUE:Double.parseDouble(o1.consumePoint);
					n2 = (o2.consumePoint.equals("－"))?Double.MAX_VALUE:Double.parseDouble(o2.consumePoint);
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

		GamePointConsumeReport report = new GamePointConsumeReport();
		report.setGetGamePointConsumeReportList(list);
		report.setTotalData(sourceReport.getTotalData());
		return report;
	}

}
