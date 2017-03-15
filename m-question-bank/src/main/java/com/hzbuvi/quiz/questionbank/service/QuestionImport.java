package com.hzbuvi.quiz.questionbank.service;

import com.hzbuvi.quiz.excelimport.ExcelImportBiz;
import com.hzbuvi.quiz.questionbank.entity.Answer;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.quiz.questionbank.repository.AnswerDao;
import com.hzbuvi.quiz.questionbank.repository.QuestionBankRepository;
import com.hzbuvi.util.basic.MapUtil;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by light on 2016/10/25.
 */
@Service
public class QuestionImport {
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
	private AnswerDao answerDao;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private KnowledgeService knowledgeService;
	@Autowired
	private ExcelImportBiz excelImportBiz;


	public synchronized boolean importFromExcel(String excelFilePath) throws HzbuviException {
		List<Map<String,Object>> datas = excelImportBiz.getData(excelFilePath,this.getClass());
		List<Questionbank> questions = new ArrayList<>();
		List<String> deleteSerialNumbers = new ArrayList<>();
		Map<String,Integer>  serialFlag = new HashMap<>() ;
		for (Map<String,Object > dt : datas) {
			System.out.println( "question:"+dt.get("questionContent") );
			if ( null == dt.get("difficultyName")  || "".equals(dt.get("difficultyName").toString().trim()) ){
				continue;
			}
			Questionbank questionbank = (Questionbank) MapUtil.mapToObject(dt,Questionbank.class) ;
			Integer cateId = categoryService.find(questionbank.getCategoryName());
			questionbank.setCategoryId(cateId);
			questionbank.setKnowledgeId(knowledgeService.find(questionbank.getKnowledgeName(),cateId,questionbank.getCategoryName()));
			questionbank.putAnswer();
			String  s=questionbank.getSerialNumber().trim();
			if (ValueUtil.notEmpity( s ) &&  null ==  serialFlag.get(questionbank.getSerialNumber()) && questionbank.verifyValue() ){
				questions.add(questionbank);
				serialFlag.put(questionbank.getSerialNumber(),1);
				deleteSerialNumbers.add(questionbank.getSerialNumber());
			}
		}

		List<String> exist = new ArrayList<>();
		if (deleteSerialNumbers.size()>0) {
			List<Questionbank> deleteList = questionBankRepository.findBySerialNumberIn(deleteSerialNumbers);
//			questionBankRepository.delete(deleteList);
			deleteList.forEach( d -> {exist.add(d.getSerialNumber()) ; });
		}


		List<Questionbank> toSave = new ArrayList<>();
		if ( questions.size() > 0) {
			for (int i = 0; i < questions.size() ; i++) {
				if ( ! exist.contains(questions.get(i).getSerialNumber())){
					toSave.add(questions.get(i));
				}
			}
		}


		if ( toSave.size() == 0 ) {
			HzbuviException hzbuviException = new HzbuviException("error");
			hzbuviException.setMsg("导入失败");
			throw hzbuviException;
		}

		questionBankRepository.save(toSave);

		List<Answer> answers = new ArrayList<>();

		for (Questionbank question: toSave) {
			answers.addAll(question.getAnswers());
		}
		answerDao.save(answers);
		return true;
	}

}
