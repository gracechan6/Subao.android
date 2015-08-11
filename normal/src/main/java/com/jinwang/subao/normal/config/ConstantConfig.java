package com.jinwang.subao.normal.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by dreamy on 2015/6/30.
 */
public class ConstantConfig {
    public static final String OS = "Android";
    public static final String ROLE = "Group0004";
    /**
     * 额外存储空间目录
     */
    public static final String EXTERNAL_FILE_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/yongbao";
    /**
     * 临时文件
     */
    public static final String TEMP_FILE_PATH = EXTERNAL_FILE_PATH + File.separator + ".temp";
    /**
     * 头像目录
     */
    public static final String HEADER_PATH = EXTERNAL_FILE_PATH + File.separator + "headers";
    /**
     * 重连成功
     */
    public static final boolean RECONNECT_STATE_SUCCESS = true;
    /**
     * 重连失败
     */
    public static final boolean RECONNECT_STATE_FAIL = false;
    /**
     * 重连结果
     */
    public static final String RECONNECT_RESULT = "RECONNECT_RESULT";
}
