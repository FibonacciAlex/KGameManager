package reportdao.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import data.reportdatas.DailyReport;
import data.reportdatas.DailyReportData;
import reportdao.DailyReportDAO;
import reportdao.querycondition.QueryCondition;

public class CopyOfDailyReportDAOImpl implements DailyReportDAO {

	@Override
	public DailyReport getDailyReport(Map<String, String> condition) {
		// TODO Auto-generated method stub

		// 以下是参考的假数据做法

		// 解释condition条件的开始日期
		String beginDate = condition.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		// 解释condition条件的结束日期
		String endDate = condition.get(QueryCondition.QUERY_KEY_END_DATE);
		// 解释condition条件的游戏区ID
		int serverId = 0;
		if (condition.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
			serverId = Integer.parseInt(condition
					.get(QueryCondition.QUERY_KEY_SERVER_ID));
		}

		// 根据endDate与beginDate的差值计算需要查询的天数（如果endDate与beginDate相同，即查询天数为1）
		int dateCount = countDays(beginDate, endDate) + 1;

		if (dateCount > 0) {
			DailyReport report = new DailyReport();
			report.setGetDailyReportDataList(new ArrayList<DailyReportData>());
			for (int i = 0; i < dateCount; i++) {
				DailyReportData data = new DailyReportData();
				data.setTotalData(false);
				data.setReportDate(getNextDates(beginDate,i));
				// 下载量
				int downloadCount = random(8000, 18000);
				data.setDownloadCount(downloadCount + "");
				// 激活用户数(首次连上服务器的客户端数)
				int firstConnectUserCount = random((downloadCount - 2000),
						downloadCount);
				data.setFirstConnectUserCount(firstConnectUserCount + "");
				// 新增用户(当天新增的注册账号数)
				int registerUserCount = random((firstConnectUserCount - 1000),
						firstConnectUserCount);
				data.setRegisterUserCount(registerUserCount + "");
				// 新增有角用户(当天新增注册并有角色的用户数)
				int registerCreateRoleUserCount = random(
						(registerUserCount - 500), registerUserCount);
				data.setRegisterCreateRoleUserCount(registerCreateRoleUserCount
						+ "");
				// 新增有效用户(有角色且等级>3的新增用户)
				int registerEffectUserCount = random(
						(registerCreateRoleUserCount - 300),
						registerCreateRoleUserCount);
				data.setRegisterEffectUserCount(registerEffectUserCount + "");
				// 登陆用户（当天所有登录过的用户数）
				int loginUserCount = random(40000, 60000);
				data.setLoginUserCount(loginUserCount + "");
				// 首充用户（在游戏中第一次充值的用户）
				int firstChargeUserCount = random(200, 500);
				data.setFirstChargeUserCount(firstChargeUserCount + "");
				// 充值用户(当天充值的用户数)
				int todayChargeUser = random(800, 1200);
				data.setTodayChargeUser(todayChargeUser + "");
				// 付费ARPU(当天消费金额/当天充值人数)
				float arpu = (float) random(150, 250);
				data.setArpu(getFloatString(arpu));
				// 充值金额(当天充值总额)
				float todayChargeMoney = todayChargeUser * arpu;
				data.setTodayChargeMoney(getFloatString(todayChargeMoney));
				// DAU(日登陆用户数/月登录用户数)
				float DAU = ((float) random(1000, 2000)) / 100;
				data.setDAU(getFloatString(DAU) + "%");
				// 活跃用户(当天登陆一次的用户，不包括新增用户)
				int activeUserCount = random((loginUserCount
						- registerCreateRoleUserCount - 300), loginUserCount
						- registerCreateRoleUserCount);
				data.setActiveUserCount(activeUserCount + "");
				// 活跃用户ARPU
				float activeArpu = (float) random(200, 300);
				data.setActiveArpu(getFloatString(activeArpu));
				// 忠诚用户(连续三天登录的用户数)
				int loyalUserCount = random((loginUserCount / 2 - 2000),
						loginUserCount / 2);
				data.setLoyalUserCount(loyalUserCount + "");
				// 回流用户（当天前3天未登陆过游戏的旧注册账号）
				int comebackUserCount = random((loginUserCount / 2 - 2000),
						loginUserCount / 2);
				data.setLoyalUserCount(loyalUserCount + "");
				data.setComebackUserCount(comebackUserCount + "");
				// 最高在线（当天在线的人数峰值）
				int highestOnlineCount = random(10000, 15000);
				data.setHighestOnlineCount(highestOnlineCount + "");
				// 平均在线（平均当天各时间段的在线用户数）
				int avgOnlineCount = random((highestOnlineCount / 4 * 3 - 500),
						highestOnlineCount / 4 * 3);
				data.setAvgOnlineCount(avgOnlineCount + "");
				// 人均登录次数（当天每人平均登陆次数）
				float perUserAvgLoginCount = ((float) random(250, 450)) / 100;
				data.setPerUserAvgLoginCount(getFloatString(perUserAvgLoginCount));
				// 人均时长（当天每人平均在线时长(单位:小时)）
				float perUserAvgOnlineTime = ((float) random(30, 100)) / 100;
				data.setPerUserAvgOnlineTime(getFloatString(perUserAvgOnlineTime));
				// 活跃用户流失率（(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数）
				float activeUserLossRate = ((float) random(1500, 3000)) / 100;
				data.setActiveUserLossRate(getFloatString(activeUserLossRate)
						+ "%");
				// 付费渗透率（当天充值人数/当天登陆人数）
				float payRate = ((float) random(200, 350)) / 100;
				data.setPayRate(getFloatString(payRate) + "%");
				// 创角率（新增用户/激活用户数）
				float createRoleRate = ((float) registerCreateRoleUserCount / (float) firstConnectUserCount) * 100;
				data.setCreateRoleRate(getFloatString(createRoleRate) + "%");

				report.getDailyReportDataList.add(data);
			}

			return report;
		} else {
			return null;
		}

	}

	/**
	 * 返回min到max之间的随机数，不包含max这个边界值
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomWithoutMax(int min, int max) {
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		return new Random().nextInt(max - min) + min;
	}

	/**
	 * 返回0到max之间的随机数，不包含max
	 * 
	 * @param max
	 * @return
	 */
	public static int random(int max) {
		if (max < 0) {
			return random(max, 0);
		} else {
			return new Random().nextInt(max);
		}
	}

	/**
	 * 返回min到max之间的随机数，包含min和max两个边界值
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max) {
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		return new Random().nextInt((max + 1) - min) + min;
	}

	public static String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(value);
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyyMMdd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
    
	/**
	 * 计算两个日期之间相差的天数
	 * 日期格式：yyyyMMdd
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int countDays(String begin, String end) {
		int days = 0;

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar c_b = Calendar.getInstance();
		Calendar c_e = Calendar.getInstance();

		try {
			c_b.setTime(df.parse(begin));
			c_e.setTime(df.parse(end));

			while (c_b.before(c_e)) {
				days++;
				c_b.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException pe) {
			System.out.println("日期格式必须为：yyyyMMdd；如：20130404.");
		}

		return days;
	}
	
	/**
	 * 根据日期条件计算N天后的日期，日期格式：yyyyMMdd
	 * @param beginDate
	 * @param dateCount
	 * @return
	 */
	public static String getNextDates(String beginDate,int dateCount){
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar c_b = Calendar.getInstance();
		try {
			c_b.setTime(df.parse(beginDate));
			c_b.add(Calendar.DAY_OF_YEAR, dateCount);
		} catch (ParseException e) {
			System.out.println("日期格式必须为：yyyyMMdd；如：20130404.");
		}
		return df.format(c_b.getTime());
	}

	public static void main(String[] args) {
		CopyOfDailyReportDAOImpl impl = new CopyOfDailyReportDAOImpl();
		Map<String, String> condition = new HashMap<String, String>();
		condition.put(QueryCondition.QUERY_KEY_BEGIN_DATE, "20130430");
		condition.put(QueryCondition.QUERY_KEY_END_DATE, "20130502");
		condition.put(QueryCondition.QUERY_KEY_SERVER_ID, "1");

		DailyReport report = impl.getDailyReport(condition);

		for (DailyReportData data : report.getDailyReportDataList) {
			StringBuilder builder = new StringBuilder();
			builder.append("***************************" + "\n");
			builder.append("getReportDate:" + data.getReportDate() + "\n");
			builder.append("getDownloadCount:" + data.getDownloadCount() + "\n");
			builder.append("getFirstConnectUserCount:"
					+ data.getFirstConnectUserCount() + "\n");
			builder.append("getRegisterUserCount:"
					+ data.getRegisterUserCount() + "\n");
			builder.append("getRegisterCreateRoleUserCount:"
					+ data.getRegisterCreateRoleUserCount() + "\n");
			builder.append("getRegisterEffectUserCount:"
					+ data.getRegisterEffectUserCount() + "\n");
			builder.append("getLoginUserCount:" + data.getLoginUserCount()
					+ "\n");
			builder.append("getFirstChargeUserCount:"
					+ data.getFirstChargeUserCount() + "\n");
			builder.append("getTodayChargeUser:" + data.getTodayChargeUser()
					+ "\n");
			builder.append("getTodayChargeMoney:" + data.getTodayChargeMoney()
					+ "\n");
			builder.append("getArpu:" + data.getArpu() + "\n");
			builder.append("getDAU:" + data.getDAU() + "\n");
			builder.append("getActiveUserCount:" + data.getActiveUserCount()
					+ "\n");
			builder.append("getActiveArpu:" + data.getActiveArpu() + "\n");
			builder.append("getLoyalUserCount:" + data.getLoyalUserCount()
					+ "\n");
			builder.append("getComebackUserCount:"
					+ data.getComebackUserCount() + "\n");
			builder.append("getHighestOnlineCount:"
					+ data.getHighestOnlineCount() + "\n");
			builder.append("getAvgOnlineCount:" + data.getAvgOnlineCount()
					+ "\n");
			builder.append("getPerUserAvgLoginCount:"
					+ data.getPerUserAvgLoginCount() + "\n");
			builder.append("getPerUserAvgOnlineTime:"
					+ data.getPerUserAvgOnlineTime() + "\n");
			builder.append("getActiveUserLossRate:"
					+ data.getActiveUserLossRate() + "\n");
			builder.append("getPayRate:" + data.getPayRate() + "\n");
			builder.append("getCreateRoleRate:" + data.getCreateRoleRate()
					+ "\n");
			builder.append("***************************" + "\n");
			System.out.println(builder.toString());
		}

//		System.out.println(strToDate("20130501"));
//		System.out.println(getNextDates("20130501",10));
	}

}
