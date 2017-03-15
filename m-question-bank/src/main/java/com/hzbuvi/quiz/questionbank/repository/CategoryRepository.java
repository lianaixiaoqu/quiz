package com.hzbuvi.quiz.questionbank.repository;

import com.hzbuvi.quiz.questionbank.entity.Category;
import com.hzbuvi.quiz.questionbank.entity.DeleteEnum;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by WangDianDian on 2016/10/24.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Page<Category> findByIsDelete(Pageable pageable, DeleteEnum isDelete);
//    Category findById(Integer id);
    @Query("select id from Category where name = :name  and   isDelete = 0 ")
    Integer findIdByCategoryName(@Param("name") String name);
    List<Category> findByName(String name);
    Page<Category> findAll(Specification<Category> whereClause, Pageable pageable);
    List<Category> findByIsDelete(DeleteEnum isDelete);

	List<Category> findByNameAndIsDelete(String category, DeleteEnum notDelete);
}
