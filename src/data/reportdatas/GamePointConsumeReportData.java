package data.reportdatas;

/**
 * <全服元宝消耗明细>界面消费功能项统计数据的数据类型定义接口
 * @author zhaizl
 *
 */
public class GamePointConsumeReportData {
	
	/**
	 * 消耗途径名称（即消费点数的功能名称）
	 * @return
	 */
	public String consumeTypeName;
	
	/**
	 * 功能使用次数
	 * @return
	 */
	public String consumeCount;
	
	/**
	 * 功能消耗的点数总量
	 * @return
	 */
	public String consumePoint;
	
	/**
	 * 功能消费占比
	 */
	public String consumeRate;
	
	/**
	 * 人数
	 */
	public String roleCount;

	public String getConsumeTypeName() {
		return consumeTypeName;
	}

	public void setConsumeTypeName(String consumeTypeName) {
		this.consumeTypeName = consumeTypeName;
	}

	public String getConsumeCount() {
		return consumeCount;
	}

	public void setConsumeCount(String consumeCount) {
		this.consumeCount = consumeCount;
	}

	public String getConsumePoint() {
		return consumePoint;
	}

	public void setConsumePoint(String consumePoint) {
		this.consumePoint = consumePoint;
	}

	public String getConsumeRate() {
		return consumeRate;
	}

	public void setConsumeRate(String consumeRate) {
		this.consumeRate = consumeRate;
	}

	public String getRoleCount() {
		return roleCount;
	}

	public void setRoleCount(String roleCount) {
		this.roleCount = roleCount;
	}
	
	

}
