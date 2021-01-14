package com.mz.ttswebapiproject.manager;

import android.text.SpannableString;

import com.mz.ttswebapiproject.bean.TextConfig;
import com.mz.ttswebapiproject.listener.TTSManagerListener;
import com.mz.ttswebapiproject.listener.TTSPlayProcessorListener;
import com.mz.ttswebapiproject.processor.TTSDataLoadProcessor;
import com.mz.ttswebapiproject.listener.TTSDataLoadProcessorListener;
import com.mz.ttswebapiproject.listener.TTSDataLoadProgressListener;
import com.mz.ttswebapiproject.listener.TTSTextProcessorListener;
import com.mz.ttswebapiproject.module.text.TTSTextProcessor;
import com.mz.ttswebapiproject.processor.TTSPlayProcessor;
import com.mz.ttswebapiproject.util.FileOperator;
import com.mz.ttswebapiproject.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 11:53
 * @Description 文件描述：中间处理类
 */
public class TTSManager implements TTSDataLoadProcessorListener, TTSPlayProcessorListener, TTSTextProcessorListener {
    private TTSDataLoadProcessor ttsDataLoadProcessor;
    private TTSPlayProcessor ttsPlayProcessor;
    private String originalText;
    private TTSTextProcessor textProcessor;
    private List<TextConfig> textConfigList = new ArrayList<>();
    private List<TTSManagerListener> ttsManagerListenerList = new ArrayList<>();
    private TTSDataLoadProgressListener audioRequestProgressListener;
    private int fileNoExistIndex = -1;
    public TTSManager(String content) {
        this.originalText = content;
        init();
    }

    public void init() {
        initTextProcessor();
        initDataLoadProcessor();
        initTTSPlayProcessor();
    }
    public void initTextProcessor(){
        textProcessor = new TTSTextProcessor(this);
        textConfigList.addAll(textProcessor.computeParaPosition(originalText));
    }
    public void initDataLoadProcessor(){
        ttsDataLoadProcessor = new TTSDataLoadProcessor();
        ttsDataLoadProcessor.switchOfflineState(true);
        ttsDataLoadProcessor.loadTextData(textConfigList);
        ttsDataLoadProcessor.addTTSHttpProcessorListener(this);
    }
    public void initTTSPlayProcessor(){
        ttsPlayProcessor = new TTSPlayProcessor();
        ttsPlayProcessor.loadTextData(textConfigList);
        ttsPlayProcessor.addTTSPlayProcessorListener(this);

    }
    public void startTTSFromHttp() {
        if (ttsDataLoadProcessor != null) {
            ttsDataLoadProcessor.removeTTSHttpProcessorListener(this);
            ttsDataLoadProcessor = null;
        }
        initDataLoadProcessor();
    }


    @Override
    public void onAudioRequestSuccess(String requestContent, int index) {
        audioRequestProgressListener.onAudioRequestProgress(index);
        LogUtil.play("缓存完了："+index);
        if(index == 0){
            ttsPlayProcessor.playStart();
        } else if(index == fileNoExistIndex){
            ttsPlayProcessor.playResume(index);
        }
    }
    public boolean contentProcessorIsPlaying() {
        if(ttsPlayProcessor!=null){
            return ttsPlayProcessor.isMediaPlaying();
        }
        return false;
    }

    public void contentProcessorPause() {
        if (ttsPlayProcessor != null) {
            ttsPlayProcessor.playPause();
        }
    }

    public void contentProcessorStop() {
        if (ttsPlayProcessor != null) {
            ttsPlayProcessor.playStop();
        }
    }

    public void changePlayState(boolean shouldPlay) {
        if (shouldPlay) {
            contentProcessorIsPlaying();
            ttsPlayProcessor.playCurrent();
        } else {
            contentProcessorPause();
        }
    }
    public void updateAudioConfigSpeed(int speed) {
        TTSDataKeeper.getInstance().getAudioConfig().setSpeed(speed+"");
        FileOperator.getInstance().clearFile();
        startTTSFromHttp();
    }
    public void updateAudioConfigSpeaker(String speakerName){
        TTSDataKeeper.getInstance().getAudioConfig().setVoice_name(speakerName);
        FileOperator.getInstance().clearFile();
        startTTSFromHttp();
    }
    public void updateAudioConfigRate(String rate){
        TTSDataKeeper.getInstance().getAudioConfig().setAuf(rate);
        FileOperator.getInstance().clearFile();
        startTTSFromHttp();
    }
    public void updateAudioConfigFormat(String format){
        TTSDataKeeper.getInstance().getAudioConfig().setAue(format);
        FileOperator.getInstance().clearFile();
        startTTSFromHttp();
    }
    @Override
    public void onTTSProcessorColorChange(SpannableString spannableString) {
        for (TTSManagerListener ttsManagerListener : ttsManagerListenerList) {
            ttsManagerListener.onTextColorChange(spannableString);
        }
    }

    public void updateReadProgress(int progress){
        int wordIndex = (originalText.length()-1)*progress/100;
        LogUtil.e("拖动到某个字的索引是："+wordIndex);
        boolean isInLimited = false;
        for(TextConfig textConfig : textConfigList){
            if(wordIndex>=textConfig.getParaStart() && wordIndex<=textConfig.getParaEnd()){
                int currentPlayIndex = textConfigList.indexOf(textConfig);
                ttsPlayProcessor.playResume(currentPlayIndex);
                break;
            }
        }
    }
    public void addTTSManagerListener(TTSManagerListener ttsManagerListener) {
        if (ttsManagerListenerList != null) {
            ttsManagerListenerList.add(ttsManagerListener);
        }
    }

    public void removeTTSContentProcessorListener(TTSManagerListener contentOperatorListener) {
        if (ttsManagerListenerList != null && ttsManagerListenerList.size() > 0) {
            ttsManagerListenerList.remove(contentOperatorListener);
        }
    }
    public void addTTSAudioRequestProgressListener(TTSDataLoadProgressListener audioRequestProgressListener){
        this.audioRequestProgressListener = audioRequestProgressListener;
    }
    public void removeTTSAudioRequestProgressListener(){
        this.audioRequestProgressListener = null;
    }

    @Override
    public void onPlayProcessorFileNotFound(int index) {
        if(++index>=textConfigList.size()){
            return;
        }
        for (TTSManagerListener ttsManagerListener : ttsManagerListenerList) {
            ttsManagerListener.onMediaPlayWait();
        }
        fileNoExistIndex = index;
        LogUtil.play("文件不存在"+index);
        //todo 判断是否该重新请求数据
        ttsDataLoadProcessor.requestResume(index);
    }

    @Override
    public void onPlayProcessorError(int index,String errorInfo) {
        for (TTSManagerListener ttsManagerListener : ttsManagerListenerList) {
            ttsManagerListener.onMediaPlayError(errorInfo);
        }
    }

    @Override
    public void onPlayProcessorPrepare(int index) {

    }

    @Override
    public void onPlayProcessorPause(int index) {
        for (TTSManagerListener ttsManagerListener : ttsManagerListenerList) {
            ttsManagerListener.onMediaPlayPause();
        }
    }

    @Override
    public void onPlayProcessorStop(int index) {

    }

    @Override
    public void onPlayProcessorCompletion(int index) {
        for (TTSManagerListener ttsManagerListener : ttsManagerListenerList) {
            ttsManagerListener.onMediaPlayComplete(index,textConfigList.size());
        }
    }

    @Override
    public void onPlayProcessorPlaying(int index,boolean isPlaying) {
        for (TTSManagerListener ttsManagerListener : ttsManagerListenerList) {
            ttsManagerListener.onMediaPlayPlaying(index,isPlaying);
        }
    }

    @Override
    public void onPlayProcessorOver() {
        for (TTSManagerListener ttsManagerListener : ttsManagerListenerList) {
            ttsManagerListener.onMediaPlayOver();
        }
    }
}
