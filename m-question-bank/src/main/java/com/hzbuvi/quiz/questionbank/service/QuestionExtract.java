package com.hzbuvi.quiz.questionbank.service;

import com.hzbuvi.quiz.questionbank.entity.Answer;
import com.hzbuvi.quiz.questionbank.entity.DeleteEnum;
import com.hzbuvi.quiz.questionbank.entity.DifficultyEnum;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.quiz.questionbank.repository.AnswerDao;
import com.hzbuvi.quiz.questionbank.repository.QuestionBankRepository;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class QuestionExtract {

	@Autowired
	private QuestionBankRepository dao;
	@Autowired
	private AnswerDao answerDao ;

	public List<Questionbank> extract(List<Integer> categorys, List<Integer> knowledges, DifficultyEnum easy, int pcs) throws HzbuviException {

		System.out.println("extract");

//		List<Questionbank> result = new ArrayList<>();
		List<Questionbank> temp = new ArrayList<>();




		List<Questionbank> queryList = dao.findByCategoryIdInAndKnowledgeIdInAndDifficultyAndIsDelete(categorys,knowledges,easy, DeleteEnum.NOT_DELETE);
//		List<Questionbank> queryList = dao.findAll();

		if (queryList.size() >= pcs) {
			Collections.shuffle(queryList);
			temp = queryList.subList(0,pcs);
		} else if ( queryList.size() > 0  ){
			int times = pcs/queryList.size() ;
			times++;
			for (int i = 0; i < times; i++) {
				temp.addAll(queryList);
			}
			temp = temp.subList(0,pcs);
		} else {
			HzbuviException exception = new HzbuviException("noMoreQuestions");
			exception.setMsg("抽题失败:本分类及知识点下难度为"+easy.getValue()+"的题目数量不足,请先完善题库或修改题目类型");
			throw exception;

//			queryList = dao.findAll();
//			Collections.shuffle(queryList);
//			int times = pcs/queryList.size() ;
//			times++;
//			for (int i = 0; i < times; i++) {
//				temp.addAll(queryList);
//			}
//			temp = temp.subList(0,pcs);
		}

		Set<Integer> questionIds = new HashSet<>();
		temp.forEach( e -> questionIds.add(e.getId()));
		List<Answer> answers = answerDao.findByQuestionIdIn(questionIds);
		Map<Integer,List<Answer>> answermap = new HashMap<>();
		for (int i = 0; i < answers.size() ; i++) {
			if ( null == answermap.get(answers.get(i).getQuestionid()) || answermap.get(answers.get(i).getQuestionid()).size()==0 ) {
				List<Answer> ans = new ArrayList<>();
				ans.add(answers.get(i));
				answermap.put(answers.get(i).getQuestionid(),ans);
			} else  {
				answermap.get(answers.get(i).getQuestionid()).add(answers.get(i));
			}
		}
		Integer a=temp.size();
		temp.forEach( q -> q.setAnswers(answermap.get(q.getId())) );
		return temp;

	}



}
