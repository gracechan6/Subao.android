package com.jinwang.subao.normal.config;

/**
 * Created by dreamy on 2015/6/23.
 */
public class UrlParam {
    private static final String HOST = "http://192.168.0.50:8080/";
    private static final String BASE_URL = HOST + "ybpt/web/ybmobile/";
    public static final String LOGIN_URL = BASE_URL + "MobileLogin_Login.action?";
    public static final String GET_VERCODE_URL = BASE_URL + "CheckMobileCode_produceCode.action?";
    public static final String VERCODE_URL = BASE_URL + "CheckMobileCode_checkCode.action?";
    public static final String REGISTER_URL = BASE_URL + "MobileReg_Register.action?";


}
