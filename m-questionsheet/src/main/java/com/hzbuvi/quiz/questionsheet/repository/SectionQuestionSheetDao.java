package com.hzbuvi.quiz.questionsheet.repository;

import com.hzbuvi.quiz.questionsheet.entity.SectionQuestionSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface SectionQuestionSheetDao extends JpaRepository<SectionQuestionSheet,Integer> {
	List<SectionQuestionSheet> findBySectionId(Integer sectionId);

	@Query("select max(id) from QuestionSheet")
	Long getMaxId();

	void deleteBySectionIdIn(List<Integer> sectionIds);
}
