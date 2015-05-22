package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import data.reportdatas.GamePointStockRankReport;
import data.reportdatas.GamePointStockRankReportData;
import data.reportdatas.NewGuideReport;
import data.reportdatas.NewGuideReportData;
import reportdao.NewGuideReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class NewGuideReportDAOImpl implements NewGuideReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(NewGuideReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	public static Map<Integer, String> stepMap = new LinkedHashMap<Integer, String>();

	static {
		stepMap.put(1, " 插画开始");
		stepMap.put(2, " 插画结束");
		stepMap.put(3, " 进入战场");
		stepMap.put(4, " 操作引导结束");
		stepMap.put(5, " 第一波战斗结束");
		stepMap.put(6, " 第二波战斗结束");
		stepMap.put(7, " 坐骑剧情 ");
		stepMap.put(8, " 第三波战斗结束 ");
		stepMap.put(9, " 结束剧情");
		stepMap.put(10, "结算界面 ");
		stepMap.put(11, "进入第一个主城 ");
		stepMap.put(999, "完成新手引导");
	}

	public NewGuideReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
	}

	@Override
	public NewGuideReport getNewGuideReport(Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("NewGuideReport condition,key:" + key + ",value:"
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

		NewGuideReport report = getNewGuideReportByServer(beginDate, serverId);

		if (report.getNewGuideReportDataList().size() == 0) {
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	public NewGuideReport getNewGuideReportByServer(int beginDate, int serverId) {
		String selectSql = "select * from new_role_loss_report where record_date = ? and server_id = ?";

		NewGuideReport report = new NewGuideReport();

		List<NewGuideReportData> list = new ArrayList<NewGuideReportData>();

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

					String info = rs.getString("complete_info");
					if (info != null && info.split(",").length > 0) {
						String[] infoData = info.split(",");
						for (int i = 0; i < infoData.length; i++) {
							NewGuideReportData data = new NewGuideReportData();
							String[] datas = infoData[i].split(":");
							int stepId = Integer.parseInt(datas[0]);
							if (stepMap.containsKey(stepId)) {
								int lossCount = Integer.parseInt(datas[1]);
								totalRoleCount += lossCount;
								String stepInfo = "";

								stepInfo = stepMap.get(stepId);
								data.setStepId(stepId + "");
								data.setLossCount(lossCount + "");
								data.setStepInfo(stepInfo);
								list.add(data);
							}

						}
					}

				}

				report.setNewGuideReportDataList(list);
				NewGuideReportData totalData = new NewGuideReportData();
				totalData.setStepId("总计");
				totalData.setStepInfo("－");
				totalData.setLossCount("" + totalRoleCount);
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
		if (report.getNewGuideReportDataList() == null) {
			report.setNewGuideReportDataList(list);
		}

		return report;
	}

	private NewGuideReportData processDefaultNullTotalData() {
		NewGuideReportData data = new NewGuideReportData();
		data.setStepId("暂无数据");
		data.setLossCount("暂无数据");
		data.setStepInfo("暂无数据");

		return data;
	}

}
