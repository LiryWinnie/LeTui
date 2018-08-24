package com.winnie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winnie.domain.User;

/*
 * Created by彭文钰
 * 2018/4/23 18:54
 * */
public interface UserRepository extends JpaRepository<User, Integer> {
}
