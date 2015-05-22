package reportdao.impl.reportCache;

import java.util.Map;

import reportdao.querycondition.QueryCondition;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import data.reportdatas.Report;

public class ReportCacheManager {

	private static ConcurrentLinkedHashMap<ReportCacheKey, Report> reportCacheMap;

	private final static long DAILY_CACHE_DATA_KEEP_TIME = 20 * 60 * 1000;

	private final static long CACHE_DATA_KEEP_TIME = 5 * 60 * 1000;

	public static void init() {
		if (reportCacheMap == null) {
			reportCacheMap = new ConcurrentLinkedHashMap.Builder<ReportCacheKey, Report>()
					.maximumWeightedCapacity(1000)
					.weigher(Weighers.singleton())
					.listener(new CacheEvictionListener()).concurrencyLevel(32)
					.build();
		}
	}

	public static Report getReport(int reportType, Map<String, String> keyMap) {

		String beginDate = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_BEGIN_DATE)) {
			beginDate = keyMap.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		}
		String endDate = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_BEGIN_DATE)) {
			endDate = keyMap.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		}
		String serverId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
			serverId = keyMap.get(QueryCondition.QUERY_KEY_SERVER_ID);
		}
		String parentPromoId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_PARENT_PROMO)) {
			parentPromoId = keyMap.get(QueryCondition.QUERY_KEY_PARENT_PROMO);
		}
		String promoId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_SUB_PROMO)) {
			promoId = keyMap.get(QueryCondition.QUERY_KEY_SUB_PROMO);
		}

		long cacheTime = System.currentTimeMillis();
		cacheTime = cacheTime - (cacheTime % (getCacheKeepTime(reportType)));

		ReportCacheKey key = new ReportCacheKey(reportType, beginDate, endDate,
				serverId, parentPromoId, promoId, cacheTime);

		if (reportCacheMap.containsKey(key)) {
			return reportCacheMap.get(key);
		}

		return null;
	}

	public static void addReport(int reportType, Map<String, String> keyMap,
			Report report) {
		String beginDate = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_BEGIN_DATE)) {
			beginDate = keyMap.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		}
		String endDate = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_BEGIN_DATE)) {
			endDate = keyMap.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		}
		String serverId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
			serverId = keyMap.get(QueryCondition.QUERY_KEY_SERVER_ID);
		}
		String parentPromoId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_PARENT_PROMO)) {
			parentPromoId = keyMap.get(QueryCondition.QUERY_KEY_PARENT_PROMO);
		}
		String promoId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_SUB_PROMO)) {
			promoId = keyMap.get(QueryCondition.QUERY_KEY_SUB_PROMO);
		}

		long cacheTime = System.currentTimeMillis();
		cacheTime = cacheTime - (cacheTime % (getCacheKeepTime(reportType)));

		ReportCacheKey key = new ReportCacheKey(reportType, beginDate, endDate,
				serverId, parentPromoId, promoId, cacheTime);

		reportCacheMap.put(key, report);
	}

	public static boolean isReportExist(int reportType,
			Map<String, String> keyMap) {
		String beginDate = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_BEGIN_DATE)) {
			beginDate = keyMap.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		}
		String endDate = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_BEGIN_DATE)) {
			endDate = keyMap.get(QueryCondition.QUERY_KEY_BEGIN_DATE);
		}
		String serverId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_SERVER_ID)) {
			serverId = keyMap.get(QueryCondition.QUERY_KEY_SERVER_ID);
		}
		String parentPromoId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_PARENT_PROMO)) {
			parentPromoId = keyMap.get(QueryCondition.QUERY_KEY_PARENT_PROMO);
		}
		String promoId = "-1";
		if (keyMap.containsKey(QueryCondition.QUERY_KEY_SUB_PROMO)) {
			promoId = keyMap.get(QueryCondition.QUERY_KEY_SUB_PROMO);
		}

		long cacheTime = System.currentTimeMillis();
		cacheTime = cacheTime - (cacheTime % (getCacheKeepTime(reportType)));

		ReportCacheKey key = new ReportCacheKey(reportType, beginDate, endDate,
				serverId, parentPromoId, promoId, cacheTime);

		return reportCacheMap.containsKey(key);
	}

	public static class CacheEvictionListener implements
			EvictionListener<ReportCacheKey, Report> {

		@Override
		public void onEviction(ReportCacheKey arg0, Report arg1) {
			// TODO Auto-generated method stub

		}
	}

	private static long getCacheKeepTime(int reportType) {
		if (reportType == Report.REPORT_TYPE_DAILY) {
			return DAILY_CACHE_DATA_KEEP_TIME;
		} else {
			return CACHE_DATA_KEEP_TIME;
		}
	}

	public static void main(String[] args) {
		long cacheTime = System.currentTimeMillis();
		System.out.println("----" + cacheTime);
		cacheTime = cacheTime - (cacheTime % (CACHE_DATA_KEEP_TIME));
		System.out.println("::::" + cacheTime);
	}

}
