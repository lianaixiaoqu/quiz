package com.hzbuvi.quiz.usersection.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Downey.hz on 2016/10/13..
 */
@Entity
@Table(name="usersection")
public class UserSection {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private Integer id;
    private Integer userId;
	private String userName;
    private Integer activityId;
    private Integer sectionId;
    private Integer questionSheetId;
	private Integer departmentId;
	private String departmentName;//中心
	private String buMen;
	private Integer buMenNumber;
    private Date createTime;
    private Date lastModifyTime;
    private Integer score;
    private Integer stars;
    private Integer correctCount;
    private Integer errorCount;
    private Double correctRate;
    private Integer submitCount;
	private Long submitSeconds; // millsecond

	@Transient
	private Integer sections=1;

	@Transient
	private String jobNumber;

	public UserSection() {
		this.createTime = new Date();
		this.lastModifyTime=this.createTime;
		this.submitCount=1;
	}

	public String getBuMen() {
		return buMen;
	}

	public void setBuMen(String buMen) {
		this.buMen = buMen;
	}

	public Integer getBuMenNumber() {
		return buMenNumber;
	}

	public void setBuMenNumber(Integer buMenNumber) {
		this.buMenNumber = buMenNumber;
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

	public Integer getSections() {
		return sections;
	}

	public void setSections(Integer sections) {
		this.sections = sections;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		if (null != jobNumber ) {
			this.jobNumber = jobNumber;
		} else {
			this.jobNumber = "";
		}
	}

	public void calculate(UserSection input) {
		this.correctCount += input.getCorrectCount();
		this.sections ++ ;
		this.errorCount +=input.getErrorCount();
		this.stars += input.getStars();
		this.submitCount += input.getSubmitCount();
		this.addSubmitSeconds(input.getSubmitSeconds());
	}

	public Long getSubmitSeconds() {
		return submitSeconds;
	}

	public void setSubmitSeconds(Long submitSeconds) {
		this.submitSeconds = submitSeconds;
	}

	public void addSubmitSeconds(Long time){
		if ( null == this.submitSeconds ) {
			this.submitSeconds = Long.valueOf(0);
		}
		if ( null != time ) {
			this.submitSeconds +=  time;
		}
	}

	public void outputCheck() {
		if ( null == this.buMen ){
			this.buMen = "";
		}
		if (null == this.departmentName ){
			this.departmentName = "";
		}
	}
}
