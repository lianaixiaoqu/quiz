package com.hzbuvi.quiz.activity.entity;


import javax.persistence.*;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "activityQuestionProperties")
public class ActivityQuestionProperties {

	@Id
	@GeneratedValue( strategy = GenerationType.TABLE )
	private Integer id;
	private Integer activityId;
	private QuestionPropertiesEnum propertiesType;
	private Integer propertyId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public QuestionPropertiesEnum getPropertiesType() {
		return propertiesType;
	}

	public void setPropertiesType(QuestionPropertiesEnum propertiesType) {
		this.propertiesType = propertiesType;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}
}
