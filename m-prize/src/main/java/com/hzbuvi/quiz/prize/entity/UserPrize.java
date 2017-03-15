package com.hzbuvi.quiz.prize.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "prize_user")
public class UserPrize {

	@Id
	@GeneratedValue( strategy = GenerationType.TABLE )
	private Integer id;
	private Integer userId;
	private Integer activityId;
	private Integer chapterId;
	private Integer sectionId;
	private Integer chapterPrizeId;
	private String title;
	private String content;
	private Date createTime;
	private String userName;


	public UserPrize() {
		this.createTime = new Date();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public Integer getChapterPrizeId() {
		return chapterPrizeId;
	}

	public void setChapterPrizeId(Integer chapterPrizeId) {
		this.chapterPrizeId = chapterPrizeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
