package com.winnie.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
* Created by彭文钰
* 2018年5月18日 下午5:56:17
*/
@Entity
public class Score {

	@Id
    @GeneratedValue
	private Integer sid;
	
	@NotNull
	private String did;
	
	@NotNull
	private Integer userId;
	
	private Integer score;

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
    
	
}
