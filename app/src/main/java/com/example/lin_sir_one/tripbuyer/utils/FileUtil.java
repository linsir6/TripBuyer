package com.example.lin_sir_one.tripbuyer.utils;

import android.os.Environment;

/**
 * Created by linSir on 16/7/21.判断文件是否存在
 */
public class FileUtil {
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
