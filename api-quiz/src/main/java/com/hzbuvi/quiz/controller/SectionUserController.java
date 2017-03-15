package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.config.PageModel;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.organization.service.OrganizationUserService;
import com.hzbuvi.quiz.usersection.UserSecionBiz;
import com.hzbuvi.quiz.usersection.entity.UserSection;
import com.hzbuvi.quiz.usersection.repository.UserSectionDao;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by WangDianDian on 2016/10/28.
 */
@RestController
@RequestMapping("/usersection")
public class SectionUserController {
    @Autowired
    private UserSecionBiz userSecionBiz;
	@Autowired
	private OrganizationUserService organizationUserService;
	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private UserSectionDao userSectionDao;

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, Integer activityId,HttpServletRequest request, HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		if ( null == activityId  ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}
		Map<String,Object>  result = userSecionBiz.show(parm,activityId);
//        PageModel pageModel = new PageModel((Page) result.get("page"));
		String page = ValueUtil.coalesce(parm.get("page"), "0");
		int currentPage = Integer.valueOf(page);
        PageModel pageModel = new PageModel(currentPage,10);

		List<UserSection> userSecionList = (List<UserSection>) result.get("page");
		List<Integer> userIdList = new ArrayList<>();
		userSecionList.forEach( u -> userIdList.add(u.getUserId()) );
		Map<Integer,String> jobNumbers = organizationUserService.getJobNumber(userIdList);
		List<UserSection> distincted = new ArrayList<>();
		Map<Integer,Integer>  userFlag = new HashMap<>();
		for (int i = 0; i < userSecionList.size(); i++) {
			userSecionList.get(i).setJobNumber( jobNumbers.get(userSecionList.get(i).getUserId() ) );
			if (null == userFlag.get(userSecionList.get(i).getUserId())) {
				distincted.add(userSecionList.get(i));
				userFlag.put(userSecionList.get(i).getUserId(),distincted.size()-1);
			} else {
				distincted.get(userFlag.get(userSecionList.get(i).getUserId())).calculate(userSecionList.get(i));
			}
		}
		int length = 0;
		if ( distincted.size() - (currentPage*10) > 10 ){
			length = (currentPage * 10 ) + 10;
		} else {
			length = distincted.size();
		}
		pageModel.setTotalRows(distincted.size());
		List<UserSection> content = distincted.subList(currentPage*10,length);
		content.forEach( c -> {c.outputCheck();}  );
		pageModel.setContent( content );
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("userSection",pageModel);
    }
}
