package com.hzbuvi.quiz.controller;


import com.hzbuvi.quiz.activity.bean.ActivityCondition;
import com.hzbuvi.quiz.activity.entity.Activity;
import com.hzbuvi.quiz.activity.repository.ActivityRepository;
import com.hzbuvi.quiz.activity.repository.QuestionPropertiesDao;
import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.activity.service.Check;
import com.hzbuvi.quiz.chapter.service.ChapterService;
import com.hzbuvi.quiz.config.PageModel;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.questionbank.service.QuestionBankservice;
import com.hzbuvi.quiz.section.service.SectionBiz;
import com.hzbuvi.quiz.usersection.UserSecionBiz;
import com.hzbuvi.quiz.usersection.repository.UserSectionHistoryDao;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${maxiu} on 2016/8/25.
 */

@RestController
@RequestMapping("/activity")
public class ActivityController {

	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private ChapterService chapterBiz;
	@Autowired
	private SectionBiz sectionBiz;
	@Autowired
	private QuestionBankservice questionBankservice;
	@Autowired
	private QuestionPropertiesDao propDao;
	@Autowired
	private Check check;
	@Autowired
	private UserSecionBiz userSecionBiz;
	@Autowired
	private UserSectionHistoryDao historyDao;
	@Autowired
	private ActivityRepository activityRepository;

	@RequestMapping( method = RequestMethod.POST)
	public String create(@RequestParam Map<String,String> param,String userId, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		int chapters = Integer.parseInt(param.get("chapters"));
		int sectionsPerChapter = Integer.parseInt(param.get("sectionsPerChapter"));
		int questionsPerSection = Integer.parseInt(param.get("questionsPerSection"));
		int sectionLimitTime= Integer.parseInt(param.get("sectionLimitTime"));

		Integer activityId = activityBiz.create(param);
		List<Integer> chapterIds =  chapterBiz.create(activityId,chapters,sectionsPerChapter);
		sectionBiz.create(activityId,chapterIds,sectionsPerChapter);
		return ValueUtil.toJson("activity",activityId);
	}

	@RequestMapping(value = "/d/{id}", method = RequestMethod.POST)
	public String delete(@PathVariable("id") Integer activityId, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("delete",activityBiz.delete(activityId));
	}
	@RequestMapping(method = RequestMethod.GET)
	public String check(Integer activityId, HttpServletRequest request,HttpServletResponse response){
//		try {
//			SessionData.verify(request,response);
//		} catch (HzbuviException e) {
//			return ValueUtil.toError(e.getCode(),e.getMsg() );
//		}
		if ( null == activityId || 1==activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}
		HashMap<String,Integer> map=new HashMap<>();
		Integer run=activityBiz.checkDay(activityId);
		if(run==1){
			map.put("lock",1);//可以进行
		}else {
			map.put("lock",0);//不可以进行
		}
		return ValueUtil.toJson("check",map);
	}

	@RequestMapping(value = "/checkTime",method = RequestMethod.GET)
	public String checkTime(Integer activityId, HttpServletRequest request,HttpServletResponse response){
//		try {
//			SessionData.verify(request,response);
//		} catch (HzbuviException e) {
//			return ValueUtil.toError(e.getCode(),e.getMsg());
//		}
		if ( null == activityId || 1==activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}
		HashMap<String,Integer> map=new HashMap<>();
		Integer run=activityBiz.checkTime(activityId);
		if(run==2){
			map.put("lock",2);//可以进行
		}else {
			map.put("lock",3);//不可以进行
		}
		return ValueUtil.toJson("check",map);
	}

	@RequestMapping(value = "/showCategory", method = RequestMethod.GET)
	public String showCategory(Integer id, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("activity",activityBiz.showCategory(id),"knowledge",activityBiz.showKnowledge());
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id")Integer id, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("activity",activityBiz.show(id));
	}
	@RequestMapping(value = "/showOne", method = RequestMethod.GET)
	public String showOne( Integer activityId , HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}

		if ( null == activityId  ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}

		return  ValueUtil.toJson("activityEntity",activityBiz.showActivity(activityId),"activity",activityBiz.showOne(activityId),"activities",activityBiz.all());
	}
	@RequestMapping(value = "/showSum", method = RequestMethod.GET)
	public String showSum(Integer activityId, HttpServletRequest request,HttpServletResponse response) {
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}

		if (null == activityId) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity", e.getMessage());
			}
		}
		return ValueUtil.toJson("activity", activityBiz.showSum(activityId), "activitys", activityBiz.showAll());
	}
//    public String showSum(){
//        return  ValueUtil.toJson("activity",activityBiz.showSum(),"activities",activityBiz.all());
//
//    }

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ActivityCondition activity, Integer categories, String cate , HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		try {
			Integer cnt = historyDao.countByActivityId(activity.getId());//判断是否有人做
			if (null != cnt && cnt > 0 ) {
				return  ValueUtil.toJson("activity",activityBiz.update2(activity)); // 活动已开始
			} else {
				Activity oldAct = activityRepository.findOne(activity.getId());
				if(( oldAct.getChapters() == activity.getChapters()
						&& oldAct.getSections() == activity.getSections()
						&& oldAct.getQuestions() == activity.getQuestions())||ValueUtil.isEmpity(oldAct.getChapters()) ) {
					return  ValueUtil.toJson("activity",activityBiz.update2(activity)); // 活动已开始
				} else {
					return  ValueUtil.toJson("activity",activityBiz.update(activity,categories,cate)); // 活动沒开始
				}

			}
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
	}
	@RequestMapping(value = "/showlines", method = RequestMethod.GET)
	public String updateLond( Integer id , HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response );
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		Integer cnt = historyDao.countByActivityId(activityRepository.findOne(id).getId());//判断是否有人做
		if (null != cnt && cnt > 0 ) {//有人
			return  ValueUtil.toJson("activity",activityBiz.updateLoad(id),"categories",questionBankservice.getCategory(),"knowledge",questionBankservice.getKnowledge(),"lock",1);//有人做
		}else {
			return  ValueUtil.toJson("activity",activityBiz.updateLoad(id),"categories",questionBankservice.getCategory(),"knowledge",questionBankservice.getKnowledge(),"lock",0);//没人做
		}

	}
	@RequestMapping(value = "/showAll", method = RequestMethod.GET)
	public String showAll(Integer activityId ,  HttpServletRequest request,HttpServletResponse response ){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		if ( null == activityId  ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				List<Activity> activities  = activityBiz.all();
				return  ValueUtil.toJson("activity",activities.get(0),"activities",activities);
			}
		}
		return  ValueUtil.toJson("activity",activityBiz.showAll(activityId),"activities",activityBiz.all());
	}
	//    @RequestMapping(value = "/index",method = RequestMethod.GET)
//    public String index(Integer page, HttpServletRequest request,HttpServletResponse response){
//		try {
//			SessionData.verify(request,response);
//		} catch (HzbuviException e) {
//			return ValueUtil.toError(e.getCode(),e.getMsg());
//		}
//		return  ValueUtil.toJson("activitys",activityBiz.index(page));
//    }
	@RequestMapping(value = "/check",method = RequestMethod.GET)
	public String check(int difficult,int normal,int easy, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("activitys",check.division(easy, normal, difficult));
	}
	@RequestMapping(value = "/showKnowledge", method = RequestMethod.GET)
	public String showKnowledge(Integer categerId, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("activity",activityBiz.showKnowledge(categerId));
	}
	@RequestMapping(value = "/showActivity", method = RequestMethod.GET)
	public String showActivity(@RequestParam Map<String,String> parm, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		Map<String,Object>  result = activityBiz.showActivity(parm);
		PageModel pageModel = new PageModel((Page) result.get("page"));
		pageModel.setCondition(result.get("condition"));
		return  ValueUtil.toJson("activityName",pageModel);
	}

	@RequestMapping(value = "/name", method = RequestMethod.GET)
	public String idName( HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("activities",activityBiz.all() );
	}
}



















