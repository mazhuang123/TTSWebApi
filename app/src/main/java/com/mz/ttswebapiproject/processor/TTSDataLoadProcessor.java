package com.mz.ttswebapiproject.processor;

import com.mz.ttswebapiproject.bean.TextConfig;
import com.mz.ttswebapiproject.config.Config;
import com.mz.ttswebapiproject.listener.TTSDataLoadListener;
import com.mz.ttswebapiproject.listener.TTSDataLoadProcessorListener;
import com.mz.ttswebapiproject.module.synthesizer.DataSynthesizer;
import com.mz.ttswebapiproject.module.synthesizer.SynthesizerCreateFactory;
import com.mz.ttswebapiproject.util.FileOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/6 19:59
 * @Description 文件描述：网络请求结果处理类，决定如何请求，请求索引等
 */
public class TTSDataLoadProcessor implements TTSDataLoadListener {
    private int currentIndex;//当前正在请求的索引
    private DataSynthesizer dataSynthesizer;
    private List<TextConfig> textConfigList;
    private SynthesizerCreateFactory synthesizerCreateFactory;
    private TTSDataLoadProcessorListener ttsDataLoadProcessorListener;

    public TTSDataLoadProcessor() {
        textConfigList = new ArrayList<>();
        synthesizerCreateFactory = new SynthesizerCreateFactory();
        dataSynthesizer = synthesizerCreateFactory.createSynthesizer(Config.ENGINE_TYPE_HTTP);
        dataSynthesizer.addTTSDataLoadListener(this);
    }

    public void loadTextData(List<TextConfig> configs) {
        textConfigList.addAll(configs);
    }

    /**
     * 第一次开始请求，理论上只需调用一次
     */
    public void requestStart() {
        currentIndex = 0;
        load();
    }

    /**
     * 从章节中间的某一处重新请求
     */
    public void requestRetry(int index) {
        currentIndex = index;
        load();
    }

    public void requestRetryWithoutIndex() {
        load();
    }

    /**
     * 开始启动之后，后续的请求都是走这个方法
     */
    public void requestNext() {
        currentIndex++;
        load();
    }

    private void load() {
        if (!isLimited()) {
            return;
        }
        if (dataSynthesizer != null) {
            dataSynthesizer.synthesizeStart(textConfigList.get(currentIndex).getContent(), currentIndex);
        }
    }

    public boolean isLimited() {
        if (currentIndex < textConfigList.size()) {
            return true;
        }
        return false;
    }

    public void configSynthesizerType(String engineType) {
        if (dataSynthesizer == null) {
            return;
        }
        dataSynthesizer.cancelSynthesize();
        dataSynthesizer = synthesizerCreateFactory.createSynthesizer(engineType);
        dataSynthesizer.addTTSDataLoadListener(this);
        requestRetryWithoutIndex();
    }


    public void addTTSHttpProcessorListener(TTSDataLoadProcessorListener ttsDataLoadProcessorListener) {
        this.ttsDataLoadProcessorListener = ttsDataLoadProcessorListener;
    }

    public void removeTTSHttpProcessorListener() {
        this.ttsDataLoadProcessorListener = null;
    }

    @Override
    public void onDataLoadSuccess(String requestContent, int index, ArrayList<byte[]> resultBytes) {
        FileOperator.getInstance().saveFileIntoLocal(index, resultBytes);
        ttsDataLoadProcessorListener.onAudioRequestSuccess(requestContent, index);
        index++;
        if (FileOperator.getInstance().loadFileFromMap(index) == null || !FileOperator.getInstance().loadFileFromMap(index).exists()) {
            requestNext();
        }
    }

    @Override
    public void onDataLoadError(String requestContent, int index, String errorInfo) {
        if(ttsDataLoadProcessorListener!=null){
            ttsDataLoadProcessorListener.onAudioRequestFailed(errorInfo);
        }
    }
}
