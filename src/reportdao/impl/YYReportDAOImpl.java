package reportdao.impl;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.kola.core.web.admin.AdminAction;

import data.reportdatas.DailyReportData;
import data.reportdatas.WorldBossReport;
import data.reportdatas.WorldBossReportData;
import data.reportdatas.YYReport;
import data.reportdatas.YYReportData;
import reportdao.YYReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.ShopSalesReportDAOImpl.ItemInfo;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;
import reportdao.util.DateUtil;
import reportdao.util.XmlUtil;

public class YYReportDAOImpl implements YYReportDAO {
	private static final KGameLogger logger = KGameLogger
			.getLogger(YYReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;
	private DefineDataSourceManagerIF platformPool;
	private static HashMap<String, Integer> channelInfoMap;

	public YYReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
		initChannelInfo();
	}

	public void initChannelInfo() {
		if (channelInfoMap == null) {
			channelInfoMap = new HashMap<String, Integer>();
			File f = new File(".");
			URL url = DBConnectionPoolAdapter.class.getResource("/");
			String dir = (url.getPath() + "../").replaceAll("%20", " ");
			String filePath = dir + "res/config/reportConfig/channelConfig.xml";

			Document doc = XmlUtil.openXml(filePath);
			Element root = doc.getRootElement();

			List<Element> channelInfoList = root.getChild("channelInfo")
					.getChildren("channel");

			for (Element itemE : channelInfoList) {
				String userName = itemE.getChildText("name");
				int promoId = Integer.parseInt(itemE.getChildText("promoId"));

				channelInfoMap.put(userName, promoId);
			}
		}
	}

	@Override
	public YYReport getYYReport(Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("getYYReport condition,key:" + key + ",value:"
					+ condition.get(key));
		}

		// logger.error("getYYReport user:"+AdminAction.user.getUsername());

		// 解释condition条件的开始日期
		String beginDateStr = condition
				.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		// 解释condition条件的结束日期
		String endDateStr = condition.get(QueryCondition.QUERY_KEY_END_DATE);
		// 解释condition条件的游戏区ID

		int beginDate = Integer.parseInt(beginDateStr);
		int endDate = Integer.parseInt(endDateStr);

		String userName = null;
		if (AdminAction.user != null) {
			userName = AdminAction.user.getUsername();
		}
		YYReport report = getYYReportNative(beginDate, endDate, userName);

		if (report.getYYReportDataList().size() == 0) {
			report.setTotalData(processDefaultNullTotalData());
		}

		return report;
	}

	public YYReport getYYReportNative(int beginDate, int endDate,
			String userName) {
		String selectSql = "select record_date rd,register_count rc,charge_money cm from daily_report where record_date >= ? and record_date <= ?";
		String selectSql1 = "select record_date rd,register_count rc,charge_money cm from daily_report_promo where record_date >= ? and record_date <= ? and promo_id = ?";

		YYReport report = new YYReport();

		List<YYReportData> list = new ArrayList<YYReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int totalCreateCount = 0;
		int totalMoney = 0;
		int totalChannelCreateCount = 0;
		int totalChannelMoney = 0;

		try {

			Map<Integer, YYReportData> dataMap = new LinkedHashMap<Integer, YYReportData>();

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
					YYReportData data = new YYReportData();

					int recordDate = rs.getInt("rd");
					data.setReportDate(recordDate + "");

					int registerCount = rs.getInt("rc");
					data.setCreateUserCount(registerCount + "");
					totalCreateCount += registerCount;

					int money = rs.getInt("cm");
					data.setTodayChargeMoney(money + "");
					totalMoney += money;

					dataMap.put(recordDate, data);
				}
			}

			// /////////////////////////////////
			if (channelInfoMap.containsKey(userName)) {
				ps.clearParameters();
				ps.close();
				bgPool.closeResultSet(rs);

				int promoId = channelInfoMap.get(userName);

				ps = con.prepareStatement(selectSql1,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ps.setInt(1, beginDate);
				ps.setInt(2, endDate);
				ps.setInt(3, promoId);

				rs = bgPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					rs.beforeFirst();
					while (rs.next()) {

						int recordDate = rs.getInt("rd");
						if (dataMap.containsKey(recordDate)) {
							YYReportData data = dataMap.get(recordDate);

							int cRegisterCount = rs.getInt("rc");
							data.setChannelCreateUserCount(cRegisterCount + "");
							totalChannelCreateCount += cRegisterCount;

							int cMoney = rs.getInt("cm");
							data.setChannelTodayChargeMoney(cMoney + "");
							totalChannelMoney += cMoney;
						}
					}
				}
			}

			if (dataMap.size() > 0) {
				list.addAll(dataMap.values());
			}

			// ////////////////////////////////

			int nowDate = DateUtil.getNowDate();
			if (beginDate <= nowDate && nowDate <= endDate) {
				int promoId = -1;
				if (channelInfoMap.containsKey(userName)) {
					promoId = channelInfoMap.get(userName);
				}
				list.add(getTodayData(promoId));
			}

			report.setYYReportDataList(list);
			YYReportData totalData = new YYReportData();
			totalData.setReportDate("总计");
			totalData.setCreateUserCount(totalCreateCount + "");
			totalData.setChannelCreateUserCount(totalChannelCreateCount + "");
			totalData.setTodayChargeMoney(totalMoney + "");
			totalData.setChannelTodayChargeMoney(totalChannelMoney + "");
			report.setTotalData(totalData);

			return report;

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getYYReportDataList() == null) {
			report.setYYReportDataList(list);
		}

		return report;
	}

	private YYReportData processDefaultNullTotalData() {

		YYReportData totalData = new YYReportData();
		totalData.setReportDate("暂无数据");
		totalData.setCreateUserCount("暂无数据");
		totalData.setChannelCreateUserCount("暂无数据");
		totalData.setTodayChargeMoney("暂无数据");
		totalData.setChannelTodayChargeMoney("暂无数据");

		return totalData;
	}

	public static String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(value);
	}

	private YYReportData getTodayData(int promoId) {
		int nowDate = DateUtil.getNowDate();
		int endDate = DateUtil.getBeforeOrNextDates(nowDate, 1);
		int yesterday = DateUtil.getBeforeOrNextDates(nowDate, -1);

//		long lastMinPlayerId = 0;
//		long lastMaxPlayerId = 0;
//		long lastLoginId = 0;
//		long lastChargeId = 0;

		int todayRegisterCount = 0;
		int channelRegisterCount = 0;
		int todayChargeMoney = 0;
		int channelChargeMoney = 0;

//		String selectIdSql = "select * from last_daily_record_collect_id where record_date="
//				+ yesterday;

		// String selectGsIdSql =
		// "select * last_gs_collect_id where record_date="
		// + yesterday + " and server_id = " + serverId;

		String selectRegisterSql = "select count(player_id) cpid from player where register_time>="+nowDate+" and register_time<"
				+ endDate;

		String selectRegisterSql1 = "select count(player_id) cpid from player where register_time>="+nowDate+" and register_time<"
				+ endDate + " and promo_id = " + promoId;

		String selectChargeSql = "select (sum(rmb)/100) srmb from charge_record where charge_time>="+nowDate+" and charge_time<"
				+ endDate;

		String selectChargeSql1 = "select (sum(rmb)/100) srmb from charge_record where charge_time>="+nowDate+" and charge_time<"
				+ endDate + " and promo_id = " + promoId;

		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isSelectOK = true;

		try {
			bgCon = bgPool.getConnection();

//			ps = bgCon.prepareStatement(selectIdSql,
//					ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_READ_ONLY);
//
//			rs = bgPool.executeQuery(ps);

//			if (rs != null && rs.next()) {
//				lastMinPlayerId = rs.getLong("min_player_id");
//				lastMaxPlayerId = rs.getLong("max_player_id");
//				lastLoginId = rs.getLong("max_login_record_id");
//				lastChargeId = rs.getLong("max_charge_record_id");
//				isSelectOK = true;
//				// logger.error("#######getTodayData::::isSelectOK:{}:{}:{}:{}:{}",isSelectOK,lastMinPlayerId,lastMaxPlayerId,lastLoginId,lastChargeId);
//			}
//			logger.error("#######getYYReport getTodayData::::isSelectOK:{}:{}:{}:{}:{}",
//					isSelectOK, lastMinPlayerId, lastMaxPlayerId, lastLoginId,
//					lastChargeId);

			if (isSelectOK) {
//				ps.clearParameters();
//				ps.close();
//				bgPool.closeResultSet(rs);

				// if (serverId > -1) {
				//
				// ps = bgCon.prepareStatement(selectGsIdSql,
				// ResultSet.TYPE_SCROLL_INSENSITIVE,
				// ResultSet.CONCUR_READ_ONLY);
				//
				// rs = bgPool.executeQuery(ps);
				//
				// if (rs != null && rs.next()) {
				// lastMaxRoleId = rs.getLong("max_role_id");
				// }
				// ps.clearParameters();
				// ps.close();
				// bgPool.closeResultSet(rs);
				// }
				// /////////////////////////////////////////////

//				logger.error("#######getYYReport getTodayData::::selectRegisterSql:::::"
//						+ selectRegisterSql);
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectRegisterSql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					todayRegisterCount = rs.getInt("cpid");
					// logger.error("#######getTodayData:::::::::"+todayRegisterCount);
				}

				ps.clearParameters();
				ps.close();
				platformPool.closeResultSet(rs);

				// /////////////////////
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectRegisterSql1,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					channelRegisterCount = rs.getInt("cpid");
					// logger.error("#######getTodayData:::::::::"+todayRegisterCount);
				}

				ps.clearParameters();
				ps.close();
				platformPool.closeResultSet(rs);
				// //////////////////////

				logger.error("#######getYYReport getTodayData::::selectChargeSql:::::"
						+ selectChargeSql);
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectChargeSql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					todayChargeMoney = rs.getInt("srmb");
				}

				ps.clearParameters();
				ps.close();
				platformPool.closeResultSet(rs);
				// //////////////////////////////
				plCon = platformPool.getConnection();

				ps = plCon.prepareStatement(selectChargeSql1,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				rs = platformPool.executeQuery(ps);
				if (rs != null && rs.next()) {
					channelChargeMoney = rs.getInt("srmb");
				}
				// /////////////////////////////
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}

		YYReportData data = new YYReportData();
		data.setReportDate("今天即时");
		data.setCreateUserCount(todayRegisterCount + "");
		data.setChannelCreateUserCount(channelRegisterCount + "");
		data.setTodayChargeMoney(todayChargeMoney + "");
		data.setChannelTodayChargeMoney(channelChargeMoney + "");

		return data;

	}

}
