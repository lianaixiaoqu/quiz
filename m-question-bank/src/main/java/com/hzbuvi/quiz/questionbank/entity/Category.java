package com.hzbuvi.quiz.questionbank.entity;

import javax.persistence.*;

/**
 * Created by root on 2016/10/21.
 */
@Entity
@Table(name= "category")
public class Category {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private  Integer id;
    private  String name;
    @Enumerated(EnumType.ORDINAL)
    private  DeleteEnum isDelete;


	public Category() {
		this.isDelete = DeleteEnum.NOT_DELETE;
	}

	public Category(String name) {
		this.name = name;
		this.isDelete = DeleteEnum.NOT_DELETE;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
