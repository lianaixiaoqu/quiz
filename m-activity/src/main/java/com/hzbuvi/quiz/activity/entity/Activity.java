package com.hzbuvi.quiz.activity.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Downey.hz on 2016/10/10..
 */
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private Integer id;
    private String name;
    private String description;
    private Date startTime;
    private  Date endTime;
    private  String dailyStart; // 18:28:23  ->  182823
    private  String dailyEnd;
    private  Integer sectionLimitTime;
    private  Integer chapters;
    private  Integer sections;
    private  Integer questions;
    private Integer  questionSheets; // per section
    private Integer  createUserId;
    private Date createTime;
    private Integer isDelete;
    private Integer sectionsPerChapter;
    private Integer questionsPerSection;
    private Integer easyPerSection;
    private Integer normalPerSection;
    private Integer hardPerSection;

	private Integer isCurrent; // 是当前活动 , 1:是当前活动, 0:不是当前活动

	public Activity() {
		this.isDelete = 0;
		this.createTime = new Date();
	}


	public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDailyStart() {
        return dailyStart;
    }

    public void setDailyStart(String dailyStart) {
        this.dailyStart = dailyStart;
    }

    public String getDailyEnd() {
        return dailyEnd;
    }

    public void setDailyEnd(String dailyEnd) {
        this.dailyEnd = dailyEnd;
    }

    public Integer getSectionLimitTime() {
        return sectionLimitTime;
    }

    public void setSectionLimitTime(Integer sectionLimitTime) {
        this.sectionLimitTime = sectionLimitTime;
    }

    public Integer getChapters() {
        return chapters;
    }

    public void setChapters(Integer chapters) {
        this.chapters = chapters;
    }

    public Integer getSections() {
        return sections;
    }

    public void setSections(Integer sections) {
        this.sections = sections;
    }

    public Integer getQuestions() {
        return questions;
    }

    public void setQuestions(Integer questions) {
        this.questions = questions;
    }

    public Integer getQuestionSheets() {
        return questionSheets;
    }

    public void setQuestionSheets(Integer questionSheets) {
        this.questionSheets = questionSheets;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSectionsPerChapter() {
        return sectionsPerChapter;
    }

    public void setSectionsPerChapter(Integer sectionsPerChapter) {
        this.sectionsPerChapter = sectionsPerChapter;
    }

    public Integer getQuestionsPerSection() {
        return questionsPerSection;
    }

    public Integer getEasyPerSection() {
        return easyPerSection;
    }

    public void setEasyPerSection(Integer easyPerSection) {
        this.easyPerSection = easyPerSection;
    }

    public Integer getNormalPerSection() {
        return normalPerSection;
    }

    public void setNormalPerSection(Integer normalPerSection) {
        this.normalPerSection = normalPerSection;
    }

    public Integer getHardPerSection() {
        return hardPerSection;
    }

    public void setHardPerSection(Integer hardPerSection) {
        this.hardPerSection = hardPerSection;
    }

    public void setQuestionsPerSection(Integer questionsPerSection) {
        this.questionsPerSection = questionsPerSection;
    }

	public Integer getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(Integer isCurrent) {
		this.isCurrent = isCurrent;
	}

	public void setCurrent(){
		this.isCurrent = 1;
	}
	public void setNotCurrent(){
		this.isCurrent = 0;
	}
}
