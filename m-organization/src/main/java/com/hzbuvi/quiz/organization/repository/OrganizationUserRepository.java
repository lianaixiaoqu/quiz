package com.hzbuvi.quiz.organization.repository;


import com.hzbuvi.quiz.organization.entity.DeleteEnum;
import com.hzbuvi.quiz.organization.entity.Organization;
import com.hzbuvi.quiz.organization.entity.OrganizationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by WangDianDian on 2016/10/27.
 */
@Repository
public interface OrganizationUserRepository extends JpaRepository<OrganizationUser,Integer> {
    @Query("select max(id) from OrganizationUser")
    Integer getMaxId();
    Page<OrganizationUser> findByIsDelete(Pageable pageable, DeleteEnum IsDelete);
    Page<OrganizationUser> findByNameContaining(String name,Pageable pageable);
    OrganizationUser findByJobNumber(String JobNumber);
    Page<OrganizationUser> findAll(Specification<OrganizationUser> whereClause, Pageable pageable);

    void deleteByJobNumber(String jobNumber);

    List<OrganizationUser> findByJobNumberIn(List<String> deljobNUmberList);
    @Query("select id from OrganizationUser where jobNumber = :jobNumber  and   isDelete = 0 ")
    Integer findIdByJobNumber(@Param("jobNumber") String jobNumber);
    List<OrganizationUser> findByOrganizationId(Integer organizationId);
//  @Query("select id from OrganizationUser where jobNumber = :jobNumber  and   isDelete = 1 ")
//  Integer findIdByJobNumberNot(@Param("jobNumber") String jobNumber);
	Integer countByOrganizationIdIn(Set<Integer> integers);

	List<Integer> findIdByJobNumberAndIsDelete(String jobNumber, DeleteEnum deleteEnum);
}
