package com.hzbuvi.quiz.section.repository;

import com.hzbuvi.quiz.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Downey.hz on 2016/10/11..
 */
@Repository
public interface SectionRepository extends JpaRepository<Section,Integer> {

	List<Section> findByActivityIdAndEndOfChapter(Integer activityId ,Integer endOfChapter);

	List<Section> findByActivityIdOrderBySequenceInActivity(Integer activityId);

	Integer countByActivityId(Integer activityId);

	Section findByIdAndActivityId(Integer id,Integer activityId);

	void deleteByActivityId(int activityId);

	Section findByChapterIdAndEndOfChapter(Integer chapterId, int i);

//    List<Section> findByActivityIdAndChapterId(Integer activityId, Integer chapterId);

	List<Section> findByActivityIdAndChapterIdOrderBySequenceInActivityAsc(Integer activityId, Integer chapterId);
}
