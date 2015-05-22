package data.reportdatas;

import java.util.List;

/**
 * <铜钱库存统计报表>界面的数据类型定义接口
 * 
 * @author zhaizl
 * 
 */
public class GameCopperStockReport implements Report {

	/**
	 * 返回每项道具统计数据的合集
	 * 
	 * @return
	 */
	public List<GameCopperStockReportData> gameCopperStockReportList;

	/**
	 * 返回报表的合计项数据
	 * 
	 * @return
	 */
	public GameCopperStockReportData totalData;

	public List<GameCopperStockReportData> getGameCopperStockReportList() {
		return gameCopperStockReportList;
	}

	public void setGameCopperStockReportList(
			List<GameCopperStockReportData> gameCopperStockReportList) {
		this.gameCopperStockReportList = gameCopperStockReportList;
	}

	public GameCopperStockReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(GameCopperStockReportData totalData) {
		this.totalData = totalData;
	}
	
	

}
