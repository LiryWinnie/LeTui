package com.winnie.exception;

import com.winnie.enums.ResultEnum;

/*
 * Created by彭文钰
 * 2018/4/23 19:27
 * */
public class UserException extends RuntimeException {

    private Integer code;

    public UserException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
