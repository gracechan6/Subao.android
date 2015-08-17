package com.jinwang.subao.courier.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dreamy on 2015/6/23.
 */
public class PreferenceUtils {
    public static final String PREFERENCE = "PREFERENCE";
    //保存是否首次运行标识
    public static final String PREFERENCE_FIRSTRUN = "PREFERENCE_FIRSTRUN";
    //保存用户mUuid
    public static final String PREFERENCE_MUUID = "PREFERENCE_MUUID";
    //保存用户账号
    public static final String PREFERENCE_USERNAME = "PREFERENCE_USERNAME";
    //保存密码
    public static final String PREFERENCE_PASSWORD = "PREFERENCE_PASSWORD";

    public static void saveFirstRunFlag(Context context){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(PREFERENCE_FIRSTRUN, false);
        editor.commit();
    }

    public static boolean isFirstRun(Context context){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE, context.MODE_PRIVATE);
        if(sp.getBoolean(PREFERENCE_FIRSTRUN, true)){
            return true;
        }
        return false;
    }

    public static void saveUserInfo(Context context, String userName, String password,String mUuid){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREFERENCE_MUUID,mUuid);
        editor.putString(PREFERENCE_USERNAME, userName);
        editor.putString(PREFERENCE_PASSWORD, password);
        editor.commit();
    }
}
