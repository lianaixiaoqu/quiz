//package com.hzbuvi.quiz.prize.service;
//
//import com.hzbuvi.quiz.prize.repository.PrizeWinnerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by WangDianDian on 2016/10/28.
// */
//@Service
//public class PrizeWinnerService {
//    @Autowired
//    PrizeWinnerRepository prizeWinnerRepository;
//    public static Integer defaultPageSize = 10;
//
//    public Map<String, Object> searchPrizeWinner(Integer pageNumber, Integer activityId) {
//        List<Map<String, Object>> prizeWinner = prizeWinnerRepository.findSummaryByActivityId(activityId);
//        List<Map<String, Object>> pageResult = getPageList(prizeWinner, pageNumber);
//        Map<String, Object> result = new HashMap<>();
//        result.put("查看获奖信息", pageResult);
//        return result;
//    }
//
//    private List<Map<String, Object>> getPageList(List<Map<String, Object>> list, Integer pageNumber) {
//        Integer totalPage = getTotalPage(list);
//        if (pageNumber > totalPage) {
//            return null;
//        }
//        List<Map<String, Object>> resultList = new ArrayList<>();
//        Integer start = (pageNumber - 1) * defaultPageSize;
//        Integer end = getEndNumber(start, totalPage, pageNumber, list);
//        for (int i = start; i < end; i++) {
//            resultList.add(list.get(i));
//        }
//        return resultList;
//    }
//
//    private Integer getTotalPage(List list) {
//        Integer size = list.size();
//        return size % defaultPageSize == 0 ? size / defaultPageSize : (size / defaultPageSize) + 1;
//    }
//
//    private Integer getEndNumber(Integer start, Integer totalPage, Integer pageNumber, List list) {
//        if (pageNumber >= totalPage) {
//            Integer n = list.size() % defaultPageSize;
//            return n == 0 ? start + defaultPageSize : start + n;
//        }
//        return start + defaultPageSize;
//    }
//}