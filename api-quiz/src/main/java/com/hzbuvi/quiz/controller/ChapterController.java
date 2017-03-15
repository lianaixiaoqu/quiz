package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.chapter.service.ChapterService;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Downey.hz on 2016/10/11..
 */


@RestController
@RequestMapping("/chapter")
public class ChapterController {
    @Autowired
    private   ChapterService chapterService;
    @Autowired
    private ActivityBiz activityBiz;

    @RequestMapping(value = "/showAll", method = RequestMethod.GET)
    public String showChapter(Integer activityId, HttpServletRequest request, HttpServletResponse response){

		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}

		if ( null == activityId||activityId==1) {
            try {
                activityId = activityBiz.currentActivityId();
            } catch (Exception e) {
                return ValueUtil.toError("noCurrentActivity",e.getMessage());
            }
        }
        return  ValueUtil.toJson("chapter",chapterService.showChapter(activityId));
}
    @RequestMapping(value = "/setChapter", method = RequestMethod.POST)
    public String get(String arr,Integer activityId,HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		String [] b=arr.split("&");
        List<String> list=new ArrayList();
        for(int i=0;i<b.length;i++){
            list.add(b[i]);
        }
        String chapter=list.get(0);
        int chapterId=Integer.parseInt(chapter);
        if ( null == activityId  ) {
            try {
                activityId = activityBiz.currentActivityId();
            } catch (Exception e) {
                return ValueUtil.toError("noCurrentActivity",e.getMessage());
            }
        }
        for(int j=1;j<chapterId+1;j++) {
            String d = list.get(j);
            String averageStart = ValueUtil.coalesce(ValueUtil.getFromJson(d, "averageStart"),"");
            String sumStart =ValueUtil.coalesce(ValueUtil.getFromJson(d, "sumStart"),"");
            String underStart =ValueUtil.coalesce(ValueUtil.getFromJson(d, "underStart"),"");
            String lessChapter =ValueUtil.coalesce(ValueUtil.getFromJson(d, "lessChapter"),"");
            Integer activityIdOne=activityId;
          setChapter(activityIdOne,averageStart,sumStart,underStart,lessChapter,j);
        }
        return ValueUtil.toJson("chapter","failure");
    }
        public  String setChapter(Integer activityIdOne
            ,String averageStart
            ,String sumStart
            ,String underStart
            ,String lessChapter
                ,Integer j
            ){
        return  ValueUtil.toJson("chapter",chapterService.setChapter(activityIdOne,averageStart,sumStart,underStart,lessChapter,j));
    }
}
