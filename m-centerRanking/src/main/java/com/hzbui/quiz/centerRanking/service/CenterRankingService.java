//package com.hzbui.quiz.centerRanking.service;
//
//import com.hzbui.quiz.centerRanking.repository.CenterRankingRepository;
//import com.hzbuvi.quiz.activity.entity.Activity;
//import com.hzbuvi.quiz.activity.repository.ActivityRepository;
////import com.hzbuvi.quiz.organization.entity.Organization;
////import com.hzbuvi.quiz.organization.repository.OrganizationRepository;
//import com.hzbuvi.quiz.section.service.OrderByTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//import com.hzbuvi.util.basic.PageResult;
//import com.hzbuvi.util.basic.ValueUtil;
//import org.springframework.data.domain.Pageable;
//
//import java.util.*;
//
///**
// * Created by WangDianDian on 2016/10/17.
// */
//@Service
//public class CenterRankingService {
//    @Autowired
//    CenterRankingRepository centerRankingRepository;
//   // @Autowired
//  //  OrganizationRepository organizationRepository;
//    @Autowired
//    ActivityRepository activityRepository;
//    public static Integer defaultPageSize = 10;
//
//    public Map<String, Object> searchCenter(Integer pageNumber, Integer activityId, Integer userId) {
////        String dailyend = activityRepository.findOne(activityId).getDailyEnd();
////        OrderByTest orderByTest=new OrderByTest();
////        List<Map<String,Object>> individual= centerRankingRepository.findSummaryByActivityId(activityId);
////        String orderBy = "joinRate,AvgSection,AvgScore";
////        String desc = "1,1,1";
////        List<Map<String, Object>> userSections = orderByTest.sort(individual,orderBy,desc);
////        Integer departmentId =organizationRepository.findByOrganizationId(userId);
////        int i=1;
////        p:for(Map<String, Object> user:userSections){
////            for (String key:user.keySet()){
////                Object value=user.get(key);
////                if (departmentId.equals(value)&&"departmentId".equals(key)){
////                    break p;
////                }
////            }
////            i++;
////        }
////        List<Map<String, Object>> pageResult=getPageList(userSections,pageNumber);
////        Map<String,Object> result = new HashMap<String,Object>();
////        result.put("myCenterRank", i);
////        result.put("dailyEnd",  dailyend);
////        result.put("ranks",pageResult);
////        return result;
////    }
////    private List<Map<String,Object>> getPageList(List<Map<String,Object>> list,Integer pageNumber){
////        Integer totalPage=getTotalPage(list);
////        if (pageNumber>totalPage){
////            return null;
////        }
////        List<Map<String,Object>> resultList=new ArrayList<Map<String, Object>>();
////        Integer start=(pageNumber-1)*defaultPageSize;
////        Integer end=getEndNumber(start,totalPage,pageNumber,list);
////        for(int i=start;i<end;i++){
////            resultList.add(list.get(i ));
////        }
////        return resultList;
////    }
////    private Integer getTotalPage(List list){
////        Integer size=list.size();
////        return size%defaultPageSize==0?size/defaultPageSize:(size/defaultPageSize)+1;
////    }
////    private Integer getEndNumber(Integer start,Integer totalPage,Integer pageNumber,List list){
////        if (pageNumber>=totalPage){
////            Integer n=list.size()%defaultPageSize;
////            return n==0?start+defaultPageSize:start+n;
////        }
////        return start+defaultPageSize;
////    }
//        return null;
//    }
//}