//package com.hzbuvi.quiz.individualRanking.repository;
//
//import com.hzbuvi.quiz.section.entity.UserSection;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by WangDianDian on 2016/10/17.
// */
//@Repository
//public interface IndividualRankingRepository  extends JpaRepository<UserSection,Integer> {
//
//    @Query("select AA.userId as userId," +
////            "(select OG.organizationName  from Organization OG where OG.id=(select  OU.organizationId from OrganizationUser OU where OU.id=AA.userId))  as department," +
//            "(select OG.organizationName  from Organization OG where OG.id= "+
//            "(select OG.parentId from Organization OG where OG.id = (select OU.organizationId from OrganizationUser OU	where	OU.jobNumber = AA.userId))) AS department,"+
//            "(select OU.name from OrganizationUser OU where OU.id=AA.userId ) as  personName," +
//            "max(AA.sectionId) as sections,sum(AA.stars) as stars," +
//            "sum(AA.submitCount) as submitCount,sum(AA.submitTime-AA.startTime)* 24 * 60 * 60 as submitTime," +
//            "(select count(SS.userId) from RankLike SS where SS.targetId=AA.userId and SS.likeType=1) as likeCount " +
//            "from UserSection AA where activityId = :activityId And AA.submitTime is not null  group by userId")
//
//    List<Map<String,Object>> findSummaryByActivityId(@Param("activityId") Integer activityId);
//}
