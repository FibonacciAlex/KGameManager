package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.reportdatas.FirstChargeReport;
import data.reportdatas.FirstChargeUserInfo;
import data.reportdatas.FirstFunConsumeReport;
import data.reportdatas.FirstFunConsumeReportData;
import data.reportdatas.GamePointStockRankReport;
import data.reportdatas.GamePointStockRankReportData;
import reportdao.FirstChargeReportDAO;
import reportdao.FirstFunConsumeReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class FirstFunConsumeReportDAOImpl implements FirstFunConsumeReportDAO{
	private static final KGameLogger logger = KGameLogger
			.getLogger(FirstFunConsumeReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;
	
	private static Map<String, Boolean> sortKeyMap;

	public FirstFunConsumeReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		
		initSortKeyMap();
	}
	
	private static void initSortKeyMap() {
		if (sortKeyMap == null) {
			sortKeyMap = new HashMap<String, Boolean>();
			sortKeyMap.put(FirstFunConsumeReportDAO.SORT_KEY_ROLE_COUNT, true);
			sortKeyMap.put(FirstFunConsumeReportDAO.SORT_KEY_CONSUME_POINT, true);
		}
	}
	@Override
	public FirstFunConsumeReport getFirstFunConsumeReport(
			Map<String, String> condition) {
		
		for (String key : condition.keySet()) {
			logger.error("getFirstChargeReport condition,key:" + key
					+ ",value:" + condition.get(key));
		}

		// 解释condition条件的开始日期
		String beginDateStr = condition
				.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		// 解释condition条件的游戏区ID
		int serverId = -1;
		if (condition.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
			serverId = Integer.parseInt(condition
					.get(QueryCondition.QUERY_KEY_SERVER_ID));
		}
		int beginDate = Integer.parseInt(beginDateStr);

		FirstFunConsumeReport report = getFirstFunConsumeReportByServer(beginDate, serverId);
		
		if(report.getFirstFunConsumeReportDataList().size()==0){
			report.setTotalData(processDefaultNullTotalData());
		}
		
		return sortReport(report, condition.get("sortname"), condition.get("sortorder"));
	}
	
	public FirstFunConsumeReport getFirstFunConsumeReportByServer(int beginDate,int serverId){
		
        String selectSql = "select * from first_fun_consume_rank_report_server where record_date = ? and server_id = ?";
		
        FirstFunConsumeReport report = new FirstFunConsumeReport();

		List<FirstFunConsumeReportData> list = new ArrayList<FirstFunConsumeReportData>();
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalConsumeCount = 0;
		
		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, serverId);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					FirstFunConsumeReportData data = new FirstFunConsumeReportData();

					
					String funName = rs.getString("fun_name");
					data.setFunName(funName);

					long roleCount = rs.getLong("total_consume_count");
					data.setRoleCount(roleCount + "");

					int consumePoint = rs.getInt("total_consume_point");
					data.setConsumePoint(consumePoint + "");
					totalConsumeCount+=consumePoint;

					list.add(data);
				}
				
				
				report.setFirstFunConsumeReportDataList(list);
				FirstFunConsumeReportData totalData = new FirstFunConsumeReportData();
				totalData.setFunName("总计");
				totalData.setRoleCount("－");
				totalData.setConsumePoint(""+totalConsumeCount);
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
		if(report.getFirstFunConsumeReportDataList()==null){
			report.setFirstFunConsumeReportDataList(list);
		}

		return report;
	}
	
	private FirstFunConsumeReportData processDefaultNullTotalData() {
		FirstFunConsumeReportData data = new FirstFunConsumeReportData();
		data.setFunName("暂无数据");
		data.setRoleCount("暂无数据");
		data.setConsumePoint("暂无数据");

		return data;
	}
	
	public FirstFunConsumeReport sortReport(FirstFunConsumeReport sourceReport,
			final String sortKey, final String sortType) {
		if (!sortKeyMap.containsKey(sortKey)) {
			return sourceReport;
		}

		// if(sortKey.equals(DailyReportDAO.SORT_KEY_CHARGE_MONEY)){
		List<FirstFunConsumeReportData> list = new ArrayList<FirstFunConsumeReportData>(
				sourceReport.firstFunConsumeReportDataList);
		Collections.sort(list, new Comparator<FirstFunConsumeReportData>() {

			@Override
			public int compare(FirstFunConsumeReportData o1, FirstFunConsumeReportData o2) {
				double n1 = 0f;
				double n2 = 0f;			
				

				if (sortKey.equals(FirstFunConsumeReportDAO.SORT_KEY_ROLE_COUNT)) {
					
					n1 = (o1.roleCount.equals("－"))?Double.MAX_VALUE:Double.parseDouble(o1.roleCount);
					n2 = (o2.roleCount.equals("－"))?Double.MAX_VALUE:Double.parseDouble(o2.roleCount);
				} else if (sortKey.equals(FirstFunConsumeReportDAO.SORT_KEY_CONSUME_POINT)) {
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

		FirstFunConsumeReport report = new FirstFunConsumeReport();
		report.setFirstFunConsumeReportDataList(list);
		report.setTotalData(sourceReport.getTotalData());
		return report;
	}

}
