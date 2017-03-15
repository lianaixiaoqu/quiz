package com.hzbuvi.quiz.usersection.biz;

import com.hzbuvi.quiz.chapter.entity.Chapter;
import com.hzbuvi.quiz.chapter.repository.ChapterRepository;
import com.hzbuvi.quiz.usersection.entity.UserSection;
import com.wrqzn.wheel.condition.biz.ConditionBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;


/**
// * Created by WANG, RUIQING on 10/17/16
// * Twitter : @taylorwang789
// * E-mail : i@wrqzn.com
// */
@Service
public class UnlockChapterCondition implements ConditionBiz {
    @Autowired
    ChapterRepository chapterRepository;

    public Object verify(Object... objects) {

        Integer stars=0;
        List<UserSection> userSections = (List<UserSection>) objects[0];
        int userId = Integer.valueOf(objects[1].toString());//用户Id
        int activity = Integer.valueOf(objects[2].toString());//活动Id
        int chapterId = Integer.valueOf(objects[3].toString());//chapterId
        int sectionId = Integer.valueOf(objects[4].toString());//sectionId
        ArrayList<String> arrayList=new ArrayList<>();


        Chapter chapter=chapterRepository.findOne(chapterId);
        String[] conditions = chapter.getUnlockCondition().split(",");
        String[] conditiontypes = chapter.getConditionType().split(",");
        String[] conditiondatas =chapter.getConditionData().split(",");

        boolean[] passes = new boolean[conditions.length];

        for (int i = 0; i < conditions.length; i++) {
            switch ( conditions[i] ) {
                case "1" :
                    passes[i] = greatThan(conditiontypes[i],conditiondatas[i],userSections);//总星数和平均数
                    break;
                case "2":
                    passes[i] = maxSectionStars(conditiontypes[i],conditiondatas[i],userSections);
                    break;
            }
        }
        boolean pass = true;
        for (int i = 0; i < passes.length; i++) {
            if ( !passes[i]) {
                pass = false;
                break;
            }
        }
        if (pass) {
            return 1;
        } else {
            return 0;
        }

    }

    //                                      useless       10:4
    private boolean maxSectionStars(String type, String data, List<UserSection> userSections) {//大于N个星的关数
        String[] datas = data.split(":");
        Integer passSections = Integer.parseInt(datas[1]);
        Integer passStars  = Integer.parseInt(datas[0]);
        Integer userPassSections = 0 ;

        for (int i = 0; i < userSections.size() ; i++) {
            if (passStars <= userSections.get(i).getStars() ) {
                userPassSections +=1;
            }
        }
        if (userPassSections >= passSections) {
            return true;
        } else {
            return false;
        }

    }
    private boolean greatThan(String type,String data,List<UserSection> sections){//平均星数和总星数
        Double target = Double.valueOf(data);
        Double userData = Double.valueOf(0);
        int sumstars = 0;
        switch (type.toLowerCase()) {
            case "s" :
                for (int i = 0; i < sections.size(); i++) {
                    sumstars += sections.get(i).getStars();
                }
                userData = Double.valueOf(sumstars);
                break;
            case "a" :
                for (int i = 0; i < sections.size(); i++) {
                    sumstars += sections.get(i).getStars();
                }
                userData = Double.valueOf(sumstars) / sections.size();
                break;
        }
        if ( userData >= target ){
            return true;
        } else {
            return false;
        }
    }


}
