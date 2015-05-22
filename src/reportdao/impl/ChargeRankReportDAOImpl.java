package reportdao.impl;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import data.reportdatas.ChargeRankReport;
import data.reportdatas.ChargeRankReportData;
import data.reportdatas.FirstChargeReport;
import data.reportdatas.FirstChargeUserInfo;
import reportdao.ChargeRankReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.FirstChargeReportDAOImpl.FirstChargeUserData;
import reportdao.impl.ShopSalesReportDAOImpl.ItemInfo;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;
import reportdao.util.XmlUtil;

public class ChargeRankReportDAOImpl implements ChargeRankReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(ChargeRankReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	private static HashMap<Integer, String> serverNameMap;

	public ChargeRankReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		if (serverNameMap == null) {
			serverNameMap = new HashMap<Integer, String>();

			File f = new File(".");
			URL url = DBConnectionPoolAdapter.class.getResource("/");
			String dir = (url.getPath() + "../").replaceAll("%20", " ");
			String filePath = dir + "res/config/reportConfig/serverName.xml";

			Document doc = XmlUtil.openXml(filePath);
			Element root = doc.getRootElement();

			List<Element> serverInfoList = root.getChildren("server");

			for (Element serverE : serverInfoList) {

				Integer serverId = Integer.parseInt(serverE.getChildText("id"));
				String serverName = serverE.getChildText("name");

				serverNameMap.put(serverId, serverName);
			}
		}
	}

	public static void reloadServerNameMap() {
		File f = new File(".");
		URL url = DBConnectionPoolAdapter.class.getResource("/");
		String dir = (url.getPath() + "../").replaceAll("%20", " ");
		String filePath = dir + "res/config/reportConfig/serverName.xml";

		Document doc = XmlUtil.openXml(filePath);
		Element root = doc.getRootElement();

		List<Element> serverInfoList = root.getChildren("server");

		for (Element serverE : serverInfoList) {

			Integer serverId = Integer.parseInt(serverE.getChildText("id"));
			String serverName = serverE.getChildText("name");

			if (!serverNameMap.containsKey(serverId)) {
				serverNameMap.put(serverId, serverName);
			}
		}
	}

	@Override
	public ChargeRankReport getChargeRankReport(Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("getFirstChargeReport condition,key:" + key
					+ ",value:" + condition.get(key));
		}
		reloadServerNameMap();

		// 解释condition条件的开始日期
		String beginDateStr = condition
				.get(QueryCondition.QUERY_KEY_BEGIN_DATE);

		int beginDate = Integer.parseInt(beginDateStr);

		ChargeRankReport report = getChargeRankReport(beginDate);

		if (report == null) {
			report = new ChargeRankReport();
			report.setTotalData(processTotalData());
			report.setChargeRankReportDataList(new ArrayList<ChargeRankReportData>());
		}

		return report;
	}

	public ChargeRankReport getChargeRankReport(int beginDate) {
		String selectSql = "select * from "
				+ "charge_rank_report where record_date=?";

		ChargeRankReport report = new ChargeRankReport();

		List<ChargeRankReportData> list = new ArrayList<ChargeRankReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					ChargeRankReportData data = new ChargeRankReportData();
					data.rankNum = rs.getInt("rank_num") + "";
					data.playerId = rs.getLong("player_id") + "";
					data.roleName = rs.getString("role_name");
					data.serverName = serverNameMap.get(rs.getInt("server_id"))
							+ "";
					data.chargeMoney = rs.getInt("total_charge_rmb") + "";
					data.chargeCount = rs.getInt("charge_count") + "";
					data.lastChargeMoney = rs.getInt("last_charge_rmb") + "";
					data.lastChargeTime = rs
							.getTimestamp("lastest_charge_time").toString();
					data.registerTime = rs.getTimestamp("register_time")
							.toString();
					data.lastLoginTime = rs.getTimestamp("lastest_login_time")
							.toString();

					list.add(data);

				}

				report.setChargeRankReportDataList(list);
				report.setTotalData(processTotalData());
				return report;
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getChargeRankReportDataList() == null) {
			report.setChargeRankReportDataList(list);
			report.setTotalData(processTotalData());
		}

		return report;
	}

	private ChargeRankReportData processTotalData() {

		ChargeRankReportData data = new ChargeRankReportData();
		data.rankNum = "合计";
		data.playerId = "－";
		data.roleName = "－";
		data.serverName = "－";
		data.chargeMoney = "－";
		data.chargeCount = "－";
		data.lastChargeMoney = "－";
		data.lastChargeTime = "－";
		data.registerTime = "－";
		data.lastLoginTime = "－";
		return data;
	}

}
