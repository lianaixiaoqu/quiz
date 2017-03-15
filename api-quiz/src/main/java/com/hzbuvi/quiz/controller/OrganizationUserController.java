package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.config.PageModel;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.organization.entity.OrganizationUser;
import com.hzbuvi.quiz.organization.repository.OrganizationUserRepository;
import com.hzbuvi.quiz.organization.service.OrganizationUserService;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangDianDian on 2016/10/27.
 */
@RestController
@RequestMapping("/organizationUser")
public class OrganizationUserController {
    @Autowired
    private OrganizationUserService organizationUserService;
    @Autowired
    OrganizationUserRepository organizationUserRepository;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public String create(OrganizationUser organizationUser, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organizationUser",organizationUserService.addOrganization(organizationUser));
    }
//    @RequestMapping(value = "/checkCreate",method = RequestMethod.GET)
//    public String checkCreate(String jobNumber){
//        Map<String,String> map=new HashMap<>();
//        OrganizationUser user= organizationUserRepository.findByJobNumber(jobNumber);
//        if(ValueUtil.notEmpity(user)){
//            map.put("status","1");
//        }
//        return ValueUtil.toJson("organizationUser",map);
//    }
    @RequestMapping(value = "/d/{id}",method = RequestMethod.POST)
    public String destroy(@PathVariable("id") Integer id, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("organizationUser",organizationUserService.delete(id));
    }
     @RequestMapping(value = "/checkupdate",method = RequestMethod.GET)
    public String checkupdateSave(String jobNumber, HttpServletRequest request,HttpServletResponse response){
		 try {
			 SessionData.verify(request,response);
		 } catch (HzbuviException e) {
			 return ValueUtil.toError(e.getCode(),e.getMsg());
		 }
		 Map<String,String> map=new HashMap<>();
        OrganizationUser user= organizationUserRepository.findByJobNumber(jobNumber);
        if(ValueUtil.notEmpity(user)){
            map.put("status","1");
        }
        return ValueUtil.toJson("organizationUser",map);
    }
    @RequestMapping(value = "/showlines",method = RequestMethod.GET)
    public String updateLoad(Integer id, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organizationUser",organizationUserService.updateLoad(id));
    }
    @RequestMapping(value = "/showall",method = RequestMethod.GET)
    public String showall( HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organizationUser",organizationUserService.showall());
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String updateSave(OrganizationUser organizationUser, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return ValueUtil.toJson("organizationUser",organizationUserService.updateSave(organizationUser));
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, HttpServletRequest request, HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		Map<String,Object>  result = organizationUserService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("organizationUser",pageModel);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String searchAll (Integer pageNumber, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("organizationUser",organizationUserService.searchAll(pageNumber));
    }
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String inputFromExcel ( String excelFilePath , HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("organizationUser",organizationUserService.inputFromExcel(excelFilePath));
    }
}


