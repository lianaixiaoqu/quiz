package com.hzbuvi.quiz.prize.repository;

import com.hzbuvi.quiz.prize.entity.UserPrize;
import org.hibernate.sql.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface UserPrizeRepository extends JpaRepository<UserPrize,Integer>{
    UserPrize findByActivityIdAndChapterIdAndUserId(Integer activityId,Integer chapterId,Integer userId);
    Page<UserPrize> findAllByOrderByCreateTimeDesc(Pageable pageable);
    Page<UserPrize> findAll(Specification<UserPrize> whereClause, Pageable pageable);

	UserPrize findByChapterIdAndUserId(Integer sectionId, Integer ouserId);

    UserPrize findByActivityIdAndChapterIdAndUserIdAndSectionId(Integer activityId, Integer maxChatperId, Integer ouserId, Integer sectionId);

	Integer countByActivityIdAndChapterIdAndSectionIdAndUserId(Integer activityId, Integer chapterId, Integer sectionId, Integer userId);

    UserPrize findByUserIdAndContent(Integer userId, String content);
}
