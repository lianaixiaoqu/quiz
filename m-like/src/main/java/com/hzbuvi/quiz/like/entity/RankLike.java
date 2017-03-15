package com.hzbuvi.quiz.like.entity;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by WangDianDian on 2016/10/13.
 */
@Entity
@Table(name="ranklike")
public class RankLike {
    @Id
	@GeneratedValue( strategy = GenerationType.TABLE )
    private Integer id;
    private Integer userId;
    private Integer targetId;
	@Enumerated(EnumType.ORDINAL)
    private LikeType likeType;
    private Integer activityId;
	private Date createTime;


	public RankLike() {
		this.createTime = new Date();
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public LikeType getLikeType() {
		return likeType;
	}

	public void setLikeType(LikeType likeType) {
		this.likeType = likeType;
	}

	public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
