package data.reportdatas;

/**
* <首充用户等级分布报表>某个等级的首充统计数据的数据类型定义接口
* @author zhaizl
*
*/
public class FirstChargeUserInfo {
	
	/**
	 * 表示是否合计的数据。返回true表示合计数据，返回false则表示其中某天的数据
	 * @return 
	 */
	public boolean isTotalData;	
	
	/**
	 * 首充用户的角色等级
	 * @return
	 */
	public String roleLevel;
	
	/**
	 * 该等级的首充用户人数
	 * @return
	 */
	public String firstChargeUserCount;
	
    /**
     * 该等级的首充用户的充值总次数
     * @return
     */
	public String totalChargeCount;
	
	/**
	 * 该等级首充用户占比
	 * @return
	 */
	public String chargeUserRate;
	
	/**
	 * 该等级首充金额
	 * @return
	 */
	public String totalFirstChargeMoney;
	
	/**
	 * 该等级首充金额占比
	 * @return
	 */
	public String firstChargeMoneyRate;

	public boolean isTotalData() {
		return isTotalData;
	}

	public void setTotalData(boolean isTotalData) {
		this.isTotalData = isTotalData;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getFirstChargeUserCount() {
		return firstChargeUserCount;
	}

	public void setFirstChargeUserCount(String firstChargeUserCount) {
		this.firstChargeUserCount = firstChargeUserCount;
	}

	public String getTotalChargeCount() {
		return totalChargeCount;
	}

	public void setTotalChargeCount(String totalChargeCount) {
		this.totalChargeCount = totalChargeCount;
	}

	public String getChargeUserRate() {
		return chargeUserRate;
	}

	public void setChargeUserRate(String chargeUserRate) {
		this.chargeUserRate = chargeUserRate;
	}

	public String getTotalFirstChargeMoney() {
		return totalFirstChargeMoney;
	}

	public void setTotalFirstChargeMoney(String totalFirstChargeMoney) {
		this.totalFirstChargeMoney = totalFirstChargeMoney;
	}

	public String getFirstChargeMoneyRate() {
		return firstChargeMoneyRate;
	}

	public void setFirstChargeMoneyRate(String firstChargeMoneyRate) {
		this.firstChargeMoneyRate = firstChargeMoneyRate;
	}
	
	

}
