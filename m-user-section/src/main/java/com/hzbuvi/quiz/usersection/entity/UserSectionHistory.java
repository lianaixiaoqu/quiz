package com.hzbuvi.quiz.usersection.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Downey.hz on 2016/10/13..
 */
@Entity
@Table(name="usersectionHistory")
public class UserSectionHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	private Integer userId;
	private Integer activityId;
	private Integer chapterId;
	private Integer sectionId;
	private Integer questionSheetId;
	private String answers;
	private Date startTime;
	private Date submitTime;
	private Integer score;
	private Integer stars;
	private Integer correctCount;
	private Integer errorCount;
	private Double correctRate;
	private Integer submitCount;

	public UserSectionHistory() {
		this.startTime = new Date();
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

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public Integer getQuestionSheetId() {
		return questionSheetId;
	}

	public void setQuestionSheetId(Integer questionSheetId) {
		this.questionSheetId = questionSheetId;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public Integer getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Double getCorrectRate() {
		return correctRate;
	}

	public void setCorrectRate(Double correctRate) {
		this.correctRate = correctRate;
	}

	public Integer getSubmitCount() {
		return submitCount;
	}

	public void setSubmitCount(Integer submitCount) {
		this.submitCount = submitCount;
	}

	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}
}
