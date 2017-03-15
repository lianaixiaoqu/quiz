package com.hzbuvi.quiz.config;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by WANG, RUIQING on 11/4/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class PageModel {


	private int page ; // 当前页
	private int totalPages ; // 总页数
	private int pageSize;// 每页10条数据
	private int totalRows ; // 总数据数
	private List content; // query condition

	private boolean hasPrevPage = true;
	private boolean hasNextPage = true;

	private String url;
	private Object condition;


	/// generate pageString
	private List<String> pageParam;
	private List<String>  pageParamVal;


	public PageModel(Page questionbanks) {
		this.page = questionbanks.getNumber();
		this.totalPages = questionbanks.getTotalPages();
		this.pageSize = questionbanks.getContent().size();
		this.totalRows = (int) questionbanks.getTotalElements();
		this.content = questionbanks.getContent();

		if (this.page == totalPages -1 ) {
			hasNextPage = false;
		}
		if (this.page == 0) {
			hasPrevPage = false;
		}
	}

	public PageModel(int page,int pageSize) {
		this.page = page;
		this.pageSize = pageSize;
	}






	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
		if ( totalRows % pageSize ==0 ) {
			this.totalPages = totalRows/pageSize;
		} else {
			this.totalPages = totalRows/pageSize + 1;
		}

		if (this.page >= totalPages -1 ) {
			hasNextPage = false;
		}
		if (this.page <= 0) {
			hasPrevPage = false;
		}


	}

	public List getContent() {
		return content;
	}

	public void setContent(List content) {
		this.content = content;
	}


	public List<String> getPageParam() {
		return pageParam;
	}

	public void setPageParam(List<String> pageParam) {
		this.pageParam = pageParam;
	}

	public List<String> getPageParamVal() {
		return pageParamVal;
	}

	public void setPageParamVal(List<String> pageParamVal) {
		this.pageParamVal = pageParamVal;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isHasPrevPage() {
		return hasPrevPage;
	}

	public void setHasPrevPage(boolean hasPrevPage) {
		this.hasPrevPage = hasPrevPage;
	}

	public Object getCondition() {
		return condition;
	}

	public void setCondition(Object condition) {
		this.condition = condition;
	}
}
