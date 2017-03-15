package com.hzbuvi.quiz.questionbank.repository;

import com.hzbuvi.quiz.questionbank.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface AnswerDao extends JpaRepository<Answer,Integer> {
	List<Answer> findByQuestionIdIn(Set<Integer> questionId);
	List<Answer> findByQuestionId(Integer questionId);
}
