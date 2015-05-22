package com.kola.core.util.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * 与具体ORM实现无关的分页查询结果封装,并继承PageRequest中的分页查询参数.
 * 
 * @param <T> Page中记录的类型.
 * 
 * @author dengzhifeng
 */
public class Page<T> extends PageRequest {
	private List<T> rows = null;
	private int total = 0;
	protected long pageCount = 10;
	private String property ="";// 查找属性名称
	private String keyword ="";// 查找关键字
	
	public static final int DEF_COUNT = 12;

	// 构造函数

	public Page() {
	}

	public Page(final int pagesize) {
		super(pagesize);
	}

	public Page(final int pagesize, final boolean autoCount) {
		super(pagesize, autoCount);
	}
	
	public Page(final int page,final int pagesize,final int total){
		this.page = page;
		this.pagesize = pagesize;
		this.total = total;
	}
	
	@SuppressWarnings("unchecked")
	public Page(int page, int pagesize, int total, List list) {
		if (total <= 0) {
			this.total = 0;
		} else {
			this.total = total;
		}
		if (pagesize <= 0) {
			this.pagesize = DEF_COUNT;
		} else {
			this.pagesize = pagesize;
		}
		if (page <= 0) {
			this.page = 1;
		} else {
			this.page = page;
		}
		if ((this.page - 1) * this.pagesize >= total) {
			this.page = total / pagesize;
		}
		this.rows = list;
	}


	/**
	 * 取得页内的记录列表.
	 */
	public List<T> getRows() {
		return rows;
	}

	public void setRows(final List<T> rows) {
		this.rows = rows;
	}

	/**
	 * 取得总记录数.
	 */
	public int getTotal() {
		return total;
	}

	public void setTotal(final int total) {
		this.total = total;
	}

	/**
	 * 计算总页数.
	 */
 

	/**
	 * 取得倒转的排序方向.
	 */
	public String getInverseOrder() {
		String[] orders = StringUtils.split(order, ',');

		for (int i = 0; i < orders.length; i++) {
			if (DESC.equals(orders[i])) {
				orders[i] = ASC;
			} else {
				orders[i] = DESC;
			}
		}
		return StringUtils.join(orders);
	}

	
	public long getPageCount() {
		if (total < 0)
			return -1;

		pageCount = total / pagesize;
		if (total % pagesize > 0) {
			pageCount++;
		}
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (page + 1 <= getPageCount());
	}

	/**
	 * 取得下页的页号,序号从1开始.
	 */
	public int getNextPage() {
		if (isHasNext())
			return page + 1;
		else
			return page;
	}

	/**
	 * 是否还有上一页. 
	 */
	public boolean isHasPre() {
		return (page - 1 >= 1);
	}

	/**
	 * 取得上页的页号,序号从1开始.
	 */
	public int getPrePage() {
		if (isHasPre())
			return page - 1;
		else
			return page;
	}
	
	
	public  Page<T> buildPage(HttpServletRequest request){
		Page<T> page=new Page<T>();
		if(StringUtils.isNotEmpty(request.getParameter("order"))){
			page.setOrder(request.getParameter("order"));
		} 
		if(StringUtils.isNotEmpty(request.getParameter("orderBy"))){
			page.setOrderBy(request.getParameter("orderBy"));
		} 
		String start=request.getParameter("start");
		String limit=request.getParameter("limit");
		if(StringUtils.isNotEmpty(start)){
			int pageno=Integer.parseInt(start)/Integer.parseInt(limit)+1;
			page.setPage(pageno);
		}else{
			page.setPage(1);
		}
		if(StringUtils.isNotEmpty(limit)){
			page.setPagesize(Integer.parseInt(limit));
		}else{
			page.setPagesize(DEF_COUNT);
		}
		
		return page;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	
	public String ajaxPage(){
		JSONObject json = new JSONObject();
		json.put("Total", this.getTotal());
		json.put("Rows", this.getRows());
		return json.toString();
	}
}
