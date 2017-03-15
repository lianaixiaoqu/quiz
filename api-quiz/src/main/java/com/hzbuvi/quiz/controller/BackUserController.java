package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.backUser.service.BackGroundService;
import com.hzbuvi.quiz.config.ConstantData;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by WangDianDian on 2016/10/28.
 */
@RestController
@RequestMapping("/backUser")
public class BackUserController {
    @Autowired
    private BackGroundService backGroundService;

    @RequestMapping(value = "/cookie",method = RequestMethod.GET)
    public String testCookie( HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        return "test";
    }


    @RequestMapping(value = "/showAll",method = RequestMethod.GET)
    public String getAll( HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request, response );
			return ValueUtil.toJson("backUser",backGroundService.getAll());
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
    @RequestMapping(value = "/d/{id}",method = RequestMethod.POST)
    public String delete(@PathVariable("id") Integer id,HttpServletRequest session,HttpServletResponse response){
		try {
			SessionData.verify(session,response);
			return ValueUtil.toJson("backUser",backGroundService.delete(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public String register(String loginName,String password,String passwordReal , HttpServletRequest session,HttpServletResponse response){
        try {
			SessionData.verify(session,response);
            return ValueUtil.toJson("backUser",backGroundService.register(loginName,password,passwordReal) );
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
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
    @RequestMapping(value = "/updateLoad",method = RequestMethod.GET)
    public String get(Integer id,HttpServletRequest session,HttpServletResponse response){
		try {
			SessionData.verify(session,response);
			return ValueUtil.toJson("backUser",backGroundService.get(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
}
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String update(Integer id,String password,String newPassword,HttpServletRequest session,HttpServletResponse response){
        try {
			SessionData.verify(session,response);
            return ValueUtil.toJson("backUser",backGroundService.changePassword(id, password, newPassword));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

}
