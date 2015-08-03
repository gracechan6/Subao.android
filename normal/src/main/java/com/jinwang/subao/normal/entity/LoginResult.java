package com.jinwang.subao.normal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dreamy on 2015/7/5.
 */
public class LoginResult {

    @JsonProperty("returnFlag")
    String returnFlag;

    @JsonProperty("userName")
    String userName;

    @JsonProperty("mUuid")
    String mUuid;

    @JsonProperty("role")
    String role;

    @JsonProperty("errMsg")
    String errMsg;
    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getmUuid() {
        return mUuid;
    }

    public void setmUuid(String mUuid) {
        this.mUuid = mUuid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
