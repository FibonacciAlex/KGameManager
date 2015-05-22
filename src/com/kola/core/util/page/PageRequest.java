package com.kola.core.util.page;

import org.apache.commons.lang.StringUtils;

/**
 * 与具体ORM实现无关的分页查询参数封装.
 *
 *
 */
public class PageRequest {

	public static final String ASC = "asc";
	public static final String DESC = "desc";

	public static final int MIN_PAGESIZE = 1;
	public static final int MAX_PAGESIZE = 200;

	protected int page = 1;
	protected int pagesize = 30;
	protected String orderBy = "createDate";
	protected String order = DESC;
	protected boolean autoCount = true;

	public PageRequest() {
	}

	public PageRequest(final int pagesize) {
		setPagesize(pagesize);
	}

	public PageRequest(final int pagesize, final boolean autoCount) {
		setPagesize(pagesize);
		this.autoCount = autoCount;
	}

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setPage(final int page) {
		this.page = page;
		if (page < 1) {
			this.page = 1;
		}
	}

	/**
	 * 获得每页的记录数量,默认为10.
	 */
	public int getPagesize() {
		return pagesize;
	}

	/**
	 * 设置每页的记录数量,超出MIN_PAGESIZE与MAX_PAGESIZE范围会自动调整.
	 */
	public void setPagesize(final int pagesize) {
		this.pagesize = pagesize;
		if (pagesize < MIN_PAGESIZE) {
			this.pagesize = MIN_PAGESIZE;
		}
		if (pagesize > MAX_PAGESIZE) {
			this.pagesize = MAX_PAGESIZE;
		}
	}

	/**
	* 根据page和pagesize计算当前页第一条记录在总结果集中的位置,序号从0开始.
	*/
	public int getFirst() {
		return ((page - 1) * pagesize);
	}

	/**
	 * 获得排序字段,无默认值,多个排序字段时用逗号分隔,仅在Criterion查询时有效.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段,多个排序字段时用逗号分隔,仅在Criterion查询时有效.
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 是否已设置排序字段,仅在Criterion查询时有效.
	 */
	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(orderBy);
	}

	/**
	 * 获得排序方向,默认为asc,仅在Criterion查询时有效.
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用逗号分隔.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序方式向,仅在Criterion查询时有效.
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用逗号分隔.
	 */
	public void setOrder(final String order) {
		this.order = order.toLowerCase();
	}

	/**
	 * 取得分页参数的组合字符串.
	 * 组合字符串方便参数的传递,格式为page|orderBy|order.
	 */
	public String getPageRequest() {
		return getPage() + "|" + StringUtils.defaultString(getOrderBy()) + "|" + getOrder();
	}

	/**
	 * 设置分页参数的组合字符串.
	 * 组合字符串方便参数的传递,格式为page|orderBy|order.
	 */
	public void setPageRequest(final String pageParam) {

		if (StringUtils.isBlank(pageParam))
			return;

		String[] params = StringUtils.splitPreserveAllTokens(pageParam, '|');

		if (StringUtils.isNumeric(params[0])) {
			setPage(Integer.valueOf(params[0]));
		}

		if (StringUtils.isNotBlank(params[1])) {
			setOrderBy(params[1]);
		}

		if (StringUtils.isNotBlank(params[2])) {
			setOrder(params[2]);
		}
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数,默认为false,仅在Criterion查询时有效.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数,仅在Criterion查询时有效.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}
	
}
