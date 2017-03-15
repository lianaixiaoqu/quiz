package com.hzbuvi.quiz.prize.service;

import com.hzbuvi.quiz.organization.repository.OrganizationUserRepository;
import com.hzbuvi.quiz.prize.entity.ChapterPrize;
import com.hzbuvi.quiz.prize.entity.UserPrize;
import com.hzbuvi.quiz.prize.repository.ChapterPrizeRepository;
import com.hzbuvi.quiz.prize.repository.UserPrizeRepository;
import com.hzbuvi.util.basic.PageResult;
import com.hzbuvi.util.basic.ValueUtil;
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
import java.util.*;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class PrizeBiz {

	@Autowired
	private ChapterPrizeRepository chapterPrizeDao;
	@Autowired
	private UserPrizeRepository userPrizeDao;
	@Autowired
	private OrganizationUserRepository organizationUserRepository;
	public static Integer defaultPageSize = 10;

	public ArrayList luckyDraw(Integer activityId,Integer chapterId, Integer sectionId, Integer userId ) {
		List<ChapterPrize> chapterPrizes = chapterPrizeDao.findByActivityIdAndChapterId(activityId, chapterId);
		HashMap<String,Object> prizeMap=new HashMap<>();
		ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
		UserPrize userPrize=new UserPrize();
		userPrize.setActivityId(activityId);
		userPrize.setChapterId(chapterId);
		userPrize.setUserId(userId);
		userPrize.setSectionId(sectionId);
		Map<Integer, Double> map = new HashMap<>();
		List<Double> list = new ArrayList<>();
		chapterPrizes.forEach(chapterPrize -> {
			Integer prizeId = chapterPrize.getId();
			Double rate =chapterPrize.getRate();
			map.put(prizeId, rate);
			list.add(rate);
		});
		Integer pid =sortByRate(map, list);
		ChapterPrize chapterPrize= chapterPrizeDao.findByActivityIdAndChapterIdAndId(activityId,chapterId,pid);
		if (ValueUtil.isEmpity(pid)) {//没抽中
			userPrize.setChapterPrizeId(0);
			userPrize.setCreateTime(new Date());
			userPrize.setUserName(organizationUserRepository.findOne(userId).getName());
			userPrize.setContent("未中奖");
			userPrize.setTitle("谢谢参与");
			userPrizeDao.save(userPrize);
			prizeMap.put("name","未中奖");
			prizeMap.put("content","谢谢惠顾");
			prizeMap.put("prizeGrade",0);
		}else {
			Integer remain = chapterPrize.getRemainingAmount();
			if ( null == remain  || 0 == remain ) {//没奖品了
				userPrize.setChapterPrizeId(0);
				userPrize.setCreateTime(new Date());
				userPrize.setUserName(organizationUserRepository.findOne(userId).getName());
				userPrize.setContent("没中奖");
				userPrize.setTitle("谢谢参与");
				prizeMap.put("name","未中奖");
				prizeMap.put("content","谢谢惠顾");
				prizeMap.put("prizeGrade",0);
				userPrizeDao.save(userPrize);
			}else {
				chapterPrize.setRemainingAmount(remain - 1);
				chapterPrizeDao.saveAndFlush(chapterPrize);
				userPrize.setChapterPrizeId(chapterPrize.getId());
				userPrize.setContent(chapterPrize.getContent());
				userPrize.setTitle(chapterPrize.getTitle());
				userPrize.setCreateTime(new Date());
				userPrize.setUserName(organizationUserRepository.findOne(userId).getName());
				userPrizeDao.save(userPrize);
				prizeMap.put("name",chapterPrize.getTitle());
				prizeMap.put("content",chapterPrize.getContent());
				if(chapterPrize.getTitle().equals("一等奖")){
					prizeMap.put("prizeGrade",1);
				}else if(chapterPrize.getTitle().equals("二等奖")){
					prizeMap.put("prizeGrade",2);
				}else if(chapterPrize.getTitle().equals("三等奖")){
					prizeMap.put("prizeGrade",3);
				}
			}

		}
		arrayList.add(prizeMap);
		return arrayList;
	}
	private Integer sortByRate(Map<Integer, Double> map, List<Double> list) {
		Collections.sort(list);
		List<Integer> keys = getKeyList(map, list);
		Integer n = changeToInt(list);
		if (ValueUtil.isEmpity(n)) {
			return null;
		}
		return keys.get(n);
	}

	private List<Integer> getKeyList(Map<Integer, Double> map, List<Double> list) {
		List<Integer> results = new ArrayList<>();
		list.forEach(elem -> {
			Integer removeKey = 0;
			for (Integer key : map.keySet()) {
				if (elem.equals(map.get(key))) {
					results.add(key);
					removeKey = key;
					break;
				}
			}
			map.remove(removeKey);
		});
		return results;
	}

	private Integer changeToInt(List<Double> list) {
		List<Double> results = new ArrayList<>();
		String min = String.valueOf(list.get(0));
		String[] split = min.split("\\.");
		Integer length = split[1].length();
		Integer total = mathPow(10, length);
		Integer random = getRandomNumber(total);
		list.forEach(elem -> {
			results.add(elem * total);
		});
		Double left = 0d;
		for (int i = 0; i < results.size(); i++) {
			Double right = left + results.get(i);
			if (random >= left && random < right) {
				return i;
			}
			left = right;
		}
		return null;
	}

	private Integer getRandomNumber(Integer n) {
		Random rand = new Random();
		return rand.nextInt(n);
	}

	private Integer mathPow(Integer m, Integer n) {
		Double a = Double.parseDouble(m.toString());
		Double b = Double.parseDouble(n.toString());
		return (int) Math.pow(a, b);
	}

	public String giveUp(Integer activityId,Integer chapterId, Integer sectionId, Integer userId ) {
		UserPrize userPrize=new UserPrize();
		userPrize.setActivityId(activityId);
		userPrize.setChapterId(chapterId);
		userPrize.setUserId(userId);
		userPrize.setSectionId(sectionId);
		userPrize.setChapterPrizeId(0);
		userPrize.setContent("放弃抽奖");
		userPrize.setTitle("放弃");
		userPrize.setUserName(organizationUserRepository.findOne(userId).getName());
		userPrizeDao.save(userPrize);
		return "您已放弃抽奖，且不可累计";
	}
	public String giveUp2(Integer activityId, Integer maxChatperId,Integer ouserId, Integer sectionId) {
	UserPrize up=userPrizeDao.findByActivityIdAndChapterIdAndUserIdAndSectionId(activityId,maxChatperId,ouserId,sectionId);
		if(up.getChapterPrizeId()!=0){
			ChapterPrize cp=chapterPrizeDao.findOne(up.getChapterPrizeId());
		Integer newR=cp.getRemainingAmount()+1;
			cp.setRemainingAmount(newR);
			chapterPrizeDao.save(cp);
		}
		up.setContent("放弃抽奖");
		up.setTitle("放弃");
		userPrizeDao.save(up);
		return "您已放弃抽奖，且不可累计";
	}

	public boolean judgeDraw(Integer userId, Integer activityId,Integer chapterId, Integer sectionId) {
	UserPrize userPrize=userPrizeDao.findByActivityIdAndChapterIdAndUserId(activityId,chapterId,userId);
		if (ValueUtil.isEmpity(userPrize)){
			return  true;  // 可以抽
		}else {
			return false;  // 已抽过, 不能再抽
		}
	}
	public  Map<String,Object> show(Map<String,String> parm) {
		String page = ValueUtil.coalesce( parm.get("page") , "0") ;
		Pageable pageable = new PageRequest(Integer.valueOf(page),defaultPageSize);
		Page<UserPrize> categories = userPrizeDao.findAll(getWhereClause(), pageable);
		Map<String,Object> map = new HashMap<>();
		map.put("page",categories);
		return map;
	}
	private Specification<UserPrize> getWhereClause() {
		return new Specification<UserPrize>() {
			@Override
			public Predicate toPredicate(Root<UserPrize> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				return predicate;
			}
		};
	}

	public PageResult showUserPrize(Integer pageNumber){
		Integer	defaultPageSize=5;
		Pageable pageable = new PageRequest(ValueUtil.coalesce(pageNumber,0),defaultPageSize);
		Page<UserPrize> userPrizes = userPrizeDao.findAllByOrderByCreateTimeDesc(pageable);
		return  PageUtil.toPage(userPrizes,pageNumber);
	}

	public boolean didntDraw(Integer sectionId, Integer ouserId) {
		UserPrize userPrize = userPrizeDao.findByChapterIdAndUserId(sectionId,ouserId);
		if (null == userPrize) {
			return  true;
		} else {
			return false;
		}
	}
	public void save(UserPrize userPirze){
		userPrizeDao.save(userPirze);
	}


	public Integer count(Integer activityId, Integer maxChatperId, Integer maxUserSectionId, Integer ouserId) {
		Integer cnt = userPrizeDao.countByActivityIdAndChapterIdAndSectionIdAndUserId(activityId,maxChatperId,maxUserSectionId,ouserId);
		return cnt;
	}

	public UserPrize findByUserIdAndContent(Integer userId, String content) {
	return 	userPrizeDao.findByUserIdAndContent(userId,content);
	}
}
