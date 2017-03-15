package com.hzbuvi.quiz.questionbank.entity;

import javax.persistence.*;

/**
 * Created by Downey.hz on 2016/10/12..
 */
@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private  Integer id;
    private  Integer questionId;
    private  String content;
    private  Integer correct;

	public Answer() {
	}

	public Answer(String content) {
		this.content = content;
		this.correct = 0;
	}

	public Answer(String content,Integer correct) {
		this.content = content;
		this.correct = correct;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionid() {
        return questionId;
    }

    public void setQuestionid(Integer questionid) {
        this.questionId = questionid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }
}
