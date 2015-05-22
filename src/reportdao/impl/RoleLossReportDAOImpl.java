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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import data.reportdatas.DailyReport;
import data.reportdatas.DailyReportData;
import data.reportdatas.RoleLossReport;
import data.reportdatas.RoleLossReportData;
import data.reportdatas.VIPReport;
import data.reportdatas.VIPReportData;
import reportdao.DailyReportDAO;
import reportdao.RoleLossReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.VIPReportDAOImpl.VipData;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class RoleLossReportDAOImpl implements RoleLossReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(RoleLossReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	private static Map<String, Boolean> sortKeyMap;

	// private DefineDataSourceManagerIF platformPool;

	public RoleLossReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		// platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
		initSortKeyMap();
	}

	private static void initSortKeyMap() {
		if (sortKeyMap == null) {
			sortKeyMap = new HashMap<String, Boolean>();
			sortKeyMap.put(RoleLossReportDAO.SORT_KEY_RANK, true);
			sortKeyMap.put(RoleLossReportDAO.SORT_KEY_COMPLETE_COUNT, true);
			sortKeyMap.put(RoleLossReportDAO.SORT_KEY_COMPLETE_RATE, true);
			sortKeyMap.put(RoleLossReportDAO.SORT_KEY_MISSION_ID, true);
		}
	}

	@Override
	public RoleLossReport getRoleLossReport(Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("getRoleOccupationReport condition,key:" + key
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

		RoleLossReport report = null;
		report = getRoleLossReportbyServer(beginDate, serverId);

		if (report == null) {
			report = new RoleLossReport();
			report.setRoleLossReportList(new ArrayList<RoleLossReportData>());
		}
		
		if (report.getTotalData() == null) {
			report.setTotalData(processDefaultNullTotalData());
		}
		// logger.error("List<DailyReportData>:::size:"+result.size());
		// report.setGetDailyReportDataList(result);
		return sortReport(report, condition.get("sortname"),
				condition.get("sortorder"));
	}

	public RoleLossReport getRoleLossReportbyServer(int beginDate, int serverId) {
		String selectSql = "select * from "
				+ "role_loss_report where record_date=? and server_id=?";

		RoleLossReport report = new RoleLossReport();

		List<RoleLossReportData> list = new ArrayList<RoleLossReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalVip = 0;
		int totalLogin = 0;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, serverId);

			rs = bgPool.executeQuery(ps);

			int unReceiveCount = 0;
			String completeInfo = null;
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {

					unReceiveCount = rs.getInt("unreceive_mission_count");

					completeInfo = rs.getString("complete_mission_info");
				}
			}

			if (completeInfo != null) {

				RoleLossReportData udata = new RoleLossReportData();
				udata.setRank("1");
				udata.setMissionId("未接任务");
				udata.setCompleteCount("" + unReceiveCount);
				udata.setCompleteRate("—");
				udata.rate = Float.MAX_VALUE;
				list.add(udata);

				String[] MInfo = completeInfo.split(",");
				for (int i = 0; i < MInfo.length; i++) {
					String[] info = MInfo[i].split(":");
					RoleLossReportData data = new RoleLossReportData();
					// data.setRank(""+rank);
					data.setMissionId(info[0]);
					data.setCompleteCount(info[1]);
					int completeCount = Integer.parseInt(info[1]);
					int lvCount = Integer.parseInt(info[2]);
					float rate = 0f;
					if (lvCount > 0) {
						rate = ((float) completeCount * 100) / lvCount;
					}
					data.setCompleteRate(getFloatString(rate) + "%");
					data.rate = rate;
					list.add(data);
				}

				Collections.sort(list, new Comparator<RoleLossReportData>() {

					@Override
					public int compare(RoleLossReportData o1,
							RoleLossReportData o2) {
						double n1 = 0f;
						double n2 = 0f;

						n1 = o1.rate;
						n2 = o2.rate;

						if (n1 > n2)
							return -1;
						else if (n1 < n2)
							return 1;
						return 0;

					}
				});
				
				int rank = 1;
				for (RoleLossReportData data:list) {
					data.setRank(rank+"");
					rank++;
				}

			}
			report.setRoleLossReportList(list);

			return report;
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getRoleLossReportList() == null) {
			report.setRoleLossReportList(list);
		}

		return report;
	}

	private int[] decodeMissionInfo(String missionInfo) {
		int[] result = new int[0];
		if (missionInfo != null && missionInfo.length() > 0) {
			String[] mStr = missionInfo.split(",");
			if (mStr != null && mStr.length > 0) {
				result = new int[mStr.length];
				for (int i = 0; i < mStr.length; i++) {
					String[] info = mStr[i].split(":");
					if (info != null && info.length > 0) {
						result[i] = Integer.parseInt(info[1]);
					}
				}
			}
		}
		return result;
	}

	public RoleLossReportData processDefaultNullTotalData() {
		RoleLossReportData data = new RoleLossReportData();
		data.setRank("暂无数据");
		data.setMissionId("暂无数据");
		data.setCompleteCount("暂无数据");
		data.setCompleteRate("暂无数据");

		return data;
	}

	public RoleLossReport sortReport(RoleLossReport sourceReport,
			final String sortKey, final String sortType) {
		if (!sortKeyMap.containsKey(sortKey)) {
			return sourceReport;
		}

		// if(sortKey.equals(DailyReportDAO.SORT_KEY_CHARGE_MONEY)){
		List<RoleLossReportData> list = new ArrayList<RoleLossReportData>(
				sourceReport.roleLossReportList);
		Collections.sort(list, new Comparator<RoleLossReportData>() {

			@Override
			public int compare(RoleLossReportData o1, RoleLossReportData o2) {
				double n1 = 0f;
				double n2 = 0f;

				if (sortKey.equals(RoleLossReportDAO.SORT_KEY_RANK)) {

					n1 = (o1.rank.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o1.rank);
					n2 = (o2.rank.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o2.rank);
				} else if (sortKey
						.equals(RoleLossReportDAO.SORT_KEY_COMPLETE_COUNT)) {
					n1 = (o1.completeCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.completeCount);
					n2 = (o2.completeCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.completeCount);
				} else if (sortKey
						.equals(RoleLossReportDAO.SORT_KEY_COMPLETE_RATE)) {
					n1 = o1.rate;
					n2 = o2.rate;
				}else if (sortKey
						.equals(RoleLossReportDAO.SORT_KEY_MISSION_ID)) {
					n1 = (o1.missionId.equals("－")||o1.missionId.equals("未接任务")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.missionId);
					n2 = (o2.missionId.equals("－")||o2.missionId.equals("未接任务")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.missionId);
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

		RoleLossReport report = new RoleLossReport();
		report.setRoleLossReportList(list);
		report.setTotalData(sourceReport.totalData);
		return report;
		// }

		// return sourceReport;
	}

	private String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.000");
		return fnum.format(value);
	}

}
