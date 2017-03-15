package com.hzbuvi.quiz.questionsheet.bean;

import javax.persistence.*;

/**
 * Created by light on 2016/11/9.
 */
@Entity
@Table(name = "DIFFNOREAY")
public class DiffNorEasy {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private Integer id;
    private Integer activityId;
    private Integer easyPerSection;
    private Integer normalPerSection;
    private Integer hardPerSection;
    private String sorce;

    public String getSorce() {
        return sorce;
    }

    public void setSorce(String sorce) {
        this.sorce = sorce;
    }

    public Integer getHardPerSection() {
        return hardPerSection;
    }

    public void setHardPerSection(Integer hardPerSection) {
        this.hardPerSection = hardPerSection;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
