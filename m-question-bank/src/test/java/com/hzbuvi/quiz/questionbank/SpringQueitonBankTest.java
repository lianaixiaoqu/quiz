package com.hzbuvi.quiz.questionbank;


import com.hzbuvi.quiz.questionbank.entity.DifficultyEnum;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.quiz.questionbank.entity.TypeEnum;
import com.hzbuvi.quiz.questionbank.repository.QuestionBankRepository;
import com.hzbuvi.quiz.questionbank.service.QuestionImport;
import com.hzbuvi.util.basic.ValueUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2016/10/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ActivityConfig.class})
public class SpringQueitonBankTest {

	@Autowired
	private QuestionImport questionImport;
	@Autowired
	private QuestionBankRepository questionBankRepository;
	@PersistenceContext
	private EntityManager entityManager;


	@Test
	public void test(){
		String path = "/Users/taylor/Downloads/tmb1.xls";

//		questionImport.importFromExcel(path);
	}

	@Test
	public void category(){
		List<String> list =new ArrayList<String>();
		for(int i=1;i<8;i++){
			list.add("你说"+i+"我拍"+i+"");
		}
		for(int j=4;j<6;j++){
			list.remove(j);
		}
		System.out.print(list);
	}


	@Test
	public void counttest(){
		Query q = entityManager.createNativeQuery("SELECT id ,questionContent ,answerContent from questionbank ");
		List<Object[]> result = q.getResultList();
		for (int i = 0; i <  result.size() ; i++) {

			System.out.println(result.get(i)[0]+":"+result.get(i)[1]);

		}
		System.out.println( ValueUtil.toJson( result ) );
	}

	@Test
	public  void page() {

		Pageable pageable = new PageRequest(0,10);
		Page<Questionbank> questionbankPage = questionBankRepository.findAll(getWhereClause(null,null), pageable);

//		System.out.println(ValueUtil.toJson(questionbankPage));


	}

	private Specification<Questionbank> getWhereClause(Integer category,Integer knowledge) {
		return new Specification<Questionbank>() {
			@Override
			public Predicate toPredicate(Root<Questionbank> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if (null != category ) {
					predicate.getExpressions().add( cb.equal(root.<Integer>get("categoryId"),category) );
				}
				if (null!=knowledge) {
					predicate.getExpressions().add( cb.equal(root.<Integer>get("knowledgeId"),knowledge) );
				}
				return predicate;
			}
		};
	}

}
