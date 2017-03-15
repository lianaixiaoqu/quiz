package com.hzbuvi.quiz.usersection.bean;


import com.hzbuvi.util.basic.ValueUtil;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/9/16.
 * twitter: @taylorwang789
 * e-mail: i@wrqzn.com
 */
public class OrderBy{


//    @Test
//    public void orderBy() {
//        System.out.println("");
//        List<Map<String, Object>> data = new ArrayList<>();
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("name","tom");
//        map1.put("score", 200);
//        map1.put("star", 29);
//        map1.put("cnt", 5);
//
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("name","jerry");
//        map2.put("score", 180);
//        map2.put("star", 30);
//        map2.put("cnt", 2);
//
//        Map<String, Object> map3 = new HashMap<>();
//        map3.put("name","jack");
//        map3.put("score", 200);
//        map3.put("star", 29);
//        map3.put("cnt", 2);
//
//
//        Map<String, Object> map4 = new HashMap<>();
//        map4.put("name","dog");
//        map4.put("score", 180);
//        map4.put("star", 39);
//        map4.put("cnt", 2);
//
//        data.add(map3);
//        data.add(map4);
//        data.add(map2);
//        data.add(map1);
//
//        System.out.println(ValueUtil.toJson(data));
//
//        String orderBy = "star,score,cnt";
//        String desc = "1,1,0";
//
//        sort(data, orderBy, desc);
//
//
//    }


    public static List<Map<String,Object>>  sort(List<Map<String, Object>> data, String orderBy, String desc) {
        String[] orderBys = orderBy.split(",");
        String[] descs = desc.split(",");
        List<Map<String, Object>> order = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            int k = order.size();

            for (int j = -1; j < k; j++) {
                Map<String, Object> before = null;
                if (j >= 0) {
                    before = order.get(j);
                }
                Map<String, Object> after = null;
                if ((j + 1) < k) {
                    after = order.get(j + 1);
                }
                if (sortBy(data.get(i), before, after, orderBys, descs, 0)) {
                    order.add(j + 1, data.get(i));
//                    System.out.println("order:" + ValueUtil.toJson(order));
					System.out.println("before:"+ ValueUtil.toJson(before));
					System.out.println("after:"+ ValueUtil.toJson(after));
					System.out.println("insert:"+j+1+":"+ValueUtil.toJson(data.get(i)));
					break;
                }
            }

        }
        return  order;
    }


    private static boolean sortBy(Map<String, Object> src, Map<String, Object> before, Map<String, Object> after, String[] orderBy, String[] desc, int start) {
        // start = 0
        // desc 0:asc  1:desc
        if (null == after) {
            return true;
        } else if (null == before) {

            System.out.println(src.get(orderBy[start]) + "::" + after.get(orderBy[start]));

            switch (desc[start]) {
                case "0":
//                    if (src.get(orderBy[start]).toString().compareTo(after.get(orderBy[start]).toString()) < 0) {
					if ( Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(after.get(orderBy[start]).toString()) < 0.0 ) {
						System.out.println("1");
						return true;
//                    } else if ( src.get(orderBy[start]).toString().compareTo(after.get(orderBy[start]).toString()) == 0 && ++start < orderBy.length) {
					} else if ( Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(after.get(orderBy[start]).toString()) == 0.0 && ++start < orderBy.length) {
						System.out.println("2");
                        return sortBy(src, before, after, orderBy, desc, start);
                    }
                    break;
                case "1":
//                    if (src.get(orderBy[start]).toString().compareTo(after.get(orderBy[start]).toString()) > 0) {
					if ( Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(after.get(orderBy[start]).toString())  > 0.0 ) {
						System.out.println("3");
                        return true;
//                    } else if (src.get(orderBy[start]).toString().compareTo(after.get(orderBy[start]).toString()) == 0 && ++start < orderBy.length) {
					} else if (Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(after.get(orderBy[start]).toString()) == 0.0 && ++start < orderBy.length) {
						System.out.println("4");
                        return sortBy(src, before, after, orderBy, desc, start);
                    }
                    break;

            }


        } else {

            System.out.println(src.get(orderBy[start]) + ":" + before.get(orderBy[start]) + ":" + after.get(orderBy[start]));

            switch (desc[start]) {
                case "0":
//                    if (src.get(orderBy[start]).toString().compareTo(before.get(orderBy[start]).toString()) > 0 &&
//                            src.get(orderBy[start]).toString().compareTo(after.get(orderBy[start]).toString()) < 0) {
					if ( Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(before.get(orderBy[start]).toString()) > 0.0 &&
						Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(after.get(orderBy[start]).toString()) < 0.0) {
						System.out.println("5");
                        return true;
                    } else if (++start < orderBy.length) {
						System.out.println("6");
                        return sortBy(src, before, after, orderBy, desc, start);
//                    } else if (src.get(orderBy[--start]).toString().compareTo(before.get(orderBy[start]).toString()) == 0) {
					} else if (Double.valueOf(src.get(orderBy[--start]).toString()) - Double.valueOf(before.get(orderBy[start]).toString()) == 0.0) {
						System.out.println("7");
                        return true;
                    }
                    break;
                case "1":
//                    if (src.get(orderBy[start]).toString().compareTo(before.get(orderBy[start]).toString()) < 0 &&
//                            src.get(orderBy[start]).toString().compareTo(after.get(orderBy[start]).toString()) > 0) {
					if (Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(before.get(orderBy[start]).toString()) <= 0.0 &&
							Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(after.get(orderBy[start]).toString()) > 0.0) {
						System.out.println("8");
                        return true;
                    } else if( Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(after.get(orderBy[start]).toString()) > 0.0) {
						return true;
					} else if (Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(before.get(orderBy[start]).toString()) > 0.0 ||
									Double.valueOf(src.get(orderBy[start]).toString()) - Double.valueOf(after.get(orderBy[start]).toString()) < 0.0) {
						return false;
					} else if (++start < orderBy.length) {
						System.out.println("9");
                        return sortBy(src, before, after, orderBy, desc, start);
//                    } else if (src.get(orderBy[--start]).toString().compareTo(before.get(orderBy[start]).toString()) == 0) {
					} else if (Double.valueOf(src.get(orderBy[--start]).toString()) - Double.valueOf(before.get(orderBy[start]).toString()) == 0.0) {
						System.out.println("10");
                        return true;
                    }
                    break;

            }
        }

		System.out.println("11");
        return false;

    }



}
