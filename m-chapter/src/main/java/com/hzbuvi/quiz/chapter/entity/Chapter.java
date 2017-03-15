package com.hzbuvi.quiz.chapter.entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

/**
 * Created by Downey.hz on 2016/10/11..
 */
@Entity
@Table(name = "chapter")
public class Chapter {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private  Integer id;
    private  String name;
    private  Integer sequenceChapter;
    private Integer chapterId;
    private  Integer activityId;
    private  Integer sections;
    private  String unlockCondition;
    private  String conditionType;
    private  String conditionData;
    private String  passDescribe;
	private Date createDate;
    private String avgStar;
    private String sumStar;
    private String section;
    private String lessStar;

	public Chapter() {
		this.createDate = new Date();
	}

	public Integer getSequenceChapter() {
        return sequenceChapter;
    }

    public void setSequenceChapter(Integer sequenceChapter) {
        this.sequenceChapter = sequenceChapter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUnlockCondition() {
        return unlockCondition;
    }

    public void setUnlockCondition(String unlockCondition) {
        this.unlockCondition = unlockCondition;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getConditionData() {
        return conditionData;
    }

    public void setConditionData(String conditionData) {
        this.conditionData = conditionData;
    }

    public String getPassDescribe() {
        return passDescribe;
    }

    public void setPassDescribe(String passDescribe) {
        this.passDescribe = passDescribe;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(String avgStar) {
        this.avgStar = avgStar;
    }

    public String getSumStar() {
        return sumStar;
    }

    public void setSumStar(String sumStar) {
        this.sumStar = sumStar;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLessStar() {
        return lessStar;
    }

    public void setLessStar(String lessStar) {
        this.lessStar = lessStar;
    }
}
