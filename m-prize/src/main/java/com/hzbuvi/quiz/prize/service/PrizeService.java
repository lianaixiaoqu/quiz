package com.hzbuvi.quiz.prize.service;

import com.hzbuvi.quiz.prize.entity.ChapterPrize;
import com.hzbuvi.quiz.prize.repository.ChapterPrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by light on 2016/11/4.
 */
@Service
public class PrizeService {
    @Autowired
    private ChapterPrizeRepository chapterPrizeRepository;

    public String create(Integer activityId,Integer ChapterId,Integer first_id,String first_title, String first_przie_name,Integer first_prize_count
            ,Integer second_id,String second_title,String second_przie_name,Integer second_prize_count
            ,Integer third_id,String third_title,String third_przie_name,Integer third_prize_count){
        ChapterPrize chapterPrizeFirst;
        ChapterPrize chapterPrizeSecond;
        ChapterPrize chapterPrizeThird;
        first_title="一等奖";
        second_title="二等奖";
        third_title="三等奖";
        if(null==first_id){
            chapterPrizeFirst= new ChapterPrize();
            chapterPrizeSecond=new ChapterPrize();
            chapterPrizeThird= new ChapterPrize();
        }else{
            chapterPrizeFirst=chapterPrizeRepository.findOne(first_id);
            chapterPrizeSecond=chapterPrizeRepository.findOne(second_id);
            chapterPrizeThird=chapterPrizeRepository.findOne(third_id);
        }
        List<ChapterPrize> list=new ArrayList<>();
        chapterPrizeFirst.setActivityId(activityId);
        chapterPrizeFirst.setChapterId(ChapterId);
        chapterPrizeFirst.setRemainingAmount(first_prize_count);
        chapterPrizeFirst.setTotalAmount(first_prize_count);
        chapterPrizeFirst.setTitle(first_title);
        chapterPrizeFirst.setContent(first_przie_name);
        chapterPrizeFirst.setRate(0.25);
        list.add(chapterPrizeFirst);
        chapterPrizeSecond.setActivityId(activityId);
        chapterPrizeSecond.setChapterId(ChapterId);
        chapterPrizeSecond.setRemainingAmount(second_prize_count);
        chapterPrizeSecond.setTotalAmount(second_prize_count);
        chapterPrizeSecond.setTitle(second_title);
        chapterPrizeSecond.setContent(second_przie_name);
        chapterPrizeSecond.setRate(0.25);
        list.add(chapterPrizeSecond);
        chapterPrizeThird.setActivityId(activityId);
        chapterPrizeThird.setChapterId(ChapterId);
        chapterPrizeThird.setRemainingAmount(third_prize_count);
        chapterPrizeThird.setTotalAmount(third_prize_count);
        chapterPrizeThird.setTitle(third_title);
        chapterPrizeThird.setContent(third_przie_name);
        chapterPrizeThird.setRate(0.25);
        list.add(chapterPrizeThird);
        chapterPrizeRepository.save(list);
        return "success";
    }

    public List<ChapterPrize> updateLoad(Integer activityId,Integer chapterId){
       List<Integer> chapterPrize =chapterPrizeRepository.findIdByactivityIdOrderByIdAs(activityId, chapterId);
        if(null==chapterPrize){
            return null;
        }
        List<ChapterPrize> list=new ArrayList<>();
        for(int i=0;i<chapterPrize.size();i++){
            list.add(chapterPrizeRepository.findOne(chapterPrize.get(i)));
        }
        return list;
    }
//    public String update(, String first_przie_name,Integer first_prize_count
//            ,String second_przie_name,Integer second_prize_count
//            ,String third_przie_name,Integer third_prize_count){
//
//        String a=chapterPrize.getTitle();
//        List<ChapterPrize> list=new ArrayList<>();
//        chapterPrize.setActivityId(1);
//       chapterPrize.setContent(first_przie_name);
//       chapterPrize.setTotalAmount(first_prize_count);
//        chapterPrize.setRate(0.25);
//        list.add(chapterPrize);
//        chapterPrize1.setActivityId(1);
//        chapterPrize1.setContent(second_przie_name);
//        chapterPrize1.setTotalAmount(second_prize_count);
//        chapterPrize1.setRate(0.25);
//        list.add(chapterPrize1);
//        chapterPrize2.setActivityId(1);
//        chapterPrize2.setContent(third_przie_name);
//        chapterPrize2.setTotalAmount(third_prize_count);
//        chapterPrize2.setRate(0.25);
//        list.add(chapterPrize2);
//        chapterPrizeRepository.save(list);
//        return "success";
//    }
//    public List<ChapterPrize> index(Integer chapterId){
//        List<ChapterPrize> list=chapterPrizeRepository.findByChapterId(chapterId);
//        return list;
//    }
}
