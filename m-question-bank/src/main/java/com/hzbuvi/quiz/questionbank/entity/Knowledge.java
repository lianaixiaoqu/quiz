package com.hzbuvi.quiz.questionbank.entity;

import javax.persistence.*;

/**
 * Created by root on 2016/10/21.
 */
@Entity
@Table(name= "knowledge")
public class Knowledge {
    @Id
	@GeneratedValue( strategy = GenerationType.TABLE )
    private  Integer id;
    private  String knowledgeName;
    @Enumerated(EnumType.ORDINAL)
    private  DeleteEnum isDelete;
    private String categoryName;
    private Integer categoryId;
	public Knowledge() {
		this.isDelete = DeleteEnum.NOT_DELETE;
	}

	public Knowledge(String knowledgeName,Integer categoryId,String categoryName) {
		this.knowledgeName = knowledgeName;
		this.isDelete = DeleteEnum.NOT_DELETE;
		this.categoryId=categoryId;
		this.categoryName = categoryName;
	}

	public DeleteEnum getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(DeleteEnum isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
