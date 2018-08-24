package com.winnie.domain;

/*
 * Created by彭文钰
 * 2018/4/23 19:01
 * */
public class Result<T> {

	/*错误码。 */
    private Integer code;

    /*提示信息。*/
    private String msg;

    /*具体内容。*/
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
