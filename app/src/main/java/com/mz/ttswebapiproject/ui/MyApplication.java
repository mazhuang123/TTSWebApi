package com.mz.ttswebapiproject.ui;

import android.app.Application;
import android.content.Context;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/23 17:33
 * @Description 文件描述：
 */
public class MyApplication extends Application {
    private Context context;
    private static MyApplication instance;
    public static MyApplication getInstance(){
        if(instance == null){
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public Context getContext(){
        return context;
    }
}
