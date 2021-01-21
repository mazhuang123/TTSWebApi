package com.mz.ttswebapiproject.listener;

import com.mz.ttswebapiproject.bean.SentenceInfo;
import com.mz.ttswebapiproject.config.PlayState;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 14:26
 * @Description 文件描述：
 */
public interface TTSPlayProcessorListener {

    void onPlayProcessorFileNotFound(int index);

    void onPlayProcessorState(PlayState playState,int index);

    void onPlayProcessorItemCompletion(int index, float progress);

    void onPlayProcessorPlaying(int index, boolean isPlaying, SentenceInfo sentenceInfo);

}
