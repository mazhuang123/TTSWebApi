package com.mz.ttswebapiproject.listener;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 14:26
 * @Description 文件描述：
 */
public interface TTSPlayProcessorListener {

    void onPlayProcessorFileNotFound(int index);

    void onPlayProcessorError(int index,String errorInfo);


    void onPlayProcessorPause(int index);

    void onPlayProcessorStop(int index);

    void onPlayProcessorCompletion(int index);

    void onPlayProcessorPlaying(int index,boolean isPlaying);

    void onPlayerProcessorPlayState(int index,boolean isPlaying);

    void onPlayProcessorOver();

}
