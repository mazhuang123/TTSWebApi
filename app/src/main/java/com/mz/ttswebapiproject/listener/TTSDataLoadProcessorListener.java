package com.mz.ttswebapiproject.listener;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/7 17:19
 * @Description 文件描述：
 */
public interface TTSDataLoadProcessorListener {
    /**
     * 请求成功后的回调，主要是给TTSContentPresenter提供回调数据
     * @param requestContent
     * @param index
     */
    void onAudioRequestSuccess(String requestContent, int index);




}
