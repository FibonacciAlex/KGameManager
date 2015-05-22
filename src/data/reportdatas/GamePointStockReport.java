package data.reportdatas;

import java.util.List;

/**
 * <元宝库存统计报表>界面的数据类型定义接口
 * @author zhaizl
 *
 */
public class GamePointStockReport implements Report {
	
	/**
	 * 返回每项道具统计数据的合集
	 * @return
	 */
	public List<GamePointStockReportData> gamePointStockReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public GamePointStockReportData totalData;

	public List<GamePointStockReportData> getGamePointStockReportDataList() {
		return gamePointStockReportDataList;
	}

	public void setGamePointStockReportDataList(
			List<GamePointStockReportData> gamePointStockReportDataList) {
		this.gamePointStockReportDataList = gamePointStockReportDataList;
	}

	public GamePointStockReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(GamePointStockReportData totalData) {
		this.totalData = totalData;
	}
	
	

}
