package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.activity.repository.ActivityRepository;
import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.chapter.repository.ChapterRepository;
import com.hzbuvi.quiz.organization.repository.OrganizationUserRepository;
import com.hzbuvi.quiz.organization.service.OrganizationUserService;
import com.hzbuvi.quiz.section.repository.SectionRepository;
import com.hzbuvi.quiz.section.service.SectionBiz;
import com.hzbuvi.quiz.usersection.UserSecionBiz;
import com.hzbuvi.quiz.usersection.repository.UserSectionDao;
import com.hzbuvi.user.center.biz.PersonBiz;
import com.hzbuvi.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by WANG, RUIQING on 10/14/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonBiz personBiz;
	@Autowired
	private SectionBiz sectionBiz;

	@Autowired
	private UserSecionBiz userSecionBiz;
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private ChapterRepository chapterRepository;
	@Autowired
	private ActivityRepository activityRepository;
	@Autowired
	private  UserSectionDao userSectionDao;
	@Autowired
	private OrganizationUserService organizationUserService;
	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private OrganizationUserRepository organizationUserRepository;
	@RequestMapping(value = "/infoReal",method = RequestMethod.GET)
	public String check(String userId){
		if (null == userId || "".equals( userId ) || "undefined".equals( userId ) ) {
			return ValueUtil.toError("notLogin","未找到员工工号");
		}else if(null==organizationUserRepository.findIdByJobNumber(userId)){
			return ValueUtil.toError("login","该员工不存在");
		}
		return null;
	}

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public String show(String userId,Integer activityId){
		if (null == userId || "".equals( userId ) || "undefined".equals( userId ) ) {
			return ValueUtil.toError("notLogin","未找到员工工号");
		}else if(null==organizationUserRepository.findIdByJobNumber(userId)){
			return ValueUtil.toError("login","该员工不存在");
		}
		if ( null == activityId || 1==activityId ) {
			try {
				activityId = activityBiz.currentActivityId();
			} catch (Exception e) {
				return ValueUtil.toError("noCurrentActivity",e.getMessage());
			}
		}
		Integer ouserId = organizationUserService.findIdByJobNumber(userId);
		Map<String,Object> summary = userSecionBiz.findSummaryByUserId(ouserId,activityId);
		summary.put("totalSections", activityRepository.findOne(activityId).getSections());
		summary.put("havePass",userSectionDao.findByUserIdAndActivityId(ouserId,activityId).size());
		Map<String,Object> map = userSecionBiz.personRank(ouserId,activityId,0);
		summary.put("beat",map.get("beat"));
		return ValueUtil.toJson("info",summary);
    }

	@RequestMapping(value = "/getstars",method = RequestMethod.GET)
	public String hint(Integer sectionId,Integer activityId,String userId){
		if ( null == activityId || 1==activityId ) {
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
		HashMap<String,Object> map=new HashMap<>();
		Integer sectionSequence = sectionBiz.findOne(sectionId).getSequenceInActivity();
		if(sectionBiz.isEndOfChapter(sectionId-1) && userSecionBiz.isNextSection(ouserId,activityId,sectionSequence)){
			Integer chapterId= sectionRepository.findByIdAndActivityId(sectionId,activityId).getChapterId();
			String detialHint=chapterRepository.findOne(chapterId-1).getPassDescribe();
			map.put("lock",1);
			map.put("detialHint",ValueUtil.coalesce(detialHint,""));
		}else {
			map.put("lock",0);
			map.put("detialHint","");
		}
		return ValueUtil.toJson("info",map);
	}
//    @RequestMapping(value = "/input",method = RequestMethod.PUT)
//    public String input(){
//        return ValueUtil.toJson("info",organizationMemberService.inputFromExcel());
//    }

    /*
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(HttpSession session,String loginCode,String password){
        Integer id = personBiz.register(loginCode,password);
        if (0 != id) {
            SessionData.put(session,id);
            return ValueUtil.toJson("status","success");
        } else {
            return ValueUtil.toJson("status","fail");
        }
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(HttpSession session , String loginCode, String password){

        Integer id = personBiz.login(loginCode,password);
        if (0 != id) {
            SessionData.put(session,id);
            return ValueUtil.toJson("status","success");
        } else {
            return ValueUtil.toError("loginFail","");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpSession session){
        SessionData.destroy(session);
        return ValueUtil.toJson("status","success");
    }
*/
}
