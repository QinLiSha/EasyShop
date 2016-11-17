package com.feicuiedu.com.easyshop.model;

import com.google.gson.annotations.SerializedName;

/**
 * 网络请求返回用户对应的实体类
 */
@SuppressWarnings("unused")
public class UserResult {

    /*响应码*/
    private int code;
    /*响应消息*/
    @SerializedName("msg")
    private String message;

    private User data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public User getData() {
        return data;
    }
}
