package data.reportdatas;
/**
* <铜钱库存统计报表>每天库存铜钱统计数据的数据类型定义接口
* @author zhaizl
*
*/
public class GameCopperStockReportData {
	
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
	 * 系统当天产出铜钱数量
	 * @return
	 */
	public String produceCopper;
	
	/**
	 * 玩家当天消耗铜钱数量
	 * @return
	 */
	public String consumeCopper;
	
	/**
	 * 当天铜钱数量囤积量
	 * @return
	 */
	public String stockCopper;

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

	public String getProduceCopper() {
		return produceCopper;
	}

	public void setProduceCopper(String produceCopper) {
		this.produceCopper = produceCopper;
	}

	public String getConsumeCopper() {
		return consumeCopper;
	}

	public void setConsumeCopper(String consumeCopper) {
		this.consumeCopper = consumeCopper;
	}

	public String getStockCopper() {
		return stockCopper;
	}

	public void setStockCopper(String stockCopper) {
		this.stockCopper = stockCopper;
	}


	
	

}
