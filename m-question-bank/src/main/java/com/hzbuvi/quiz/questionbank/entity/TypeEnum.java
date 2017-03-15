package com.hzbuvi.quiz.questionbank.entity;

import java.util.HashMap;

/**
 * Created by Downey.hz on 2016/10/11..
 */
public enum TypeEnum {

	SingleSelection ("单选题")
    ,MultiSelect("多选题") ;

	private String value;

	 private static HashMap<String,TypeEnum> map = new HashMap<>();

    static {
        for (TypeEnum typeEnum: TypeEnum.values()) {
            map.put(typeEnum.getValue(),typeEnum);
        }
    }


	TypeEnum(String s) {
		this.value = s;
	}

	public String getValue(){
        return  value;
    }

    public static TypeEnum getType(String value) {
        return  map.get(value);
    }
}
