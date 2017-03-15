package com.hzbuvi.quiz.biz.activity;

import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.chapter.service.ChapterService;
import com.hzbuvi.quiz.section.service.SectionBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class ActivityService {

	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private ChapterService chapterBiz;
	@Autowired
	private SectionBiz sectionBiz;


	// chapters,  sectionsPerChapter , questionsPerSection , sectionLimitTime , name ,description ,startTime . endTime  , dailyStart ,  dailyEnd
	// categorys , knowledges
	public Object create(Map<String, String> param, Integer userId) {
		int chapters = Integer.parseInt(param.get("chapters"));
		int sectionsPerChapter = Integer.parseInt(param.get("sectionsPerChapter"));
		int questionsPerSection = Integer.parseInt(param.get("questionsPerSection"));
		int sectionLimitTime= Integer.parseInt(param.get("sectionLimitTime"));
		Integer activityId = activityBiz.create(param);
		List<Integer> chapterIds =  chapterBiz.create(activityId,chapters,sectionsPerChapter);
		sectionBiz.create(activityId,chapterIds,sectionsPerChapter);
        return activityId;
	}



}
