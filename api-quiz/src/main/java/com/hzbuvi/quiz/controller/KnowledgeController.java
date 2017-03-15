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
@RequestMapping("/knowledge")
public class KnowledgeController {
    @Autowired
    private KnowledgeService knowledgeService;
	@Autowired
	private CategoryService categoryService;
    @RequestMapping( method = RequestMethod.POST)
    public String insert(String knowledgeName,String categoryName, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		int cateid = categoryService.find(categoryName);
            return ValueUtil.toJson("knowledge",knowledgeService.find(knowledgeName,cateid,categoryName));
    }

    @RequestMapping(value = "/d/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Integer id , HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			e.printStackTrace();
		}
		try {
			return ValueUtil.toJson("delete",knowledgeService.delete(id));
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
	}
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, HttpServletRequest request, HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		Map<String,Object>  result = knowledgeService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("knowledge",pageModel);
    }
    @RequestMapping(value = "/showlines",method = RequestMethod.GET)
    public String updateLoad(Integer id, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response );
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organization",knowledgeService.updateLoad(id));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(int oldId,String knowledgeName,int categoryId , HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("knowledge",knowledgeService.update(oldId,knowledgeName,categoryId));
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Integer page, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("knowledge",knowledgeService.index(page));
    }

}

