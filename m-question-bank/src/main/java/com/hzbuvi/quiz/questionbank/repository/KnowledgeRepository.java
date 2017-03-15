package com.hzbuvi.quiz.questionbank.repository;


import com.hzbuvi.quiz.questionbank.entity.Category;
import com.hzbuvi.quiz.questionbank.entity.DeleteEnum;
import com.hzbuvi.quiz.questionbank.entity.Knowledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WangDianDian on 2016/10/24.
 */
@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge,Integer> {
    Page<Knowledge> findByIsDelete(Pageable pageable, DeleteEnum isDelete);
    Knowledge findByKnowledgeName(String knowledgeName);
    Page<Knowledge> findAll(Specification<Knowledge> whereClause, Pageable pageable);
    List<Knowledge> findByCategoryId(Integer categerId);

	List<Knowledge> findByIsDelete(DeleteEnum isDelete);

	Knowledge findByKnowledgeNameAndCategoryIdAndCategoryName(String knowledgeName,Integer categoryId,String categoryName);

	List<Knowledge> findByKnowledgeNameAndIsDelete(String knowledge, DeleteEnum notDelete);
}
