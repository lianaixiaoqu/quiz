package com.hzbuvi.quiz.activity.repository;

import com.hzbuvi.quiz.activity.entity.ActivityQuestionProperties;
import com.hzbuvi.quiz.activity.entity.QuestionPropertiesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface QuestionPropertiesDao  extends JpaRepository<ActivityQuestionProperties,Integer>{


	@Query("select propertyId from ActivityQuestionProperties where activityId = :activityId  and   propertiesType = :propertiesType ")
	List<Integer> findIdByType(@Param("activityId") Integer activityId,@Param("propertiesType") QuestionPropertiesEnum questionPropertiesEnum);
	//ActivityQuestionProperties findByPropertiesType(QuestionPropertiesEnum questionPropertiesEnum);
	List<ActivityQuestionProperties> findByPropertiesType(QuestionPropertiesEnum questionPropertiesEnum);
	List<ActivityQuestionProperties> findByActivityId(Integer activityId);
}
