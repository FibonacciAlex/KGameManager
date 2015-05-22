package data.reportdatas;

/**
* <元宝库存统计报表>每天库存元宝统计数据的数据类型定义接口
* @author zhaizl
*
*/
public class GamePointStockReportData {
	
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
	 * 充值元宝数量
	 * @return
	 */
	public String chargePoint;
	
	/**
	 * 赠送元宝数量
	 * @return
	 */
	public String presentPoint;
	
	/**
	 * 消耗元宝数量
	 * @return
	 */
	public String consumePoint;
	
	/**
	 * 囤积元宝数量
	 * @return
	 */
	public String stockPoint;

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

	public String getChargePoint() {
		return chargePoint;
	}

	public void setChargePoint(String chargePoint) {
		this.chargePoint = chargePoint;
	}

	public String getPresentPoint() {
		return presentPoint;
	}

	public void setPresentPoint(String presentPoint) {
		this.presentPoint = presentPoint;
	}

	public String getConsumePoint() {
		return consumePoint;
	}

	public void setConsumePoint(String consumePoint) {
		this.consumePoint = consumePoint;
	}

	public String getStockPoint() {
		return stockPoint;
	}

	public void setStockPoint(String stockPoint) {
		this.stockPoint = stockPoint;
	}
	
	

}
