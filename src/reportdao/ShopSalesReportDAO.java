package reportdao;

import java.util.Map;

import reportdao.querycondition.GameServerIDConditionData;
import reportdao.querycondition.QueryCondition;
import data.reportdatas.GamePointConsumeReport;
import data.reportdatas.ShopSalesReport;
/**
 * <商城道具销售统计报表>的数据访问接口
 * 
 * @author zhaizl
 * 
 */
public interface ShopSalesReportDAO {
	
    public final static String SORT_KEY_CONSUME_COUNT = "sellCount";
	
	public final static String SORT_KEY_CONSUME_POINT = "sellTotalPoint";
	
	public final static String SORT_KEY_CONSUME_RATE = "sellRate";	

	public final static String SORT_KEY_ITEM_STOCK_COUNT = "itemStockCount";
	
	public final static String SORT_KEY_ITEM_SHOP_TYPE = "itemType";
	

	/**
	 * <pre>
	 * 查询游戏全部区或者某个区的商城道具销售统计报表数据
	 * 有效的查询条件:
	 * {@link QueryCondition#QUERY_KEY_BEGIN_DATE}，表示开始日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_END_DATE}，结束日期，value格式：String,"20130501"
	 * {@link QueryCondition#QUERY_KEY_SERVER_ID}，游戏区ID，value格式：String,{@link GameServerIDConditionData#getServerId()}
	 * @param condition
	 * @return
	 * </pre>
	 */
	public ShopSalesReport getShopSalesReport(Map<String,String> condition);



}
