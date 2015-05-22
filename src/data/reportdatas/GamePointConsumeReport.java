package data.reportdatas;

import java.util.List;

/**
 * <全服元宝消耗明细>报表数据类型接口
 * @author zhaizl
 *
 */
public class GamePointConsumeReport implements Report {
	
	/**
	 * 返回每项功能点数消耗统计数据的合集
	 * @return
	 */
	public List<GamePointConsumeReportData> getGamePointConsumeReportList;
	
	/**
	 * 获取所有功能项的使用总次数
	 * @return
	 */
	public GamePointConsumeReportData totalData;

	public List<GamePointConsumeReportData> getGetGamePointConsumeReportList() {
		return getGamePointConsumeReportList;
	}

	public void setGetGamePointConsumeReportList(
			List<GamePointConsumeReportData> getGamePointConsumeReportList) {
		this.getGamePointConsumeReportList = getGamePointConsumeReportList;
	}

	public GamePointConsumeReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(GamePointConsumeReportData totalData) {
		this.totalData = totalData;
	}


	
	


	
	

}
