package reportdao;

import java.util.List;

import reportdao.querycondition.GameServerIDConditionData;

public interface QueryConditionDAO {
	
	/**
	 * 该方法可以获取以游戏区服务器ID为查询条件的所有服务器ID条件数据
	 * @return
	 */
	public List<GameServerIDConditionData> getAllGameServerIDConditionData();

}
