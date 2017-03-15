package com.hzbuvi.quiz.chapter.service;

import com.hzbuvi.quiz.chapter.entity.Chapter;
import com.hzbuvi.quiz.chapter.repository.ChapterRepository;
import com.sun.corba.se.impl.io.ValueUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Downey.hz on 2016/10/11..
 */
@Service
public class ChapterService {
    @Autowired
    ChapterRepository chapterRepository;

    public List<Chapter> showChapter(Integer activitId){
		if(null==chapterRepository.findByActivityId(activitId)){
			return null;
		}else
        return  chapterRepository.findByActivityIdOrderBySequenceChapterAsc(activitId);
    }

	public List<Integer> create(Integer activityId, Integer chapters, Integer sectionsPerChapter) {
		List<Chapter> list = new ArrayList<>();
		for (int i = 0; i < chapters ; i++) {
			Chapter chp = new Chapter();
			chp.setActivityId(activityId);
			chp.setSections(sectionsPerChapter);
			chp.setSequenceChapter(i+1);
			list.add(chp);
		}
		List<Integer> chpterIds = new ArrayList<>();
		chapterRepository.save(list);
		list.forEach( chpter -> chpterIds.add(chpter.getId()));
		return chpterIds;
	}
	public String setChapter(Integer activityIdOne
            ,String averageStart
            ,String sumStart
            ,String underStart
            ,String lessChapter
            ,Integer j){
        Chapter chapter;
        if(null!=chapterRepository.findIdBySequenceChapter(activityIdOne,j)){
			Integer id=chapterRepository.findIdBySequenceChapter(activityIdOne,j);
             chapter=chapterRepository.findOne(id);
        }else {
            chapter=new Chapter();
        }
//        List<String> list=new ArrayList<>();
//        List<String> list1=new ArrayList<>();
//        List<String> list2=new ArrayList<>();
		chapter.setActivityId(activityIdOne);

		String unlockCondition = "";
		String conditionType = "";
		String conditionData = "";
		String description = "";

		boolean flag = false;


        if(!"".equals(averageStart)){
//            list.add("1,");
//            list.add("A,");
//            list.add("之前所有关卡的平均星数不少于"+averageStart+",");
//            list.add(averageStart+",");
			unlockCondition = "1";
			conditionType= "A";
			conditionData = averageStart;
			description =  "之前闯关平均星数不少于"+averageStart+"星";
			flag = true;

		}
        if(!"".equals(sumStart)){
//            list1.add("1,");
//            list1.add("S");
//            list1.add("之前所有关卡需要总星数不少于"+sumStart+",");
//            list1.add(sumStart+",");
				if (flag) {
				unlockCondition += ",1";
				conditionType +=",S";
				conditionData += ","+sumStart ;
				description +=",总星数不少于"+sumStart+"星";
			} else {
				unlockCondition = "1";
				conditionType ="S";
				conditionData = ""+sumStart ;
				description ="总星数不少于"+sumStart+"星";
				flag = true;
			}
        }
        if(!"".equals(underStart)){
//            list2.add("2");
//            list2.add(underStart+":"+lessChapter);
//            list2.add("之前所有关"+underStart+"星以上的需要达到"+lessChapter+"关");
			if (flag) {
				unlockCondition += ",2";
				conditionType +=",X";
				conditionData += ","+underStart+":"+lessChapter;
				description += ","+underStart+"星以上的需要达到"+lessChapter+"关";
			} else {
				unlockCondition = "2";
				conditionType ="X";
				conditionData = underStart+":"+lessChapter;
				description = underStart+"星以上的需要达到"+lessChapter+"关";
				flag = true;
			}
        }



//        chapter.setUnlockCondition(list.get(0)+ list1.get(0)+list2.get(0));
//        chapter.setConditionData(list.get(3)+list1.get(3)+list2.get(1));
//        chapter.setConditionType(list.get(1)+list1.get(1));
//        String descrip=list.get(2)+list1.get(2)+list2.get(2);
//        chapter.setPassDescribe(descrip);
		chapter.setUnlockCondition(unlockCondition);
		chapter.setConditionData(conditionData);
		chapter.setConditionType(conditionType);
		chapter.setPassDescribe(description);
		if(null==averageStart){
			chapter.setAvgStar("0");
		}else{
			chapter.setAvgStar(averageStart);
		}
       	if(null==underStart){
			chapter.setLessStar("0");
		}else {
			chapter.setLessStar(underStart);
		}
        if(null==sumStart){
			chapter.setSumStar("0");
		}else {
			chapter.setSumStar(sumStart);
		}
		if(null==lessChapter){
			chapter.setSection("0");
		}else {
			chapter.setSection(lessChapter);
		}
        chapter.setChapterId(j);
        chapter.setSequenceChapter(j);
        chapterRepository.save(chapter);
        return "success" ;
	}

	public void deleteByActivityId(int activityId) {
		chapterRepository.deleteByActivityId(activityId);
	}

	public List<Integer> findByActivityId(int activityId) {
		List<Chapter> chapters = chapterRepository.findByActivityIdOrderBySequenceChapterAsc(activityId);
		List<Integer> ids = new ArrayList<>();
		if (null != chapters ) {
			chapters.forEach( c -> ids.add(c.getId()) );
		}

		return ids;
	}

	public Integer findByActivityIdAndSequenceChapter(Integer activityId, Integer sequenceChapter) {
		Chapter chapter = chapterRepository.findByActivityIdAndSequenceChapter(activityId,sequenceChapter);
		return chapter.getId();
	}
}
