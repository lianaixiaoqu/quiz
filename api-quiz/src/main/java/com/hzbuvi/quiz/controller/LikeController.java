package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.like.entity.LikeType;
import com.hzbuvi.quiz.like.service.LikeService;
import com.hzbuvi.quiz.organization.service.OrganizationUserService;
import com.hzbuvi.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by WangDianDian on 2016/10/13.
 */
@RestController
@RequestMapping("/ranklike")
public class LikeController {
    @Autowired
    private LikeService likeService;
	@Autowired
	private OrganizationUserService organizationUserService;
	@Autowired
	private ActivityBiz activityBiz;

    @RequestMapping(method = RequestMethod.POST)
    public  String insert( Integer activityId,String userId,Integer targetId ,Integer likeType ) {

		if (null == activityId || 1 == activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}

		if (null == userId || "".equals( userId ) || "undefined".equals( userId ) ) {
			return ValueUtil.toError("notLogin","未找到员工工号");
		}

		Integer ouserId = organizationUserService.findIdByJobNumber(userId);
		if ( 0==likeType) {
			return ValueUtil.toJson("ranklike", likeService.insert(activityId, ouserId, targetId, LikeType.person));
		} else {
			return ValueUtil.toJson("ranklike", likeService.insert(activityId, ouserId, targetId, LikeType.department));
		}
    }
}
