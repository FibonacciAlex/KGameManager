package data.reportdatas;

import java.util.List;

public class WorldBossReport implements Report{
	/**
	 * 返回每天报表统计数据的合集
	 * @return
	 */
	public List<WorldBossReportData> worldBossReportDataList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public WorldBossReportData totalData;

	public List<WorldBossReportData> getWorldBossReportDataList() {
		return worldBossReportDataList;
	}

	public void setWorldBossReportDataList(
			List<WorldBossReportData> worldBossReportDataList) {
		this.worldBossReportDataList = worldBossReportDataList;
	}

	public WorldBossReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(WorldBossReportData totalData) {
		this.totalData = totalData;
	}
}
