package com.mz.ttswebapiproject.presenter;

import android.text.SpannableString;

import com.mz.ttswebapiproject.bean.TextConfig;
import com.mz.ttswebapiproject.http.TTSHttpProcessor;
import com.mz.ttswebapiproject.listener.TTSHttpProcessorListener;
import com.mz.ttswebapiproject.listener.TTSTextProcessorListener;
import com.mz.ttswebapiproject.play.TTSAudioPlayerProcessor;
import com.mz.ttswebapiproject.text.TTSTextProcessor;
import com.mz.ttswebapiproject.util.FileOperator;
import com.mz.ttswebapiproject.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/11 11:17
 * @Description 文件描述：
 */
public class TTSContentProxy implements TTSHttpProcessorListener, TTSTextProcessorListener {

    private TTSHttpProcessor ttsHttpProcessor;
    private TTSTextProcessor textProcessor;
    private String[] stringArray;
    private String originalText;
    private List<TextConfig> textConfigList = new ArrayList<>();
    private List<TTSContentPresenter.TTSContentProcessorListener> contentProcessorListenerList = new ArrayList<>();

    public TTSContentProxy(String content) {
        this.originalText = content;
    }

    public void init() {
        ttsHttpProcessor = new TTSHttpProcessor(stringArray);
        ttsHttpProcessor.addTTSHttpProcessorListener(this);
        textProcessor = new TTSTextProcessor(this);
    }
    public void startTTS(){
        stringArray = textProcessor.executeSplit(originalText);
        ttsHttpProcessor.startRequest(stringArray[0]);
    }

    @Override
    public void onTTSHttpOperatorSuccess(String requestContent, int index) {

    }

    @Override
    public void onTTSProcessorColorChange(SpannableString spannableString) {

    }
}
