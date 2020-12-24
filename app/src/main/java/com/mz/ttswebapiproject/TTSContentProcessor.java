package com.mz.ttswebapiproject;

import android.content.Context;
import android.text.SpannableString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 11:53
 * @Description 文件描述：
 */
public class TTSContentProcessor implements TTSHttpProcessor.TTSHttpListener, TTSAudioPlayerProcessor.TTSAudioPlayerListener , TTSTextProcessor.TTSTextProcessorListener {
    private TTSHttpProcessor ttsHttpProcessor;
    private TTSAudioPlayerProcessor audioPlayer;
    private FileOperator fileOperator;
    private int currentPlayIndex;
    private int currentHttpIndex;
    private boolean isFirstSource = true;
    private boolean isCanPlay = true;
    private String[] stringArray;
    private Context context;
    private String originalText;
    private TTSTextProcessor textProcessor;
    private List<TextConfig> textConfigList = new ArrayList<>();
    private List<TTSContentProcessorListener> contentProcessorListenerList = new ArrayList<>();
    public TTSContentProcessor(Context context, String content) {
        fileOperator = new FileOperator();
        this.originalText = content;
        this.context = context;
        textProcessor = new TTSTextProcessor(this);
        stringArray = textProcessor.executeSplit(originalText);
        textConfigList.addAll(textProcessor.getTextConfigList());
    }
    public void startTTSFromHttp(AudioConfig audioConfig){
        if(ttsHttpProcessor != null){
            ttsHttpProcessor = null;
        }
        ttsHttpProcessor = new TTSHttpProcessor(this);
        ttsHttpProcessor.startPost(audioConfig,textConfigList.get(currentHttpIndex).getContent(), currentHttpIndex);
    }


    @Override
    public void onMediaPlayerError(String errorInfo) {
        for(TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList){
            contentProcessorListener.onTTSContentOperatorMediaPlayError(errorInfo);
        }
    }

    @Override
    public void onMediaPlayerPrepare() {

    }

    @Override
    public void onMediaPlayerPause() {

    }

    @Override
    public void onMediaPlayerStop() {

    }

    @Override
    public void onMediaPlayerCompletion(int index) {
        fileOperator.removeExistFile(index);
        index++;
        currentPlayIndex = index;
        if(index >= stringArray.length){
           LogUtil.play("已经播放到最后一个");
            for(TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList){
                contentProcessorListener.onTTSContentOperatorMediaPlayComplete();
            }
           audioPlayer = null;
        }
        if(index >= textConfigList.size()){
            return;
        }
        TextConfig textConfig = textConfigList.get(index);
        textProcessor.fillPlayTextColor(originalText,textConfig);
        startPlay();
    }

    @Override
    public void onTTSHttpOperatorSuccess(String requestContent,int index,byte[] resultBytes) {
        fileOperator.saveFileIntoLocal(context,index,resultBytes);
        for(TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList){
            contentProcessorListener.onTTSContentOperatorHttpSuccess();
        }
        if(isFirstSource){
            audioPlayer = new TTSAudioPlayerProcessor();
            audioPlayer.addTTSAudioPlayerListener(this);
            textProcessor.fillPlayTextColor(originalText,textConfigList.get(index));
            startPlay();
            isFirstSource = false;
        }
        currentHttpIndex = ++index;
        if(currentHttpIndex < stringArray.length){
            startTTSFromHttp(new AudioConfig());
        } else {
            ttsHttpProcessor = null;
            LogUtil.httpLog("网络请求已经到最后一个");
        }
    }

    @Override
    public void onTTsHttpOperatorFail(String requestContent,int index,String failInfo) {
        for(TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList){
            contentProcessorListener.onTTSContentOperatorHttpFail(failInfo);
        }
    }
    public void startPlay(){
        if(!isCanPlay){
            return;
        }
        if(audioPlayer!=null){
            File file = fileOperator.loadFileFromMap(currentPlayIndex);
            if(file == null){
                return;
            }
            audioPlayer.startPlay(currentPlayIndex,file);
        }
    }
    public void contentProcessorPause(){
        if(audioPlayer!=null){
            audioPlayer.pausePlay();
        }
    }
    public void contentProcessorStop(){
        if(audioPlayer!=null){
            audioPlayer.stopPlay();
        }
    }
    public void changePlayState(boolean isCanPlay){
        this.isCanPlay = isCanPlay;
    }
    @Override
    public void onTTSProcessorColorChange(SpannableString spannableString) {
        for(TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList){
            contentProcessorListener.onTTSContentOperatorTextColorChange(spannableString);
        }
    }
    public boolean contentProcessorIsPlaying(){
        if(audioPlayer != null){
            return audioPlayer.isPlaying();
        }
        return false;
    }
    public void addTTSContentProcessorListener(TTSContentProcessorListener contentOperatorListener){
        if(contentProcessorListenerList!=null){
            contentProcessorListenerList.add(contentOperatorListener);
        }
    }
    public void removeTTSContentProcessorListener(TTSContentProcessorListener contentOperatorListener){
        if(contentProcessorListenerList!=null && contentProcessorListenerList.size() > 0){
            contentProcessorListenerList.remove(contentOperatorListener);
        }
    }
    public interface TTSContentProcessorListener {
        void onTTSContentOperatorMediaPlayError(String errorInfo);
        void onTTSContentOperatorMediaPlayComplete();
        void onTTSContentOperatorHttpSuccess();
        void onTTSContentOperatorHttpFail(String info);
        void onTTSContentOperatorTextColorChange(SpannableString spannableString);
    }
}
