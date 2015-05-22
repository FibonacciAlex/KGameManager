package data.reportdatas;

/**
* <商城道具销售统计报表>某个道具统计数据的数据类型定义接口
* @author zhaizl
*
*/
public class ShopSalesReportData {
	
	/**
	 * 表示是否合计的数据。返回true表示合计数据，返回false则表示其中某项的数据
	 * @return 
	 */
	public boolean isTotalData;
	
	/**
	 * 道具分类
	 * @return
	 */
	public String itemType;
	
	/**
	 * 道具编码
	 * @return
	 */
	public String itemCode;
	
	/**
	 * 道具名称
	 * @return
	 */
	public String itemName;
	
	/**
	 * 道具价格（单位：元）
	 * @return
	 */
	public String sellPrice;
	
	/**
	 * 购买数量
	 * @return
	 */
	public String sellCount;
	
	/**
	 * 消费金额（单位：元）
	 * @return
	 */
	public String sellTotalPoint;
	
	/**
	 * 消费占比
	 * @return
	 */
	public String sellRate;
	
	/**
	 * 道具囤积量
	 * @return
	 */
	public String itemStockCount;
	
	/**
	 * 道具拥有人数
	 */
	public String ownerCount;
	
	/**
	 * 商店类型名称
	 */
	public String shopName;
	

	public boolean isTotalData() {
		return isTotalData;
	}

	public void setTotalData(boolean isTotalData) {
		this.isTotalData = isTotalData;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getSellCount() {
		return sellCount;
	}

	public void setSellCount(String sellCount) {
		this.sellCount = sellCount;
	}

	public String getSellTotalPoint() {
		return sellTotalPoint;
	}

	public void setSellTotalPoint(String sellTotalPoint) {
		this.sellTotalPoint = sellTotalPoint;
	}

	public String getSellRate() {
		return sellRate;
	}

	public void setSellRate(String sellRate) {
		this.sellRate = sellRate;
	}

	public String getItemStockCount() {
		return itemStockCount;
	}

	public void setItemStockCount(String itemStockCount) {
		this.itemStockCount = itemStockCount;
	}

	public String getOwnerCount() {
		return ownerCount;
	}

	public void setOwnerCount(String ownerCount) {
		this.ownerCount = ownerCount;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	

}
