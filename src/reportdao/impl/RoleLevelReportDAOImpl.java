package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.reportdatas.RoleLevelReport;
import data.reportdatas.RoleLevelReportData;
import data.reportdatas.RoleOccupationReport;
import data.reportdatas.RoleOccupationReportData;
import reportdao.RoleLevelReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class RoleLevelReportDAOImpl implements RoleLevelReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(RoleLevelReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	// private DefineDataSourceManagerIF platformPool;

	public RoleLevelReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		// platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
	}

	@Override
	public RoleLevelReport getRoleLevelReport(Map<String, String> condition) {
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

		// 解释condition条件的推广渠道ID
		int promoId = -1;
		if (condition.containsKey(QueryCondition.QUERY_KEY_PARENT_PROMO)) {
			promoId = Integer.parseInt(condition
					.get(QueryCondition.QUERY_KEY_PARENT_PROMO));
		}

		if (serverId == -1) {
			serverId = 1;
		}
		RoleLevelReport report = getRoleLevelReport(beginDate,serverId);
		if (report == null) {
			report = new RoleLevelReport();
			report.setGetRoleLevelReportDataList(new ArrayList<RoleLevelReportData>());
		}
		if (report.getTotalData() == null) {
			report.setTotalData(processDefaultNullTotalData());
		}
		// logger.error("List<DailyReportData>:::size:"+result.size());
		// report.setGetDailyReportDataList(result);
		return report;
	}

	private RoleLevelReport getRoleLevelReport(int beginDate,int serverId) {
		
		String selectSql = "select level_info from "
			+ "role_level_report where record_date=? and server_id=?";
		
		RoleLevelReport report = new RoleLevelReport();

		List<RoleLevelReportData> list = new ArrayList<RoleLevelReportData>();

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
			ps.setInt(2, serverId);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				
				String levelInfo = rs.getString("level_info");
				
				list = decodeLevelInfo(levelInfo);
				
				if(list.size()>0){
					
					List<RoleLevelReportData> dataList = list.subList(0, list.size()-1);
					
					RoleLevelReportData totalData = list.get(list.size()-1);
					
					report.setGetRoleLevelReportDataList(dataList);
					report.setTotalData(totalData);				
					

					return report;
				}
				
				
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getGetRoleLevelReportDataList() == null) {
			report.setGetRoleLevelReportDataList(list);
		}

		return report;
		
	}
	
	private List<RoleLevelReportData> decodeLevelInfo(String levelInfo){
		List<RoleLevelReportData> list = new ArrayList<RoleLevelReportData>();
		
		if(levelInfo!=null&&!levelInfo.equals("")){
			String[] levelInfoStr = levelInfo.split(",");
			if(levelInfoStr!=null&&levelInfoStr.length>0){
				int totalCount = 0; 
				for (int i = 0; i < levelInfoStr.length; i++) {
					String[] info = levelInfoStr[i].split(":");
					int level = Integer.parseInt(info[0]);
					int count = Integer.parseInt(info[1]);
					long time = Long.parseLong(info[2]);
					totalCount+=count;
					
					RoleLevelReportData data = new RoleLevelReportData();
					data.setLevel(level+"");
					data.setRoleCount(count+"");
					data.setUpGradeAvgTime(getFloatString((float)time/count/1000)+"");
					list.add(data);
				}
				
				if(list.size()>0){
					for (RoleLevelReportData data:list) {
						int count = Integer.parseInt(data.roleCount);
						data.setLevelRate(getFloatString((float)count*100/totalCount)+"%");
						
					}
					
					RoleLevelReportData totalData = new RoleLevelReportData();
					totalData.setLevel("合计");
					totalData.setRoleCount(totalCount+"");
					totalData.setLevelRate("－");
					list.add(totalData);
				}
			}
		}
		
		return list;
	}

	private RoleLevelReportData processDefaultNullTotalData() {
		RoleLevelReportData data = new RoleLevelReportData();
		data.setLevel("暂无数据");
		data.setLevelRate("暂无数据");
		data.setLevel("暂无数据");
		return data;
	}
	
	private String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.000");
		return fnum.format(value);
	}

}
