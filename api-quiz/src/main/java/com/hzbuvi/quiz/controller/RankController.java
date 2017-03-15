package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.organization.entity.Organization;
import com.hzbuvi.quiz.organization.entity.OrganizationUser;
import com.hzbuvi.quiz.organization.repository.OrganizationRepository;
import com.hzbuvi.quiz.organization.service.OrganizationUserService;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hzbuvi.quiz.usersection.UserSecionBiz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/31/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/rank")
public class RankController {

	@Autowired
	private UserSecionBiz userSecionBiz;
	@Autowired
	private OrganizationUserService organizationUserService;
	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private OrganizationRepository organizationDao;

	@RequestMapping(value = "/person", method = RequestMethod.GET)
	public String personRank(String userId,Integer activityId,Integer page){
		if (null == userId || "".equals( userId ) || "undefined".equals( userId ) ) {
			return ValueUtil.toError("notLogin","未找到员工工号");
		}
		if ( null == activityId || 1==activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}

		Integer ouserId = organizationUserService.findIdByJobNumber(userId);
		Map<String,Object>  map = userSecionBiz.personRank(ouserId,activityId,page);
		return ValueUtil.toJson("rank", map.get("rank"),"beat",map.get("beat"),"selfRank",map.get("selfRank") );
	}


	@RequestMapping(value = "/personSituation", method = RequestMethod.GET)
	public String personRankSituation(Integer activityId, Integer page, HttpServletRequest request, HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}

		if ( null == activityId || 1==activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}

		Map<String,Object>  map = userSecionBiz.personRankSituation(activityId,page);
		return ValueUtil.toJson("rank", map.get("rank"),"beat",map.get("beat") );
	}

	@RequestMapping(value = "/department", method = RequestMethod.GET)
	public String departmentRank(String userId,Integer activityId,Integer page){
		if (null == userId || "".equals( userId ) || "undefined".equals( userId ) ) {
			return ValueUtil.toError("notLogin","未找到员工工号");
		}
		if ( null == activityId || 1==activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}
		Integer ouserId = organizationUserService.findIdByJobNumber(userId);


		OrganizationUser organizationUser=organizationUserService.findByJobNumber(userId);
		Integer organizationId=null;
		if (null!=organizationUser) {
			organizationId = organizationUser.getOrganizationId();//组织Id
			ouserId =  organizationUser.getId();
		}
		Integer depId=0;
		if ( null != organizationId) {
			Organization tempOrg = organizationDao.findByOrganizationId(organizationId);
			Integer level= tempOrg.getOrganizationLevel();//父Id  7部门或8科室level

			String buMenName="";
			Integer buMenNumber=0;
			Integer departmentId=0;

			if(level==7){
				Organization o1 = organizationDao.findByOrganizationId(organizationId);
				if ( null!= o1) {
					buMenName = o1.getOrganizationName();//部门
					buMenNumber = organizationId;//部门id
					Organization o2 = organizationDao.findByOrganizationId(organizationId);
					if (null != o2) {
						Integer zhongxinid= o2.getParentId();//部门上级 本部id或中心id
						Organization o3 = organizationDao.findByOrganizationId(zhongxinid);
						if (null != o3 ) {
							Integer zhonglevel= o3.getOrganizationLevel();//本部或中心level
							if(zhonglevel==4){
								departmentId =zhongxinid;// 中心id
							} else{//5本部
								Organization o5 = organizationDao.findByOrganizationId(zhongxinid);
								if (null != o5) {
									departmentId = o5.getParentId();//中心id
									Organization o6 = organizationDao.findByOrganizationId(departmentId);
								}
							}
						}
					}
				}
			} else if (level == 4)  {
			departmentId = tempOrg.getOrganizationId();
			} else {//8科室
				Organization o11 = organizationDao.findByOrganizationId(organizationId) ;
				if (null != o11) {
					buMenNumber = o11.getParentId();//父Id 部门
					Organization o12 = organizationDao.findByOrganizationId(buMenNumber);
					if (null != o12) {
						buMenName = o12.getOrganizationName();
						Organization o13 = organizationDao.findByOrganizationId(buMenNumber);
						if (null != o13) {
							Integer zhongxinId = o13.getParentId();//本部id或 中心id
							Organization o14 = organizationDao.findByOrganizationId(zhongxinId);
							if (null != o14) {
								Integer organizationLevel = o14.getOrganizationLevel();
								if (organizationLevel == 4) {//中心
									departmentId = zhongxinId;//本部id或 中心id
									Organization o15 = organizationDao.findByOrganizationId(departmentId);
								} else {//5本部
									Organization o16 = organizationDao.findByOrganizationId(zhongxinId);
									if (null !=o16) {
										departmentId = o16.getParentId();//中心
										Organization o17 = organizationDao.findByOrganizationId(departmentId);
									}
								}
							}
						}
					}
				}
			}
		depId=departmentId;
		}
		Map<String,Object>  map = userSecionBiz.departmentRank(ouserId,activityId,page,depId);
//		return ValueUtil.toJson("rank",userSecionBiz.departmentRank(userId,activityId,page));
		return ValueUtil.toJson("rank", map.get("rank"),"beat",map.get("beat"), "selfRank",map.get("selfRank") );
	}


	@RequestMapping(value = "/departmentSituation", method = RequestMethod.GET)
	public String departmentRankSituation(Integer page, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		Integer activityId=null;
		try {
			activityId = activityBiz.currentActivityId();
		} catch (Exception e) {
			return ValueUtil.toError("noCurrentActivity",e.getMessage());
		}
//		Map<String,Object>  map = userSecionBiz.departmentRankSituation(activityId,page);
		Map<String,Object>  map = userSecionBiz.departmentRank(0,activityId,page,0);
		return ValueUtil.toJson("rank", map.get("rank"),"beat",map.get("beat") );
	}


}
