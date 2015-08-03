package com.jinwang.subao.normal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dreamy on 2015/6/29.
 */
public class UserInfo {
    @JsonProperty("UUID")
    String uuid;

    @JsonProperty("tel")
    String tel;

    @JsonProperty("username")
    String userName;

    @JsonProperty("login_time")
    String loginTime;

    @JsonProperty("os_type")
    String osType;

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

}
