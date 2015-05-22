package data.reportdatas;

import java.util.List;

public class GamePointStockRankReport implements Report{
	/**
	 * 返回每项功能点数消耗排行统计数据的合集
	 * @return
	 */
	public List<GamePointStockRankReportData> gamePointStockRankReportList;
	
	/**
	 * 获取所有功能项的使用总次数
	 * @return
	 */
	public GamePointStockRankReportData totalData;

	public List<GamePointStockRankReportData> getGamePointStockRankReportList() {
		return gamePointStockRankReportList;
	}

	public void setGamePointStockRankReportList(
			List<GamePointStockRankReportData> getGamePointStockReportList) {
		this.gamePointStockRankReportList = getGamePointStockReportList;
	}

	public GamePointStockRankReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(GamePointStockRankReportData totalData) {
		this.totalData = totalData;
	}
}
