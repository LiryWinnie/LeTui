package com.winnie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winnie.domain.HotMovie;

/**
* Created by彭文钰
* 2018年5月18日 下午5:41:41
*/
public interface MovieRepository extends JpaRepository<HotMovie, Integer> {
	
}
