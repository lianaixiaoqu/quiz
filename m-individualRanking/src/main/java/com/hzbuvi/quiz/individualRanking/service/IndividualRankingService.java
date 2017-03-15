//package com.hzbuvi.quiz.individualRanking.service;
//
//import com.hzbuvi.quiz.activity.repository.ActivityRepository;
//import com.hzbuvi.quiz.individualRanking.repository.IndividualRankingRepository;
//import com.hzbuvi.quiz.section.service.OrderByTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.*;
//
///**
// * Created by WangDianDian on 2016/10/17.
// */
//@Service
//public class IndividualRankingService {
//    @Autowired
//    IndividualRankingRepository  individualRepository;
//    @Autowired
//    ActivityRepository activityRepository;
//    public static Integer defaultPageSize = 10;
//    public  Map<String,Object> searchIndividual(Integer pageNumber, Integer activityId,Integer userId) {
//        String dailyend = activityRepository.findOne(activityId).getDailyEnd();
//        OrderByTest orderByTest=new OrderByTest();
//        List<Map<String,Object>> individual= individualRepository.findSummaryByActivityId(activityId);
//        String orderBy = "sectionS,stars,submitCount,submitTime";
//        String desc = "1,1,0,0";
//        List<Map<String, Object>> userSections = orderByTest.sort(individual,orderBy,desc);
//        int i=1;
//        p:for(Map<String, Object> user:userSections){
//            for (String key:user.keySet()){
//                Object value=user.get(key);
//                if (userId.equals(value)&&"userId".equals(key)){
//                    break p;
//                }
//            }
//            i++;
//        }
//        List<Map<String, Object>> pageResult=getPageList(userSections,pageNumber);
////        System.out.println(ValueUtil.toJson(userSections));
//        Map<String,Object> result = new HashMap<String,Object>();
//        result.put("myRank", i);
//        result.put("dailyEnd",  dailyend);
//        result.put("ranks",pageResult);
//        return result;
//    }
//    private List<Map<String,Object>> getPageList(List<Map<String,Object>> list,Integer pageNumber){
//        Integer totalPage=getTotalPage(list);
//        if (pageNumber>totalPage){
//            return null;
//        }
//        List<Map<String,Object>> resultList=new ArrayList<Map<String, Object>>();
//        Integer start=(pageNumber-1)*defaultPageSize;
//        Integer end=getEndNumber(start,totalPage,pageNumber,list);
//        for(int i=start;i<end;i++){
//            resultList.add(list.get(i ));
//        }
//        return resultList;
//    }
//    private Integer getTotalPage(List list){
//        Integer size=list.size();
//        return size%defaultPageSize==0?size/defaultPageSize:(size/defaultPageSize)+1;
//    }
//    private Integer getEndNumber(Integer start,Integer totalPage,Integer pageNumber,List list){
//        if (pageNumber>=totalPage){
//            Integer n=list.size()%defaultPageSize;
//            return n==0?start+defaultPageSize:start+n;
//        }
//        return start+defaultPageSize;
//    }
//    public  Map<String,Object> personalRank(Integer activityId,Integer userId) {
//        OrderByTest orderByTest=new OrderByTest();
//        List<Map<String,Object>> individual= individualRepository.findSummaryByActivityId(activityId);
//        String orderBy = "sectionS,stars,submitCount,submitTime";
//        String desc = "1,1,0,0";
//        List<Map<String, Object>> userSections = orderByTest.sort(individual,orderBy,desc);
//        int i=1;
//        p:for(Map<String, Object> user:userSections){
//            for (String key:user.keySet()){
//                Object value=user.get(key);
//                if (userId.equals(value)&&"userId".equals(key)){
//                    break p;
//                }
//            }
//            i++;
//        }
//        Map<String,Object> result = new HashMap<String,Object>();
//        result.put("myRank", i);
//        return result;
//    }
//
//
//}