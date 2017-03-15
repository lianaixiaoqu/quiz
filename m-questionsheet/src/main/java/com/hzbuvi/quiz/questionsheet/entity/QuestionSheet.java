package com.hzbuvi.quiz.questionsheet.entity;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by Downey.hz on 2016/10/11..
 */
@Entity
@Table(name = "questionsheet")
public class QuestionSheet {

    @Id
	@GeneratedValue( strategy = GenerationType.TABLE)
//	@SequenceGenerator(name="questionsheet", sequenceName="zzz_questionsheet", initialValue = 1, allocationSize=1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="questionsheet")
    private  Integer id;
    private  Integer questions;
	@Lob
    private String questionJson;//包括答案以及相应的选项
	@Lob
    private String rightAnswerJson;
    private Integer activityId;
	private Date createDate;

	public QuestionSheet() {
		this.createDate = new Date();
	}

	public QuestionSheet(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuestions() {
        return questions;
    }

    public void setQuestions(Integer questions) {
        this.questions = questions;
    }

	public String getQuestionJson() {
		return questionJson;
	}

	public void setQuestionJson(String questionJson) {
		this.questionJson = questionJson;
	}

	public String getRightAnswerJson() {
		return rightAnswerJson;
	}

	public void setRightAnswerJson(String rightAnswerJson) {
		this.rightAnswerJson = rightAnswerJson;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
