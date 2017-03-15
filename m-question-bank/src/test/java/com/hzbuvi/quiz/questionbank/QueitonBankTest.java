package com.hzbuvi.quiz.questionbank;


import com.hzbuvi.quiz.questionbank.entity.DifficultyEnum;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.quiz.questionbank.entity.TypeEnum;
import com.hzbuvi.util.basic.MapUtil;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.date.DateUtil;
import com.hzbuvi.util.encryption.SHA;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;


/**
 * Created by root on 2016/10/18.
 */
public class QueitonBankTest {


	@Test
	public void typetest(){
		System.out.println(TypeEnum.getType("单选题"));
		System.out.println(TypeEnum.getType("多选题"));

		System.out.println(DifficultyEnum.getDifficulty("易"));

	}

	@Test
	public void test(){
		String correct = "ABCD".toLowerCase();
		System.out.println(correct.length());
		for (int i = 0; i < correct.length(); i++) {
			System.out.println(correct.charAt(i) );
			System.out.println(correct.contains("c"));
		}

		System.out.println(this.getClass().getName());
	}

	@Test
	public void digital(){

		List<String> list = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			list.add(String.valueOf(i));
		}

		System.out.println(ValueUtil.toJson(list));

		System.out.println(ValueUtil.toJson(list.subList(3,5)));

	}

	@Test
	public void timetest(){
		System.out.println(SHA.e512("123456"));

	}



	@Test
	public  void substrin(){
//		Questionbank questionbank = new Questionbank();
//		questionbank.setQuestionContent("jfkdajfk");
//		questionbank.setA("jfdk");

		Map<String,Object> map =  new HashMap<>();
		map.put("id",34);
		map.put("questionContent","jfkdasjfk");

		Questionbank q = (Questionbank) MapUtil.mapToObject(map,Questionbank.class);

		System.out.println(q);


	}

	@Test
	public void timetestfdkfsf(){
		Double d1 = Double.valueOf(30.0);
		Double d2 = Double.valueOf(26.09);

		if (  d2 - d1  > 0) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}

	}




}
