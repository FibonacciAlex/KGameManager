package reportdao.querycondition;

import reportdao.QueryConditionDAO;

/**
 * <pre>
 * 表示查询条件为游戏区ID时的键值对应的某个游戏区数据，此数据从服务器获取。
 * 每个游戏区有专属的区ID和名称：例如游戏区ID为"1"，区名为"游戏1区"。
 * 如果需要查询条件全部所有游戏区，则GameServerIDConditionData的serverId为"0"，serverName为"全部"
 * 获取方法参考{@link QueryConditionDAO#getAllGameServerIDConditionData()}
 * @author zhaizl
 * </pre>
 */
public class GameServerIDConditionData {
	/**
	 * <pre>
	 * 表示游戏区的服务器ID。 
	 * </pre>
	 */
	private String serverId;
	/**
	 * 表示游戏区的名称。
	 */
	private String serverName;
	
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	

}
