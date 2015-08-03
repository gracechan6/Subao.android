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
}
