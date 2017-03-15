package com.hzbuvi.quiz.questionsheet.repository;

import com.hzbuvi.quiz.questionsheet.entity.QuestionSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface QuestionSheetDao  extends JpaRepository<QuestionSheet,Integer>{

	@Query("select max(id) from QuestionSheet")
	Long getMaxId();

	void deleteByActivityId(Integer activityId);
}
