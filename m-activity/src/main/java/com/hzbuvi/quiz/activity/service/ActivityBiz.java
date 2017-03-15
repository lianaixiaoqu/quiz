package com.hzbuvi.quiz.activity.service;


import com.hzbuvi.page.util.PageResult;
import com.hzbuvi.page.util.PageUtil;
import com.hzbuvi.quiz.activity.bean.ActivityCondition;
import com.hzbuvi.quiz.activity.entity.Activity;
import com.hzbuvi.quiz.activity.entity.ActivityQuestionProperties;
import com.hzbuvi.quiz.activity.entity.QuestionPropertiesEnum;
import com.hzbuvi.quiz.activity.repository.ActivityRepository;
import com.hzbuvi.quiz.activity.repository.QuestionPropertiesDao;
import com.hzbuvi.quiz.chapter.service.ChapterService;
import com.hzbuvi.quiz.questionbank.entity.Category;
import com.hzbuvi.quiz.questionbank.entity.DeleteEnum;
import com.hzbuvi.quiz.questionbank.entity.Knowledge;
import com.hzbuvi.quiz.questionbank.repository.CategoryRepository;
import com.hzbuvi.quiz.questionbank.repository.KnowledgeRepository;
import com.hzbuvi.quiz.section.service.SectionBiz;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.date.DateUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;


/**
 * Created by Downey.hz on 2016/10/10..
 */
@Service
public class ActivityBiz {
    @Autowired
    private ActivityRepository repositoty;
	@Autowired
	private QuestionPropertiesDao propDao;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;
	@Autowired
	private ChapterService chapterBiz;
	@Autowired
	private SectionBiz sectionBiz;
	@Autowired
	private ActivityBiz activityBiz;

    public static Integer id;
    public static Integer defaultPageSize = 10;

    private static final String dateFormat = "yyyyMMdd";

    public Integer create(Map<String, String> param) {
        int chapters = Integer.parseInt(param.get("chapters"));
        int sectionsPerChapter = Integer.parseInt(param.get("sectionsPerChapter"));
        int questionsPerSection = Integer.parseInt(param.get("questionsPerSection"));
        int sectionLimitTime= Integer.parseInt(param.get("sectionLimitTime"));
        Activity activity = new Activity();
        activity.setName(param.get("name"));
        activity.setDescription(param.get("description"));
        String a=param.get("startTime");
        String b=param.get("endTime");
        Date dateStart=DateUtil.toDate(a,dateFormat);
        Date dateEnd=DateUtil.toDate(b,dateFormat);
        activity.setStartTime(dateStart);
        activity.setEndTime(dateEnd);
        activity.setDailyStart(param.get("dailyStart"));
        activity.setDailyEnd(param.get("dailyEnd"));
        activity.setChapters(chapters);
        activity.setSections(chapters*sectionsPerChapter);
        activity.setQuestions(chapters*sectionsPerChapter*questionsPerSection);
        activity.setSectionLimitTime(sectionLimitTime);
        activity.setQuestionsPerSection(questionsPerSection);
        activity.setSectionsPerChapter(sectionsPerChapter);
		activity.setIsCurrent( Integer.valueOf( ValueUtil.coalesce( param.get("isCurrent"),"0" ) ) );
        repositoty.save(activity);

		if ( 1 == activity.getIsCurrent() ) {
			activityBiz.updateCurrentActivity(activity.getId());
		}

        if (null== param.get("categories")){
            param.put("categories","");
        }
        if (null== param.get("knowledge")){
            param.put("knowledge","");
        }
		Integer categorys = Integer.parseInt(param.get("categories"));

		List<ActivityQuestionProperties> props = new ArrayList<>();
//		for (int i = 0; i < categorys.length ; i++) {
			ActivityQuestionProperties prop = new ActivityQuestionProperties();
			prop.setActivityId(activity.getId());
			prop.setPropertiesType(QuestionPropertiesEnum.category);
			prop.setPropertyId(categorys);
			props.add(prop);
        String cate=param.get("cate");
        if(null==cate){
            List<Knowledge> list1=knowledgeRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
            for(int j=0;j<list1.size();j++){
                ActivityQuestionProperties prop1 = new ActivityQuestionProperties();
                prop1.setActivityId(activity.getId());
                prop1.setPropertiesType(QuestionPropertiesEnum.knowledge);
                prop1.setPropertyId(knowledgeRepository.findByKnowledgeName(list1.get(j).getKnowledgeName()).getId());
                props.add(prop1);
            }
        }else{
            String[] knowledges = cate.split(" ");
            for (int i = 1; i < knowledges.length; i++) {
                ActivityQuestionProperties prop1 = new ActivityQuestionProperties();
                prop1.setActivityId(activity.getId());
                prop1.setPropertiesType(QuestionPropertiesEnum.knowledge);
                prop1.setPropertyId(knowledgeRepository.findByKnowledgeName(knowledges[i]).getId());
                props.add(prop1);
            }
        }

		propDao.save(props);
		return activity.getId();
    }

    public Map<String,String> delete(Integer id) {
        Map<String,String> map=new HashMap<>();
        if(repositoty.findOne(id).getIsCurrent()==1){
            map.put("status","failure");
            return map;
        }
       Activity activity= repositoty.findOne(id);
        activity.setIsDelete(1);
        repositoty.save(activity);
        map.put("status","success");
        return map;
    }
    public List showKnowledge(){
        List<ActivityQuestionProperties> activityQuestionProperties=propDao.findByPropertiesType(QuestionPropertiesEnum.knowledge);
        if(null==activityQuestionProperties||activityQuestionProperties.size()==0){
           List<Knowledge> list= knowledgeRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
            return list;
        }
        List<Knowledge> list1=new ArrayList<>();
        for(int i=0;i<activityQuestionProperties.size();i++){
            list1.add(knowledgeRepository.findOne(activityQuestionProperties.get(i).getPropertyId()));
        }
        return list1;
    }
    public Object showCategory(Integer id){
        List<ActivityQuestionProperties> questionBankRepository=propDao.findByPropertiesType(QuestionPropertiesEnum.category);
        if(null==questionBankRepository||questionBankRepository.size()==0){
            List<Category> list1= categoryRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
            return list1;
        }
        Integer activityId=propDao.findIdByType(id,QuestionPropertiesEnum.category).get(0);
        Category category=categoryRepository.findOne(activityId);
        return category;
    }
    public Activity showActivity(Integer activityId){
        Activity activity= repositoty.findOne(activityId);
//        int sum=activity.getChapters();
//		List<Integer>  chapters = chapterBiz.findByActivityId(activityId);

//        List<Integer> list=new ArrayList<>();
//        for(int i=1;i<sum+1;i++){
//            list.add(i);
//        }
        return  activity;
    }
    public List<Integer> showOne(Integer activityId){
        Activity activity= repositoty.findOne(activityId);
//        int sum=activity.getChapters();
		List<Integer>  chapters = chapterBiz.findByActivityId(activityId);

//        List<Integer> list=new ArrayList<>();
//        for(int i=1;i<sum+1;i++){
//            list.add(i);
//        }
		chapters.remove(chapters.size()-1);

        return  chapters;
    }
    public Activity showSum(Integer activityId){
        Activity activity= repositoty.findOne(activityId);
        return activity;
    }
    public List<Activity> showAll(){
        List<Activity> activity=repositoty.findByIsDelete(0);
        return  activity;
    }
    public  Activity updateLoad(Integer id){
        Activity activity=repositoty.findOne(id);
        return  activity;
    }
    public List<Category> getCategory()  {
        List<Category> list=categoryRepository.findAll();
        return list;
    }
    public List<Knowledge> getKnowledge()  {
        List<Knowledge> list=knowledgeRepository.findAll();
        return list;
    }

    @Transactional
    public boolean update(ActivityCondition activity, Integer categories, String cate) throws HzbuviException {
		Activity activity1= null;
		try {
			activity1 = repositoty.findOne( activity.getId() );
		} catch (Exception e) {
			HzbuviException exception = new HzbuviException("noCurrentActivity");
			exception.setMsg(e.getMessage());
			throw exception;
		}
        activity1.setIsDelete(0);//是否删除
        activity1.setName(activity.getName());//名字
        //activity1.setCreateTime(DateUtil.toDate(activity.getCreateTime(),dateFormat));
        activity1.setDailyEnd(activity.getDailyEnd());//当天结束时间
		if ( null != activity.getStartTime()  && activity.getStartTime().length() < 10 ) {
			activity1.setStartTime(DateUtil.toDate(activity.getStartTime(),dateFormat));//开始时间
		}

		if ( null != activity.getEndTime()  && activity.getEndTime().length() < 10 ) {
			activity1.setEndTime(DateUtil.toDate(activity.getEndTime(),dateFormat));//结束时间
		}
        activity1.setDescription(activity.getDescription());//活动描述
        activity1.setDailyStart(activity.getDailyStart());//活动开始时间

        activity1.setChapters(activity.getChapters());
        activity1.setSectionLimitTime(activity.getSectionLimitTime());//答题限制时间
        activity1.setQuestionsPerSection(activity.getQuestionsPerSection());//每关问题
        activity1.setSectionsPerChapter(activity.getSectionsPerChapter());//每个卡关数
		activity1.setSections(activity.getChapters() * activity.getSectionsPerChapter());//总关数
		activity1.setIsCurrent(activity.getIsCurrent());//是否当前活动
        repositoty.save(activity1);
		if (activity1.getIsCurrent() == 1 ) {
			activityBiz.updateCurrentActivity(activity1.getId());
		 }
        ActivityQuestionProperties category = new ActivityQuestionProperties();
		List<ActivityQuestionProperties> activityQuestionProperties = new ArrayList<>();
		List<ActivityQuestionProperties> propsOld = propDao.findByActivityId(activity1.getId());
        propDao.delete(propsOld);
        category.setActivityId(activity.getId());
        category.setPropertiesType(QuestionPropertiesEnum.category);
        category.setPropertyId(categories);
        propDao.save(category);
        if(null==cate){
            List<Knowledge> list1=knowledgeRepository.findByIsDelete(DeleteEnum.NOT_DELETE);
            for(int j=0;j<list1.size();j++){
                ActivityQuestionProperties prop1 = new ActivityQuestionProperties();
                prop1.setActivityId(activity1.getId());
                prop1.setPropertiesType(QuestionPropertiesEnum.knowledge);
                prop1.setPropertyId(knowledgeRepository.findByKnowledgeName(list1.get(j).getKnowledgeName()).getId());
                activityQuestionProperties.add(prop1);
            }
        }else{
            String[] knowledges=cate.split(" ");
            for (int i = 1; i < knowledges.length; i++) {
                ActivityQuestionProperties prop1 = new ActivityQuestionProperties();
                prop1.setActivityId(activity1.getId());
                prop1.setPropertiesType(QuestionPropertiesEnum.knowledge);
                prop1.setPropertyId(knowledgeRepository.findByKnowledgeName(knowledges[i]).getId());
                activityQuestionProperties.add(prop1);
            }
        }
		propDao.save(activityQuestionProperties);
		chapterBiz.deleteByActivityId(activity1.getId());
		sectionBiz.deleteByActivityId(activity1.getId());
		List<Integer> chapterIds =  chapterBiz.create(activity1.getId(),activity1.getChapters(),activity1.getSectionsPerChapter());
		sectionBiz.create(activity1.getId(),chapterIds,activity1.getSectionsPerChapter());
        return true;
    }

    @Transactional
    public boolean update2(ActivityCondition activity) throws HzbuviException {//活动已开始
        Activity activity1= null;
        try {
            activity1 = repositoty.findOne( activity.getId() );
        } catch (Exception e) {
            HzbuviException exception = new HzbuviException("noCurrentActivity");
            exception.setMsg(e.getMessage());
            throw exception;
        }
        activity1.setName(activity.getName());//名字
        //activity1.setCreateTime(DateUtil.toDate(activity.getCreateTime(),dateFormat));
        if ( null != activity.getStartTime()  && activity.getStartTime().length() < 10 ) {
            activity1.setStartTime(DateUtil.toDate(activity.getStartTime(),dateFormat));//开始时间
        }
        if ( null != activity.getEndTime()  && activity.getEndTime().length() < 10 ) {
            activity1.setEndTime(DateUtil.toDate(activity.getEndTime(),dateFormat));//结束时间
        }
        activity1.setDescription(activity.getDescription());//活动描述
        activity1.setDailyStart(activity.getDailyStart());//活动开始时间
        activity1.setDailyEnd(activity.getDailyEnd());//当天结束时间
        activity1.setIsCurrent(activity.getIsCurrent());//是否当前活动
        activity1.setIsDelete(0);//是否删除
        repositoty.save(activity1);

		if ( 1 == activity1.getIsCurrent() ) {
			updateCurrentActivity(activity1.getId());
		}


        return true;
    }

    public Activity show(Integer id) {
        return repositoty.findOne(id);
    }
    public List<Knowledge> showKnowledge(Integer categerId){
       List<Knowledge> list= knowledgeRepository.findByCategoryId(categerId);
        return list;
    }

//    public PageResult index(Integer page) {
//        if(null==page){
//            page=0;
//        }
//        Pageable pageable = new PageRequest(ValueUtil.coalesce(page,0),defaultPageSize);
//        Page<Activity> activities = repositoty.findByIsDelete(pageable,0);
//        PageResult result = PageUtil.toPage(activities,page);
//        return result;
//    }

	public List<Integer> categoryIds(Integer activityId) {
		return propDao.findIdByType(activityId,QuestionPropertiesEnum.category);
	}

	public List<Integer> knowledgeIds(Integer activityId) {
		return propDao.findIdByType(activityId,QuestionPropertiesEnum.knowledge);
	}

	public Integer questionSheetCount(Integer activityId) {
		Integer index = repositoty.findOne(activityId).getQuestionSheets();
		Random r = new Random();
		return r.nextInt(index);
	}

	public  Integer limitTime(Integer activityId) {
		return repositoty.findOne(activityId).getSectionLimitTime();
	}


    public int checkDay(Integer activityId) {
		Activity activity = repositoty.findOne(activityId);
		Integer starTime= Integer.valueOf( ValueUtil.coalesce(activity.getDailyStart(),"000000") );
      	Integer endTime= Integer.valueOf( ValueUtil.coalesce(activity.getDailyEnd(),"235959" ) );
        Date date =new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("HHmmss");//只有时分秒
        String time=sdf.format(date);
        Integer currentTime=Integer.valueOf(time);
     if(currentTime>=starTime&&currentTime<=endTime){
         return 1;
        }else {
            return 0;
        }
    }

    public int checkTime(Integer activityId){
		Activity activity = repositoty.findOne(activityId);
        Date starDate=activity.getStartTime();
        Date endDate=activity.getEndTime();
		Integer currentTime = Integer.valueOf(DateUtil.toString(new Date(),"HHmmss"));
		Integer dailyStart = Integer.valueOf(ValueUtil.coalesce(activity.getDailyStart(),"090000"));
		Integer dailyEnd = Integer.valueOf(ValueUtil.coalesce(activity.getDailyEnd(),"180000"));
        long time=System.currentTimeMillis();
        if(time>=starDate.getTime()&&time<=endDate.getTime()
//				&& dailyStart <=currentTime && currentTime >= dailyEnd
				){
            return 2;
        }else {
            return 3;
        }
    }
    public Activity showAll(Integer activityId){
        Activity activity=repositoty.findOne(activityId);
        return activity;
    }

	public Activity findOne(Integer activityId) {
		return repositoty.findOne(activityId);
	}

	public Integer currentActivityId() throws Exception {
		Activity activity = repositoty.findByIsCurrent(1).get(0);
		if (null == activity) {
			throw new Exception("你没有设置当前活动");
		} else {
			return activity.getId();
		}
	}

	public void updateCurrentActivity(Integer currentActivityId){
		List<Activity> currentActivities = repositoty.findByIsCurrent(1);
		List<Activity> toUpdate = new ArrayList<>();

		for (int i = 0; i < currentActivities.size() ; i++) {
			currentActivities.get(i).setNotCurrent();
			toUpdate.add(currentActivities.get(i));
		}
		Activity activity = repositoty.findOne(currentActivityId);
		activity.setCurrent();
		toUpdate.add(activity);
		repositoty.save(toUpdate);
	}

	public List<Activity> all() {
		return repositoty.findAll();
	}
    public  Map<String,Object> showActivity(Map<String,String> parm) {
        String page = ValueUtil.coalesce( parm.get("page") , "0") ;
        String activityName= parm.get(" ");
        Map<String,String> map1=new HashMap<>();
        if (ValueUtil.notEmpity(activityName) ) {
            map1.put("activityName",activityName);
        }
        Pageable pageable = new PageRequest(Integer.valueOf(page),defaultPageSize);
        Page<Activity> categories = repositoty.findAll(getWhereClause(activityName),pageable);
        Map<String,Object> map = new HashMap<>();
        map.put("page",categories);
        map.put("condition",map1);
        return map;
    }
    private Specification<Activity> getWhereClause(String name) {
        return new Specification<Activity>() {
            @Override
            public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(name)) {
                    predicate.getExpressions().add( cb.like(root.<String>get("name"),"%"+name+"%") );
                }
                predicate.getExpressions().add( cb.equal(root.<Integer>get("isDelete"),0));
                return predicate;
            }
        };
    }

}