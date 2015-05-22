package data.reportdatas;

import java.util.List;

/**
 * <角色职业统计报表>界面的数据类型定义接口
 * @author zhaizl
 *
 */
public class RoleOccupationReport implements Report {
	
	public List<RoleOccupationReportData> roleOccupationDataList;
	
	public RoleOccupationReportData totalData;

	public List<RoleOccupationReportData> getRoleOccupationDataList() {
		return roleOccupationDataList;
	}

	public void setRoleOccupationDataList(
			List<RoleOccupationReportData> roleOccupationDataList) {
		this.roleOccupationDataList = roleOccupationDataList;
	}

	public RoleOccupationReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(RoleOccupationReportData totalData) {
		this.totalData = totalData;
	}
	
	
	

}
