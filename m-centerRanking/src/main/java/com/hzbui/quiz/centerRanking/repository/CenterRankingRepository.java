//package com.hzbui.quiz.centerRanking.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
///**
// * Created by WangDianDian on 2016/10/17.
// */
//@Repository
//public interface CenterRankingRepository extends JpaRepository<UserSection,Integer> {
////    @Query("select  departmentId," +
////            "(select OG.organizationName from Organization OG where OG.id = departmentId) as department," +
////            "count(userId)/(select count(OU.jobNumber)from OrganizationUser OU where OU.organizationId = ( " +
////            "select OG.id from Organization OG where OG.parentId = departmentId)) as joinRate," +
////            "sum (sections)/count (userId) as AvgSection," +
////            "sum (scors)/count (userId) as AvgScore, " +
////            "(select count (AA.id)from RankLike AA where AA.departmentId = departmentId AND AA.likeType = 0) AS likeCount " +
////            "from (select " +
////            "(select OG.parentId from Organization OG where OG.id = " +
////            "(select OG.parentId from Organization OG where OG.id = " +
////            "(select OU.organizationId from OrganizationUser OU where OU.jobNumber = SS.userId)))as departmentId," +
////            "SS.userId,max(SS.sectionId) as sections,max(SS.score) as scors " +
////            "from UserSection SS where SS.activityId = :activityId and SS.submitTime is not null group by SS.userId)   " +
////            "group by departmentId ")
////    List<Map<String,Object>> findSummaryByActivityId(@Param("activityId") Integer activityId);
//
//
//}
