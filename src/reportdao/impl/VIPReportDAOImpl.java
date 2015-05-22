package reportdao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.bind.v2.runtime.output.Encoded;

import data.reportdatas.RoleOccupationReport;
import data.reportdatas.RoleOccupationReportData;
import data.reportdatas.VIPReport;
import data.reportdatas.VIPReportData;
import reportdao.VIPReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;

public class VIPReportDAOImpl implements VIPReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(VIPReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	// private DefineDataSourceManagerIF platformPool;

	public VIPReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		// platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
	}

	@Override
	public VIPReport getVIPReport(Map<String, String> condition) {
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


		VIPReport report = null;
		if (serverId == -1) {
			serverId = 188;
		}
		report = getVIPReportByServer(beginDate, serverId);

		if (report == null) {
			report = new VIPReport();
			report.setGetVIPReportDataList(new ArrayList<VIPReportData>());
		}
		if (report.getTotalData() == null) {
			report.setTotalData(processDefaultNullTotalData());
		}
		// logger.error("List<DailyReportData>:::size:"+result.size());
		// report.setGetDailyReportDataList(result);
		return report;
	}

	private VIPReport getVIPReportByServer(int beginDate, int serverId) {
		String selectSql = "select vip_info from "
				+ "vip_report where record_date=? and server_id=?";

		VIPReport report = new VIPReport();

		List<VIPReportData> list = new ArrayList<VIPReportData>();

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
			if (rs != null && rs.next()) {

				String vipInfo = rs.getString("vip_info");

				LinkedHashMap<Integer, VipData> map = decodeVipInfo(vipInfo);

				for (VipData data : map.values()) {
					VIPReportData vData = new VIPReportData();
					vData.setVipLevel(data.vipLv + "");
					vData.setVipRoleCount(data.vipCount + "");
					vData.setLoginVipRoleCount(data.vipLoginCount + "");
					list.add(vData);
					totalVip += data.vipCount;
					totalLogin += data.vipLoginCount;
				}

				VIPReportData totalData = new VIPReportData();
//				totalData.setReportDate("合计");
				totalData.setVipRoleCount(totalVip + "");
				totalData.setLoginVipRoleCount(totalLogin + "");
				totalData.setVipLevel("合计");

				report.setGetVIPReportDataList(list);
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
		if (report.getGetVIPReportDataList() == null) {
			report.setGetVIPReportDataList(list);
		}

		return report;

	}

	public LinkedHashMap<Integer, VipData> decodeVipInfo(String vipInfo) {
		LinkedHashMap<Integer, VipData> map = new LinkedHashMap<Integer, VIPReportDAOImpl.VipData>();

		if (vipInfo != null && vipInfo.length() > 0) {
			String[] vipStr = vipInfo.split(",");
			if (vipStr != null && vipStr.length > 0) {
				for (int i = 0; i < vipStr.length; i++) {
					String[] info = vipStr[i].split(":");
					VipData data = new VipData();
					data.vipLv = Integer.parseInt(info[0]);
					data.vipCount = Integer.parseInt(info[1]);
					data.vipLoginCount = Integer.parseInt(info[2]);
					map.put(data.vipLv, data);
				}
			}
		}

		return map;
	}

	public VIPReportData processDefaultNullTotalData() {
		VIPReportData totalData = new VIPReportData();
		totalData.setReportDate("暂无数据");
		totalData.setVipLevel("暂无数据");
		totalData.setVipRoleCount("暂无数据");
		totalData.setLoginVipRoleCount("暂无数据");
		return totalData;
	}

	public static class VipData {
		int vipLv;
		int vipCount;
		int vipLoginCount;
	}

}
