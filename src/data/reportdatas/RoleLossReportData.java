package data.reportdatas;

import java.text.DecimalFormat;

public class RoleLossReportData {

	/**
	 * 排名
	 */
	public String rank;
	/**
	 * 创建账号数
	 * 
	 * @return
	 */
	public String missionId;

	/**
	 * 创建角色数
	 * 
	 * @return
	 */
	public String completeCount;

	/**
	 * 未接过任何任务的角色数量
	 * 
	 * @return
	 */
	public String completeRate;
	
	public float rate;

	public String getMissionId() {
		return missionId;
	}

	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}

	public String getCompleteCount() {
		return completeCount;
	}

	public void setCompleteCount(String completeCount) {
		this.completeCount = completeCount;
	}

	public String getCompleteRate() {
		return completeRate;
	}

	public void setCompleteRate(String completeRate) {
		this.completeRate = completeRate;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
	
	

}
