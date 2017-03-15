package com.hzbuvi.quiz.questionbank.service;

/**
 * Created by WangDianDian on 2016/10/24.
 */

import com.hzbuvi.page.util.PageResult;
import com.hzbuvi.page.util.PageUtil;
import com.hzbuvi.quiz.questionbank.entity.Category;
import com.hzbuvi.quiz.questionbank.entity.DeleteEnum;
import com.hzbuvi.quiz.questionbank.entity.Knowledge;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.quiz.questionbank.repository.CategoryRepository;
import com.hzbuvi.quiz.questionbank.repository.KnowledgeRepository;
import com.hzbuvi.quiz.questionbank.repository.QuestionBankRepository;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
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

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionBankRepository questionBankRepository;
	@Autowired
	private KnowledgeRepository knowledgeRepository;

    public static Integer defaultPageSize = 10;

//	private Map<String,Category>  categoryMap = new HashMap<>();


//	private synchronized void refresh(){
//		categoryMap.clear();
//		List<Category> categories = categoryRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
//		for (Category c : categories) {
//			categoryMap.put(c.getName(),c);
//		}
//	}


	public Integer find(String category){
		Integer id = null;
		List<Category> categories = categoryRepository.findByNameAndIsDelete(category,DeleteEnum.NOT_DELETE);
		if (null == categories || categories.size() < 1 ) {
			id = insert(category);
		} else {
			id = categories.get(0).getId();
		}
		return id;
	}


    public Integer insert(String categoryName){
//        Category  data=categoryRepository.findByName(categoryName).get(0);
//        DeleteEnum a=categoryRepository.findByName(categoryName).get(0).getIsDelete();
//        if((null!=data&&a==DeleteEnum.DELETED)){
//            data.setIsDelete(DeleteEnum.NOT_DELETE);
//            categoryRepository.save(data);
//        }if(null==data){
//            Category category=new Category(categoryName);
//            categoryRepository.save(category);
//            refresh();
//            return category.getId();
//        }
//        int id=categoryRepository.findByName(categoryName).get(0).getId();
        if(null==categoryRepository.findIdByCategoryName(categoryName)){
            Category category = new Category(categoryName);
            categoryRepository.save(category );
            return category.getId();
        }
		return null;
    }

    public String delete(Integer id){
//        Category category = categoryRepository.findOne(id);
		List<Questionbank> list = questionBankRepository.findByCategoryIdAndIsDelete(id,DeleteEnum.NOT_DELETE);
        if(null==list || list.size() == 0 ){
        	Category category = categoryRepository.findOne(id);
            category.setIsDelete(DeleteEnum.DELETED);
            categoryRepository.save(category);
			List<Knowledge> knowledges =  knowledgeRepository.findByCategoryId(id);
			knowledgeRepository.delete(knowledges);
            return "success";
        }
        return "failure";
    }
    public List<Category> showAll(){
        List<Category> list=categoryRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
        return list;
    }
    public Map<String,Object> updateLoad(Integer id){
        Category category=categoryRepository.findOne(id);
        List<Category> list=categoryRepository.findAll();
        Map<String,Object> map=new HashMap<>();
        map.put("name",category);
        map.put("all",list);
        return map;
    }
    public Category check(Integer id){
        Category category=categoryRepository.findOne(id);
        return category;
    }
    public String update(int oldId,String categoryName) {
        Category category=categoryRepository.findOne(oldId);
        category.setName(categoryName);
        categoryRepository.save(category);
        return "updateSuccess";
    }

    public  Map<String,Object> show(Map<String,String> parm) {
        String page = ValueUtil.coalesce( parm.get("page") , "0") ;
        String category= parm.get("category");
        Map<String,String> map1=new HashMap<>();
        if (ValueUtil.notEmpity(category) ) {
            map1.put("category",category);
        }
        Pageable pageable = new PageRequest(Integer.valueOf(page),defaultPageSize);
        Page<Category> categories = categoryRepository.findAll(getWhereClause(category), pageable);
        Map<String,Object> map = new HashMap<>();
        map.put("page",categories);
        map.put("condition",map1);
        return map;
    }
    private Specification<Category> getWhereClause(String category) {
        return new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(category) ) {
                    predicate.getExpressions().add( cb.equal(root.<Integer>get("id"),Integer.valueOf(category)) );
                }
                predicate.getExpressions().add( cb.equal(root.<Integer>get("isDelete"),0));
                return predicate;
            }
        };
    }

    public PageResult index(Integer page) {
        if(null==page){
            page=0;
        }
        Pageable pageable = new PageRequest(ValueUtil.coalesce(page,0),defaultPageSize);
        Page<Category> categories = categoryRepository.findByIsDelete(pageable,DeleteEnum.NOT_DELETE);
        PageResult result = PageUtil.toPage(categories,page);
        return result;
    }

}


