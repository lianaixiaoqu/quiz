package com.hzbuvi.quiz.activity.service;

/**
 * Created by light on 2016/11/4.
 */
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/9/16.
 * twitter: @taylorwang789
 * e-mail: i@wrqzn.com
 */
@Service
public class Check {
//    public static void main(String[] args) {
//        int[] rate = {3, 4, 3};
//        division(rate, 100);
//    }
//    public void getCheck() {
//        int[] rate = {3, 4, 3};
//        division(rate, 100);
//    }
//    public double check(List<Double> result, int[] rate) {
//        return result.get(0) * rate[0] +
//                result.get(1) * rate[1] +
//                result.get(2) * rate[2];
//    }
    public Map<Integer,List> division(int difficult, int normal, int easy){
        int[] rate=new int[3];
        rate[0]=difficult;
        rate[1]=normal;
        rate[2]=easy;
        int totalScore=100;
        List<List<Integer>> results ;
        int[] maxScore = new int[rate.length];

        for (int i = 0; i < maxScore.length; i++) {
            maxScore[i] = totalScore / rate[i];
        }
        results = putNumber(maxScore, rate, totalScore);
//        for (int i = 0; i < results.size(); i++) {
//            System.out.println(i + ":" + results.get(i));
//        }
        Map<Integer,List> map=new HashMap();
        for (int i = 0; i < results.size(); i++) {
            map.put(i,results.get(i));
        }
        return map;
    }
    private  List<List<Integer>> putNumber(int[] maxScore, int[] rate, int totalScore) {

        List<List<Integer>> results = new ArrayList<>();

        for (int i = 0; i < maxScore[0]; i++) {

            for (int j = 0; j < maxScore[1]; j++) {

                for (int k = 0; k < maxScore[2]; k++) {
                    List<Integer> result = new ArrayList<>();
                    result.add(i);
                    result.add(j);
                    double rst = calcLast(i, j, rate, totalScore);
                    if ( rst > 0 && (rst % 1 == 0)  && i > 0 && j > 0 &&  i < j && j < rst ) {
                        result.add((int) rst);
                        results.add(result);
                        break;
                    }
                }

            }
        }
        return results;
    }

    private  Double calcLast(int i, int j, int[] rate, int totalScore) {

        double rst = (totalScore - rate[0] * i - rate[1] * j) / Double.valueOf(rate[2]);
        return rst;
    }
}

