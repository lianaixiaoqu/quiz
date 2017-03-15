package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.activity.entity.Activity;
import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.chapter.service.ChapterService;
import com.hzbuvi.quiz.organization.entity.Organization;
import com.hzbuvi.quiz.organization.entity.OrganizationUser;
import com.hzbuvi.quiz.organization.repository.OrganizationRepository;
import com.hzbuvi.quiz.organization.repository.OrganizationUserRepository;
import com.hzbuvi.quiz.organization.service.OrganizationUserService;
import com.hzbuvi.quiz.prize.entity.ChapterPrize;
import com.hzbuvi.quiz.prize.entity.UserPrize;
import com.hzbuvi.quiz.prize.repository.ChapterPrizeRepository;
import com.hzbuvi.quiz.prize.service.PrizeBiz;
import com.hzbuvi.quiz.questionsheet.QuestionSheetBiz;
import com.hzbuvi.quiz.section.entity.Section;
import com.hzbuvi.quiz.section.repository.SectionRepository;
import com.hzbuvi.quiz.section.service.SectionBiz;
import com.hzbuvi.quiz.usersection.UserSecionBiz;
import com.hzbuvi.quiz.usersection.entity.UserSection;
import com.hzbuvi.quiz.usersection.entity.UserSectionHistory;
import com.hzbuvi.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Downey.hz on 2016/10/11..
 */

@RestController
@RequestMapping("/section")
public class SectionController {
    @Autowired
    private SectionBiz sectionBiz;
	@Autowired
	private UserSecionBiz userSecionBiz;
	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private QuestionSheetBiz questionSheetBiz;
	@Autowired
	private PrizeBiz prizeBiz;
	@Autowired
	private OrganizationUserRepository organizationUserDao;
	@Autowired
	private OrganizationRepository organizationDao;
	@Autowired
	private OrganizationUserService organizationUserService;
	@Autowired
	private SectionRepository sectionDao;


    @RequestMapping(value = "/question", method = RequestMethod.GET)//答题界面
    public String showQuestion(Integer sectionId,Integer activityId,String userId){

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
			List<Integer>  questionSheetIds = userSecionBiz.userUsedSheetId(sectionId,ouserId);

			Integer nextSection = userSecionBiz.getNextSectionId(ouserId,activityId);
			if ( sectionId > nextSection) {
				return ValueUtil.toError("nextSectionError","不能越级闯关");
			}
			Section section = sectionBiz.findOne(sectionId);
		List<Section> sections=sectionDao.findByActivityIdOrderBySequenceInActivity(activityId);
		if(sections.get(0).getId()<=sectionId-1){
			Section section2 = sectionBiz.findOne(sectionId-1);
			if(sectionBiz.isEndOfChapter(section2.getId())){
				Map<String,Object> map= userSecionBiz.judgeNextChapter(ouserId,activityId,section2.getChapterId(),sectionId-1);
				if (  3 != Integer.valueOf(map.get("lock").toString())  ) {
					return ValueUtil.toError("unlock","未达到过关条件");
				}
			}
		}

			Object[] objs = questionSheetBiz.getQuestionSheet(sectionId, questionSheetIds);
			Integer historyId=userSecionBiz.openQuestion(ouserId,activityId,sectionId,objs[1],section.getChapterId());
			return  ValueUtil.toJson("questions",objs[0],"historyId",historyId,"sectionLimitTime",activityBiz.limitTime(activityId));
    }

    @RequestMapping(value = "/commitAnswer", method = RequestMethod.POST)//提交
    public String returnShow(String content,Integer historyId, String userId) {
//		content:1765=1925,1765=1926,1766=1931,1767=1936,1768=1941,1770=1951,1773=1966

		if (null == userId || "".equals( userId ) || "undefined".equals( userId ) ) {
			return ValueUtil.toError("notLogin","未找到员工工号");
		}
		UserSectionHistory history = userSecionBiz.getQuestionSheetIdByHistroyId(historyId);

		long openDate = history.getStartTime().getTime();
		Activity activity  = activityBiz.findOne(history.getActivityId());
		long a=1000;
		long dueTime = openDate + activity.getSectionLimitTime()*1000+a;
		long systime=System.currentTimeMillis();
		if (systime> dueTime){
			return ValueUtil.toError("timeOut","");
		}
		// need to do
		Map<String,Object> result  = questionSheetBiz.verifyAnswer(history.getQuestionSheetId(),content);
		// need to do
		OrganizationUser organizationUser=organizationUserService.findByJobNumber(userId);
		String userName="";
		Integer ouserId = 1;
		Integer organizationId=null;
		if (null!=organizationUser) {
			userName = organizationUser.getName();
			organizationId = organizationUser.getOrganizationId();//组织Id
			ouserId =  organizationUser.getId();
		}
		Integer depId = 0;
		String depName = "";
		String buMen = "";
		Integer pId = 0;
		if ( null != organizationId) {
			Organization tempOrg = organizationDao.findByOrganizationId(organizationId);
			Integer level= tempOrg.getOrganizationLevel();//父Id  7部门或8科室level
			String buMenName="";
			Integer buMenNumber=0;
			String departmentName="";
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
								Organization o4 = organizationDao.findByOrganizationId(zhongxinid);
								if (null != o4) {
									departmentName = o4.getOrganizationName();
								}
							} else{//5本部
								Organization o5 = organizationDao.findByOrganizationId(zhongxinid);
								if (null != o5) {
									departmentId = o5.getParentId();//中心id
									Organization o6 = organizationDao.findByOrganizationId(departmentId);
									if (null != o6)	 {
										departmentName = o6.getOrganizationName();
									}
								}
							}
						}
					}
				}
			} else if (level == 4)  {
				departmentId = tempOrg.getOrganizationId();
				departmentName = tempOrg.getOrganizationName();
			} else if (level == 8 ){//8科室
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
									if (null!=o15) {
										departmentName = o15.getOrganizationName();
									}
								} else  if ( organizationLevel < 4 ) {

								} else {//5本部
									Organization o16 = organizationDao.findByOrganizationId(zhongxinId);
									if (null !=o16) {
										departmentId = o16.getParentId();//中心
										Organization o17 = organizationDao.findByOrganizationId(departmentId);
										if (null !=o17){
											departmentName = o17.getOrganizationName();
										}
									}
								}
							}
						}
					}
				}
			}

//			if ( level < 7) {
//				buMenName = "";
//				buMenNumber = 0;
//			}

			depId = departmentId;
			depName = departmentName;
			buMen = buMenName;
			pId = buMenNumber;
		}
		HashMap<String,Object> newmap =  userSecionBiz.commitAnswer(history,
				(Integer)result.get("ErrorCount"),
				Double.valueOf(result.get("correctRate").toString()),
				(Integer) result.get("score"),
				(Integer) result.get("correctCount"),
				userName,depId,depName,buMen,pId);

		Integer sections=activityBiz.findOne(history.getActivityId()).getSections();
		Integer maxUserSection = userSecionBiz.isRetry(historyId,ouserId,sections);

		if ( (sectionBiz.isEndOfChapter( history.getSectionId() ))
				||
			( null != maxUserSection && prizeBiz.didntDraw(maxUserSection,ouserId ) )
				) {
			Integer calculateSectionId = 0;
			Integer calculateChapterId = 0;
			if (null == maxUserSection) {
				calculateSectionId = history.getSectionId();
				calculateChapterId = history.getChapterId();
			} else {
				calculateSectionId=maxUserSection;
				calculateChapterId = sectionBiz.findOne(calculateSectionId).getChapterId();
			}
			Map<String,Object> map= userSecionBiz.judgeNextChapter(ouserId,history.getActivityId(),calculateChapterId,calculateSectionId);
			if(map.get("lock").equals(0)){
				result.put("condition",map.get("condition"));
				result.put("lock","0"); // 闖关失败
				result.put("canDraw", false  );
			}else {
				result.put("canDraw", true );
				Integer activityId = history.getActivityId();
				Integer chapterId= history.getChapterId();
				if (null != maxUserSection) {
					chapterId = sectionBiz.findOne(maxUserSection).getChapterId();
				}
				if ( prizeBiz.judgeDraw(ouserId,activityId , calculateChapterId ,calculateSectionId) ) {
					Integer	sectionSize=userSecionBiz.showSections(activityId,ouserId).size();
					if ( sectionBiz.endOfSection(sectionSize,sections) ){
						UserPrize userPrize=new UserPrize();
						userPrize.setUserName(organizationUserDao.findOne(ouserId).getName());
						userPrize.setCreateTime(new Date());
						userPrize.setUserId(ouserId);
						userPrize.setContent("通关大奖");
						if(ValueUtil.notEmpity(prizeBiz.findByUserIdAndContent(Integer.valueOf(ouserId),"通关大奖"))){
							result.put("lock","4"); //
						}else {
							prizeBiz.save(userPrize);
							result.put("lock","4"); // 通关
						}

					} else {
						result.put("lock","3"); // 可抽奬
						result.put("currentTotalStars",userSecionBiz.starCount(history.getActivityId(),ouserId));
					}

				} else {
					// /if( prizeBiz.judgeDraw(ouserId,activityId , calculateChapterId ,calculateSectionId)==false){
					result.put("lock","2"); // 已抽过獎 或 放弃
				}
			}

		} else {
			result.put("canDraw", false  );
			result.put("lock","1"); // 小关直接走
		}
		result.put("starCount",newmap.get("stars"));
		result.put("correctRate",result.get("correctRate")+"%");
		result.put("score",newmap.get("score"));

		return  ValueUtil.toJson("result",result);
	}


	@RequestMapping(method = RequestMethod.GET)//个人关卡情况
    public String sections(String userId,Integer activityId){

		if (null==activityId || 1 == activityId) {
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
		List<Section>  allSections = sectionBiz.allSectionsByActivityId(activityId);
		List<UserSection> userSections = userSecionBiz.showSections(activityId,ouserId);

		if ( null == userSections || userSections.size() == 0 ) {
			allSections.get(0).setStarCount(7);
		}

		for (int i = 0; i < userSections.size() ; i++) {
			allSections.get(i).setStarCount(userSections.get(i).getStars()); ;
			if ( i == userSections.size() -1 ) {
				if ( 0 == allSections.get(i).getEndOfChapter() ) {
					allSections.get(i+1).setStarCount(7);
				} else {
					Map<String,Object> map= userSecionBiz.judgeNextChapter(ouserId,activityId,allSections.get(i).getChapterId(),allSections.get(i).getId());
					if (  0 == Integer.valueOf(map.get("lock").toString()) ) {
						if ( userSections.size() == allSections.size() ) {
							allSections.get(i).setStarCount(3);
						} else {
							allSections.get(i+1).setStarCount(0);
						}
					}else {
						if ( ! sectionBiz.endOfActivity(activityId, userSections.get(i).getSectionId() ) ){
							allSections.get(i+1).setStarCount(7);
						}
					}
				}
			}
		}
        return  ValueUtil.toJson("result",allSections);
    }
}
