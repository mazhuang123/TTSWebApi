package com.mz.ttswebapiproject.listener;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/11 17:41
 * @Description 文件描述：
 */
public interface TTSConfigItemListener {

    void onSpeakerItemClick(String content);

    void onSampleItemClick(String content);

    void onFormatItemClick(String content);
}
