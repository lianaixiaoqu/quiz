package com.hzbuvi.quiz.config;

import com.hzbuvi.util.exception.HzbuviException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by WANG, RUIQING on 10/14/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class SessionData {



	public static String verify(HttpServletRequest request,HttpServletResponse response) throws HzbuviException {
		response.setHeader("Access-Control-Allow-Origin", ConstantData.backendManageUrl);
		Cookie[] cookies = request.getCookies();
		String userCode = "";
		if ( null != cookies ) {
			for (int i = 0; i < cookies.length ; i++) {
				switch (cookies[i].getName()) {
					case "uid" :
						userCode = cookies[i].getValue();
						System.out.println(System.currentTimeMillis()+":uid:"+userCode);
						break;
				}
			}
			return userCode;
		} else {
			System.out.println(System.currentTimeMillis()+":notLogin:");
			HzbuviException exception = new HzbuviException("notLogin");
			exception.setMsg("请重新登录");
			throw exception;
		}
	}

	public static  void login(HttpServletResponse response ,String userCode){
        response.setHeader("Access-Control-Allow-Origin", ConstantData.backendManageUrl);
		Cookie user = new Cookie("uid",userCode);
		System.out.println(System.currentTimeMillis()+":login:"+userCode);
		response.addCookie(user);
	}
}
