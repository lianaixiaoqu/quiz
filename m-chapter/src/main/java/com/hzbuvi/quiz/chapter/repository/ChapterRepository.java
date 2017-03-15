package com.hzbuvi.quiz.chapter.repository;

import com.hzbuvi.quiz.chapter.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Downey.hz on 2016/10/11..
 */
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {

    List<Chapter> findByActivityId(Integer activityId);
    Chapter findByChapterId(Integer chapterId);
  //  Chapter findBySequenceChapter(Integer sequenceChapter);

    List<Chapter> findByActivityIdOrderBySequenceChapterAsc(Integer activitId);

	void deleteByActivityId(Integer activityId);
    @Query("select id from Chapter where activityId = :activityId  and   sequenceChapter = :sequenceChapter ")
    Integer findIdBySequenceChapter(@Param("activityId") Integer activityId, @Param("sequenceChapter") Integer sequenceChapter);

	Chapter findByActivityIdAndSequenceChapter(Integer activityId, Integer sequenceChapter);
}
