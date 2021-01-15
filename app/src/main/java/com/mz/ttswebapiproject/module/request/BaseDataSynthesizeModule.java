package com.mz.ttswebapiproject.module.request;

import com.mz.ttswebapiproject.listener.TTSDataLoadListener;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/15 11:32
 * @Description 文件描述：
 */
public abstract class BaseDataSynthesizeModule {

    abstract void initSynthesizeModule();

    abstract void synthesizeStart(String content, TTSDataLoadListener ttsDataLoadListener);

}