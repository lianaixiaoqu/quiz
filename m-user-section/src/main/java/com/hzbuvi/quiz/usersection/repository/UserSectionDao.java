package com.hzbuvi.quiz.usersection.repository;

import com.hzbuvi.quiz.usersection.entity.UserSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface UserSectionDao extends JpaRepository<UserSection,Integer> {

	List<UserSection> findByUserIdAndActivityIdOrderBySectionId(Integer userId, Integer activityId);
//	List<UserSection> findByJobNumber(String JobNumber);


	//    private Integer score;
//    private Integer stars;
//    private Double correctRate;
//    private Integer submitCount;
//	Page<UserSection> findByUserIdAndActivityIdOrderByStarsDescScoreDescSubmitCountAsc(Pageable pageable, Integer userId, Integer activityId);

	List<UserSection> findByUserIdAndActivityId( Integer userId, Integer activityId);

	long countByUserIdAndActivityId(Integer userId, Integer activityId);

	@Query("select count(sectionId) from UserSection where userId = :userId and activityId=:activityId")
	Integer findBySectionId(@Param("userId") Integer userId, @Param("activityId") Integer activityId);

//			"order by sum(us.score) as score ,sum(us.stars) as stars ,sum(us.submitCount) as submitCount  " )
//			" order by   sum(stars) desc  , sum(score)  desc , sum(submitCount) asc "
	@Query("select  us.departmentId as departmentid ,us.departmentName as departmentname , count(us.sectionId) as sectioncnt, count( DISTINCT us.userId) as usercnt ,sum(us.score) as score ,sum(us.stars) as stars ,sum(us.submitCount) as submitCount from UserSection us  where  us.activityId = :activityId  group by us.departmentId,us.departmentName  " )
	List<Map<String,Object>> findByUserIdAndActivityIdPageDepartment(@Param("activityId") Integer activityId);

//	@Query("select us.userId as userid , us.userName as username, us.buMenNumber as departmentid ,us.buMen as departmentname , count(us.sectionId) as sectioncnt, count(us.userId) as usercnt ,sum(us.score) as score ,sum(us.stars) as stars ,sum(us.submitCount) as submitCount from UserSection us  where  us.activityId = :activityId  group by  us.userId,us.userName, us.buMenNumber  ,us.buMen  " )
	@Query("select us.userId as userid , us.userName as username, us.buMenNumber as departmentid ,us.buMen as departmentname , count(us.sectionId) as sectioncnt, count(us.userId) as usercnt ,sum(us.score) as score ,sum(us.stars) as stars ,sum(us.submitCount) as submitCount, sum(coalesce(submitSeconds,0)) as submitSeconds from UserSection us  where  us.activityId = :activityId  group by  us.userId,us.userName, us.buMenNumber  ,us.buMen   order by  sectioncnt desc , stars desc  , submitCount ,submitSeconds " )
	List<Map<String,Object>> findByUserIdAndActivityIdPagePerson(@Param("activityId") Integer activityId);

	UserSection findByUserIdAndSectionIdAndActivityId(Integer userId,Integer sectionId,Integer activityId);

	Page<UserSection> findAll(Specification<UserSection> whereClause, Pageable pageable);
	List<UserSection> findAll(Specification<UserSection> whereClause);

	UserSection findBySectionIdAndUserId(Integer endSectionId, Integer ouserId);
}

