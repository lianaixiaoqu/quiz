package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.config.PageModel;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.questionbank.entity.DifficultyEnum;
import com.hzbuvi.quiz.questionbank.entity.Questionbank;
import com.hzbuvi.quiz.questionbank.service.QuestionBankservice;
import com.hzbuvi.quiz.questionbank.service.QuestionExtract;
import com.hzbuvi.quiz.questionbank.service.QuestionImport;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 2016/10/18.
 */

@RestController
@RequestMapping("/questionbank")
public class QuestionBankController {

    @Autowired
    private QuestionBankservice questionBankservice;
	@Autowired
	private QuestionImport questionImport;
    @Autowired
    private QuestionExtract questionExtract;

    @RequestMapping( value="/item",method = RequestMethod.GET)
    public String insert( HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response );
			return ValueUtil.toJson("categories",questionBankservice.getCategory(),"knowledge",questionBankservice.getKnowledge());
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
    @RequestMapping( value="/category",method = RequestMethod.POST)
    public String insertSave(Questionbank questionbank, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("questionbank",questionBankservice.insertSave(questionbank));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
//		return ValueUtil.toJson("questionbank",questionBankservice.insertSave(questionbank));
    }
    @RequestMapping(value = "/d/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Integer id , HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("delete",questionBankservice.delete(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),ValueUtil.coalesce(e.getMsg(),""));
		}
    }
    @RequestMapping(value = "/showlines",method = RequestMethod.GET)
    public String updateLoad(Integer id, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("questionbank",questionBankservice.updateLoad(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, HttpServletRequest request, HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			Map<String,Object>  result = questionBankservice.show(parm);
			PageModel pageModel = new PageModel((Page) result.get("page"));
			pageModel.setCondition(result.get("condition"));
			return  ValueUtil.toJson("questionbank",pageModel);
		} catch (HzbuviException e) {
			return ValueUtil.toJson(e.getCode(),e.getMsg());
		}
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Questionbank questionbank , HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return  ValueUtil.toJson("questionbank",questionBankservice.update(questionbank));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}

    }
    @RequestMapping(value = "/answer",method = RequestMethod.GET)
        public String getAnswer(Integer id, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return  ValueUtil.toJson("questionbank",questionBankservice.getAnswer(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Integer page, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return  ValueUtil.toJson("questionbank",questionBankservice.index(page));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public String importExcel(String excelFilePath, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("import",questionImport.importFromExcel(excelFilePath));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }

    @RequestMapping(value = "/extract",method = RequestMethod.GET)
    public String extract(List<Integer> categorys, List<Integer> knowledges, DifficultyEnum easy, int pcs, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("import",questionExtract.extract(categorys,knowledges,easy,pcs));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
	}
}
