package com.hzbuvi.quiz.section.entity;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by Downey.hz on 2016/10/11..
 */
@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private  Integer id;
    private  String name;
    private  Integer activityId;
    private  Integer chapterId;
    private  Integer endOfChapter;
    private Integer sequenceInChapter;
    private Integer sequenceInActivity;
	private Date createTime;

	@Transient
	private Integer starCount = 0;

	@Transient
	private boolean unlocked = false;
	@Transient
	private Object userSection;

	public Section() {
		this.createTime = new Date();
	}

    public Integer getSequenceInChapter() {
        return sequenceInChapter;
    }

    public void setSequenceInChapter(Integer sequenceInChapter) {
        this.sequenceInChapter = sequenceInChapter;
    }

    public Integer getSequenceInActivity() {
        return sequenceInActivity;
    }

    public void setSequenceInActivity(Integer sequenceInActivity) {
        this.sequenceInActivity = sequenceInActivity;
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

    public Integer getEndOfChapter() {
        return endOfChapter;
    }

    public void setEndOfChapter(Integer endOfChapter) {
        this.endOfChapter = endOfChapter;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

	public Object getUserSection() {
		return userSection;
	}

	public void setUserSection(Object userSection) {
		this.userSection = userSection;
	}

	public Integer getStarCount() {
		return starCount;
	}

	public void setStarCount(Integer starCount) {
		this.starCount = starCount;
	}
}
