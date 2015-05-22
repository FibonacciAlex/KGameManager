package data.reportdatas;

import java.util.List;

/**
 * 关卡流水数据报表
 * @author Administrator
 *
 */
public class GameLevelLossReport {
	/**
	 * 返回每项道具统计数据的合集
	 * 
	 * @return
	 */
	public List<GameLevelLossReportData> gameLevelLossReportDataList;

	/**
	 * 返回报表的合计项数据
	 * 
	 * @return
	 */
	public GameLevelLossReportData totalData;

	public List<GameLevelLossReportData> getGameLevelLossReportDataList() {
		return gameLevelLossReportDataList;
	}

	public void setGameLevelLossReportDataList(
			List<GameLevelLossReportData> gameLevelLossReportDataList) {
		this.gameLevelLossReportDataList = gameLevelLossReportDataList;
	}

	public GameLevelLossReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(GameLevelLossReportData totalData) {
		this.totalData = totalData;
	}
}
