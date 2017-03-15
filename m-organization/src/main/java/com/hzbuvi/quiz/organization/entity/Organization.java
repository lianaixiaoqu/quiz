package com.hzbuvi.quiz.organization.entity;

/**
 * Created by Downey.hz on 2016/10/17..
 */

import javax.persistence.*;

@Entity
@Table(name = "Organization")
public class Organization {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private Integer id;
    private Integer organizationId;
    private String organizationName;
    private Integer organizationLevel;
    private  String organizationLevelName;
    private  String organizationPath;
    private  Integer parentId;
    @Enumerated(EnumType.ORDINAL)
    private DeleteEnum isDelete;

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrganizationLevel() {
        return organizationLevel;
    }

    public void setOrganizationLevel(Integer organizationLevel) {
        this.organizationLevel = organizationLevel;
    }


    public Organization() {
        this.isDelete = DeleteEnum.NOT_DELETE;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationLevelName() {
        return organizationLevelName;
    }

    public void setOrganizationLevelName(String organizationLevelName) {
        this.organizationLevelName = organizationLevelName;
    }

    public String getOrganizationPath() {
        return organizationPath;
    }

    public void setOrganizationPath(String organizationPath) {
        this.organizationPath = organizationPath;
    }

    public Integer getParentId() {
        return parentId;
    }

    public DeleteEnum getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(DeleteEnum isDelete) {
        this.isDelete = isDelete;
    }
}
