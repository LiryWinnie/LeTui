package com.winnie.controller;

import java.util.HashMap;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.winnie.domain.Result;
import com.winnie.domain.User;

import com.winnie.enums.ResultEnum;
import com.winnie.repository.UserRepository;
import com.winnie.service.UserService;
import com.winnie.utils.ResultUtil;


/*
 * Created by彭文钰
 * 2018/4/23 18:53
 * */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

	private Object username;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    /*
    *查询一个用户信息
     */
    @GetMapping(value = "/{userId}")
    public User userInfo(@PathVariable("userId") Integer userId){
        return userRepository.findById(userId).get();
    }

    
    /*
    *添加一个用户
    * */
    @PostMapping(value = "/add")
    public Result<User> userAdd(@RequestBody Map map){
    	/*logger.info(map.get("username").toString());
    	logger.info(map.get("password").toString());*/
    	User user = new User();
    	user.setUserName(map.get("username").toString());
    	user.setPassword(map.get("password").toString());
        return userService.userAdd(user);
    }

    /*
    * 验证用户登录信息
    * */
    @PostMapping(value = "/login")
    public Result<User> userLogin(@RequestBody Map map) /*throws UnsupportedEncodingException*/{
        String userName = map.get("username").toString();
        /*byte[] utf8Bytes = userName.getBytes("UTF-8");
        String nameString = new String(utf8Bytes,"UTF-8");*/
        String password = map.get("password").toString();
        return userService.userLogin(userName, password);
    }
    /*
    * 修改更新用户信息
     */
    @PostMapping(value = "/update/{userId}")
    public Result<User> userUpdate(@PathVariable("userId") Integer userId,
            					   @RequestBody User user){
        User user1 = userRepository.findById(userId).get();
        user1.setAge(user.getAge());
        user1.setGender(user.getGender());
        return userService.userUpdate(user1);
    }

    /**
	 * 修改密码
	 */
    @PostMapping(value = "/update/password/{userId}")
    public Result<User> userPsUpdate(@PathVariable("userId") Integer userId,@RequestBody Map map){
    	String password1 = map.get("oldpassword").toString();
    	String password2 = map.get("newpassword").toString();
    	return userService.userPsUpdate(userId,password1,password2);
    }
}
