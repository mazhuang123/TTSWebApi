package com.mz.ttswebapiproject.ui;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechUtility;
import com.mz.ttswebapiproject.R;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/23 17:33
 * @Description 文件描述：
 */
public class MyApplication extends Application {
    private static Context context;
    private static MyApplication instance;
    public static MyApplication getInstance(){
        if(instance == null){
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        // 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
        // 设置你申请的应用appid
        StringBuffer param = new StringBuffer();
        param.append("appid=" + getString(R.string.app_id));
        SpeechUtility.createUtility(getApplicationContext(), param.toString());
        super.onCreate();
        context = this;
    }
    public static Context getContext(){
        return context;
    }
}
