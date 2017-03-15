package com.hzbuvi.quiz.controller;

import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.chapter.service.ChapterService;
import com.hzbuvi.quiz.config.PageModel;
import com.hzbuvi.quiz.config.SessionData;
import com.hzbuvi.quiz.organization.service.OrganizationUserService;
import com.hzbuvi.quiz.prize.service.PrizeBiz;
import com.hzbuvi.quiz.prize.service.PrizeService;
import com.hzbuvi.quiz.section.service.SectionBiz;
import com.hzbuvi.quiz.usersection.UserSecionBiz;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.hzbuvi.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Created by WangDianDian on 2016/10/12.
 */
@RestController
@RequestMapping("/prize")
public class PrizeController {

    @Autowired
    private PrizeBiz prizeBiz;
    @Autowired
    private PrizeService prizeService;
	@Autowired
	private SectionBiz sectionBiz;
	@Autowired
	private OrganizationUserService organizationUserService;
	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private ChapterService chapterService;
	@Autowired
	private UserSecionBiz userSecionBiz;


    @RequestMapping(value = "/draw/{sectionId}", method = RequestMethod.POST)
    public String drawPrize(Integer activityId,@PathVariable("sectionId") Integer sectionId, String userId ){

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
		Integer maxUserSectionId = userSecionBiz.maxUserChapter(ouserId,activityId);
		Integer maxChatperId = sectionBiz.findOne(maxUserSectionId).getChapterId();

		Integer drawCount = prizeBiz.count(activityId,maxChatperId,maxUserSectionId,ouserId);

		if (null == drawCount || drawCount.equals(0)) {
			return ValueUtil.toJson("activity",prizeBiz.luckyDraw( activityId,maxChatperId, maxUserSectionId ,ouserId));
		} else {
			return ValueUtil.toError("alreadyDraw","您已经抽过奖了");
		}


    }

    @RequestMapping(value = "/giveupdraw",method = RequestMethod.GET)
    public String giveUpDraw2(Integer activityId,String userId ){
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
		Integer maxUserSectionId = userSecionBiz.maxUserChapter(ouserId,activityId);//sectionId
		Integer maxChatperId = sectionBiz.findOne(maxUserSectionId).getChapterId();
            return ValueUtil.toJson("activity",prizeBiz.giveUp2( activityId,maxChatperId,ouserId, maxUserSectionId));
    }

	@RequestMapping(method = RequestMethod.GET)
	public synchronized String giveUpDraw(Integer activityId, Integer sectionId, String userId ){
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
		Integer chapterId = sectionBiz.findOne(sectionId).getChapterId();
		Integer ouserId = organizationUserService.findIdByJobNumber(userId);
		return ValueUtil.toJson("activity",prizeBiz.giveUp( activityId,chapterId, sectionId,ouserId));
	}

    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public String insert(Integer activityId,Integer ChapterId,Integer first_id,String first_title, String first_przie_name,Integer first_prize_count
            ,Integer second_id,String second_title,String second_przie_name,Integer second_prize_count
            ,Integer third_id,String third_title,String third_przie_name,Integer third_prize_count

			,HttpServletRequest request,HttpServletResponse response

		) {

		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		Integer chatperId =  chapterService.findByActivityIdAndSequenceChapter(activityId,ChapterId);

		return ValueUtil.toJson("prize", prizeService.create(activityId,chatperId,first_id,first_title,first_przie_name,first_prize_count,
                second_id, second_title, second_przie_name,second_prize_count,third_id,third_title,third_przie_name,third_prize_count));
    }

    @RequestMapping(value = "/showlines", method = RequestMethod.GET)
    public String uopdateload(Integer activityId,Integer chapterId, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		if(null == chapterId || "".equals( chapterId ) || "undefined".equals( chapterId )){
			chapterId=1;
		}
		if ( null == activityId||activityId==1) {
            try {
                activityId = activityBiz.currentActivityId();
            } catch (Exception e) {
                return ValueUtil.toError("noCurrentActivity",e.getMessage());
            }
        }
		Integer realchatperId =  chapterService.findByActivityIdAndSequenceChapter(activityId,chapterId);
        return ValueUtil.toJson("prize", prizeService.updateLoad(activityId,realchatperId));
    }

    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
    public String searchAll (@PathVariable("pageNumber") Integer pageNumber, HttpServletRequest request,HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		return  ValueUtil.toJson("files",prizeBiz.showUserPrize(pageNumber));
    }
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, HttpServletRequest request, HttpServletResponse response){
		try {
			SessionData.verify(request,response);
		} catch (HzbuviException e) {
			return ValueUtil.toError(e.getCode(),e.getMsg());
		}
		Map<String,Object>  result = prizeBiz.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("prize",pageModel);
    }

}

