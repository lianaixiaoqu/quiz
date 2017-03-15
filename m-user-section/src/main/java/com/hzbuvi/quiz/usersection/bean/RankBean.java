package com.hzbuvi.quiz.usersection.bean;

import java.util.Date;

/**
 * Created by WANG, RUIQING on 11/3/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class RankBean {

	private Integer index;
	private Integer userId;
	private String  userName;
	private Integer activityId;
	private Integer sections;
	private Integer stars;
	private Integer scores;
	private Integer departmentId;
	private String departmentName;
	private Integer like;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getSections() {
		return sections;
	}

	public void setSections(Integer sections) {
		this.sections = sections;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public Integer getScores() {
		return scores;
	}

	public void setScores(Integer scores) {
		this.scores = scores;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getLike() {
		return like;
	}

	public void setLike(Integer like) {
		this.like = like;
	}
}
