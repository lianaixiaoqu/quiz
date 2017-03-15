package com.hzbuvi.quiz.usersection.repository;

import com.hzbuvi.quiz.usersection.entity.UserSectionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface UserSectionHistoryDao  extends JpaRepository<UserSectionHistory,Integer>{

	List<UserSectionHistory>  findBySectionIdAndUserId(Integer sectionId,Integer userId);

	Integer countByActivityId(int activityId);

    List<UserSectionHistory> findByActivityIdAndUserId(Integer activityId, Integer ouserId);
}
