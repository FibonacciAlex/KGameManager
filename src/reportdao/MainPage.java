package reportdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.DailyReportDAOImpl;
import reportdao.logging.KGameLogger;
import reportdao.util.DateUtil;

import json.JSONException;
import json.JSONObject;

public class MainPage {

	private final KGameLogger logger = KGameLogger.getLogger(MainPage.class);

	private DefineDataSourceManagerIF platformPool;
	private DefineDataSourceManagerIF bgPool;

	private static MainPage instance;

	public static final DateFormat DATE_FORMAT2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public MainPage() {
		DBConnectionPoolAdapter.init();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		platformPool = DBConnectionPoolAdapter.getPlatformDBConnectionPool();
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();

	}

	public static MainPage getInstance() {
		if (instance == null) {
			instance = new MainPage();
		}
		return instance;
	}

	// private DefineDataSourceManagerIF getPlatformPool() {
	// if (platformPool == null) {
	// platformPool = DBConnectionPoolAdapter
	// .getPlatformDBConnectionPool();
	// }
	// return platformPool;
	// }
	//
	// private DefineDataSourceManagerIF getBgPool() {
	// if (bgPool == null) {
	// bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
	// }
	// return bgPool;
	// }

	private int getTodayCharge() {

		int nowDate = DateUtil.getNowDate();
		int yesterday = DateUtil.getBeforeOrNextDates(nowDate, -1);
		int result = 0;

		String selectSql = "select sum(rmb)/100 ma from charge_record where charge_time>="
				+ nowDate;
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				result = rs.getInt("ma");
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return result;
	}

	private int getYesterdayCharge() {

		int nowDate = DateUtil.getNowDate();
		int yesterday = DateUtil.getBeforeOrNextDates(nowDate, -1);
		int result = 0;

		String selectSql = "select sum(rmb)/100 ma from charge_record where charge_time>="
				+ yesterday + " and charge_time<" + nowDate;
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				result = rs.getInt("ma");
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return result;
	}

	private int getThisMonthCharge() {
		int result = 0;

		String selectSql = "select sum(rmb)/100 ma from charge_record where DATE_FORMAT(charge_time,'%Y%m')=DATE_FORMAT(curdate(),'%Y%m')";
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				result = rs.getInt("ma");
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return result;
	}

	private int getLastMonthCharge() {
		int result = 0;

		String selectSql = "select sum(rmb)/100 ma from charge_record where date_format(charge_time,'%Y%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y%m')";
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				result = rs.getInt("ma");
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return result;
	}

	private int getTotalCharge() {
		int result = 0;

		String selectSql = "select sum(rmb)/100 ma from charge_record";
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				result = rs.getInt("ma");
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return result;
	}

	private int getNowOnlineCount() {
		int result = 0;
		int nowDate = DateUtil.getNowDate();

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int serverCount = 1;
				
		String selectServerCountSql = "select count(*) cc from (select * from server_online_record where record_time>"+nowDate+" group by server_id)t" ;		

		
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();
			
			ps = plCon.prepareStatement(selectServerCountSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);
			

			if (rs != null && rs.next()) {
				serverCount = rs.getInt("cc");
			}
			
			rs.close();
			ps.close();
			
			String selectSql = "select sum(t.connect_count) ma from(select connect_count from server_online_record order by id desc limit "+serverCount+") t";
			
			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				result = rs.getInt("ma");
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return result;
	}

	private int getNowOnlineCount1() {
		int result = 0;

		int nowDate = DateUtil.getNowDate();
		int yesterday = DateUtil.getBeforeOrNextDates(nowDate, -1);

		String selectIdSql = "select * from last_daily_record_collect_id where record_date="
				+ yesterday;

		String selectSql = "select sum(connect_count) ma from server_online_record where id > ?"
				+ " group by HOUR(record_time),MINUTE(record_time)";

		long maxOnlineRecordId = 0;

		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			bgCon = bgPool.getConnection();

			ps = bgCon.prepareStatement(selectIdSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			rs = bgPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				maxOnlineRecordId = rs.getLong("max_online_record_id");
				// logger.error("#######getTodayData::::isSelectOK:{}:{}:{}:{}:{}",isSelectOK,lastMinPlayerId,lastMaxPlayerId,lastLoginId,lastChargeId);
			}
			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setLong(1, maxOnlineRecordId);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					result = rs.getInt("ma");
				}
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}

		return result;
	}

	private List<String> getNewChargeInfo() {
		List<String> list = new ArrayList<String>();
		String selectSql = "select * from charge_record where id>(select max(id)-10 from charge_record) order by id desc";
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					int server_id = rs.getInt("server_id");
					int rmb = rs.getInt("rmb") / 100;
					String roleName = rs.getString("role_name");
					Date date = new Date(
							rs.getTimestamp("charge_time") != null ? rs
									.getTimestamp("charge_time").getTime() : 0);
					String chargeInfo = "<p>[" + server_id + "区] <span>"
							+ roleName + "</span>于" + DATE_FORMAT2.format(date)
							+ "充值<span>" + rmb + "</span>元。</p>";
					list.add(chargeInfo);
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return list;
	}

	private List<String> getTodayChargeRankInfo() {
		int nowDate = DateUtil.getNowDate();
		int yesterday = DateUtil.getBeforeOrNextDates(nowDate, -1);
		List<String> list = new ArrayList<String>();
		String selectSql = "select t.server_id sid,t.role_name rn,t.srmb tsrmb from"
				+ "(select server_id,role_name,sum(rmb) srmb from charge_record where charge_time>="
				+ nowDate
				+ " group by role_id) t order by t.srmb desc limit 10";
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					int server_id = rs.getInt("sid");
					int rmb = rs.getInt("tsrmb") / 100;
					String roleName = rs.getString("rn");
					String chargeInfo = "<p>[" + server_id + "区] <span>"
							+ roleName + "</span>今天充值<span>" + rmb
							+ "</span>元。</p>";
					list.add(chargeInfo);
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return list;
	}

	public List<String> getNowOnlineDiagramInfo() {
		List<String> list = new ArrayList<String>();
		int nowDate = DateUtil.getNowDate();
		// nowDate = DateUtil.getBeforeOrNextDates(nowDate, -1);
		int nextday = DateUtil.getBeforeOrNextDates(nowDate, 1);

		String selectSql = "SELECT HOUR(record_time) h, FLOOR(MINUTE(record_time) / 30) v, max(connect_count) cn,server_id sid "
				+ "FROM server_online_record "
				+ "WHERE record_time >= "
				+ nowDate
				+ " AND record_time < "
				+ nextday
				+ " GROUP BY h, v ,sid";

		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			Map<String, AtomicInteger> map = new LinkedHashMap<String, AtomicInteger>();
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					int h = rs.getInt("h");
					String v = (rs.getInt("v") == 0) ? "00" : "30";
					int count = rs.getInt("cn");
					// String chargeInfo = "";
					// list.add(chargeInfo);
					String key = h + ":" + v;
					if (!map.containsKey(key)) {
						map.put(key, new AtomicInteger(count));
					} else {
						map.get(key).addAndGet(count);
					}
					logger.error(
							"############ onlineInfo :::::h:{}:v:{}:count:{}",
							h, v, count);
				}
				if (map.size() > 0) {
					for (String key : map.keySet()) {
						String onlineInfo = "<set label='" + key + "' value='"
								+ map.get(key).get() + "' />";
						list.add(onlineInfo);
						logger.error("############ onlineInfo ::::::::{}",
								onlineInfo);
					}
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		return list;
	}

	public List<String> getOnlineDiagramInfo(String dateStr) {
		List<String> list = new ArrayList<String>();

		String selectSql = "SELECT HOUR(record_time) h, FLOOR(MINUTE(record_time) / 30) v, max(connect_count) cn,server_id sid "
				+ "FROM server_online_record "
				+ "WHERE record_time like '"
				+ dateStr + "%' GROUP BY h, v ,sid";
		logger.error("############ getOnlineDiagramInfo :::::sql::{}",
				selectSql);

		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String[] str = dateStr.split("-");
		int year = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);
		int date = Integer.parseInt(str[2]);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		int thisYear = cal.get(Calendar.YEAR);
		int thisMonth = cal.get(Calendar.MONTH) + 1;
		int thisDate = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		Map<String, AtomicInteger> map = new LinkedHashMap<String, AtomicInteger>();

		if (year == thisYear && month == thisMonth && date == thisDate) {
			for (int i = 0; i < 24 && i < (hour+1); i++) {
				String key = i + ":00";
				map.put(key, new AtomicInteger(0));
				if (i<hour) {
					key = i + ":30";
					map.put(key, new AtomicInteger(0));
				}else if(minute > 36){
					key = i + ":30";
					map.put(key, new AtomicInteger(0));
				}
			}
		} else {
			for (int i = 0; i < 24; i++) {
				String key = i + ":00";
				map.put(key, new AtomicInteger(0));
				key = i + ":30";
				map.put(key, new AtomicInteger(0));
			}
		}

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					int h = rs.getInt("h");
					String v = (rs.getInt("v") == 0) ? "00" : "30";
					int count = rs.getInt("cn");
					// String chargeInfo = "";
					// list.add(chargeInfo);
					String key = h + ":" + v;
					if (map.containsKey(key)) {
						map.get(key).addAndGet(count);
					}
//					logger.error(
//							"############ onlineInfo :::::h:{}:v:{}:count:{}",
//							h, v, count);
				}
			}

		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		if (map.size() > 0) {
			for (String key : map.keySet()) {
				String onlineInfo = "<set label='" + key + "' value='"
						+ map.get(key).get() + "' />";
				list.add(onlineInfo);
			}
		}
		return list;
	}

	public List<String> getChargeDiagramInfo(String monthStr) {
		List<String> list = new ArrayList<String>();
		String[] str = monthStr.split("-");
		int year = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);

		String selectSql = "select date_format(charge_time,'%d') ddd,sum(rmb)/100 ma from charge_record where date_format(charge_time,'%Y-%m')='"
				+ monthStr + "'" + " group by DATE(charge_time)";
		Connection bgCon = null;
		Connection plCon = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		int thisYear = cal.get(Calendar.YEAR);
		int thisMonth = cal.get(Calendar.MONTH) + 1;
		int thisDate = cal.get(Calendar.DAY_OF_MONTH);

		int monthDates = DateUtil.getMonthLastDay(year, month);

		Map<String, AtomicInteger> map = new LinkedHashMap<String, AtomicInteger>();
		if (year == thisYear && month == thisMonth) {
			for (int i = 1; i <= thisDate; i++) {
				String dateStr = (i<10)?("0"+i):(""+i);
				map.put(dateStr, new AtomicInteger(0));
			}
		} else {
			for (int i = 1; i <= monthDates; i++) {
				String dateStr = (i<10)?("0"+i):(""+i);
				map.put(dateStr, new AtomicInteger(0));
			}
		}

		try {

			plCon = platformPool.getConnection();

			ps = plCon.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = platformPool.executeQuery(ps);

			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					String date = rs.getString("ddd");
					int rmb = rs.getInt("ma");
					if (map.containsKey(date)) {
						map.get(date).set(rmb);
					}
					logger.error(
							"############ getChargeDiagramInfo :::::date:{}:rmb:{}",
							date,rmb);
				}
			}
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(bgCon);
			platformPool.closeConnection(plCon);
		}
		for (String date : map.keySet()) {
			String chargeInfo = "<set label='" + date + "日' value='"
					+ map.get(date).get() + "' />";
			list.add(chargeInfo);
		}
		return list;
	}

	public String getZxcz(Map<String, String> params) {
		StringBuffer sf = new StringBuffer();
		JSONObject json = new JSONObject();
		try {
			json.put("jrsr", getTodayCharge());
			json.put("zrsr", getYesterdayCharge());// 昨日收入
			json.put("bysr", getThisMonthCharge());// 本月收入
			json.put("sysr", getLastMonthCharge());// 本月收入
			json.put("zsr", getTotalCharge());// 总收入
			json.put("zxrs", getNowOnlineCount());// 在线人数
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 今日收入

		sf.append(json.toString() + "`~");// 这个不能去掉
		// 这里取得最新充值记录
		List<String> newChargeList = getNewChargeInfo();
		for (String info : newChargeList) {
			sf.append(info);
		}
		sf.append("`~");// 这个不能去掉
		// 这里取得排行充值赋值
		List<String> rankChargeList = getTodayChargeRankInfo();
		for (String info : rankChargeList) {
			sf.append(info);
		}
		// for (int i = 0; i < 3; i++) {
		// sf.append("<p>***于20:50分《天形变》雄霸天下充值<span>100</span>元。</p>");
		// }
		return sf.toString();
	}

	// 获取在线人数
	public String getZxrs(Map<String, String> params) {
		for (String key : params.keySet()) {
			logger.error("############ getZxrs ::::::::Key:{},Value:{}", key,
					params.get(key));
		}
		List<String> onlineInfo = getOnlineDiagramInfo(params.get("date"));
		StringBuffer sf = new StringBuffer();
		// for (int i = 0; i < 3; i++) {
		// sf.append("<set label='Jan" + i + "' value='" + i * 200 + "' />");
		// }
		for (String info : onlineInfo) {
			sf.append(info);
		}
		return sf.toString();
	}

	// 获取月收入
	public String getYsrt(Map<String, String> params) {
		for (String key : params.keySet()) {
			logger.error("############ getYsrt ::::::::Key:{},Value:{}", key,
					params.get(key));
		}
		List<String> monthChargeInfo = getChargeDiagramInfo(params.get("month"));
		StringBuffer sf = new StringBuffer();
		// for (int i = 0; i < 3; i++) {
		// sf.append("<set label='Jan" + i + "' value='" + i * 200 + "' />");
		// }
		for (String info : monthChargeInfo) {
			sf.append(info);
		}
		return sf.toString();
	}

	public static void main(String[] a) {
		// MainPage page = MainPage.getInstance();
		// String aa = page.getZxcz(new HashMap<String, String>());
		// System.out.println(aa);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		String aa = year + "-" + month + "-" + day + " " + hour + ":"
				+ (minute < 10 ? "0" : "") + minute + "%";
		System.out.println(aa);
	}

}
