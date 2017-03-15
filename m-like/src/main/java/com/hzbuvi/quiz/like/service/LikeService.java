package com.hzbuvi.quiz.like.service;

import com.hzbuvi.quiz.like.entity.LikeType;
import com.hzbuvi.quiz.like.entity.RankLike;
import com.hzbuvi.quiz.like.repository.LikeRepository;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by WangDianDian on 2016/10/13.
 */
@Service
public class LikeService {
    @Autowired
    LikeRepository likeRepository;
    public static Integer id;

    public boolean insert(Integer activityId, Integer userId, Integer targetId, LikeType likeType)  {

        List<RankLike> rankLikes = likeRepository.findByActivityIdAndUserIdAndTargetId(activityId, userId, targetId);
        if (null == rankLikes || rankLikes.size() == 0 ) {
            RankLike rankLike = new RankLike();
            rankLike.setActivityId(activityId);
            rankLike.setLikeType(likeType);
            rankLike.setUserId(userId);
            rankLike.setTargetId(targetId);
            rankLike.setCreateTime(new Date());
            likeRepository.save(rankLike);
			return true;
        } else {
            likeRepository.delete(rankLikes);
            return false;
        }
    }

	public Map<Integer, Integer> findLike(List<Integer> userids, Integer activityId, LikeType likeType) {
		List<RankLike> rankLikes = likeRepository.findByTargetIdInAndActivityIdAndLikeType(userids,activityId,likeType);

		Map<Integer,Integer> map = new HashMap<>();

		for (int i = 0; i < rankLikes.size() ; i++) {
			if (null == map.get(rankLikes.get(i).getTargetId())) {
				map.put(rankLikes.get(i).getTargetId(),1);
			} else {
				Integer val = map.get(rankLikes.get(i).getTargetId());
				map.put(rankLikes.get(i).getTargetId(),val+1);
			}
		}

		return map;
	}


	public List<Integer> likeTarget(Integer activityId, Integer userId, LikeType person) {
		List<RankLike>	rankLikes = likeRepository.findByActivityIdAndUserIdAndLikeType(activityId,userId,person);
		List<Integer> targetId = new ArrayList<>();
		rankLikes.forEach( r -> targetId.add(r.getTargetId()) );
		return targetId;
	}
}