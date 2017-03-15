package com.hzbuvi.quiz.backUser.service;

import com.hzbuvi.quiz.backUser.entity.BackgroundUser;
import com.hzbuvi.quiz.backUser.repository.BackUserRepository;
import com.hzbuvi.util.basic.ValueUtil;
import com.hzbuvi.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by WangDianDian on 2016/10/28.
 */
@Service
public class BackGroundService {
    @Autowired
    private BackUserRepository backUserRepository;
    public String login(String loginName,String password) throws HzbuviException {
        ValueUtil.verify(loginName,"loginNameNull");
        ValueUtil.verify(password,"passwordNull");
        ValueUtil.verify(backUserRepository.findByLoginName(loginName),"failure");
//        String passwordReal=backUserRepository.findByLoginName(loginName).getPassword();
		BackgroundUser user = backUserRepository.findByLoginName(loginName);
        if(user.verifyPassword(password)){
            return "success";
        }
        return "failure";
    }
    public BackgroundUser get(Integer id){
        return backUserRepository.findOne(id);
    }
    public String register(String loginName,String password,String passwordReal) throws HzbuviException {
        ValueUtil.verify(loginName,"loginNameNull");
        ValueUtil.verify(password,"passwordNull");
        ValueUtil.verify(passwordReal,"请再次输入密码");
        if(null==backUserRepository.findByLoginName(loginName)&&password.equals(passwordReal)) {
            BackgroundUser backgroundUser = new BackgroundUser();
            backgroundUser.setLoginName(loginName);
            backgroundUser.setPassword(password);
            backUserRepository.save(backgroundUser);
            return "success";
        }
        return "failure";
    }
    public List<BackgroundUser> getAll(){
        return backUserRepository.findAll();
    }
    public String delete(Integer id){
        backUserRepository.delete(id);
        return "success";
    }
    public String changePassword(Integer id,String password,String newPassword) throws HzbuviException {
        ValueUtil.verify(password,"密码为空");
        ValueUtil.verify(newPassword,"密码为空");
        if(password.equals(newPassword)){
            BackgroundUser backgroundUse = backUserRepository.findOne(id);
            backgroundUse.setPassword(newPassword);
            backUserRepository.save(backgroundUse);
            return "success";
        }
        return "两次密码不同";
    }
}
