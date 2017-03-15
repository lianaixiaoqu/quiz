package com.hzbuvi.quiz.usersection;

import com.hzbuvi.quiz.activity.service.ActivityBiz;
import com.hzbuvi.quiz.chapter.repository.ChapterRepository;
import com.hzbuvi.quiz.like.entity.LikeType;
import com.hzbuvi.quiz.like.service.LikeService;
import com.hzbuvi.quiz.organization.entity.Organization;
import com.hzbuvi.quiz.organization.repository.OrganizationRepository;
import com.hzbuvi.quiz.organization.repository.OrganizationUserRepository;
import com.hzbuvi.quiz.section.entity.Section;
import com.hzbuvi.quiz.section.repository.SectionRepository;
import com.hzbuvi.quiz.usersection.bean.OrderBy;
import com.hzbuvi.quiz.usersection.bean.RankCache;
import com.hzbuvi.quiz.usersection.entity.UserSection;
import com.hzbuvi.quiz.usersection.entity.UserSectionHistory;
import com.hzbuvi.quiz.usersection.repository.UserSectionDao;
import com.hzbuvi.quiz.usersection.repository.UserSectionHistoryDao;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import com.wrqzn.wheel.condition.biz.ConditionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class UserSecionBiz {

	@Autowired
	private UserSectionDao userSectionDao;
	@Autowired
	private UserSectionHistoryDao historyDao;
	@Autowired
	private ConditionFactory conditionFactory;
	@Autowired
	private ChapterRepository chapterRepository;
	@Autowired
	private LikeService likeService;
	@Autowired
	private ActivityBiz activityBiz;
	@Autowired
	private OrganizationUserRepository organizationUserRepository;
	@Autowired
	private OrganizationRepository organizationRepository;
	@Autowired
	private SectionRepository sectionDao;

	private Map<String,RankCache>  rankCache = new HashMap<>();


	private static final Integer defaultPageSize = 30;


	public List<UserSection> showSections(Integer activityId, Integer userId) {
		System.out.println("activityId:" + activityId);
		System.out.println("userId:" + userId);
		return userSectionDao.findByUserIdAndActivityIdOrderBySectionId(userId, activityId);
	}

	public Integer userQuestionSheetId(Integer activityId, Integer userId) {
		List<UserSection> userSections = showSections(activityId, userId);
		if (null == userSections || userSections.size() == 0) {
			return 0;
		} else {
			return userSections.get(0).getQuestionSheetId();
		}
	}


	public Integer openQuestion(Integer userId, Integer activityId, Integer sectionId, Object obj, Integer chapterId) {
		UserSectionHistory history = new UserSectionHistory();
		history.setActivityId(activityId);
		history.setSectionId(sectionId);
		history.setUserId(userId);
		history.setQuestionSheetId(Integer.valueOf(obj.toString()));
		history.setChapterId(chapterId);
		historyDao.save(history);
		return history.getId();
	}

	public UserSectionHistory getQuestionSheetIdByHistroyId(Integer historyId) {
		return historyDao.findOne(historyId);
	}

	public synchronized HashMap<String, Object> commitAnswer(UserSectionHistory history,
												Integer errorCount,
												double correctRate,
												Integer score,
												Integer correctCnt,
												String username,
												Integer departmentId, String departmentName,
												String buMen, Integer buMenNumber) {
		HashMap<String, Object> map = new HashMap<>();
		history.setSubmitTime(new Date());//提交时间
		history.setScore(score);//分数
		history.setErrorCount(errorCount);
		history.setCorrectRate(correctRate);
		history.setCorrectCount(correctCnt);//正确个数
		historyDao.save(history);
		Integer starCount = 1;
		Integer activityId = history.getActivityId();
		Integer userId = history.getUserId();
		Integer sectionId = history.getSectionId();
		Integer ErrorCount = history.getErrorCount();
//		BigDecimal correctRate = BigDecimal.valueOf(history.getCorrectCount()).divide(BigDecimal.valueOf(history.getCorrectCount()+history.getErrorCount())).setScale(2);
		UserSection usersection = userSectionDao.findByUserIdAndSectionIdAndActivityId(userId, sectionId, activityId);
		if (90 < score) {
			starCount = 5;
		} else if (80 < score) {
			starCount = 4;
		} else if (70 < score) {
			starCount = 3;
		} else if (60 < score) {
			starCount = 2;
		} else if (score <= 60) {
			starCount = 1;
		}
		if (ValueUtil.isEmpity(usersection)) {
			UserSection newUsersection = new UserSection();
			newUsersection.setActivityId(activityId);
			newUsersection.setCorrectCount(correctCnt);
			newUsersection.setDepartmentId(ValueUtil.coalesce(departmentId, 0));
			newUsersection.setDepartmentName(ValueUtil.coalesce(departmentName, ""));
			newUsersection.setBuMen(ValueUtil.coalesce(buMen, ""));
			newUsersection.setBuMenNumber(ValueUtil.coalesce(buMenNumber, 0));
			newUsersection.setErrorCount(ValueUtil.coalesce(ErrorCount, 0));
			newUsersection.setScore(ValueUtil.coalesce(score, 0));
			newUsersection.setSectionId(ValueUtil.coalesce(sectionId, 0));
			newUsersection.setStars(ValueUtil.coalesce(starCount, 0));
			newUsersection.setUserId(ValueUtil.coalesce(userId, 0));
			newUsersection.setUserName(ValueUtil.coalesce(username, ""));
			newUsersection.setQuestionSheetId(history.getQuestionSheetId());
			newUsersection.setCorrectRate(ValueUtil.coalesce(correctRate, 0.0));
			newUsersection.addSubmitSeconds( history.getSubmitTime().getTime() - history.getStartTime().getTime() );
			userSectionDao.save(newUsersection);
		} else {
			if (usersection.getCorrectCount() < history.getCorrectCount()) {
				usersection.setCorrectCount(ValueUtil.coalesce(correctCnt, 0));
				usersection.setCorrectRate(ValueUtil.coalesce(correctRate, 0.0));
				usersection.setStars(ValueUtil.coalesce(starCount, 0));
				usersection.setQuestionSheetId(history.getQuestionSheetId());
				Integer submit = usersection.getSubmitCount();
				usersection.setSubmitCount(submit + 1);
			}
			if (usersection.getStars() < starCount) {
				usersection.setStars(starCount);
			}
			if (usersection.getScore() < score) {
				usersection.setScore(score);
			}
			usersection.addSubmitSeconds( history.getSubmitTime().getTime() - history.getStartTime().getTime() );
			userSectionDao.save(usersection);
		}
		map.put("stars", starCount);
		map.put("score", score);
		return map;
	}


	public Map<String, Object> personRankSituation(Integer activityId, Integer page) {
//		List<Map<String, Object>> userSection = userSectionDao.findByUserIdAndActivityIdPagePerson(activityId);
//
//		String orderBy = "stars,score,submitCount";
//		String desc = "1,1,0";
//		List<Map<String, Object>> userSectionPage = OrderBy.sort(userSection, orderBy, desc);

		List<Map<String, Object>> userSection = null ;
		RankCache cache = rankCache.get("person");
		if (null != cache && cache.verifyTime() ) {
			userSection = cache.getData();
		} else {
			userSection = userSectionDao.findByUserIdAndActivityIdPagePerson(activityId);
			RankCache newCache = new RankCache();
			newCache.setData(userSection);
			rankCache.put("person",newCache);
		}

		List<Map<String,Object>> userSectionPage = new ArrayList<>();
		for (int i = 0; i < userSection.size(); i++) {
			userSectionPage.add(userSection.get(i));
		}


		List<Integer> userids = new ArrayList<>();
//		userSectionPage.forEach( e ->  userids.add(Integer.valueOf(e.get("userid").toString())) );
		int selfRank = 0;
		for (int i = 0; i < userSectionPage.size(); i++) {
			Object tempId = userSectionPage.get(i).get("userid");
			userids.add(Integer.valueOf(tempId.toString()));
		}
		Map<Integer, Integer> likeMap = likeService.findLike(userids, activityId, LikeType.person);
		userSectionPage.forEach(e -> e.put("like", likeMap.get(e.get("userid"))));

		for (int i = 0; i < userSectionPage.size(); i++) {
			Integer like = likeMap.get(userSectionPage.get(i).get("userid"));
			if (null == like) {
				like = 0;
			}
			userSectionPage.get(i).put("like", like);
			if (null == userSectionPage.get(i).get("departmentname")) {
				userSectionPage.get(i).put("departmentname", "");
			}
			if (null == userSectionPage.get(i).get("username")) {
				userSectionPage.get(i).put("username", "");
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("rank", userSectionPage);
		map.put("beat", userSectionPage.size() - selfRank);

		return map;
	}

	public Map<String, Object> personRank(Integer userId, Integer activityId, Integer page) {
		List<Map<String, Object>> userSection = null ;
		RankCache cache = rankCache.get("person");
		if (null != cache && cache.verifyTime() ) {
			userSection = cache.getData();
		} else {
			userSection = userSectionDao.findByUserIdAndActivityIdPagePerson(activityId);
			RankCache newCache = new RankCache();
			newCache.setData(userSection);
			rankCache.put("person",newCache);
		}

//		@Query("select us.userId as userid , us.userName as username, us.buMenNumber as departmentid ,us.buMen as departmentname , count(us.sectionId) as sectioncnt, count(us.userId) as usercnt ,sum(us.score) as score ,sum(us.stars) as stars ,sum(us.submitCount) as submitCount from UserSection us  where  us.activityId = :activityId  group by  us.userId,us.userName, us.buMenNumber  ,us.buMen  " )
//		String orderBy = "sectioncnt,stars,submitCount";
//		String desc = "1,1,0";

//		System.out.println(ValueUtil.toJson("origin:"+userSection));
//		List<Map<String, Object>> userSectionPage = OrderBy.sort(userSection, orderBy, desc);
//		System.out.println(ValueUtil.toJson("order:"+userSectionPage));
		List<Map<String,Object>> userSectionPage = new ArrayList<>();
		for (int i = 0; i < userSection.size(); i++) {
			userSectionPage.add(userSection.get(i));
		}


		List<Integer> userids = new ArrayList<>();
//		userSectionPage.forEach( e ->  userids.add(Integer.valueOf(e.get("userid").toString())) );
		int selfRank = -1;
		for (int i = 0; i < userSectionPage.size(); i++) {
			Object tempId = userSectionPage.get(i).get("userid");
			userids.add(Integer.valueOf(tempId.toString()));
			if (userId.equals(Integer.valueOf(tempId.toString()))) {
				selfRank = i;
			}
		}

		List<Integer> likeTargetId = likeService.likeTarget(activityId, userId, LikeType.person);

		Map<Integer, Integer> likeMap = likeService.findLike(userids, activityId, LikeType.person);
		userSectionPage.forEach(e -> e.put("like", likeMap.get(e.get("userid"))));

		for (int i = 0; i < userSectionPage.size(); i++) {
			Integer like = likeMap.get(userSectionPage.get(i).get("userid"));
			if (null == like) {
				like = 0;
			}
			userSectionPage.get(i).put("like", like);
			userSectionPage.get(i).put("status", checkLike(userSectionPage.get(i).get("userid"), likeTargetId));

			if (null == userSectionPage.get(i).get("departmentname")) {
				userSectionPage.get(i).put("departmentname", "");
			}
			if (null == userSectionPage.get(i).get("username")) {
				userSectionPage.get(i).put("username", "");
			}

		}

		int beat = 0;
		selfRank++;

		if (selfRank > 0) {
			beat = userSectionPage.size() - selfRank;
		}

		int startIndex = 0;
		int endindex = 50;
		if (null != page) {
			startIndex = 50*page ;
			endindex = startIndex + 50;
		}

		List<Map<String,Object>> result = new ArrayList<>();

		if ( userSectionPage.size() > startIndex && userSectionPage.size() >= endindex ) {
			result = userSectionPage.subList(startIndex,endindex);
		} else  if ( userSectionPage.size() > startIndex) {
			result = userSectionPage.subList(startIndex,userSectionPage.size());
		}

		Map<String, Object> map = new HashMap<>();
		map.put("rank", result );
		map.put("beat", beat);
		map.put("selfRank", selfRank);

		return map;
	}

	private int checkLike(Object targetId, List<Integer> idList) {
		Integer obj = Integer.valueOf(targetId.toString());
		if (idList.contains(obj)) {
			return 1;
		} else {
			return 0;
		}

	}

	public Map<String, Object> departmentRank(Integer userId, Integer activityId, Integer page, Integer depId) {
		List<Map<String, Object>> userSectionPage = userSectionDao.findByUserIdAndActivityIdPageDepartment(activityId);
//		@Query("select  us.departmentId as departmentid ,us.departmentName as departmentname , count(us.sectionId) as sectioncnt, count(us.userId) as usercnt ,sum(us.score) as score ,sum(us.stars) as stars ,sum(us.submitCount) as submitCount from UserSection us  where  us.activityId = :activityId  group by us.departmentId,us.departmentName  " )

		List<Integer> userids = new ArrayList<>();
		userSectionPage.forEach(e -> userids.add(Integer.valueOf(e.get("departmentid").toString())));
		Map<Integer, Integer> likeMap = likeService.findLike(userids, activityId, LikeType.department);
		userSectionPage.forEach(e -> e.put("like", likeMap.get(e.get("departmentid"))));

		List<Integer> likeTargetId = likeService.likeTarget(activityId, userId, LikeType.department);
		Integer myDepartmentId = depId;
//				getDepartmentId(userId,activityId);


		List<Integer> departmentId = new ArrayList<>();//zhongxin Id
		userSectionPage.forEach(u -> departmentId.add(Integer.valueOf(String.valueOf(u.get("departmentid")))));

		//  depId   totalUser
		// 查询每个中心下的所有人数 放到 departmentUser
		Map<Integer, Integer> departmentUser = centerUser(departmentId);

		List<Integer> toSave = new ArrayList<>();

		for (int i = 0; i < userSectionPage.size(); i++) {

			if (null == userSectionPage.get(i).get("departmentname")) {
//				userSectionPage.get(i).put("departmentname","");
				continue;
			}

			Integer like = likeMap.get(userSectionPage.get(i).get("departmentid"));
			if (null == like) {
				like = 0;
			}
			Integer usercnt = Integer.valueOf(userSectionPage.get(i).get("usercnt").toString());
			BigDecimal totalScore = new BigDecimal(userSectionPage.get(i).get("score").toString());
			BigDecimal totalSections = new BigDecimal(userSectionPage.get(i).get("sectioncnt").toString());
			userSectionPage.get(i).put("like", like);
			userSectionPage.get(i).put("avgSection", totalSections.divide(BigDecimal.valueOf(usercnt), 2, BigDecimal.ROUND_HALF_UP));
			userSectionPage.get(i).put("avgScore", totalScore.divide(BigDecimal.valueOf(usercnt), 2, BigDecimal.ROUND_HALF_UP).divide(totalSections, 2, BigDecimal.ROUND_HALF_UP));

			Integer totalUser = departmentUser.get(userSectionPage.get(i).get("departmentid"));
			if (null == totalUser || 0 == totalUser) {
				totalUser = 100000;
			}
			//need to do
			System.out.println("usercnt:" + usercnt + " , totaluser:" + totalUser + " , centerId:" + userSectionPage.get(i).get("departmentid"));
			Double joinRate = BigDecimal.valueOf(usercnt).divide(BigDecimal.valueOf(totalUser), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
			userSectionPage.get(i).put("joinRate", joinRate);
			userSectionPage.get(i).put("status", checkLike(userSectionPage.get(i).get("departmentid"), likeTargetId));
			toSave.add(i);
		}

		List<Map<String, Object>> trimedList = new ArrayList<>();
		for (int i = 0; i < toSave.size(); i++) {
			trimedList.add(userSectionPage.get(toSave.get(i)));
		}

		String orderBy = "joinRate,avgSection,avgScore";
		String desc = "1,1,1";
		System.out.println(ValueUtil.toJson(trimedList));
		List<Map<String, Object>> userSectionRank = OrderBy.sort(trimedList, orderBy, desc);
		System.out.println(ValueUtil.toJson(userSectionRank));

		int selfRank = -1;
		System.out.println("myDepartmentId:" + myDepartmentId);
		if (null != myDepartmentId) {
			for (int i = 0; i < userSectionRank.size(); i++) {
				System.out.println(userSectionRank.get(i).get("departmentid"));
				if (userSectionRank.get(i).get("departmentid").equals(myDepartmentId)) {
					selfRank = i + 1;
					break;
				}
			}
		}

		if (-1 == selfRank) {
			selfRank = 0;
		}

		Map<String, Object> map = new HashMap<>();
		map.put("rank", userSectionRank);
		map.put("beat", userSectionPage.size() - selfRank);
		map.put("selfRank", selfRank);
		return map;
	}

	public Map<String, Object> departmentRankSituation(Integer activityId, Integer page) {
		List<Map<String, Object>> userSection = userSectionDao.findByUserIdAndActivityIdPageDepartment(activityId);
		String orderBy = "stars,score,submitCount";
		String desc = "1,1,0";
		List<Map<String, Object>> userSectionPage = OrderBy.sort(userSection, orderBy, desc);

		List<Integer> userids = new ArrayList<>();
		userSectionPage.forEach(e -> userids.add(Integer.valueOf(e.get("departmentid").toString())));
		Map<Integer, Integer> likeMap = likeService.findLike(userids, activityId, LikeType.department);
		userSectionPage.forEach(e -> e.put("like", likeMap.get(e.get("departmentid"))));

		List<Integer> departmentId = new ArrayList<>();//zhongxin Id
		userSectionPage.forEach(u -> departmentId.add(Integer.valueOf(String.valueOf(u.get("departmentid")))));
		Map<Integer, Integer> departmentUser = centerUser(departmentId);

		for (int i = 0; i < userSectionPage.size(); i++) {
			Integer like = likeMap.get(userSectionPage.get(i).get("departmentid"));
			if (null == like) {
				like = 0;
			}

			Integer totalUser = departmentUser.get(userSectionPage.get(i).get("departmentid"));
			if (null == totalUser || 0 == totalUser) {
				totalUser = 1000;
			}
			Integer usercnt = Integer.valueOf(userSectionPage.get(i).get("usercnt").toString());
			BigDecimal totalScore = new BigDecimal(userSectionPage.get(i).get("score").toString());
			BigDecimal totalSections = new BigDecimal(userSectionPage.get(i).get("sectioncnt").toString());
			userSectionPage.get(i).put("like", like);
			userSectionPage.get(i).put("avgSection", totalSections.divide(BigDecimal.valueOf(usercnt), 2, BigDecimal.ROUND_HALF_UP));
			userSectionPage.get(i).put("avgScore", totalScore.divide(BigDecimal.valueOf(usercnt), 2, BigDecimal.ROUND_HALF_UP).divide(totalSections, 2, BigDecimal.ROUND_HALF_UP));
			Double joinRate = BigDecimal.valueOf(usercnt).divide(BigDecimal.valueOf(totalUser), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
			userSectionPage.get(i).put("joinRate", joinRate);
			if (null == userSectionPage.get(i).get("departmentname")) {
				userSectionPage.get(i).put("departmentname", "");
			}
		}

		Map<String, Object> map = new HashMap<>();
		map.put("rank", userSectionPage);
		map.put("beat", userSectionPage.size());
		return map;
	}

	public Map<String, Object> findSummaryByUserId(Integer userId, Integer activityId) {
		Map<String, Object> map = new HashMap<>();
		List<UserSection> usersections = userSectionDao.findByUserIdAndActivityId(userId, activityId);
		int correctAmount = 0;
		int errorAmount = 0;
		Integer totalStars = 0;
		BigDecimal correctRate;
		int bit = 2;
		String rate;
		for (int i = 0; i < usersections.size(); i++) {
			correctAmount += usersections.get(i).getCorrectCount();
			errorAmount += usersections.get(i).getErrorCount();
			totalStars += usersections.get(i).getStars();
		}
		if (0 != (correctAmount)) {
			StringBuilder builder = new StringBuilder();
			Integer total = activityBiz.findOne(activityId).getQuestionsPerSection() * usersections.size();
			correctRate = BigDecimal.valueOf(correctAmount).divide(BigDecimal.valueOf(total), 3, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(1, BigDecimal.ROUND_HALF_UP);
//					()float)correctAmount/total;
//			for(int i=0;i<bit;i++){
//				builder.append("0");
//			}
//			DecimalFormat df=new DecimalFormat("#."+builder.toString()+"");
//			double dbRate=Double.parseDouble(df.format(correctRate));
//			BigDecimal rate1 = BigDecimal.valueOf(dbRate*100).setScale(1,BigDecimal.ROUND_HALF_UP);
			rate = correctRate + "%";
		} else {
			rate = "0%";
		}
		map.put("sectionAmount", ValueUtil.coalesce(userSectionDao.findBySectionId(userId, activityId), 0)); // 用户通过关数
		map.put("correctAmount", ValueUtil.coalesce(correctAmount, 0)); // 总正确数
		map.put("stars", totalStars); // 总星数
		map.put("correctRate", rate); // 总正确率
		return map;
	}

	public HashMap<String, Object> judgeNextChapter(Integer userId, Integer activityId, Integer chapterId, Integer sectionId) {
		HashMap<String, Object> map = new HashMap<>();
		List<UserSection> userSections = userSectionDao.findByUserIdAndActivityId(userId, activityId);
		Integer lock = (Integer) conditionFactory.getInstance("unlockChapter").verify(userSections, userId, activityId, chapterId, sectionId);
		if (lock == 0) {
			map.put("lock", 0);//未过关
			map.put("condition", chapterRepository.findOne(chapterId).getPassDescribe());
		} else {
//                    if(sectionId==activityRepository.findOne(activityId).getSections()){
//                        map.put("lock",4);
//                        map.put("lastSection","已完成所有关卡！恭喜获得通关大奖");
//                    }else {
			map.put("lock", 3);//过关
//                    }
		}
		return map;
	}

	public List<Integer> userUsedSheetId(Integer sectionId, Integer userId) {
		List<UserSectionHistory> histories = historyDao.findBySectionIdAndUserId(sectionId, userId);
		List<Integer> sheetId = new ArrayList<>();
		histories.forEach(h -> sheetId.add(h.getQuestionSheetId()));
		return sheetId;
	}

	public Integer starCount(Integer activityId, Integer userId) {
		Integer stars = 0;
		List<UserSection> list = userSectionDao.findByUserIdAndActivityId(userId, activityId);
		for (int i = 0; i < list.size(); i++) {
			stars += list.get(i).getStars();
		}
		return stars;
	}

	public Map<String, Object> show(Map<String, String> parm, Integer activityId) {//姓名，中心，部门
//		String page = ValueUtil.coalesce(parm.get("page"), "0");
		String userName = parm.get("userName");//姓名
		String buMen = parm.get("buMen");//部门
		String departmentName = parm.get("departmentName");//中心
		Map<String, String> map1 = new HashMap<>();
		if (ValueUtil.notEmpity(userName)) {
			map1.put("userName", userName);
		}
		if (ValueUtil.notEmpity(buMen)) {
			map1.put("buMen", buMen);
		}
		if (ValueUtil.notEmpity(departmentName)) {
			map1.put("departmentName", departmentName);
		}
		List<UserSection> userSectionPage = new ArrayList<>();

		boolean flag = true;
		int i=0;
		while (flag) {
			Pageable pageable = new PageRequest(i++, 900);
			Page<UserSection> data = userSectionDao.findAll(getWhereClause(activityId, userName, buMen, departmentName), pageable);
			if (null == data.getContent() || data.getContent().size() == 0) {
				flag = false;
			} else {
				userSectionPage.addAll(data.getContent());
			}
		}
//		List<UserSection> userSectionPage = userSectionDao.findAll(getWhereClause(activityId, userName, buMen, departmentName));

		Map<String, Object> map = new HashMap<>();
		map.put("page", userSectionPage);
		map.put("condition", map1);
		return map;
	}

	private Specification<UserSection> getWhereClause(Integer activityId, String userName, String buMen, String departmentName) {
		return new Specification<UserSection>() {
			@Override
			public Predicate toPredicate(Root<UserSection> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if (ValueUtil.notEmpity(userName)) {
					predicate.getExpressions().add(cb.like(root.<String>get("userName"), "%" + userName + "%"));
				}
				if (ValueUtil.notEmpity(buMen)) {
					predicate.getExpressions().add(cb.like(root.<String>get("buMen"), "%" + buMen + "%"));
				}
				if (ValueUtil.notEmpity(departmentName)) {
					predicate.getExpressions().add(cb.like(root.<String>get("departmentName"), "%" + departmentName + "%"));
				}
				if (ValueUtil.notEmpity(activityId)) {
					predicate.getExpressions().add(cb.equal(root.<Integer>get("activityId"), Integer.valueOf(activityId)));
				}
				return predicate;
			}
		};
	}


	public void canActivityUpdate(int activityId) throws HzbuviException {
		Integer cnt = historyDao.countByActivityId(activityId);
		if (null != cnt && cnt > 0) {
			HzbuviException exception = new HzbuviException("cantUpdate");
			exception.setMsg("本活动已有人进行闯关, 不能再进行修改");
			throw exception;
		}
	}

	private Integer getDepartmentId(Integer userId, Integer activityId) {
		List<UserSection> userSections = userSectionDao.findByUserIdAndActivityId(userId, activityId);
		if (null != userSections) {
			return userSections.get(0).getDepartmentId();
		} else {
			return null;
		}


	}

	public boolean isNextSection(Integer userId, Integer activityId, Integer sectionSequence) {
		Long sectionCnt = userSectionDao.countByUserIdAndActivityId(userId, activityId);
		if (sectionCnt + 1 == Long.valueOf(sectionSequence)) {
			return true;
		} else {
			return false;
		}
	}

	public Map<Integer, Integer> centerUser(List<Integer> depid) {
		Map<Integer, Set<Integer>> cmap = getCenterList(depid);

		Map<Integer, Integer> result = new HashMap<>();
		for (int i = 0; i < depid.size(); i++) {
			if (null == cmap.get(depid.get(i))) {
				result.put(depid.get(i), 0);
			} else {
				cmap.get(depid.get(i)).add(depid.get(i));
				result.put(depid.get(i), organizationUserRepository.countByOrganizationIdIn(cmap.get(depid.get(i))));
			}
		}
		System.out.println("orgIds : " + ValueUtil.toJson(cmap));
		System.out.println("orguser: " + ValueUtil.toJson(result));
		return result;
	}


	public Map<Integer, Set<Integer>> getCenterList(List<Integer> depId) {
		List<Organization> all = organizationRepository.findAll();

		Map<Integer, List<Integer>> pMap = new HashMap<>();

		for (int i = 0; i < all.size(); i++) {
			if (null == pMap.get(all.get(i).getParentId())) {
				List<Integer> list = new ArrayList<>();
				list.add(all.get(i).getOrganizationId());
				pMap.put(all.get(i).getParentId(), list);
			} else {
				pMap.get(all.get(i).getParentId()).add(all.get(i).getOrganizationId());
			}
		}

		Map<Integer, Set<Integer>> result = new HashMap<>();
		for (int i = 0; i < depId.size(); i++) {
			Set<Integer> rst = new HashSet<>();
			result.put(depId.get(i), calculate(rst, depId.get(i), pMap));
		}

		return result;
	}

	private Set<Integer> calculate(Set<Integer> rst, Integer depId, Map<Integer, List<Integer>> pmap) {
		if (null != pmap.get(depId)) {
			List<Integer> sec = pmap.get(depId);
			rst.addAll(sec);
			for (int i = 0; i < sec.size(); i++) {
				rst.addAll(calculate(rst, sec.get(i), pmap));
			}
			return rst;
		} else {
			return rst;
		}
	}


	public Integer getNextSectionId(Integer userId,Integer activityId){
		Integer maxUserSection = 0;
		List<Section> sections = sectionDao.findByActivityIdOrderBySequenceInActivity(activityId);
		List<UserSection> userSections = userSectionDao.findByUserIdAndActivityId(userId, activityId );

		if ( userSections.size() == sections.size() ){
			return sections.get(sections.size()-1).getId();
		} else if( 0 == userSections.size() ){
			return sections.get(0).getId();
		} else {
			for (int i = 0; i < userSections.size() ; i++) {
				if ( maxUserSection < userSections.get(i).getSectionId() ){
					maxUserSection = userSections.get(i).getSectionId();
				}
			}
			maxUserSection ++ ;
		}

		return maxUserSection;
	}


	public Integer isRetry(Integer historyId, Integer ouserId,Integer sectionss) {
		UserSectionHistory history = historyDao.findOne(historyId);
		Integer activityId=history.getActivityId();
		//need to do 判断是否做过endofsection
		List<Section> sections = sectionDao.findByActivityIdOrderBySequenceInActivity(history.getActivityId());
		List<UserSection> userSections = userSectionDao.findByUserIdAndActivityId(ouserId, history.getActivityId());
        if(sectionss!=userSections.size()){
			if (sections.size() == userSections.size() ) {
				return null;
			}
		}
		Integer maxUserSection = 0;
		for (int i = 0; i < userSections.size(); i++) {
			if (maxUserSection < userSections.get(i).getSectionId()) {
				maxUserSection = userSections.get(i).getSectionId();
			}
		}

		if (maxUserSection > history.getSectionId()||(userSections.size()==sectionss)) {
			for (int i = 0; i < sections.size(); i++) {
				if (maxUserSection.equals( sections.get(i).getId()) && sections.get(i).getEndOfChapter().equals(1)) {
					return maxUserSection;
				}
			}
		} else {
			return null;
		}

		return null;
	}

	public Integer maxUserChapter(Integer ouserId, Integer activityId) {
		List<UserSection> userSections = userSectionDao.findByUserIdAndActivityId(ouserId,activityId);
		Integer maxUserSection = 0;
		for (int i = 0; i < userSections.size(); i++) {
			if (maxUserSection < userSections.get(i).getSectionId()) {
				maxUserSection = userSections.get(i).getSectionId();
			}
		}
		if( maxUserSection.equals( 0 )){
			return  null;
		} else {
			return maxUserSection;
		}

	}
}
