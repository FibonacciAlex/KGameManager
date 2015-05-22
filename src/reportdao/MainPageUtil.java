package reportdao;

import java.util.Map;

public class MainPageUtil {
	public static String getZxcz(Map<String, String> params) {
		return MainPage.getInstance().getZxcz(params);
	}

	public static String getZxrs(Map<String, String> params) {
		return MainPage.getInstance().getZxrs(params);
	}

	public static String getYsrt(Map<String, String> params) {
		return MainPage.getInstance().getYsrt(params);
	}

}
