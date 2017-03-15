package com.hzbuvi.quiz.prize.repository;

import com.hzbuvi.quiz.prize.entity.ChapterPrize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by WangDianDian on 2016/11/1.
 */
public interface ChapterPrizeRepository extends JpaRepository<ChapterPrize,Integer> {
   List<ChapterPrize> findByActivityIdAndChapterId(Integer activityId,Integer chapterId);
   ChapterPrize findByChapterIdAndTitle(Integer chapterId,String title);

    @Query("select id from ChapterPrize where activityId = :activityId  and   chapterId = :chapterId  order by id ")
    List<Integer> findIdByactivityIdOrderByIdAs(@Param("activityId") Integer activityId,@Param("chapterId") Integer chapterId);
    List<ChapterPrize> findByChapterIdOrderByIdAsc(Integer chapterId);
    ChapterPrize findByActivityIdAndChapterIdAndId(Integer activityId,Integer chapterId,Integer Id);

}
