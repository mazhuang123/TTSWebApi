package com.mz.ttswebapiproject;

import android.util.Log;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 16:43
 * @Description 文件描述：
 */
public class LogUtil {
    public static final String TAG = "WEBAPI";
    public static void play(String message){
        Log.e("play+++", message);
    }
    public static void httpLog(String message){
        Log.e("httplog+++", "httpLog: "+message);
    }
    public static void e(String message){
        Log.e(TAG, message);
    }
}
