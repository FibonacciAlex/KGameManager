package data.reportdatas;

/**
* <VIP统计报表>某vip等级的统计数据的数据类型定义接口
* @author zhaizl
*
*/
public class VIPReportData {
	
	/**
	 * 表示是否合计的数据。返回true表示合计数据，返回false则表示其中某天的数据
	 * @return 
	 */
	public boolean isTotalData;
	
	/**
	 * 日报表统计的日期
	 * @return
	 */
	public String reportDate;
	/**
	 * vip等级
	 * @return
	 */
	public String vipLevel;
	
	/**
	 * 当前vip等级的角色数量
	 * @return
	 */
	public String vipRoleCount;
	
	/**
	 * 当前vip等级的角色当天登录人数
	 * @return
	 */
	public String loginVipRoleCount;

	public boolean isTotalData() {
		return isTotalData;
	}

	public void setTotalData(boolean isTotalData) {
		this.isTotalData = isTotalData;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getVipRoleCount() {
		return vipRoleCount;
	}

	public void setVipRoleCount(String vipRoleCount) {
		this.vipRoleCount = vipRoleCount;
	}

	public String getLoginVipRoleCount() {
		return loginVipRoleCount;
	}

	public void setLoginVipRoleCount(String loginVipRoleCount) {
		this.loginVipRoleCount = loginVipRoleCount;
	}
	
	

}
