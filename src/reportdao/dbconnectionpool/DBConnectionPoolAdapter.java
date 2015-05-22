/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportdao.dbconnectionpool;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jdom.Document;
import org.jdom.Element;

import reportdao.dbconnectionpool.mysql.DBConnectionFactory;
import reportdao.dbconnectionpool.mysql.DefineDataSourceManagerIF;
import reportdao.logging.KGameLogger;
import reportdao.util.XmlUtil;

/**
 * 数据库连接池管理器，可以通过方法{@link #getDBConnectionPool()}获取mysql连接池，通过
 * {@link #getHsConnectionPoolManager()} 获取handlerSocket连接池
 * 
 * @author Administrator
 */
public class DBConnectionPoolAdapter {

	private static final KGameLogger logger = KGameLogger
			.getLogger(DBConnectionPoolAdapter.class);

	// private static final String logicDBConPoolUrl =
	// "";//"./res/config/dbconfig/proxool_pool_mysql.properties";
	private static final String platformDBConPoolUrl = "";// "./res/config/dbconfig/proxool_pool_mysql_platform.properties";
	private static DefineDataSourceManagerIF bgDBConPool;
	// private static HashMap<Integer,DefineDataSourceManagerIF>
	// logicDBConPoolMap;
	// private static HashMap<Integer,Integer> gsDbMapping;
	private static DefineDataSourceManagerIF platformDBConPool;
	private static boolean isInitPool = false;

	// private static boolean isInitPlatformPool = false;

	public static void init() {
		// logicDBConPoolMap = new HashMap<Integer,
		// DefineDataSourceManagerIF>();
		// gsDbMapping = new HashMap<Integer, Integer>();
		if (!isInitPool) {
			isInitPool = true;
			try {
				// logger.info("！！！数据库配置加载开始！！！");
				// Document doc =
				// XmlUtil.openXml("./res/config/dbconfig/dbpoolconfig.xml");
				// if (doc != null) {
				// Element root = doc.getRootElement();
				// Element dbConfigRootE = root.getChild("dbConfig");
				//
				// Element bgPoolE = dbConfigRootE.getChild("bg");
				// String bgPoolUrl = bgPoolE.getChildText("configPath");

				File f = new File(".");
				URL url = DBConnectionPoolAdapter.class.getResource("/");
				String file_dir = (url.getPath() + "../")
						.replaceAll("%20", " ");
				bgDBConPool = DBConnectionFactory
						.getInstance()
						.newProxoolDataSourceInstance(
								file_dir
										+ "res/config/dbconfig/proxool_pool_mysql_bg.properties");
				platformDBConPool = DBConnectionFactory
						.getInstance()
						.newProxoolDataSourceInstance(
								file_dir
										+ "res/config/dbconfig/proxool_pool_mysql_platform.properties");
				// }
			} catch (Exception e) {
				logger.error("读取数据库配置表dbpoolconfig.xml发送错误", e);
			}			
		}
	}

	// /**
	// * 初始化逻辑DB的mysql和handlerSocket连接池
	// *
	// * @throws Exception
	// */
	// public static void initLogicDbPool(String url) throws Exception {
	// // if (!isInitLogicPool) {
	// logicDBConPool = DBConnectionFactory.getInstance()
	// .newProxoolDataSourceInstance(url);
	// // }
	// }
	//
	// /**
	// * 初始化平台DB的mysql和handlerSocket连接池
	// *
	// * @throws Exception
	// */
	// public static void initPlatformDbPool() throws Exception {
	// if (!isInitPlatformPool) {
	// platformDBConPool = DBConnectionFactory.getInstance()
	// .newProxoolDataSourceInstance(platformDBConPoolUrl);
	// }
	// }

	// public static DefineDataSourceManagerIF getLogicDBConnectionPool(int
	// gsId) {
	// if (!gsDbMapping.containsKey(gsId)) {
	// return null;
	// }
	// return logicDBConPoolMap.get(gsDbMapping.get(gsId));
	// }

	public static DefineDataSourceManagerIF getPlatformDBConnectionPool() {
		return platformDBConPool;
	}

	public static DefineDataSourceManagerIF getBgDBConnectionPool() {
		return bgDBConPool;
	}

}
