package com.mz.ttswebapiproject.module.synthesizer;

import com.mz.ttswebapiproject.listener.TTSDataLoadListener;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/15 16:35
 * @Description 文件描述：用于描述合成器应具备的能力
 */
public interface DataSynthesizer {

     void synthesizeStart(String content, int index);

     void addTTSDataLoadListener(TTSDataLoadListener ttsDataLoadListener);

     void cancelSynthesize();

}
