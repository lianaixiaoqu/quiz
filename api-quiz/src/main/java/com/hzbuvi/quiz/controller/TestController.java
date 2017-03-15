package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.backUser.service.BackGroundService;
import com.hzbuvi.quiz.config.ConstantData;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by light on 2016/11/14.
 */
@RestController
public class TestController {

    @Autowired
    private BackGroundService backGroundService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String uploadFile(HttpServletRequest request,HttpServletResponse response ) {
		response.setHeader("Access-Control-Allow-Origin", ConstantData.backendManageUrl);
        Cookie cookie  = new Cookie("key",String.valueOf(System.currentTimeMillis()));
        response.addCookie(cookie);
        return "OK";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(  HttpServletRequest request,HttpServletResponse response ){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("status","OK");
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }


	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public String login(String loginName, String password , HttpServletResponse response){
		try {
			String result = backGroundService.login(loginName,password);
			SessionData.login(response,loginName);
			return ValueUtil.toJson("backUser",backGroundService.login(loginName,password),"userName",loginName );
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),"");
		}
	}



}
