package com.hzbuvi.quiz.usersection.bean;

import java.util.List;

/**
 * Created by WANG, RUIQING on 11/27/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class RankCache {

	private List data;
	private Long createTime;

	public RankCache() {
		this.createTime = System.currentTimeMillis();
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public boolean verifyTime(){
		Long now = System.currentTimeMillis();
		Long due = this.createTime + 10000;
		if ( due > now  ) {
			return true;
		} else {
			return false;
		}
	}
}


