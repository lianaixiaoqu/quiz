package com.hzbuvi.quiz.organization.repository;

import com.hzbuvi.quiz.organization.entity.DeleteEnum;
import com.hzbuvi.quiz.organization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Downey.hz on 2016/10/17..
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Integer>{
    Page<Organization> findByOrganizationNameContaining(String organizationName,Pageable pageable);
    Page<Organization> findByIsDelete(Pageable pageable,DeleteEnum IsDelete);
    @Query("select max(id) from Organization")
    Integer getMaxId();
//    @Query("select ss.parentId from Organization ss where ss.id=(select ss.parentId from Organization ss where ss.id=(select aa.organizationId from OrganizationUser aa where aa.id = :userId ))")
    Organization findByOrganizationId(Integer organizationId);
    Page<Organization> findAll(Specification<Organization> whereClause, Pageable pageable);

    List<Organization> findByOrganizationIdIn(List<Integer> delorganizationIdList);
    @Query("select id from Organization where organizationId = :organizationId  and   isDelete = 0 ")
    Integer findIdByOrganizationId(@Param("organizationId") Integer organizationId);
}
