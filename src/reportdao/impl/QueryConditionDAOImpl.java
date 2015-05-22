package reportdao.impl;

import java.util.ArrayList;
import java.util.List;

import reportdao.QueryConditionDAO;
import reportdao.querycondition.GameServerIDConditionData;

public class QueryConditionDAOImpl implements QueryConditionDAO{

	@Override
	public List<GameServerIDConditionData> getAllGameServerIDConditionData() {
		// TODO Auto-generated method stub
		
		//以下是假数据做法
		List<GameServerIDConditionData> list = new ArrayList<GameServerIDConditionData>();
		for (int i = 1; i < 5; i++) {
			GameServerIDConditionData data = new GameServerIDConditionData();
			data.setServerId(i+"");
			data.setServerName("游戏"+i+"区");
			list.add(data);
		}
		return list;
	}

}
