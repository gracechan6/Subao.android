package com.jinwang.subao.normal.config;

/**
 * Created by dreamy on 2015/6/23.
 */
public class UrlParam {
    //端口号
    private static final String HOST = "http://192.168.0.50:8080/";
    private static final String BASE_URL = HOST + "ybpt/web/";
    //登陆接口url
    public static final String LOGIN_URL = BASE_URL + "ybmobile/MobileLogin_Login.action?";
    //注册url
    public static final String REGISTER_URL = BASE_URL + "ybmobile/MobileReg_Register.action?";
    //发送验证码url
    public static final String GET_VERCODE_URL = BASE_URL + "ybmobile/CheckMobileCode_produceCode.action?";
    //验证码验证url
    public static final String VERCODE_URL = BASE_URL + "ybmobile/CheckMobileCode_checkCode.action?";
    //头像图片上传url
    public static final String SENDPICTURE_URL = BASE_URL + "ybplatform/mobileUpload.jsp?";
    //头像图片下载url
    public static final String GETPICTURE_URL = BASE_URL + "ybmobile/DownLoadPic_downLoadPic.action?";
    //密码修改url
    public static final String CHANGEPASSWORD_URL = BASE_URL + "ybmobile/ChangePwd_updatePwd.action?";
    //忘记密码之发送验证码url
    public static final String GETPWDFIRST_URL = BASE_URL + "ybmobile/ForgetPwd_doPwdFirst.action?";
    //忘记密码之获得密码url
    public static final String GETPWDSECOND_URL = BASE_URL + "ybmobile/ForgetPwd_doPwdSecond.action?";
    //推送消息url
    public static final String SENDMESSAGE_URL = BASE_URL + "ybmobile/MobileIosPush_push.action?";
}
