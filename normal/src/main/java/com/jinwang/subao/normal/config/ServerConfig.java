package com.jinwang.subao.normal.config;

/**
 * Created by michael on 14/12/17.
 */
public class ServerConfig
{
    private static boolean ISLOCAL = true;
    //是否为测试模式
    private static boolean ISTEST = true;
    //测试接口
    public static final String TEST_IP = "192.168.0.50:8080";
    public static final String TEST_API = "http://192.168.0.50:8080/ybpt/web/";
    private static final String TEST_HOST_IP = "http://192.168.0.50:8080";
    //后台接口调用地址
    public static final String BASE_IP = "60.190.37.76:8096";
    public static final String BASE_API = "http://60.190.37.76:8096/BettleWeb/api/";

    public static final String getCookieIP ()
    {
        if(ISTEST)
            return TEST_IP;
        else
            return BASE_IP;
    }

    public static final String getAbsluteUrl (String action)
    {
        if(ISTEST)
            return TEST_API+action;
        else
            return BASE_API+action;
    }
    public static final String getLoginUrl (String action)
    {
        if(ISTEST)
            return TEST_API+action;
        else
            return BASE_API+action;
    }
}
