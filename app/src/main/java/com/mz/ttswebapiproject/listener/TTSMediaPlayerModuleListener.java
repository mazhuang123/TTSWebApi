package com.mz.ttswebapiproject.listener;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 14:17
 * @Description 文件描述：
 */
public interface TTSMediaPlayerModuleListener {

    void onMediaPlayerFileNotFound(int index);

    void onMediaPlayerError(int index,String errorInfo);

    void onMediaPlayerPause(int index);

    void onMediaPlayerStop(int index);

    void onMediaPlayerCompletion(int index);

    void onMediaPlayerPlaying(int index,boolean isPlaying);

}
