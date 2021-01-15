package com.mz.ttswebapiproject.module.request;

import com.mz.ttswebapiproject.listener.TTSDataLoadListener;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/15 16:35
 * @Description 文件描述：
 */
public interface DataSynthesizeModule {

     void synthesizeStart(String content, int index);

     void addTTSDataLoadListener(TTSDataLoadListener ttsDataLoadListener);
}
