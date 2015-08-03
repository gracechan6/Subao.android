package com.jinwang.subao.normal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dreamy on 2015/7/3.
 */
public class RegisterResult {
    @JsonProperty("CHECKCODE")
    private String CHECKCODE;

    @JsonProperty("returnFlag")
    private String returnFlag;

    @JsonProperty("MOBILEPHONE")
    private String MOBILEPHONE;

    @JsonProperty("errMsg")
    String errMsg;

    public String getCHECKCODE() {
        return CHECKCODE;
    }

    public void setCHECKCODE(String CHECKCODE) {
        this.CHECKCODE = CHECKCODE;
    }

    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public String getMOBILEPHONE() {
        return MOBILEPHONE;
    }

    public void setMOBILEPHONE(String MOBILEPHONE) {
        this.MOBILEPHONE = MOBILEPHONE;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
