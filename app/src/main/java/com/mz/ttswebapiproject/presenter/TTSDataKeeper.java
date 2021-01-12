package com.mz.ttswebapiproject.presenter;

import android.content.Context;
import android.os.Environment;

import com.mz.ttswebapiproject.bean.AudioConfig;

import androidx.annotation.NonNull;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/7 16:55
 * @Description 文件描述： https://blog.csdn.net/fd2025/article/details/79711198
 */
public class TTSDataKeeper {
    private static TTSDataKeeper instance;
    private boolean isCreate;
    private AudioConfig audioConfig;
    private String audioPath;
    private TTSDataKeeper(){
        if(isCreate){
            throw new RuntimeException("已然被实例化一次，不能在实例化");
        }
        isCreate = true;
        audioConfig = new AudioConfig();
    }

    public static TTSDataKeeper getInstance(){
        if(instance == null){
            synchronized (TTSDataKeeper.class){
                if(instance == null){
                    instance = new TTSDataKeeper();
                }
            }
        }
        return instance;
    }

    public AudioConfig getAudioConfig() {
        return audioConfig;
    }

    public void setAudioPath(Context context){
        audioPath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath()+"/TTSFile";
    }
    public String getAudioPath(){
        return audioPath;
    }
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return instance;
    }

}
