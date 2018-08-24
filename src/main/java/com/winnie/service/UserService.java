package com.winnie.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.winnie.controller.UserController;
import com.winnie.domain.Result;
import com.winnie.domain.User;
import com.winnie.repository.UserRepository;
import com.winnie.utils.ResultUtil;
import static com.winnie.enums.ResultEnum.*;
/*
 * Created by彭文钰
 * 2018/4/23 19:22
 * */
@Service
public class UserService {

	@Autowired
    private UserRepository userRepository;
	
	private final static Logger logger = LoggerFactory.getLogger(UserService.class);
    /*
    * 添加一个用户验证用户名、密码是否存在，用户名是否重复
    * */
    public Result<User> userAdd(User user){
        User user1 = new User();
        user1.setUserName(user.getUserName());
        /*if(bindingResult.hasErrors()){
            return ResultUtil.error(-100, bindingResult.getFieldError().getDefaultMessage());
        }else *//*if(user.getUserName().length()==0) {
        	return ResultUtil.error(USERNAME_NULL);
        }else if(user.getPassword().length()==0) {
        	return ResultUtil.error(PASSWORD_NULL);
        }else */
        if((userRepository.exists(Example.of(user1)))==true){
            return ResultUtil.error(USERNAME_EXSIT);
        }
        /*logger.info("2:"+user.getUserName());
        logger.info("2:"+user.getPassword());*/
        return ResultUtil.success(userRepository.save(user));
    }

    public Result<User> userLogin(String userName, String password) {
        if(userName == null){
            return ResultUtil.error(USERNAME_NULL);
        }else if(password == null){
            return ResultUtil.error(PASSWORD_NULL);
        }
        User user = new User();
        user.setUserName(userName);
        if(userRepository.findOne(Example.of(user)).isPresent() == false){
            return ResultUtil.error(USERNAME_ERROR);
        }else if(userRepository.findOne(Example.of(user)).get().getPassword().equalsIgnoreCase(password)!= true){
            return ResultUtil.error(PASSWORD_ERROR);
        }
        return ResultUtil.success(userRepository.findOne(Example.of(user)).get());
    }
    /*
     * 修改更新用户信息(性别，年龄)
     */
    public Result<User> userUpdate(User user){
        return ResultUtil.success(userRepository.save(user));
    }

    /**
	 * 修改密码
	 */
	public Result<User> userPsUpdate(Integer userId, String password1, String password2) {
		User user1 = new User();
		user1.setUserId(userId);
		user1.setPassword(password1);
		if(userRepository.exists(Example.of(user1)) == false) {
			return ResultUtil.error(PASSWORD_ERROR3);
		}
		user1 = userRepository.findById(userId).get();
		user1.setPassword(password2);
		return ResultUtil.success(userRepository.save(user1));
	}
}

