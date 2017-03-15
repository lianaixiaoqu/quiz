package com.hzbuvi.quiz.questionbank.entity;

import java.util.HashMap;
/**
 * Created by WangDianDian on 2016/10/24.
 */
public enum DeleteEnum {

    NOT_DELETE(0)
    ,DELETED(1);

    private int value;
    DeleteEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, DeleteEnum> map = new HashMap<>();

    static {
        for ( DeleteEnum deleteEnum: DeleteEnum.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue(){
        return value;
    }

    public static DeleteEnum getDeleteName(int value) {
        return map.get(value);
    }

}


