package data.reportdatas;

import java.util.List;

/**
 * <角色流失明细统计报表>界面的数据类型定义接口
 * 
 * @author zhaizl
 * 
 */
public class RoleLossReport implements Report {

	public List<RoleLossReportData> roleLossReportList;

	public RoleLossReportData totalData;

	public List<RoleLossReportData> getRoleLossReportList() {
		return roleLossReportList;
	}

	public void setRoleLossReportList(
			List<RoleLossReportData> roleLossReportList) {
		this.roleLossReportList = roleLossReportList;
	}

	public RoleLossReportData getTotalData() {
		return totalData;
	}

	public void setTotalData(RoleLossReportData totalData) {
		this.totalData = totalData;
	}

}
