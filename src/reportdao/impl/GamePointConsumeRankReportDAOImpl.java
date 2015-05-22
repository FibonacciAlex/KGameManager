package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.reportdatas.GamePointConsumeRankReport;
import data.reportdatas.GamePointConsumeRankReportData;
import data.reportdatas.GamePointConsumeReport;
import data.reportdatas.GamePointConsumeReportData;
import reportdao.GamePointConsumeRankReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class GamePointConsumeRankReportDAOImpl implements GamePointConsumeRankReportDAO{

	private static final KGameLogger logger = KGameLogger
			.getLogger(GamePointConsumeReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;
	
	public GamePointConsumeRankReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
	}
	
	@Override
	public GamePointConsumeRankReport getGamePointConsumeRankReport(
			Map<String, String> condition) {
		
		for (String key : condition.keySet()) {
			logger.error("getGamePointConsumeReport condition,key:" + key + ",value:"
					+ condition.get(key));
		}

		// 解释condition条件的开始日期
		String beginDateStr = condition
				.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		
		int serverId = -1;
		if (condition.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
			serverId = Integer.parseInt(condition
					.get(QueryCondition.QUERY_KEY_SERVER_ID));
		}
		int beginDate = Integer.parseInt(beginDateStr);
		
		GamePointConsumeRankReport report = getGamePointConsumeRankReportByServer(beginDate, serverId);
		
		if(report.getGamePointConsumeRankReportList().size()==0){
			report.setTotalData(processDefaultNullTotalData());
		}
		
		return report;
	}
	
	public GamePointConsumeRankReport getGamePointConsumeRankReportByServer(int beginDate,int serverId){
		String selectSql = "select * from role_consumt_point_rank_report where record_date = ? and server_id = ? order by rank_num asc";
		
		GamePointConsumeRankReport report = new GamePointConsumeRankReport();

		List<GamePointConsumeRankReportData> list = new ArrayList<GamePointConsumeRankReportData>();
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalRoleCount = 0;
		
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
					GamePointConsumeRankReportData data = new GamePointConsumeRankReportData();

					int rank = rs.getInt("rank_num");
					data.setRank(rank+"");
					
					String roleName = rs.getString("role_name");
					data.setRoleName(roleName);

					long roleId = rs.getLong("role_id");
					data.setRoleId(roleId + "");

					int consumePoint = rs.getInt("consume_point");
					data.setConsumePoint(consumePoint + "");
					totalRoleCount+=consumePoint;
					
					data.setCreateTime(rs.getTimestamp("register_time").toString());
					data.setLastLoginTime(rs.getTimestamp("lastest_login_time").toString());

					list.add(data);
				}
				
				
				report.setGamePointConsumeRankReportList(list);
				GamePointConsumeRankReportData totalData = new GamePointConsumeRankReportData();
				totalData.setRank("总计");
				totalData.setRoleId("－");
				totalData.setRoleName("－");
				totalData.setLastLoginTime("－");
				totalData.setCreateTime("－");
				totalData.setConsumePoint(totalRoleCount +"");
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
		if(report.getGamePointConsumeRankReportList()==null){
			report.setGamePointConsumeRankReportList(list);
		}

		return report;
	}
	
	private GamePointConsumeRankReportData processDefaultNullTotalData() {
		GamePointConsumeRankReportData data = new GamePointConsumeRankReportData();
		data.setRank("暂无数据");
		data.setRoleId("暂无数据");
		data.setConsumePoint("暂无数据");
		data.setRoleName("暂无数据");
		data.setCreateTime("暂无数据");
		data.setLastLoginTime("暂无数据");

		return data;
	}

}
