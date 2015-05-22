package data.reportdatas;

import java.util.List;

public class GamePointConsumeRankReport implements Report{
	/**
	 * 返回每项功能点数消耗排行统计数据的合集
	 * @return
	 */
	public List<GamePointConsumeRankReportData> gamePointConsumeRankReportList;
	
	/**
	 * 获取所有功能项的使用总次数
	 * @return
	 */
	public GamePointConsumeRankReportData totalData;

	public List<GamePointConsumeRankReportData> getGamePointConsumeRankReportList() {
		return gamePointConsumeRankReportList;
	}

	public void setGamePointConsumeRankReportList(
			List<GamePointConsumeRankReportData> getGamePointConsumeReportList) {
		this.gamePointConsumeRankReportList = getGamePointConsumeReportList;
	}

	public GamePointConsumeRankReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(GamePointConsumeRankReportData totalData) {
		this.totalData = totalData;
	}
}
