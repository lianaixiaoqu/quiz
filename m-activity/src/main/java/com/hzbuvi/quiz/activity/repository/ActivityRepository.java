package com.hzbuvi.quiz.activity.repository;

import com.hzbuvi.quiz.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by Downey.hz on 2016/10/10..
 */

public interface ActivityRepository extends JpaRepository<Activity,Integer> {

    //Page<Activity> findByIsDelete(Pageable pageable,Integer isDelete);
    List<Activity>findByIsDelete(Integer isDelete);
    Page<Activity> findAll(Specification<Activity> whereClause, Pageable pageable);
	List<Activity> findByIsCurrent(Integer isCurrent);
}
