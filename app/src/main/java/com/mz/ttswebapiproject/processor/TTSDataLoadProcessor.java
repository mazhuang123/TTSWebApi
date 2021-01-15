package com.mz.ttswebapiproject.processor;

import com.mz.ttswebapiproject.bean.TextConfig;
import com.mz.ttswebapiproject.listener.TTSDataLoadListener;
import com.mz.ttswebapiproject.listener.TTSDataLoadProcessorListener;
import com.mz.ttswebapiproject.module.request.DataSynthesizeModule;
import com.mz.ttswebapiproject.module.request.http.TTSHttpModule;
import com.mz.ttswebapiproject.module.request.offline_tts.LingXiModule;
import com.mz.ttswebapiproject.util.FileOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/6 19:59
 * @Description 文件描述：网络请求结果处理类，决定如何请求，请求索引等
 */
public class TTSDataLoadProcessor {
    private int currentIndex;
    private DataSynthesizeModule dataSynthesizeModule;
    private List<TextConfig> textConfigList;

    public TTSDataLoadProcessor() {
        dataSynthesizeModule = new TTSHttpModule();
        textConfigList = new ArrayList<>();
        dataSynthesizeModule.addTTSDataLoadListener(this);
    }

    public void loadTextData(List<TextConfig> configs) {
        textConfigList.addAll(configs);
    }
    /**
     * 第一次开始请求，理论上只需调用一次
     */
//    public void requestStart(){
//        if(httpUtil !=null){
//            httpUtil.startPost(textConfigList.get(0).getContent(),0);
//        }
//    }

    /**
     * 从章节中间的某一处开始请求，理论上只需调用一次
     */
    public void requestResume(int index) {
        currentIndex = index;
        if (isLimited()) {
            load();
        }
    }

    /**
     * 开始启动之后，后续的请求都是走这个方法
     */
    public void requestNext() {
        currentIndex++;
        if (isLimited()) {
            load();
        }
    }

    public void load() {
        dataSynthesizeModule.synthesizeStart(textConfigList.get(currentIndex).getContent(),currentIndex);
    }

    /**
     * 停止请求
     */
    public void requestStop() {

    }

    public boolean isLimited() {
        if (currentIndex < textConfigList.size()) {
            return true;
        }
        return false;
    }
    public void switchOfflineState(boolean isOffline){
        if(isOffline){
            dataSynthesizeModule = new LingXiModule();
        } else {
            dataSynthesizeModule = new TTSHttpModule();
        }
    }
    private List<TTSDataLoadProcessorListener> ttsDataLoadProcessorListenerList = new ArrayList<>();

    public void addTTSHttpProcessorListener(TTSDataLoadProcessorListener ttsDataLoadProcessorListener) {
        if (ttsDataLoadProcessorListenerList != null) {
            ttsDataLoadProcessorListenerList.add(ttsDataLoadProcessorListener);
        }
    }

    public void removeTTSHttpProcessorListener(TTSDataLoadProcessorListener ttsDataLoadProcessorListener) {
        if (ttsDataLoadProcessorListenerList != null) {
            ttsDataLoadProcessorListenerList.remove(ttsDataLoadProcessorListener);
        }
    }

    @Override
    public void onDataLoadSuccess(String requestContent,int index,ArrayList<byte[]> resultBytes) {
        FileOperator.getInstance().saveFileIntoLocal(index, resultBytes);
        for (TTSDataLoadProcessorListener ttsDataLoadProcessorListener : ttsDataLoadProcessorListenerList) {
            ttsDataLoadProcessorListener.onAudioRequestSuccess(requestContent, index);
        }
        index++;
        if (FileOperator.getInstance().loadFileFromMap(index) == null || !FileOperator.getInstance().loadFileFromMap(index).exists()) {
            requestNext();
        }
    }

    @Override
    public void onDataLoadError(String requestContent,int index, String errorInfo) {

    }
}
