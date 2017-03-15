package com.hzbuvi.quiz.questionbank.service;

import com.hzbuvi.page.util.PageResult;
import com.hzbuvi.page.util.PageUtil;
import com.hzbuvi.quiz.questionbank.entity.DeleteEnum;
import com.hzbuvi.quiz.questionbank.entity.Knowledge;
import com.hzbuvi.quiz.questionbank.repository.CategoryRepository;
import com.hzbuvi.quiz.questionbank.repository.KnowledgeRepository;
import com.hzbuvi.quiz.questionbank.repository.QuestionBankRepository;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangDianDian on 2016/10/24.
 */
@Service
public class KnowledgeService {
    @Autowired
    KnowledgeRepository knowledgeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionBankRepository questionBankRepository;
	public static Integer defaultPageSize = 10;

//	private static Map<String,Knowledge> map = new HashMap<>();


//	private synchronized void refresh(){
//		map.clear();
//		List<Knowledge> beans = knowledgeRepository.findAll();
//		for (Knowledge k : beans) {
//			if(null!=k.getCategoryId() && null!= k.getCategoryName()) {
//				map.put(k.getKnowledgeName()+":"+k.getCategoryId()+":"+k.getCategoryName(),k);
//			} else {
//				map.put(k.getKnowledgeName(),k);
//			}
//		}
//	}

	public Integer find(String knowledge,Integer cateId, String cateName){
		Integer id = 0 ;
		List<Knowledge> knowledges =  knowledgeRepository.findByKnowledgeNameAndIsDelete(knowledge,DeleteEnum.NOT_DELETE);
		if (null == knowledges || knowledges.size() < 1) {
			id = insert(knowledge,cateId,cateName);
		} else {
			id = knowledges.get(0).getId();
		}
		return id;
	}
    public  Map<String,Object> show(Map<String,String> parm) {
        String page = ValueUtil.coalesce( parm.get("page") , "0") ;
        String knowledge= parm.get("knowledge");
        String category=parm.get("category");
        Map<String,String> map1=new HashMap<>();
        if (ValueUtil.notEmpity(knowledge) ) {
            map1.put("knowledge",knowledge);
        }
        if (ValueUtil.notEmpity(category) ) {
            map1.put("category",category);
        }
        Pageable pageable = new PageRequest(Integer.valueOf(page),defaultPageSize);
        Page<Knowledge> knowledges = knowledgeRepository.findAll(getWhereClause(knowledge,category), pageable);
        Map<String,Object> map = new HashMap<>();
        map.put("page",knowledges);
        map.put("condition",map1);
        return map;
    }
    private Specification<Knowledge>  getWhereClause( String knowledge,String category) {
        return new Specification<Knowledge>() {
            @Override
            public Predicate toPredicate(Root<Knowledge> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();

                if (ValueUtil.notEmpity(knowledge)) {
                    predicate.getExpressions().add( cb.equal(root.<Integer>get("id"),Integer.valueOf(knowledge)) );
                }
                if (ValueUtil.notEmpity(category)) {
                    predicate.getExpressions().add( cb.equal(root.<Integer>get("categoryId"),Integer.valueOf(category)) );
                }
                predicate.getExpressions().add( cb.equal(root.<Integer>get("isDelete"),0));
                return predicate;
            }
        };
    }
	private Integer insert(String knowledgeName,Integer cateId,String cateName) {
		Knowledge knowledge = knowledgeRepository.findByKnowledgeNameAndCategoryIdAndCategoryName(knowledgeName,cateId,cateName);
        if(null == knowledge ){
            Knowledge newKnow = new Knowledge(knowledgeName,cateId,cateName);
            knowledgeRepository.save(newKnow);
            return newKnow.getId();
        } else {
			return  knowledge.getId();
		}
    }
//    public Integer insert(String knowledgeName, String categoryName) {
//        if(null==knowledgeRepository.findByKnowledgeName(knowledgeName)){
//            Knowledge knowledge= new Knowledge(knowledgeName);
//            knowledge.setCategoryName(categoryName);
//            knowledgeRepository.save(knowledge);
//            return knowledge.getId();
//        }
//        return  knowledgeRepository.findByKnowledgeName(knowledgeName).getId();
//    }

    public String delete(Integer id) throws HzbuviException {
		long cnt = questionBankRepository.countByKnowledgeIdAndIsDelete(id,DeleteEnum.NOT_DELETE);
		if (cnt == 0 ) {
			knowledgeRepository.delete(id);
		} else {
			HzbuviException hzbuviException = new HzbuviException("error");
			hzbuviException.setMsg("无法删除知识点");
			throw hzbuviException;
		}
        return "success";
    }
    public List<Object> updateLoad(Integer id){
        Knowledge knowledge=knowledgeRepository.findOne(id);
        List<Knowledge> list1=knowledgeRepository.findAll();
        List<Object> list=new ArrayList<>();
        list.add(knowledge);
        list.add(list1);
        return list;
    }
    public String update(int oldId,String knowledgeName,int categoryId){
        Knowledge knowledge=knowledgeRepository.findOne(oldId);
        knowledge.setKnowledgeName(knowledgeName);
        knowledge.setCategoryName(categoryRepository.findOne(categoryId).getName());
        knowledgeRepository.save(knowledge);
        return "updateSuccess";
    }
    public PageResult index(Integer page) {
        if(null==page){
            page=0;
        }
        Pageable pageable = new PageRequest(ValueUtil.coalesce(page,0),defaultPageSize);
        Page<Knowledge> knowledges = knowledgeRepository.findByIsDelete(pageable,DeleteEnum.NOT_DELETE);
        PageResult result = PageUtil.toPage(knowledges,page);
        return result;
    }

	public void updateCategoryName(Integer oldId, String categoryName) {
		List<Knowledge> knowledges = knowledgeRepository.findByCategoryId(oldId);
		for (int i = 0; i < knowledges.size() ; i++) {
			knowledges.get(i).setCategoryName(categoryName);
		}
		knowledgeRepository.save(knowledges);
	}
}
