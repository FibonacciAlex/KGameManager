package data.reportdatas;

import java.util.List;

/**
 * <商城道具销售统计报表>界面的数据类型定义接口
 * @author zhaizl
 *
 */
public class ShopSalesReport implements Report {
	
	/**
	 * 返回每项道具统计数据的合集
	 * @return
	 */
	public List<ShopSalesReportData> getShopSalesReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public ShopSalesReportData totalData;

	public List<ShopSalesReportData> getGetShopSalesReportDataList() {
		return getShopSalesReportDataList;
	}

	public void setGetShopSalesReportDataList(
			List<ShopSalesReportData> getShopSalesReportDataList) {
		this.getShopSalesReportDataList = getShopSalesReportDataList;
	}

	public ShopSalesReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(ShopSalesReportData totalData) {
		this.totalData = totalData;
	}
	
	

}
