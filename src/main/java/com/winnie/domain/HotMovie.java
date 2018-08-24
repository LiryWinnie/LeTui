package com.winnie.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
* Created by彭文钰
* 2018年5月19日 下午11:50:12
*/
@Entity
public class HotMovie {

	@Id
    @GeneratedValue
	private Integer hid;
	
	private String did;
	
	private Integer times;

	public Integer getHid() {
		return hid;
	}

	public void setHid(Integer hid) {
		this.hid = hid;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}
	
}
