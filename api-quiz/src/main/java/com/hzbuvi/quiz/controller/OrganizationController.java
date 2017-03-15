package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.config.PageModel;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.organization.entity.Organization;
import com.hzbuvi.quiz.organization.service.OrganizationService;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by light on 2016/10/24.
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public String create(Organization organization, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organization",organizationService.addOrganization(organization));
    }

    @RequestMapping(value = "/d/{organizationId}",method = RequestMethod.POST)
    public String destroy(@PathVariable("organizationId") Integer organizationId, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("organization",organizationService.delete(organizationId));
    }

    @RequestMapping(value = "/showlines",method = RequestMethod.GET)
    public String updateLoad(Integer id, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organization",organizationService.updateLoad(id));
    }
    @RequestMapping(value = "/showAll",method = RequestMethod.GET)
    public String show( HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organization",organizationService.showall());
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String updateSave(Organization organization, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organization",organizationService.updateSave(organization));
    }
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String searchAll (Integer pageNumber, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("organization",organizationService.searchAll(pageNumber));
    }
    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public String importExcel(String excelFilePath, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("organization",organizationService.inputOrganizationFromExcel(excelFilePath));
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showPage(@RequestParam Map<String,String> parm, HttpServletRequest request, HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		Map<String,Object>  result = organizationService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
		return  ValueUtil.toJson("organization",pageModel);

    }
}
