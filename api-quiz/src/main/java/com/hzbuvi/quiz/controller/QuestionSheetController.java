package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.questionbank.entity.Category;
import com.hzbuvi.quiz.questionbank.entity.Knowledge;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.quiz.questionbank.repository.CategoryRepository;
import com.hzbuvi.quiz.questionbank.repository.KnowledgeRepository;
import com.hzbuvi.quiz.questionbank.repository.QuestionBankRepository;
import com.hzbuvi.quiz.questionsheet.QuestionSheetBiz;
import com.hzbuvi.quiz.section.service.SectionBiz;
import com.hzbuvi.quiz.usersection.UserSecionBiz;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANG, RUIQING on 10/31/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/questionsheet")
public class QuestionSheetController {

	@Autowired
	private QuestionSheetBiz questionSheetBiz;
	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private SectionBiz sectionBiz;
	@Autowired
	private UserSecionBiz userSecionBiz;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	KnowledgeRepository knowledgeRepository;

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String show(Integer activityId,HttpServletRequest request, HttpServletResponse response){
		if ( null == activityId || 1==activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("show",questionSheetBiz.show(activityId));
	}

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
	public String create(Integer activityId
//					 Integer activityId
//			,List<Integer> sectionIds
//			,List<Integer> categorys
//			,List<Integer> knowledges
//			,Integer sheetsPerSection
//			,Integer questionsPerSection
			,Integer easyPerSection
			,Integer normalPerSection
			,Integer hardPerSection
			,String scaleScore

			,HttpServletRequest request,HttpServletResponse response ){
		if ( null == activityId || 1==activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}

		try {
			SessionData.verify(request,response);
		userSecionBiz.canActivityUpdate(activityId);

		String[] newScore=scaleScore.split(",");
			System.out.println(newScore);
		Integer easyScore=Integer.valueOf(newScore[0]);
		Integer normalScore=Integer.valueOf(newScore[1]);
		Integer hardScore=Integer.valueOf(newScore[2]);
		List<Integer> sectionIds = sectionBiz.getIdByActivityId(activityId);
		List<Integer> categorys = activityBiz.categoryIds(activityId);
		List<Integer> knowledges = activityBiz.knowledgeIds(activityId);
			if(categorys.size()==0){
				List<Category> list= categoryRepository.findAll();
				for (int i = 0; i <list.size() ; i++) {
					categorys.add(list.get(i).getId());
				}
			}
			if(knowledges.size()==0){
				List<Knowledge> list= knowledgeRepository.findAll();
				for (int i = 0; i <list.size() ; i++) {
					knowledges.add(list.get(i).getId());
				}
			}

		Integer questionsPerSection = easyPerSection + normalPerSection+hardPerSection;


		questionSheetBiz.createQuestionSheet(activityId,sectionIds,categorys,knowledges,10,questionsPerSection,easyPerSection,normalPerSection,hardPerSection,easyScore,normalScore,hardScore);

//		System.out.println("start");
//		questionSheetBiz.createQuestionSheet(activityId,sectionIds,categorys,knowledges,3,10,3,4,3,2,10,18);
		System.out.println("question sheet generate finish ");
		return ValueUtil.toJson("status","success");
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
//					"活动已开始, 不能再进行修改"
		}
	}

}
