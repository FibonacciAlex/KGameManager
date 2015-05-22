package reportdao.impl.reportCache;

public class ReportCacheKey {
	
	private int reportType;
	private String beginDate;
	private String endDate;
	private String serverId;
	private String parentPromoId;
	private String promoId;
	private long cacheTime;
	public ReportCacheKey(int reportType, String beginDate, String endDate,
			String serverId, String parentPromoId, String promoId,
			long cacheTime) {
		super();
		this.reportType = reportType;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.serverId = serverId;
		this.parentPromoId = parentPromoId;
		this.promoId = promoId;
		this.cacheTime = cacheTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((beginDate == null) ? 0 : beginDate.hashCode());
		result = prime * result + (int) (cacheTime ^ (cacheTime >>> 32));
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((parentPromoId == null) ? 0 : parentPromoId.hashCode());
		result = prime * result + ((promoId == null) ? 0 : promoId.hashCode());
		result = prime * result + reportType;
		result = prime * result
				+ ((serverId == null) ? 0 : serverId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportCacheKey other = (ReportCacheKey) obj;
		if (beginDate == null) {
			if (other.beginDate != null)
				return false;
		} else if (!beginDate.equals(other.beginDate))
			return false;
		if (cacheTime != other.cacheTime)
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (parentPromoId == null) {
			if (other.parentPromoId != null)
				return false;
		} else if (!parentPromoId.equals(other.parentPromoId))
			return false;
		if (promoId == null) {
			if (other.promoId != null)
				return false;
		} else if (!promoId.equals(other.promoId))
			return false;
		if (reportType != other.reportType)
			return false;
		if (serverId == null) {
			if (other.serverId != null)
				return false;
		} else if (!serverId.equals(other.serverId))
			return false;
		return true;
	}
		
	
	
}
