package data.reportdatas;
/**
* <角色等级统计报表>每个等级的角色数量统计数据的数据类型定义接口
* @author zhaizl
*
*/
public class RoleLevelReportData {
	
	/**
	 * 角色等级
	 * @return
	 */
	public String level;
	
	/**
	 * 该等级角色数量
	 * @return
	 */
	public String roleCount;
	
	/**
	 * 该等级角色分布比率
	 * @return
	 */
	public String levelRate;
	
	/**
	 * 升级平均用时
	 */
	public String upGradeAvgTime;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getRoleCount() {
		return roleCount;
	}

	public void setRoleCount(String roleCount) {
		this.roleCount = roleCount;
	}

	public String getLevelRate() {
		return levelRate;
	}

	public void setLevelRate(String levelRate) {
		this.levelRate = levelRate;
	}

	public String getUpGradeAvgTime() {
		return upGradeAvgTime;
	}

	public void setUpGradeAvgTime(String upGradeAvgTime) {
		this.upGradeAvgTime = upGradeAvgTime;
	}
	
	

}
