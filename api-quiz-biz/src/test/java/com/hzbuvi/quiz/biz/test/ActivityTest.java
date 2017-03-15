package com.hzbuvi.quiz.biz.test;

import com.hzbuvi.quiz.biz.DBConfig;
import com.hzbuvi.quiz.biz.activity.ActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DBConfig.class})
public class ActivityTest {

	@Autowired
	private ActivityService activityService;

	@Test
	public void create(){
		// chapters,  sectionsPerChapter , questionsPerSection , sectionLimitTime , name ,description ,startTime . endTime  , dailyStart ,  dailyEnd
		// categorys , knowledges
		Map<String,String> param = new HashMap<>();
		param.put("chapters","4");
		param.put("sectionsPerChapter","5");
		param.put("questionsPerSection","10");
		param.put("sectionLimitTime","199");
		param.put("name","test14314");
		param.put("description","jfkdashgaklsfj");
		param.put("startTime","20161221000000");
		param.put("endTime","20161223000000");
		param.put("dailyStart","0922");
		param.put("dailyEnd","1823");
		param.put("categorys","1,2,3");
		param.put("knowledges","34,31");
		activityService.create(param,1);

	}


}
