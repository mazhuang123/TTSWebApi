package com.mz.ttswebapiproject.http;

import com.mz.ttswebapiproject.listener.TTSHttpProcessorListener;
import com.mz.ttswebapiproject.util.FileOperator;
import com.mz.ttswebapiproject.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/6 19:59
 * @Description 文件描述：网络请求结果处理类
 *
 * */
public class TTSHttpProcessor implements TTSHttpUtil.TTSHttpListener {
    private int currentIndex;
    private TTSHttpUtil httpRequestProcessor;
    private String[] contentArray;
    public TTSHttpProcessor(String[] contentArray) {
        this.contentArray = contentArray;
        httpRequestProcessor = new TTSHttpUtil();
        httpRequestProcessor.addTTSHttpListener(this);
    }

    /**
     * 第一次开始请求，理论上只需调用一次
     * @param content
     */
    public void startRequest(String content){
        if(httpRequestProcessor!=null){
            httpRequestProcessor.startPost(content,0);
        }
    }

    /**
     *  从章节中间的某一处开始请求，理论上只需调用一次
     * @param content
     * @param resumeIndex
     */
    public void resumeRequest(String content,int resumeIndex){
        if(httpRequestProcessor!=null){
            currentIndex = resumeIndex;
            httpRequestProcessor.startPost(content,resumeIndex);
        }
    }

    /**
     * 开始启动之后，后续的请求都是走这个方法
     */
    public void nextRequest(){
        if(httpRequestProcessor!=null){
            currentIndex++;
            httpRequestProcessor.startPost(contentArray[currentIndex],currentIndex);
        }
    }

    /**
     * 停止请求
     */
    public void stopRequest(){

    }

    @Override
    public void onTTSHttpOperatorSuccess(String requestContent, int index, byte[] resultBytes) {
        FileOperator.getInstance().saveFileIntoLocal(index,resultBytes);
        for(TTSHttpProcessorListener ttsHttpProcessorListener:ttsHttpProcessorListenerList){
            ttsHttpProcessorListener.onTTSHttpOperatorSuccess(requestContent,index);
        }
        if (currentIndex < contentArray.length) {
            if(FileOperator.getInstance().loadFileFromMap(currentIndex) == null || !FileOperator.getInstance().loadFileFromMap(currentIndex).exists()){
                nextRequest();
            }
        } else {
            stopRequest();
            LogUtil.httpLog("网络请求已经到最后一个");
        }
    }

    @Override
    public void onTTsHttpOperatorFail(String requestContent, int index, String failInfo) {

    }
    private List<TTSHttpProcessorListener> ttsHttpProcessorListenerList = new ArrayList<>();
    public void addTTSHttpProcessorListener(TTSHttpProcessorListener ttsHttpProcessorListener){
        if(ttsHttpProcessorListenerList!=null){
            ttsHttpProcessorListenerList.add(ttsHttpProcessorListener);
        }
    }
    public void removeTTSHttpProcessorListener(TTSHttpProcessorListener ttsHttpProcessorListener){
        if(ttsHttpProcessorListenerList != null){
            ttsHttpProcessorListenerList.remove(ttsHttpProcessorListener);
        }
    }
}
