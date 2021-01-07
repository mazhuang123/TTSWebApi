package com.mz.ttswebapiproject.http;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/6 19:59
 * @Description 文件描述：网络请求结果处理类
 *
 * */
public class TTSHttpResultProcessor implements TTSHttpRequestProcessor.TTSHttpListener {
    private int currentIndex;
    
    public void next(){

    }
    public void start(){

    }
    public void stop(){

    }
    public void resume(){

    }

    @Override
    public void onTTSHttpOperatorSuccess(String requestContent, int index, byte[] resultBytes) {

    }

    @Override
    public void onTTsHttpOperatorFail(String requestContent, int index, String failInfo) {

    }
}
