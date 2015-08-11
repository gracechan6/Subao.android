package com.jinwang.subao.normal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dreamy on 2015/6/29.
 */
public class UserInfo {
    public  String uuid;
    public  String tel;
    public  String userName;
    public  String password;
    public  String loginTime;
    public  String role;
   /* @JsonProperty("UUID")
    String uuid;

    @JsonProperty("tel")
    String tel;

    @JsonProperty("username")
    String userName;

    @JsonProperty("password")
    String password;

    @JsonProperty("login_time")
    String loginTime;

    @JsonProperty("role")
    String role;*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUUId()
    {
        return uuid;
    }

    public void setUUId(String uuid)
    {
        this.uuid = uuid;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

}
