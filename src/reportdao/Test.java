package reportdao;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import data.reportdatas.DailyReport;

public class Test {
	public static void main(String[] args) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("beginDate", "${beginDate}");
		condition.put("endDate", "${endDate}");
		condition.put("server", "${server}");
		DailyReport dailyReport = DAOFactory.getInstance().getDailyReportDAO().getDailyReport(condition);
		System.out.println(JSONArray.fromObject(dailyReport.getDailyReportDataList).toString());
	}
}
