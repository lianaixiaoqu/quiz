package com.hzbuvi.quiz.backUser.entity;

import com.hzbuvi.util.encryption.Password;
import com.hzbuvi.util.encryption.SHA;

import javax.persistence.*;

/**
 * Created by WangDianDian on 2016/10/28.
 */
@Entity
@Table(name = "BACKGROUNDUSER")
public class BackgroundUser {
    @Id
    @GeneratedValue( strategy = GenerationType.TABLE )
    private int id;
    private String loginName;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
		String passwd = SHA.e512(password);
		this.password = passwd;
    }
    public boolean verifyPassword(String inputPassword){
		if (this.password.equals(SHA.e512(inputPassword))) {
			return true;
		} else {
			return false;
		}
	}
}

