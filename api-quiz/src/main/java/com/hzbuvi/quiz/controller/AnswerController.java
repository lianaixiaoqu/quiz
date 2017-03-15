//package com.hzbuvi.quiz.controller;
//
//import com.hzbuvi.quiz.answer.service.AnswerService;
//import com.hzbuvi.quiz.config.SessionData;
//import com.hzbuvi.util.basic.ValueUtil;
//import com.hzbuvi.util.exception.HzbuviException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpSession;
//
///**
// * Created by root on 2016/10/18.
// */
//@RestController
//@RequestMapping("/answer")
//public class AnswerController {
//    @Autowired
//    AnswerService answerService;
//    @RequestMapping(method = RequestMethod.GET)
//    public String demo(){
//        return ValueUtil.toJson("answer",answerService.getanswer());
//    }
//}
