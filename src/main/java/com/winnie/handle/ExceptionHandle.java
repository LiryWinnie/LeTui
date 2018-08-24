package com.winnie.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winnie.domain.Result;
import com.winnie.exception.UserException;
import com.winnie.utils.ResultUtil;
import static com.winnie.enums.ResultEnum.UNKONW_ERROR;
/*
 * Created by彭文钰
 * 2018/4/23 20:47
 * */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof UserException) {
            UserException userException = (UserException) e;
            return ResultUtil.error(userException.getCode(), userException.getMessage());
        }else {
            logger.error("【系统异常】{}", e);
            return ResultUtil.error(UNKONW_ERROR);
        }
    }
}