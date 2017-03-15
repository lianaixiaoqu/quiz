package com.hzbuvi.quiz.backUser.repository;

import com.hzbuvi.quiz.backUser.entity.BackgroundUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by WangDianDian on 2016/10/28.
 */
@Repository
public interface BackUserRepository extends JpaRepository<BackgroundUser,Integer> {
    BackgroundUser findByLoginName(String loginName);
}
