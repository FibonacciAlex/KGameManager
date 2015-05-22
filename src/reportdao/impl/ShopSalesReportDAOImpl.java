package reportdao.impl;

import java.io.File;
import java.net.URL;
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

import org.jdom.Document;
import org.jdom.Element;

import data.reportdatas.GamePointConsumeReport;
import data.reportdatas.GamePointConsumeReportData;
import data.reportdatas.Report;
import data.reportdatas.ShopSalesReport;
import data.reportdatas.ShopSalesReportData;
import reportdao.GamePointConsumeReportDAO;
import reportdao.ShopSalesReportDAO;
import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DBConnectionFactory;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.impl.reportCache.ReportCacheManager;
import reportdao.logging.KGameLogger;
import reportdao.querycondition.QueryCondition;
import reportdao.util.XmlUtil;

public class ShopSalesReportDAOImpl implements ShopSalesReportDAO {

	private static final KGameLogger logger = KGameLogger
			.getLogger(ShopSalesReportDAOImpl.class);

	private DefineDataSourceManagerIF bgPool;

	private static HashMap<String, ItemInfo> itemInfoMap;

	private static Map<String, Boolean> sortKeyMap;

	private static Map<Integer, String> shopTypeMap = new HashMap<Integer, String>();

	static {
		shopTypeMap.put(1, "普通商店");
		shopTypeMap.put(2, "神秘商店");
		shopTypeMap.put(3, "军团商店");
	}

	public ShopSalesReportDAOImpl() {
		bgPool = DBConnectionPoolAdapter.getBgDBConnectionPool();
		initItemInfo();
		initSortKeyMap();
	}

	private static void initSortKeyMap() {
		if (sortKeyMap == null) {
			sortKeyMap = new HashMap<String, Boolean>();
			sortKeyMap.put(ShopSalesReportDAO.SORT_KEY_CONSUME_COUNT, true);
			sortKeyMap.put(ShopSalesReportDAO.SORT_KEY_CONSUME_POINT, true);
			sortKeyMap.put(ShopSalesReportDAO.SORT_KEY_CONSUME_RATE, true);
			sortKeyMap.put(ShopSalesReportDAO.SORT_KEY_ITEM_STOCK_COUNT, true);
			sortKeyMap.put(ShopSalesReportDAO.SORT_KEY_ITEM_SHOP_TYPE, true);
		}
	}

	public void initItemInfo() {
		if (itemInfoMap == null) {
			itemInfoMap = new HashMap<String, ShopSalesReportDAOImpl.ItemInfo>();
			File f = new File(".");
			URL url = DBConnectionPoolAdapter.class.getResource("/");
			String dir = (url.getPath() + "../").replaceAll("%20", " ");
			String filePath = dir
					+ "res/config/reportConfig/shopReportConfig.xml";

			Document doc = XmlUtil.openXml(filePath);
			Element root = doc.getRootElement();

			List<Element> itemInfoList = root.getChild("itemInfo").getChildren(
					"item");

			for (Element itemE : itemInfoList) {
				ItemInfo info = new ItemInfo();
				info.code = itemE.getChildText("code");
				info.itemName = itemE.getChildText("name");
				info.price = itemE.getChildText("price");
				info.itemType = itemE.getChildText("type");

				itemInfoMap.put(info.code, info);
			}
		}
	}

	@Override
	public ShopSalesReport getShopSalesReport(Map<String, String> condition) {
		for (String key : condition.keySet()) {
			logger.error("getShopSalesReport condition,key:" + key + ",value:"
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
		int endDate = Integer.parseInt(endDateStr);

		// if(ReportCacheManager.isReportExist(Report.REPORT_TYPE_SHOP_SALES,
		// condition)){
		// ShopSalesReport report =
		// (ShopSalesReport)(ReportCacheManager.getReport(Report.REPORT_TYPE_SHOP_SALES,
		// condition));
		// return sortReport(report, condition.get("sortname"),
		// condition.get("sortorder"));
		// }

		ShopSalesReport report = getShopSalesReportByServer(beginDate, endDate,
				serverId);
		if (report == null) {
			report = new ShopSalesReport();
			report.setGetShopSalesReportDataList(new ArrayList<ShopSalesReportData>());
		}
		if (report.getTotalData() == null) {
			report.setTotalData(processDefaultNullTotalData());
		}

		ReportCacheManager.addReport(Report.REPORT_TYPE_SHOP_SALES, condition,
				report);

		// return report;

		return sortReport(report, condition.get("sortname"),
				condition.get("sortorder"));
	}

	private ShopSalesReport getShopSalesReportByServer(int beginDate,
			int endDate, int serverId) {
		String selectSql = "select code,item_name,sum(total_consume_count) sc,sum(total_consume_point) sp,shop_type from "
				+ "shop_consume_report_server where record_date>=? and record_date<=? and server_id=? group by code";

		String selectMaxDateSql = "select max(record_date) maxdate from "
				+ "shop_consume_report_server where record_date>=? and record_date<=? and server_id=?";

		String selectStockSql = "select code,stock_count,role_count from "
				+ "shop_consume_report_server where record_date=? and server_id=?";

		ShopSalesReport report = new ShopSalesReport();

		List<ShopSalesReportData> list = new ArrayList<ShopSalesReportData>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int totalCount = 0;
		int totalPoint = 0;

		Map<String, ShopSalesReportData> itemDataMap = new LinkedHashMap<String, ShopSalesReportData>();

		try {
			con = bgPool.getConnection();
			ps = con.prepareStatement(selectSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);
			ps.setInt(3, serverId);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					ShopSalesReportData data = new ShopSalesReportData();

					String code = rs.getString("code");
					data.setItemCode(code);

					String itemName = rs.getString("item_name");
					data.setItemName(itemName);

					int totalConsumeCount = rs.getInt("sc");
					data.setSellCount(totalConsumeCount + "");
					totalCount += totalConsumeCount;

					int totalConsumePoint = rs.getInt("sp");
					data.setSellTotalPoint(totalConsumePoint + "");
					totalPoint += totalConsumePoint;
					
					int shopType = rs.getInt("shop_type");
					data.setItemType(shopType + "");
					if (shopTypeMap.containsKey(shopType)) {
						data.setShopName(
								shopTypeMap.get(shopType));
					} else {
						itemDataMap.get(code).setShopName("－");
					}
					
					
					
					// int stockCount = rs.getInt("mst");
					// data.setItemStockCount(stockCount+"");

//					if (itemInfoMap.containsKey(code)) {
////						data.setItemType(itemInfoMap.get(code).itemType);
//						data.setSellPrice(itemInfoMap.get(code).price);
//						data.setItemStockCount(0 + "");
//					} else {
////						data.setItemType("－");
//						data.setSellPrice(totalConsumePoint / totalConsumeCount
//								+ "");
//						data.setItemStockCount(0 + "");
//					}
					data.setItemStockCount(0 + "");
					data.setOwnerCount("0");
					data.setSellPrice(totalConsumePoint / totalConsumeCount
							+ "");

					// list.add(data);
					itemDataMap.put(code, data);
				}
			}

			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);

			int maxDate = endDate;

			ps = con.prepareStatement(selectMaxDateSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, beginDate);
			ps.setInt(2, endDate);
			ps.setInt(3, serverId);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				maxDate = rs.getInt("maxdate");
			}

			ps.clearParameters();
			ps.close();
			bgPool.closeResultSet(rs);

			ps = con.prepareStatement(selectStockSql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, maxDate);
			ps.setInt(2, serverId);

			rs = bgPool.executeQuery(ps);
			if (rs != null && rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					String code = rs.getString("code");
					int stockCount = rs.getInt("stock_count");
					int ownerCount = rs.getInt("role_count");
					
					if (itemDataMap.containsKey(code)) {
						itemDataMap.get(code)
								.setItemStockCount(stockCount + "");						
						itemDataMap.get(code).setOwnerCount(ownerCount + "");
					}
				}
			}

			for (ShopSalesReportData data : itemDataMap.values()) {
				list.add(data);
			}

			report.setGetShopSalesReportDataList(list);
			ShopSalesReportData totalData = new ShopSalesReportData();
			totalData.setItemType("总计");
			totalData.setItemCode("－");
			totalData.setItemName("－");
			totalData.setSellPrice("－");
			totalData.setSellRate("－");
			totalData.setSellTotalPoint(totalPoint + "");
			totalData.setSellCount("－");
			totalData.setItemStockCount("－");
			totalData.setOwnerCount("－");
			report.setTotalData(totalData);

			for (ShopSalesReportData data : list) {
				float rate = (float) (Integer.parseInt(data.sellTotalPoint))
						* 100 / (Integer.parseInt(totalData.sellTotalPoint));
				data.setSellRate(getFloatString(rate) + "%");
			}

			return report;
		} catch (SQLException ex) {
			logger.error("", ex);
		} finally {
			bgPool.closeResultSet(rs);
			bgPool.closePreparedStatement(ps);
			bgPool.closeConnection(con);
		}
		if (report.getGetShopSalesReportDataList() == null) {
			report.setGetShopSalesReportDataList(list);
		}

		return report;
	}

	private ShopSalesReportData processDefaultNullTotalData() {
		ShopSalesReportData totalData = new ShopSalesReportData();
		totalData.setItemCode("暂无数据");
		totalData.setItemName("暂无数据");
		totalData.setItemType("暂无数据");
		totalData.setSellCount("暂无数据");
		totalData.setSellPrice("暂无数据");
		totalData.setItemStockCount("暂无数据");
		totalData.setSellRate("暂无数据");
		totalData.setSellTotalPoint("暂无数据");
		totalData.setOwnerCount("暂无数据");
		totalData.setTotalData(true);

		return totalData;
	}

	private String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.000");
		return fnum.format(value);
	}

	public ShopSalesReport sortReport(ShopSalesReport sourceReport,
			final String sortKey, final String sortType) {
		if (!sortKeyMap.containsKey(sortKey)) {
			return sourceReport;
		}

		// if(sortKey.equals(DailyReportDAO.SORT_KEY_CHARGE_MONEY)){
		List<ShopSalesReportData> list = new ArrayList<ShopSalesReportData>(
				sourceReport.getShopSalesReportDataList);
		Collections.sort(list, new Comparator<ShopSalesReportData>() {

			@Override
			public int compare(ShopSalesReportData o1, ShopSalesReportData o2) {
				double n1 = 0f;
				double n2 = 0f;

				if (sortKey.equals(ShopSalesReportDAO.SORT_KEY_CONSUME_COUNT)) {

					n1 = (o1.sellCount.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o1.sellCount);
					n2 = (o2.sellCount.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o2.sellCount);
				} else if (sortKey
						.equals(ShopSalesReportDAO.SORT_KEY_CONSUME_RATE)) {
					n1 = (o1.sellRate.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o1.sellRate.substring(0,
									o1.sellRate.indexOf("%")));
					n2 = (o2.sellRate.equals("－")) ? Double.MAX_VALUE : Double
							.parseDouble(o2.sellRate.substring(0,
									o2.sellRate.indexOf("%")));
				} else if (sortKey
						.equals(ShopSalesReportDAO.SORT_KEY_CONSUME_POINT)) {
					n1 = (o1.sellTotalPoint.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.sellTotalPoint);
					n2 = (o2.sellTotalPoint.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.sellTotalPoint);
				} else if (sortKey
						.equals(ShopSalesReportDAO.SORT_KEY_ITEM_STOCK_COUNT)) {
					n1 = (o1.itemStockCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.itemStockCount);
					n2 = (o2.itemStockCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.itemStockCount);
				} else if (sortKey
						.equals(ShopSalesReportDAO.SORT_KEY_ITEM_SHOP_TYPE)) {
					n1 = (o1.itemStockCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o1.itemType);
					n2 = (o2.itemStockCount.equals("－")) ? Double.MAX_VALUE
							: Double.parseDouble(o2.itemType);
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

		ShopSalesReport report = new ShopSalesReport();
		report.setGetShopSalesReportDataList(list);
		report.setTotalData(sourceReport.getTotalData());
		return report;
	}

	public static class ItemInfo {
		public String code;
		public String itemName;
		public String price;
		public String itemType;
	}

}
