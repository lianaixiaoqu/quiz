//package com.hzbuvi.quiz.section.biz;
//
//
//import com.hzbuvi.quiz.chapter.entity.Chapter;
//import com.hzbuvi.quiz.chapter.repository.ChapterRepository;
//import com.hzbuvi.quiz.section.entity.Section;
//import com.hzbuvi.quiz.section.entity.UserSection;
//import com.hzbuvi.quiz.section.repository.SectionRepository;
//import com.wrqzn.wheel.condition.biz.ConditionBiz;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * Created by WANG, RUIQING on 10/17/16
// * Twitter : @taylorwang789
// * E-mail : i@wrqzn.com
// */
//@Service
//public class UnlockChapterCondition implements ConditionBiz {
//    @Autowired
//    private SectionRepository sectionRepository;
//    @Autowired
//    private ChapterRepository chapterRepository;
//    @Override
//    public Object verify(Object... objects) {
//
//        Integer stars=0;
//         int userId = (int)objects[1];//用户Id
//        int activity = (int)objects[2];//活动Id
//        int sectionId = (int)objects[3];//sectionId
//        ArrayList<String> arrayList=new ArrayList<>();
//        List<UserSection>  userSections = (List<UserSection>) objects[0];
//
//
//        Section section=sectionRepository.findOne(sectionId);
//            Integer chaperId=section.getChapterId();
//            Chapter chapter=chapterRepository.findOne(chaperId);
//        String[] conditions = chapter.getUnlockCondition().split(",");
//        String[] conditiontypes = chapter.getConditionType().split(",");
//        String[] conditiondatas =chapter.getConditionData().split(",");
//
//        boolean[] passes = new boolean[conditions.length];
//
//        for (int i = 0; i < conditions.length; i++) {
//            switch ( conditions[i] ) {
//                case "1" :
//                    passes[i] = greatThan(conditiontypes[i],conditiondatas[i],userSections);//总星数和平均数
//                    break;
//                case "2":
//                    passes[i] = maxSectionStars(conditiontypes[i],conditiondatas[i],userSections);
//                    break;
//            }
//        }
//        boolean pass = true;
//        for (int i = 0; i < passes.length; i++) {
//            if ( !passes[i]) {
//                pass = false;
//                break;
//            }
//        }
//        if (pass) {
//            return 1;
//        } else {
//            return 0;
//        }
//
//    }
//
//    public boolean averageStar(Integer targetStars, List<UserSection> userSections){//平均星星数
//        int sumstars = 0;
//        for (int i = 0; i <userSections.size() ; i++) {
//            sumstars+=userSections.get(i).getStars();
//        }
//        double userData = Double.valueOf(sumstars) / userSections.size();
//
//        if ( userData >= targetStars ){
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean sumStars(Integer targetSumStars, List<UserSection> userSections){//总星数
//        int sumstars = 0;
//        for (int i = 0; i <userSections.size() ; i++) {
//            sumstars+=userSections.get(i).getStars();
//        }
//        if ( sumstars >= targetSumStars ){
//            return true;
//        } else {
//            return false;
//        }
//    }
//    //                                      useless       10:4
//    private boolean maxSectionStars(String type, String data, List<UserSection> userSections) {//大于N个星的关数
//        String[] datas = data.split(":");
//        Integer passSections = Integer.parseInt(datas[0]);
//        Integer passStars  = Integer.parseInt(datas[1]);
//        Integer userPassSections = 0 ;
//
//        for (int i = 0; i < userSections.size() ; i++) {
//            if (passStars <= userSections.get(i).getStars() ) {
//                userPassSections +=1;
//            }
//        }
//        if (userPassSections >= passSections) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }
//
//
//    private boolean greatThan(String type,String data,List<UserSection> sections){//平均星数和总星数
//        Double target = Double.valueOf(data);
//        Double userData = Double.valueOf(0);
//        int sumstars = 0;
//        switch (type.toLowerCase()) {
//            case "s" :
//                for (int i = 0; i < sections.size(); i++) {
//                    sumstars += sections.get(i).getStars();
//                }
//                userData = Double.valueOf(sumstars);
//                break;
//            case "a" :
//                for (int i = 0; i < sections.size(); i++) {
//                    sumstars += sections.get(i).getStars();
//                }
//                userData = Double.valueOf(sumstars) / sections.size();
//                break;
//        }
//        if ( userData >= target ){
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//}
