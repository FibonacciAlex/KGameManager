package reportdao;

import reportdao.dbconnectionpool.DBConnectionPoolAdapter;
import reportdao.dbconnectionpool.mysql.DBConnectionFactory;
import reportdao.impl.ChargeRankReportDAOImpl;
import reportdao.impl.DailyReportDAOImpl;
import reportdao.impl.FamilyWarReportDAOImpl;
import reportdao.impl.FirstChargeReportDAOImpl;
import reportdao.impl.FirstFunConsumeReportDAOImpl;
import reportdao.impl.GameCopperStockReportDAOImpl;
import reportdao.impl.GameLevelLossReportDAOImpl;
import reportdao.impl.GamePointConsumeRankReportDAOImpl;
import reportdao.impl.GamePointConsumeReportDAOImpl;
import reportdao.impl.GamePointStockRankReportDAOImpl;
import reportdao.impl.GamePointStockReportDAOImpl;
import reportdao.impl.LoginInfoReportDAOImpl;
import reportdao.impl.NewGuideReportDAOImpl;
import reportdao.impl.QueryConditionDAOImpl;
import reportdao.impl.RoleLevelReportDAOImpl;
import reportdao.impl.RoleLossReportDAOImpl;
import reportdao.impl.RoleOccupationReportDAOImpl;
import reportdao.impl.ShopSalesReportDAOImpl;
import reportdao.impl.UserKeepOnlineReportDAOImpl;
import reportdao.impl.VIPReportDAOImpl;
import reportdao.impl.WorldBossReportDAOImpl;
import reportdao.impl.YYReportDAOImpl;
import reportdao.impl.reportCache.ReportCacheManager;

/**
 * DAO接口实例获取的工厂类
 * @author zhaizl
 *
 */
public class DAOFactory {
	
	private static DAOFactory instance;
	
	public DAOFactory(){
		DBConnectionPoolAdapter.init();
		ReportCacheManager.init();
	}
	
	public static DAOFactory getInstance(){
		if(instance == null){
			instance = new DAOFactory();
		}
		return instance;
	}
	
	public DailyReportDAO getDailyReportDAO(){
		return new DailyReportDAOImpl();
	}
	
	public GameCopperStockReportDAO getGameCopperStockReportDAO(){
		return new GameCopperStockReportDAOImpl();
	}
	
	public GamePointConsumeReportDAO getGamePointConsumeReportDAO(){
		return new GamePointConsumeReportDAOImpl();
	}
	
	public GamePointStockReportDAO getGamePointStockReportDAO(){
		return new GamePointStockReportDAOImpl();
	}
	
	public QueryConditionDAO getQueryConditionDAO(){
		return new QueryConditionDAOImpl();
	}
	
	public RoleLevelReportDAO getRoleLevelReportDAO(){
		return new RoleLevelReportDAOImpl();
	}
	
	public RoleLossReportDAO getRoleLossReportDAO(){
		return new RoleLossReportDAOImpl();
	}
	
	public RoleOccupationReportDAO getRoleOccupationReportDAO(){
		return new RoleOccupationReportDAOImpl();
	}
	
	public ShopSalesReportDAO getShopSalesReportDAO(){
		return new ShopSalesReportDAOImpl();
	}
	
	public UserKeepOnlineReportDAO getUserKeepOnlineReportDAO(){
		return new UserKeepOnlineReportDAOImpl();
	}
	
	public VIPReportDAO getVIPReportDAO(){
		return new VIPReportDAOImpl();
	}
	
	public FirstChargeReportDAO getFirstChargeReportDAO(){
		return new FirstChargeReportDAOImpl();
	}
	
	public FirstFunConsumeReportDAO getFirstFunConsumeReportDAO(){
		return new FirstFunConsumeReportDAOImpl();
	}
	
	public ChargeRankReportDAO getChargeRankReportDAO(){
		return new ChargeRankReportDAOImpl();
	}
	
	public GamePointConsumeRankReportDAO getGamePointConsumeRankReportDAO(){
		return new GamePointConsumeRankReportDAOImpl();
	}
	
	public GamePointStockRankReportDAO getGamePointStockRankReportDAO(){
		return new GamePointStockRankReportDAOImpl();
	}
	
	public WorldBossReportDAO getWorldBossReportDAO(){
		return new WorldBossReportDAOImpl();
	}
	
	public GameLevelLossReportDAO getGameLevelLossReportDAO(){
		return new GameLevelLossReportDAOImpl();
	}
	
	public NewGuideReportDAO getNewGuideReportDAO(){
		return new NewGuideReportDAOImpl();
	}
	
	public LoginInfoReportDAO getLoginInfoReportDAO(){
		return new LoginInfoReportDAOImpl();
	}
	
	public FamilyWarReportDAO getFamilyWarReportDAO(){
		return new FamilyWarReportDAOImpl();
	}
	
	public YYReportDAO getYYReportDAO(){
		return new YYReportDAOImpl();
	}

	public static MainPage getMainPage() {
		return new MainPage();
	}
	

}
