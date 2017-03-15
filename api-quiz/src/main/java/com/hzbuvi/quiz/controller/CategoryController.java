package com.hzbuvi.quiz.controller;


import com.hzbuvi.quiz.config.PageModel;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.questionbank.service.CategoryService;
import com.hzbuvi.quiz.questionbank.service.KnowledgeService;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by WangDianDian on 2016/10/24.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
	@Autowired
	private KnowledgeService knowledgeService;


    @RequestMapping( method = RequestMethod.POST)
    public String insert(String categoryName,HttpServletRequest session,HttpServletResponse response){
		try {
			SessionData.verify(session,response);
			return ValueUtil.toJson("category",categoryService.insert(categoryName));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm,HttpServletRequest session,HttpServletResponse response){
		try {
			SessionData.verify(session,response);
			Map<String,Object>  result = categoryService.show(parm);
			PageModel pageModel = new PageModel((Page) result.get("page"));
			pageModel.setCondition(result.get("condition"));
			return  ValueUtil.toJson("category",pageModel);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
    @RequestMapping(value = "/d/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Integer id ,HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("delete",categoryService.delete(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
    @RequestMapping(value = "/showAll", method = RequestMethod.GET)
    public String showAll(HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("all",categoryService.showAll());
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }

    @RequestMapping(value = "/showlines",method = RequestMethod.GET)
    public String updateLoad(Integer id,HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("id",categoryService.updateLoad(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public String check(Integer id,HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return ValueUtil.toJson("category",categoryService.check(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Integer oldId,String categoryName,HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			String result = categoryService.update(oldId,categoryName);
			knowledgeService.updateCategoryName(oldId,categoryName);
			return  ValueUtil.toJson("category", result);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Integer page,HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
			return  ValueUtil.toJson("category",categoryService.index(page));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
    }
}
