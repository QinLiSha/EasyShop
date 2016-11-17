package com.feicuiedu.com.easyshop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户实体类
 */
@SuppressWarnings("unused")
public class User implements Serializable {

    @SerializedName("username")
    private String name;
    private String password;
    @SerializedName("name")
    private String hx_Id;
    @SerializedName("uuid")
    private String table_Id;
    @SerializedName("other")
    private String head_Image;
    @SerializedName("nickname")
    private String nick_Name;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getHx_Id() {
        return hx_Id;
    }

    public String getTable_Id() {
        return table_Id;
    }

    public String getHead_Image() {
        return head_Image;
    }

    public String getNick_Name() {
        return nick_Name;
    }

    public void setNick_Name(String nick_Name) {
        this.nick_Name = nick_Name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHx_Id(String hx_Id) {
        this.hx_Id = hx_Id;
    }

    public void setTable_Id(String table_Id) {
        this.table_Id = table_Id;
    }

    public void setHead_Image(String head_Image) {
        this.head_Image = head_Image;
    }
}
