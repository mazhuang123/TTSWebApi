package com.mz.ttswebapiproject.listener;

import com.mz.ttswebapiproject.bean.SentenceInfo;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/7 17:19
 * @Description 文件描述：
 */
public interface TTSDataLoadProcessorListener {
    /**
     * 请求成功后的回调，主要是给TTSManager提供回调
     * @param requestContent
     * @param index
     */
    void onAudioRequestSuccess(String requestContent, int index, SentenceInfo sentenceInfo);

    /**
     * 合成数据失败的回调，主要是给TTSManager提供回调
     *
     *
     */
    void onAudioRequestFailed(String errorInfo);


}
