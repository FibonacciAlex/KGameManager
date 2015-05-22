package data.reportdatas;

import java.util.List;
/**
 * <角色等级统计报表>界面的数据类型定义接口
 * @author zhaizl
 *
 */
public class RoleLevelReport implements Report {
	
	/**
	 * 返回每个等级角色数量统计数据的合集
	 * @return
	 */
	public List<RoleLevelReportData> getRoleLevelReportDataList;
	
//	/**
//	 * 返回角色总数
//	 * @return
//	 */
//	public String getTotalRoleCount;
	
	public RoleLevelReportData totalData;

	public List<RoleLevelReportData> getGetRoleLevelReportDataList() {
		return getRoleLevelReportDataList;
	}

	public void setGetRoleLevelReportDataList(
			List<RoleLevelReportData> getRoleLevelReportDataList) {
		this.getRoleLevelReportDataList = getRoleLevelReportDataList;
	}

	public RoleLevelReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(RoleLevelReportData totalData) {
		this.totalData = totalData;
	}

//	public String getGetTotalRoleCount() {
//		return getTotalRoleCount;
//	}
//
//	public void setGetTotalRoleCount(String getTotalRoleCount) {
//		this.getTotalRoleCount = getTotalRoleCount;
//	}
	
	
	
	

}
