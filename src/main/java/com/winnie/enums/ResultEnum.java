package com.winnie.enums;

/*
 * Created by彭文钰
 * 2018/4/23 19:16
 * */
public enum ResultEnum {
	UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    USERNAME_NULL(-100, "用户名不能为空"),
    USERNAME_EXSIT(-101, "用户名已存在"),
    PASSWORD_NULL(-102, "密码不能为空"),
    PASSWORD_ERROR(-103, "密码输入错误"),
    PASSWORD_ERROR2(-104, "两次输入的密码不一致"),
    USERNAME_ERROR(-105, "用户名不存在"),
    PASSWORD_ERROR3(-106, "原密码输入错误"),
    SCORE_UNEXSIT(-107, "评分不存在")
    ;

	private Integer code;
	
	private String msg;
	
	private ResultEnum(Integer code, String msg) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.msg = msg;
	}
	
	public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
