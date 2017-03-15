package com.hzbuvi.quiz.organization.entity;

import javax.persistence.*;

/**
 * Created by WangDianDian on 2016/10/25.
 */
@Entity
@Table(name="OrganizationUser")
public class OrganizationUser {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private Integer id;
    private String jobNumber;
    private String name;
    private String  position;
    private String phoneNumber;
    private String email;
    private String  workStation;
    private String organizationName;
    private Integer organizationId;
    private String organizationPath;
    @Enumerated(EnumType.ORDINAL)
    private DeleteEnum isDelete;

    public OrganizationUser() {
        this.isDelete = DeleteEnum.NOT_DELETE;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationPath() {
        return organizationPath;
    }

    public void setOrganizationPath(String organizationPath) {
        this.organizationPath = organizationPath;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public DeleteEnum getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(DeleteEnum isDelete) {
        this.isDelete = isDelete;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void trimJobNumber(){
		if (null!=this.jobNumber && ""!= this.jobNumber) {
			int lastIndex = this.jobNumber.lastIndexOf(".");
			if ( lastIndex != -1) {
				this.jobNumber = this.jobNumber.substring(0,lastIndex);
			}
		}
	}

	public OrganizationUser format(OrganizationUser organizationUserInput) {
		organizationUserInput.setId(this.id);
		return organizationUserInput;
	}
}
