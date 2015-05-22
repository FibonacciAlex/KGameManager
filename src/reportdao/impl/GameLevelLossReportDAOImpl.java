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
import data.reportdatas.GamePointConsumeRankReport;
import data.reportdatas.GamePointConsumeRankReportData;
import reportdao.GameLevelLossReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class GameLevelLossReportDAOImpl implements GameLevelLossReportDAO {
	private static final KGameLogger logger = KGameLogger
			.getLogger(GameLevelLossReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	public GameLevelLossReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
	}

	@Override
	public GameLevelLossReport getGameLevelLossReport(
			Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("GameLevelLossReport condition,key:" + key + ",value:"
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
		
		GameLevelLossReport report = getGameLevelLossReportByServer(beginDate, serverId);
		
		if(report.getGameLevelLossReportDataList().size()==0){
			report.setTotalData(processDefaultNullTotalData());
		}
		
		return report;
	}

	public GameLevelLossReport getGameLevelLossReportByServer(int beginDate,
			int serverId) {
		String selectSql = "select * from game_level_loss_report where record_date = ? and server_id = ? order by level_id asc";

		GameLevelLossReport report = new GameLevelLossReport();

		List<GameLevelLossReportData> list = new ArrayList<GameLevelLossReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, serverId);
			int totalLossCount = 0;

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					GameLevelLossReportData data = new GameLevelLossReportData();

					int levelId = rs.getInt("level_id");
					data.setLevelId(levelId + "");

					int lossCount = rs.getInt("lost_count");
					data.setLossCount(lossCount + "");
					totalLossCount += lossCount;

					String levelInfo = decodeLevelInfo(rs.getString("complete_level_info"));
					data.setLevelInfo(levelInfo);

					list.add(data);
				}

				report.setGameLevelLossReportDataList(list);
				GameLevelLossReportData totalData = new GameLevelLossReportData();
				totalData.setLevelId("总计");
				totalData.setLossCount("" + totalLossCount);
				totalData.setLevelInfo("－");
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
		if (report.getGameLevelLossReportDataList() == null) {
			report.setGameLevelLossReportDataList(list);
		}

		return report;
	}
	
	private String decodeLevelInfo(String info){
		if(info!=null&&info.split(";").length>0){
			StringBuffer buffer = new StringBuffer();
			String[] infoData = info.split(";");
			for (int i = 0; i < infoData.length; i++) {
				if(i!=0){
					buffer.append(";      ");
				}
				String[] datas = infoData[i].split(":");
				buffer.append(datas[0]+"级:"+datas[1]+"人");
			}
			return buffer.toString();
		}else{
			return "－";
		}
	}
	
	private GameLevelLossReportData processDefaultNullTotalData() {
		GameLevelLossReportData data = new GameLevelLossReportData();
		data.setLevelId("暂无数据");
		data.setLossCount("暂无数据");
		data.setLevelInfo("暂无数据");

		return data;
	}

}
