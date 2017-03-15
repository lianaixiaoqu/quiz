package com.hzbuvi.quiz.like.repository;

import com.hzbuvi.quiz.like.entity.LikeType;
import com.hzbuvi.quiz.like.entity.RankLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by WangDianDian on 2016/10/13.
 */
@Repository
public interface LikeRepository extends JpaRepository<RankLike,Integer>{
    List<RankLike> findByActivityIdAndUserIdAndTargetId(Integer activityId, Integer userId, Integer targetId);
	List<RankLike> findByActivityIdAndUserIdAndLikeType(Integer activityId, Integer userId, LikeType likeType);


	List<RankLike> findByTargetIdInAndActivityIdAndLikeType(List<Integer> targetId , Integer activityId, LikeType likeType);

}
