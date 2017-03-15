package com.hzbuvi.quiz.questionbank.entity;

import java.util.HashMap;

/**
 * Created by Downey.hz on 2016/10/11..
 */
public enum DifficultyEnum {
    Difficult("难")
    ,Normal ("中")
    ,Easy("易");

    private String value;
    DifficultyEnum(String v) {
        value = v;
    }

    private static HashMap<String,DifficultyEnum> map = new HashMap<>();
	private static HashMap<String,DifficultyEnum> chineseMap = new HashMap<>();

    static {
        for (DifficultyEnum difficultyEnum: DifficultyEnum.values()) {
            map.put(difficultyEnum.name(),difficultyEnum);
			map.put(difficultyEnum.getValue(),difficultyEnum);
        }
    }

    public String getValue(){
        return  value;
    }

    public static DifficultyEnum getDifficulty(String value) {
        if (null != map.get(value) ) {
			return map.get(value);
		} else if ( null != chineseMap.get(value)) {
			return chineseMap.get(value);
		} else {
			return null;
		}
    }
}
