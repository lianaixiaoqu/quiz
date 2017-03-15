package com.hzbuvi.quiz.questionbank.repository;

import com.hzbuvi.quiz.questionbank.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * Created by root on 2016/10/18.
 */
@Repository
public interface QuestionBankRepository extends JpaRepository<Questionbank,Integer>{

	List<Questionbank>  findByCategoryIdInAndKnowledgeIdInAndDifficulty(List<Integer> categoryId, List<Integer> knowledgeId,DifficultyEnum difficulty);
	Page<Questionbank> findByIsDelete(Pageable pageable, DeleteEnum deleteEnum);
	List<Questionbank> findByCategoryIdAndTypeAndKnowledgeIdAndDifficulty(Integer categoryId, TypeEnum type,Integer knowledgeId, DifficultyEnum difficulty);
	long countByCategoryId(int i);
	List<Questionbank> findByCategoryId(Integer categoryId);
	Page<Questionbank> findAll(Specification<Questionbank> whereClause, Pageable pageable);

	List<Questionbank> findBySerialNumberIn(List<String> deleteSerialNumbers);

	long countByKnowledgeId(Integer id);
	@Query("select id from Questionbank where serialNumber = :serialNumber  and   isDelete = 0 ")
	List<Integer> findIdBySerialNumbers(@Param("serialNumber") String serialNumber);

	long countByKnowledgeIdAndIsDelete(Integer id, DeleteEnum deleted);

	List<Questionbank> findByCategoryIdAndIsDelete(Integer id, DeleteEnum notDelete);

	List<Questionbank> findByCategoryIdInAndKnowledgeIdInAndDifficultyAndIsDelete(List<Integer> categorys, List<Integer> knowledges, DifficultyEnum difficulty, DeleteEnum notDelete);
//	Page<Questionbank> findAllPage(Specification<Questionbank> whereClause, Pageable pageable);
}
