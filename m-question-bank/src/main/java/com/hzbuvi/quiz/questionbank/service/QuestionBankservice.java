package com.hzbuvi.quiz.questionbank.service;


import com.google.gson.JsonObject;
import com.hzbuvi.page.util.PageResult;
import com.hzbuvi.page.util.PageUtil;
import com.hzbuvi.quiz.questionbank.entity.*;
import com.hzbuvi.quiz.questionbank.repository.AnswerDao;
import com.hzbuvi.quiz.questionbank.repository.CategoryRepository;
import com.hzbuvi.quiz.questionbank.repository.KnowledgeRepository;
import com.hzbuvi.quiz.questionbank.repository.QuestionBankRepository;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Downey.hz on 2016/10/11..
 */
@Service
public class QuestionBankservice {
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;
    @Autowired
    private AnswerDao answerDao;

    public static Integer defaultPageSize = 10;

    public List<Category> getCategory()  {
        List<Category> list=categoryRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
        return list;
    }
    public List<Knowledge> getKnowledge() {
        List<Knowledge> list=knowledgeRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
        return list;
}
    public Object insertSave(Questionbank questionbank){
        Map<String,String> map=new HashMap<>();
        String i=questionbank.getSerialNumber();
       List j= questionBankRepository.findIdBySerialNumbers(i);
        if(j.size()!=0){
            map.put("status","该编号已存在");
            return map;
        }else
        questionbank.setIsDelete(DeleteEnum.NOT_DELETE);
        questionbank.setSerialNumber(questionbank.getSerialNumber());
        questionbank.setQuestionContent(questionbank.getQuestionContent());
        questionbank.setKnowledgeName(knowledgeRepository.findOne(questionbank.getKnowledgeId()).getKnowledgeName());
        questionbank.setCategoryName(categoryRepository.findOne(questionbank.getCategoryId()).getName());
        questionbank.setAnswerContent("null");
        questionbank.setAnalysis(" ");
        TypeEnum typeEnum=questionbank.getType();
        if(typeEnum==TypeEnum.SingleSelection){
            questionbank.setTypeName("单选题");
        }else {
            questionbank.setTypeName("多选题");
        }

		List<String> deleteSerialNumbers = new ArrayList<>();
		deleteSerialNumbers.add(questionbank.getSerialNumber());
		if (deleteSerialNumbers.size()>0) {
			List<Questionbank> deleteList = questionBankRepository.findBySerialNumberIn(deleteSerialNumbers);
			questionBankRepository.delete(deleteList);
		}

        questionBankRepository.save(questionbank);


		questionbank.putAnswer();
		List<Answer> answers = questionbank.getAnswers();
		answerDao.save(answers);

        return "success";
    }
    public boolean delete(Integer id) throws HzbuviException{
        ValueUtil.verify(id,"idNull");
		questionBankRepository.delete(id);
//        Questionbank questionbank = questionBankRepository.findOne(id);
//        questionbank.setIsDelete(DeleteEnum.DELETED);
//        questionBankRepository.save(questionbank);
        return true;
    }
    public List<Object>updateLoad(Integer id){
        Questionbank questionbank=questionBankRepository.findOne(id);
        List<Object> list=new ArrayList<>();
        List<Object> list2=new ArrayList<>();
        List<Category> list1=categoryRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
        list.add(questionbank);
        list2.add(list);
        list2.add(list1);
        return list2;
    }
    public String update(Questionbank questionbank){

        Questionbank questionbank1=questionBankRepository.findOne(questionbank.getId());
//        String a=questionbank.getSerialNumber();
        String b=questionbank.getQuestionContent();
        questionbank1.setQuestionContent(b);
        int id=questionbank.getCategoryId();
        questionbank1.setCategoryId(id);
        questionbank1.setCategoryName(categoryRepository.findOne(id).getName());
        questionbank1.setKnowledgeName(questionbank.getKnowledgeName());
        questionbank1.setDifficultyName(questionbank.getDifficulty().name());
        questionbank1.setRightAnswer(questionbank.getRightAnswer());
        TypeEnum typeEnum=questionbank.getType();
        if(typeEnum==TypeEnum.SingleSelection){
            questionbank1.setTypeName("单选题");
            questionbank1.setType(TypeEnum.SingleSelection);
        }else {
            questionbank1.setTypeName("多选题");
            questionbank1.setType(TypeEnum.MultiSelect);
        }
//        questionbank1.setSerialNumber(a);
        questionBankRepository.save(questionbank1);
        List<Answer> list=answerDao.findByQuestionId(questionbank.getId());
        for(int i=0;i<list.size();i++){
            if(i==0){
                list.get(0).setContent(questionbank.getA());
            }if(i==1){
                list.get(1).setContent(questionbank.getB());
            }if(i==2){
                list.get(2).setContent(questionbank.getC());
            }if(i==3){
                list.get(3).setContent(questionbank.getD());
            }if(i==4){
                list.get(4).setContent(questionbank.getE());
            }if(i==5){
                list.get(5).setContent(questionbank.getF());
            }
        }
        answerDao.save(list);
        return "updateSuccess";
    }
    public List<Map<String,String>> getAnswer(Integer id){
        List<Answer> list=answerDao.findByQuestionId(id);
        List<Map<String,String>> list1=new ArrayList<>();
        Map<String,String> map=new HashMap<>();
        for(int i=0;i<list.size();i++){
            if(i==0){
                String a="A";
                map.put(a,list.get(i).getContent());
            }if(i==1){
                String a="B";
                map.put(a,list.get(i).getContent());
            }if(i==2){
                String a="C";
                map.put(a,list.get(i).getContent());
            }if(i==3){
                String a="D";
                map.put(a,list.get(i).getContent());
            }if(i==4){
                String a="E";
                map.put(a,list.get(i).getContent());
            }if(i==5){
                String a="F";
                map.put(a,list.get(i).getContent());
            }
        }
        list1.add(map);
        return list1;
    }
    public  Map<String,Object> show(Map<String,String> parm) {
		String page = ValueUtil.coalesce( parm.get("page") , "0") ;
        String category= parm.get("category");
        String knowage= parm.get("knowledge");
        String type = parm.get("type");
        String difficult= parm.get("difficult");

		JsonObject conditionMap = new JsonObject();

		if (ValueUtil.notEmpity(category)) {
			conditionMap.addProperty("category",category);
		}
		if (ValueUtil.notEmpity(knowage)) {
			conditionMap.addProperty("knowledge",knowage);
		}
		if (ValueUtil.notEmpity(type)) {
			conditionMap.addProperty("type",type);
		}
		if (ValueUtil.notEmpity(difficult)) {
			conditionMap.addProperty("difficult",difficult);
		}

		Pageable pageable = new PageRequest(Integer.valueOf(page),defaultPageSize,new Sort( new Sort.Order(Sort.Direction.ASC,"id") ));
		Page<Questionbank> questionbankPage = questionBankRepository.findAll(getWhereClause(category,knowage,type,difficult), pageable);
		Map<String,Object> map = new HashMap<>();
		map.put("page",questionbankPage);
		map.put("condition",conditionMap);
        return map;
    }

    public PageResult index(Integer page) {
        if(null==page){
            page=0;
        }
        Pageable pageable = new PageRequest(ValueUtil.coalesce(page,0),defaultPageSize);
        Page<Questionbank> questionbanks = questionBankRepository.findByIsDelete(pageable,DeleteEnum.NOT_DELETE);
        PageResult result = PageUtil.toPage(questionbanks,page);
        return result;
    }


	private Specification<Questionbank>  getWhereClause(String category, String knowledge, String type,String difficult) {
		return new Specification<Questionbank>() {
			@Override
			public Predicate toPredicate(Root<Questionbank> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if (ValueUtil.notEmpity(category) ) {
					predicate.getExpressions().add( cb.equal(root.<Integer>get("categoryId"),Integer.valueOf(category)) );
				}
				if (ValueUtil.notEmpity(knowledge)) {
					predicate.getExpressions().add( cb.equal(root.<Integer>get("knowledgeId"),Integer.valueOf(knowledge)) );
				}
				if (ValueUtil.notEmpity(type)) {
					predicate.getExpressions().add( cb.equal(root.<Integer>get("type"),Integer.valueOf(type)) );
				}
				if (ValueUtil.notEmpity(difficult)) {
					predicate.getExpressions().add( cb.equal(root.<Integer>get("difficulty"),Integer.valueOf(difficult)) );
				}
                predicate.getExpressions().add( cb.equal(root.<Integer>get("isDelete"),0));
				return predicate;
			}
		};
	}

}

