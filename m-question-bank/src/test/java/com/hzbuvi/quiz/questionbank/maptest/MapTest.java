package com.hzbuvi.quiz.questionbank.maptest;

import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.util.basic.MapUtil;
import com.hzbuvi.util.basic.ValueUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class MapTest {

	@Test
	public void test(){
		Map<String,Object> map = new HashMap<>();
		map.put("name","tom");
		map.put("personCode","jkfdjafk");

		TestBean testBean = (TestBean) MapUtil.mapToObject(map,TestBean.class);

		System.out.println(ValueUtil.toJson(testBean));

	}

}
