package com.hzbuvi.quiz.questionsheet.entity;

import javax.persistence.*;

/**
 * Created by Downey.hz on 2016/10/11..
 */
@Entity
@Table(name = "sectionquestionsheet")
public class SectionQuestionSheet {
    @Id
	@GeneratedValue( strategy = GenerationType.TABLE)
//	@SequenceGenerator(name="questionsheet", sequenceName="zzz_questionsheet", initialValue = 1, allocationSize=1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="questionsheet")
    private  Integer id;
    private  Integer sectionId;
    private  Integer questionSheetId;



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

	public Integer getQuestionSheetId() {
		return questionSheetId;
	}

	public void setQuestionSheetId(Integer questionSheetId) {
		this.questionSheetId = questionSheetId;
	}
}
