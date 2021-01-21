package com.mz.ttswebapiproject.module.text;

import com.mz.ttswebapiproject.config.Config;
import com.mz.ttswebapiproject.listener.TTSTextLoadListener;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/20 17:00
 * @Description 文件描述：
 */
public class TTSDataSource {
    private TTSTextLoadListener ttsTextLoadListener;
    public void addTTSLoadListener(TTSTextLoadListener ttsTextLoadListener){
        this.ttsTextLoadListener = ttsTextLoadListener;
    }
    public void requestTextData(String chapterId){
        String content = Config.getContent();//这里就是去请求数据的流程
        ttsTextLoadListener.onTextLoadSuccess(content);
    }

}
