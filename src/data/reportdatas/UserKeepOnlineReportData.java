package data.reportdatas;

import java.text.DecimalFormat;

/**
* <玩家留存率统计报表>某天的角色留存统计数据的数据类型定义接口
* @author zhaizl
*
*/
public class UserKeepOnlineReportData {
	
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
	 * 表示星期几
	 * @return
	 */
	public String dayOfweek;
	
	/**
	 * 创建用户数量
	 * @return
	 */
	public String createUserCount;
	
	/**
	 * 统计的留存数据，该值为一个数组，长度为16。表示从第1~15天、以及第30天的每天角色留存数据
	 * 每天的数据格式为：1000(73.5%)。意思表达当天登录人数1000人，留存率为73.5%
	 * @return
	 */
	public int[] loginCountInfo;
	
	public String day1;
	public String day2;
	public String day3;
	public String day4;
	public String day5;
	public String day6;
	public String day7;
	public String day8;
	public String day9;
	public String day10;
	public String day11;
	public String day12;
	public String day13;
	public String day14;
	public String day15;
	public String day30;
	

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

	public String getDayOfweek() {
		return dayOfweek;
	}

	public void setDayOfweek(String dayOfweek) {
		this.dayOfweek = dayOfweek;
	}

	public String getCreateUserCount() {
		return createUserCount;
	}

	public void setCreateUserCount(String createUserCount) {
		this.createUserCount = createUserCount;
	}

	public String getDay1() {
		if(loginCountInfo.length>=1){
			return loginCountInfo[0]+"("+getRate(loginCountInfo[0])+")";
		}else{
			return "－";
		}		
	}

	public String getDay2() {
		if(loginCountInfo.length>=2){
			return loginCountInfo[1]+"("+getRate(loginCountInfo[1])+")";
		}else{
			return "－";
		}	
	}

	public String getDay3() {
		if(loginCountInfo.length>=3){
			return loginCountInfo[2]+"("+getRate(loginCountInfo[2])+")";
		}else{
			return "－";
		}	
	}

	public String getDay4() {
		if(loginCountInfo.length>=4){
			return loginCountInfo[3]+"("+getRate(loginCountInfo[3])+")";
		}else{
			return "－";
		}	
	}

	public String getDay5() {
		if(loginCountInfo.length>=5){
			return loginCountInfo[4]+"("+getRate(loginCountInfo[4])+")";
		}else{
			return "－";
		}	
	}

	public String getDay6() {
		if(loginCountInfo.length>=6){
			return loginCountInfo[5]+"("+getRate(loginCountInfo[5])+")";
		}else{
			return "－";
		}	
	}

	public String getDay7() {
		if(loginCountInfo.length>=7){
			return loginCountInfo[6]+"("+getRate(loginCountInfo[6])+")";
		}else{
			return "－";
		}	
	}

	public String getDay8() {
		if(loginCountInfo.length>=8){
			return loginCountInfo[7]+"("+getRate(loginCountInfo[7])+")";
		}else{
			return "－";
		}	
	}

	public String getDay9() {
		if(loginCountInfo.length>=9){
			return loginCountInfo[8]+"("+getRate(loginCountInfo[8])+")";
		}else{
			return "－";
		}	
	}

	public String getDay10() {
		if(loginCountInfo.length>=10){
			return loginCountInfo[9]+"("+getRate(loginCountInfo[9])+")";
		}else{
			return "－";
		}	
	}

	public String getDay11() {
		if(loginCountInfo.length>=11){
			return loginCountInfo[10]+"("+getRate(loginCountInfo[10])+")";
		}else{
			return "－";
		}	
	}

	public String getDay12() {
		if(loginCountInfo.length>=12){
			return loginCountInfo[11]+"("+getRate(loginCountInfo[11])+")";
		}else{
			return "－";
		}	
	}

	public String getDay13() {
		if(loginCountInfo.length>=13){
			return loginCountInfo[12]+"("+getRate(loginCountInfo[12])+")";
		}else{
			return "－";
		}	
	}

	public String getDay14() {
		if(loginCountInfo.length>=14){
			return loginCountInfo[13]+"("+getRate(loginCountInfo[13])+")";
		}else{
			return "－";
		}	
	}

	public String getDay15() {
		if(loginCountInfo.length>=15){
			return loginCountInfo[14]+"("+getRate(loginCountInfo[14])+")";
		}else{
			return "－";
		}	
	}

	public String getDay30() {
		if(loginCountInfo.length>=16){
			return loginCountInfo[15]+"("+getRate(loginCountInfo[15])+")";
		}else{
			return "－";
		}	
	}

	public int[] getLoginCountInfo() {
		return loginCountInfo;
	}

	public void setLoginCountInfo(int[] loginCountInfo) {
		this.loginCountInfo = loginCountInfo;
	}
	
	private String getRate(int loginCount){
		float rate = 0f;
		if(Integer.parseInt(createUserCount)>0){
			rate = (((float)loginCount)*100/Integer.parseInt(createUserCount));
		}
		return getFloatString(rate)+"%";
	}
	
	private String getFloatString(float value) {
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(value);
	}
	
	

}
