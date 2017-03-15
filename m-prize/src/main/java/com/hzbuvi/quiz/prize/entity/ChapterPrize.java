package com.hzbuvi.quiz.prize.entity;

import javax.persistence.*;

/**
 * Created by WangDianDian on 2016/10/14.
 */
@Entity
@Table(name = "prize_chapter")
public class ChapterPrize {
    @Id
	@GeneratedValue( strategy = GenerationType.TABLE )
    private Integer id;
    private  Integer activityId;
    private Integer chapterId;
    private Double rate;
    private Integer totalAmount;
    private Integer remainingAmount;
    private Integer sectionId;
    private String title;
    private String content;
    private String description;


    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
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


    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public void plusOneRemainingAmount() {
        if ( 0 == this.remainingAmount ){
            this.remainingAmount = 0;
        } else {
            this.remainingAmount++;
        }

    }
}
