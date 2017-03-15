package com.hzbuvi.quiz.questionsheet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hzbuvi.quiz.questionbank.entity.DifficultyEnum;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.quiz.questionbank.service.QuestionExtract;
import com.hzbuvi.quiz.questionsheet.bean.DiffNorEasy;
import com.hzbuvi.quiz.questionsheet.bean.RightAnswer;
import com.hzbuvi.quiz.questionsheet.bean.VerifyAnswerResult;
import com.hzbuvi.quiz.questionsheet.entity.QuestionSheet;
import com.hzbuvi.quiz.questionsheet.entity.SectionQuestionSheet;
import com.hzbuvi.quiz.questionsheet.repository.DiffNorEasyDao;
import com.hzbuvi.quiz.questionsheet.repository.QuestionSheetDao;
import com.hzbuvi.quiz.questionsheet.repository.SectionQuestionSheetDao;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class QuestionSheetBiz {

	@Autowired
	private QuestionSheetDao qsd;
	@Autowired
	private SectionQuestionSheetDao sqsd;
	@Autowired
	private QuestionExtract extract;
	@Autowired
	private DiffNorEasyDao diffNorEasyDao;

	@Transactional
	public void createQuestionSheet(
			 Integer activityId
			,List<Integer> sectionIds
			,List<Integer> categorys
			,List<Integer> knowledges
			,Integer sheetsPerSection
			,Integer questionsPerSection
			,Integer easyPerSection
			,Integer normalPerSection
			,Integer hardPerSection
			,Integer easyScore
			,Integer normalScore
			,Integer hardScore  ) throws HzbuviException {
		DiffNorEasy diffNorEasy;
		if(null==diffNorEasyDao.findByActivityId(activityId)){
			 diffNorEasy=new DiffNorEasy();
		}else{
			diffNorEasy=diffNorEasyDao.findByActivityId(activityId);
		}
		diffNorEasy.setActivityId(activityId);
		diffNorEasy.setEasyPerSection(easyPerSection);
		diffNorEasy.setNormalPerSection(normalPerSection);
		diffNorEasy.setHardPerSection(hardPerSection);
		diffNorEasy.setSorce(easyScore+","+normalScore+","+hardScore);
		diffNorEasyDao.save(diffNorEasy);
		System.out.println("sheetPerSection"+ sheetsPerSection);
		System.out.println("questionPerSection"+ questionsPerSection);
		System.out.println("sectionIdsSize"+ sectionIds.size());

		int pcs = sheetsPerSection * questionsPerSection *  sectionIds.size()  ;
		System.out.println("pcs:"+pcs);

		List<Questionbank> easyList =  extract.extract(categorys,knowledges, DifficultyEnum.Easy, pcs );
		List<Questionbank> normalList=  extract.extract(categorys,knowledges, DifficultyEnum.Normal , pcs );
		List<Questionbank> hardList=  extract.extract(categorys,knowledges, DifficultyEnum.Difficult , pcs );

		int looplength = sheetsPerSection * sectionIds.size() ;

		Date createDate = new Date();
		List<QuestionSheet> questionSheets = new ArrayList<>();

//		System.out.println("delete old question sheet ");
		qsd.deleteByActivityId(activityId);
//		System.out.println("delete old question sheet ,end ");

//		deleteQuestionSheet(activityId);


		for (int i = 0; i < looplength ; i++) {
			QuestionSheet qs = new QuestionSheet(createDate);
			Map<String,RightAnswer> rightAnswer = new HashMap<>();
			List<Questionbank> questions = new ArrayList<>();
			int easystart = i * easyPerSection;
			int normalstart = i * normalPerSection ;
			int hardstart = i * hardPerSection;
			questions.addAll(easyList.subList(easystart,easyPerSection + easystart )) ;
			questions.addAll(normalList.subList(normalstart,normalPerSection+ normalstart)) ;
			questions.addAll(hardList.subList(hardstart,hardPerSection+ hardstart)) ;

			for (int j = 0; j < questions.size() ; j++) {
				questions.get(j).setSequence(j+1);

			}

			List<RightAnswer> rights = new ArrayList<>();
			for (int j = 0; j < questions.size() ; j++) {
				RightAnswer right = new RightAnswer(questions.get(j),easyScore,normalScore,hardScore);
				rights.add(right);
			}
			qs.setQuestions(questions.size());
			qs.setActivityId(activityId);
			qs.setQuestionJson(ValueUtil.toJson(questions));
			qs.setRightAnswerJson(ValueUtil.toJson(rights));
			questionSheets.add(qs);
//			qsd.save(qs);
//			System.out.println( "save new question sheet "+qs.getId() );
		}
		qsd.save(questionSheets);
//		saveQuestionSheet(questionSheets);

//		System.out.println("delete old section question sheet " );
		sqsd.deleteBySectionIdIn(sectionIds);
//		System.out.println("delete old section question sheet,end " );
//		deleteSectionQuestionSheet(sectionIds);
		List<SectionQuestionSheet> sectionSheets = new ArrayList<>();
		for (int i = 0; i < questionSheets.size() ; i++) {
			SectionQuestionSheet sqs = new SectionQuestionSheet();
			sqs.setQuestionSheetId(questionSheets.get(i).getId());
			sqs.setSectionId(sectionIds.get(i/sheetsPerSection));
			sectionSheets.add(sqs);
//			sqsd.save(sqs);
//			System.out.println("save new section question sheet "+ sqs.getId() );
		}
		sqsd.save(sectionSheets);
//		saveSectionQuestionsheet(sectionSheets);
	}

//
//	@Transactional
//	private void saveSectionQuestionsheet(List<SectionQuestionSheet> sectionQuestionSheets){
//		sqsd.save(sectionQuestionSheets);
//	}
//
//
//	@Transactional
//	private void saveQuestionSheet(List<QuestionSheet> questionSheets){
//		qsd.save(questionSheets);
//	}
//
//	@Transactional
//	private void deleteSectionQuestionSheet(List<Integer> sectionIds){
//		sqsd.deleteBySectionIdIn(sectionIds);
//	}
//
//
//
//	@Transactional
//	private void deleteQuestionSheet(Integer activityId){
//		qsd.deleteByActivityId(activityId);
//	}
//
//


	public Object[] getQuestionSheet(Integer sectionId, List<Integer> questionSheetIds) {
		List<SectionQuestionSheet> sectionQuestionSheets = sqsd.findBySectionId(sectionId);
		Collections.shuffle(sectionQuestionSheets);
		Integer questionSheetId = 0;
		if (null == questionSheetIds || questionSheetIds.size() >= sectionQuestionSheets.size() ) {
			questionSheetId = sectionQuestionSheets.get(0).getQuestionSheetId();
		} else {
			for (int i = 0; i <  sectionQuestionSheets.size() ; i++) {
				if ( ! questionSheetIds.contains(sectionQuestionSheets.get(i).getQuestionSheetId()) ) {
					questionSheetId = sectionQuestionSheets.get(i).getQuestionSheetId();
					break;
				}
			}
		}
		JsonParser jsonParser = new JsonParser();

		JsonArray jsonArray = jsonParser.parse(qsd.findOne(questionSheetId).getQuestionJson()).getAsJsonArray() ;

		for (int i = 0; i < jsonArray.size() ; i++) {
			jsonArray.get(i).getAsJsonObject().remove("rightAnswer");
		}

		Object[] returnObj = { jsonArray , questionSheetId };
		return returnObj;
	}

	public Map<String,Object> verifyAnswer(Integer questionSheetId, String content) {
		Map<Integer,List<Integer>>  inputAnswer = new HashMap<>();

		String[] inputs = content.split(",");
		for (String str :inputs ) {
			Integer key = Integer.valueOf(str.substring(0,str.indexOf("=")));
			Integer value = Integer.valueOf(str.substring(str.indexOf("=")+1));
			if ( null == inputAnswer.get( key ) ) {
				List<Integer> list = new ArrayList<>();
				list.add(value);
				inputAnswer.put(key,list);
			} else {
				inputAnswer.get(key).add(value);
			}
		}
		QuestionSheet questionSheet = qsd.findOne(questionSheetId);
		Gson gson = new Gson();
		Type listType = new TypeToken<List<RightAnswer>>(){}.getType();
		List<RightAnswer> right = gson.fromJson(questionSheet.getRightAnswerJson(),listType);

		int score = 0;
		int correctCnt = 0;
		List<VerifyAnswerResult> errorList = new ArrayList<>();
		for (int i = 0; i < right.size() ; i++) {
			VerifyAnswerResult temp = right.get(i).getScore(inputAnswer.get(right.get(i).getSequence()));
			if (temp.isCorrect() ) {
				correctCnt++;
				score += temp.getScore();
			} else  {
				errorList.add(temp);
			}
		}
		Integer questions=questionSheet.getQuestions();

		BigDecimal div  = BigDecimal.valueOf(questions);
		BigDecimal correctRate = BigDecimal.valueOf(correctCnt);
		correctRate = correctRate.divide(div,7,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(1,BigDecimal.ROUND_HALF_UP);
		Map<String,Object> result = new HashMap<>();
		result.put("score",score);
		result.put("correctCount",correctCnt);
		result.put("ErrorCount",errorList.size());
		result.put("errorList",errorList);
		result.put("correctRate",correctRate.toString());

		return result;
	}
	public Object show(Integer activityId){
		if(null==diffNorEasyDao.findByActivityId(activityId)){
			Map<String,String> map=new HashMap();
			map.put("status","0");
			return map;
		}else{
			return diffNorEasyDao.findByActivityId(activityId);
		}
	}
}
