package com.niuyun.hire.ui.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/1.
 */

public class SuperBean<T> implements Serializable {
    private int code;
    private String msg;

    public SuperBean() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    private T data;
}
