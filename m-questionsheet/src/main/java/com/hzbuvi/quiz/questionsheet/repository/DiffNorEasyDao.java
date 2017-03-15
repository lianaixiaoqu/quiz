package com.hzbuvi.quiz.questionsheet.repository;

import com.hzbuvi.quiz.questionsheet.bean.DiffNorEasy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by light on 2016/11/9.
 */
public interface DiffNorEasyDao extends JpaRepository<DiffNorEasy,Integer> {
    DiffNorEasy findByActivityId(Integer activityId);
}
