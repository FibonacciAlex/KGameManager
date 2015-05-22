package data.reportdatas;

import java.util.List;

/**
 * <首充用户等级分布报表>界面的数据类型定义接口
 * @author zhaizl
 *
 */
public class FirstChargeReport implements Report {
	
	/**
	 * 返回所有等级的首充情况统计数据的合集
	 * @return
	 */
	public List<FirstChargeUserInfo> firstChargeUserInfoList;
	
	/**
	 * 返回报表的合计项数据
	 * @return
	 */
	public FirstChargeUserInfo totalData;

	public List<FirstChargeUserInfo> getFirstChargeUserInfoList() {
		return firstChargeUserInfoList;
	}

	public void setFirstChargeUserInfoList(
			List<FirstChargeUserInfo> firstChargeUserInfoList) {
		this.firstChargeUserInfoList = firstChargeUserInfoList;
	}

	public FirstChargeUserInfo getTotalData() {
		return totalData;
	}

	public void setTotalData(FirstChargeUserInfo totalData) {
		this.totalData = totalData;
	}
	
	

}
