package com.hzbuvi.quiz.section.service;

import com.hzbuvi.quiz.section.entity.Section;
import com.hzbuvi.quiz.section.repository.SectionRepository;
import com.hzbuvi.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 10/29/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class SectionBiz {

	@Autowired
	private SectionRepository sectionRepository;


	public void create(Integer activityId, List<Integer> chapterIds, int sectionsPerChapter) {

		List<Section> sections = new ArrayList<>();

		int seq = 1;
		for (int i = 0; i < chapterIds.size() ; i++) {
			for (int j = 0; j < sectionsPerChapter; j++) {
				Section section = new Section();
				section.setActivityId(activityId);
				section.setChapterId(chapterIds.get(i));
				section.setSequenceInChapter(j+1);
				section.setSequenceInActivity(seq++);
				if (j == sectionsPerChapter-1) {
					section.setEndOfChapter(1);
				} else {
					section.setEndOfChapter(0);
				}
				sections.add(section);
			}
		}
		sectionRepository.save(sections);
	}

////           chapterid  sectionId
//	public Map<Integer,Integer> endOfChapter(Integer activityId){
//		List<Section> sections = sectionRepository.findByActivityIdAndEndOfChapter(activityId,1);
//		Map<Integer,Integer> result = new HashMap<>();
//		sections.forEach( s -> result.put(s.getChapterId(),s.getId()));
//		return result;
//	}

	public boolean isEndOfChapter(Integer sectionId){
		Section section = sectionRepository.findOne(sectionId);
		if(ValueUtil.isEmpity(section)){
			return  false;
		}else {
			if (1 == section.getEndOfChapter()) {
				return true;
			} else {
				return false;
			}
		}

	}

	public List<Integer> getIdByActivityId(Integer activityId) {
		List<Section> sections = sectionRepository.findByActivityIdOrderBySequenceInActivity(activityId);
		System.out.println(ValueUtil.toJson(sections));
		List<Integer> ids = new ArrayList<>();
		sections.forEach( s -> ids.add(s.getId())  );
		System.out.println(ValueUtil.toJson(ids));
		return ids;
	}

	public List<Section> allSectionsByActivityId(Integer activityId) {
		return sectionRepository.findByActivityIdOrderBySequenceInActivity(activityId);
	}

	public Integer sectionsCount(Integer activityId){
		return sectionRepository.countByActivityId(activityId);
	}


	public Section findOne(Integer sectionId) {
		return sectionRepository.findOne(sectionId);

	}

	public void deleteByActivityId(int activityId) {
		sectionRepository.deleteByActivityId(activityId);
	}

	public boolean endOfActivity(Integer activityId,Integer sectionId) {
		Section section = sectionRepository.findOne(sectionId);
		Integer cnt  = sectionRepository.countByActivityId(activityId);
		if ( cnt.equals(section.getSequenceInActivity()) ) {
			return true;
		} else {
			return false;
		}
	}

	public Integer findEndSectionId(Integer chapterId) {
		Section section = sectionRepository.findByChapterIdAndEndOfChapter(chapterId,1);
		if ( null != section ) {
			return section.getId();
		} else {
			return null;
		}
	}

	public boolean endOfSection(Integer sectionSize, Integer sections) {
		if(sectionSize==0){
			return false;
		}
		if(sectionSize==sections){
			return  true;
		}else {
			return false;
		}
	}
}
