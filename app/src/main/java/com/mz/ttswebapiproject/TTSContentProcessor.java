package com.mz.ttswebapiproject;

import android.content.Context;
import android.text.SpannableString;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 11:53
 * @Description 文件描述：中间处理类
 */
public class TTSContentProcessor implements TTSHttpProcessor.TTSHttpListener, TTSAudioPlayerProcessor.TTSAudioPlayerListener, TTSTextProcessor.TTSTextProcessorListener {
    private TTSHttpProcessor ttsHttpProcessor;
    private TTSAudioPlayerProcessor audioPlayer;
    private FileOperator fileOperator;
    private int currentPlayIndex;
    private int currentHttpIndex;
    private boolean isPlaying = true;
    private String[] stringArray;
    private String originalText;
    private AudioConfig audioConfig;
    private TTSTextProcessor textProcessor;
    private List<TextConfig> textConfigList = new ArrayList<>();
    private List<TTSContentProcessorListener> contentProcessorListenerList = new ArrayList<>();

    public TTSContentProcessor(Context context, String content) {
        audioConfig = new AudioConfig();
        fileOperator = new FileOperator(context);
        textProcessor = new TTSTextProcessor(this);
        audioPlayer = new TTSAudioPlayerProcessor();
        ttsHttpProcessor = new TTSHttpProcessor();
        this.originalText = content;
    }

    public void init() {
        stringArray = textProcessor.executeSplit(originalText);
        textConfigList.addAll(textProcessor.getTextConfigList());
        audioPlayer.addTTSAudioPlayerListener(this);
    }

    public void startTTSFromHttp() {
        if (ttsHttpProcessor != null) {
            ttsHttpProcessor.removeTTSHttpListener(this);
            ttsHttpProcessor = null;
        }
        if (currentHttpIndex >= stringArray.length) {
            return;
        }
        ttsHttpProcessor = new TTSHttpProcessor();
        ttsHttpProcessor.addTTSHttpListener(this);
        ttsHttpProcessor.startPost(audioConfig, textConfigList.get(currentHttpIndex).getContent(), currentHttpIndex);
    }


    @Override
    public void onMediaPlayerError(String errorInfo) {
        for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
            contentProcessorListener.onTTSContentOperatorMediaPlayError(errorInfo);
        }
    }

    @Override
    public void onMediaPlayerPrepare() {

    }

    @Override
    public void onMediaPlayerPause() {
        for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
            contentProcessorListener.onTTSContentOperatorMediaPlayPause();
        }
    }

    @Override
    public void onMediaPlayerStop() {

    }

    @Override
    public void onMediaPlayerCompletion(int index) {
//        fileOperator.removeExistFile(index);
        for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
            contentProcessorListener.onTTSContentOperatorMediaPlayComplete(index,textConfigList.size());
        }
        if(currentPlayIndex == index){
            currentPlayIndex = ++index;
        }
        if (index >= stringArray.length) {
            LogUtil.e("已经播放到最后一个");
            return;
        }
        if (index >= textConfigList.size()) {
            LogUtil.e("index大于textConfiglist的大小，直接return");
            return;
        }
        if(!isPlaying){
            LogUtil.e("没有正在播放，直接return");
            return;
        }
        startPlay();
    }

    @Override
    public void onTTSHttpOperatorSuccess(String requestContent, int index, byte[] resultBytes) {
        fileOperator.saveFileIntoLocal(index, audioConfig,resultBytes);
        for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
            contentProcessorListener.onTTSContentOperatorHttpSuccess();
        }
        LogUtil.e("接口返回成功"+index+"::::"+currentPlayIndex);
        if (index == currentPlayIndex) {
            textProcessor.fillPlayTextColor(originalText, textConfigList.get(index));
            startPlay();
        }
        currentHttpIndex = ++index;
        if (currentHttpIndex < stringArray.length) {
            if(fileOperator.loadFileFromMap(currentHttpIndex) == null || !fileOperator.loadFileFromMap(currentHttpIndex).exists()){
                startTTSFromHttp();
            }
        } else {
            ttsHttpProcessor = null;
            LogUtil.httpLog("网络请求已经到最后一个");
        }
    }

    @Override
    public void onTTsHttpOperatorFail(String requestContent, int index, String failInfo) {
        for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
            contentProcessorListener.onTTSContentOperatorHttpFail(failInfo);
        }
    }

    public void startPlay() {
        if (audioPlayer != null) {
            File file = fileOperator.loadFileFromMap(currentPlayIndex);
            if (file == null || !file.exists()) {
                for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
                    contentProcessorListener.onTTSContentOperatorMediaPlayWait();
                }
                currentHttpIndex = currentPlayIndex;
                LogUtil.e("第"+currentHttpIndex+"个没有发现本地文件，开始请求");
                startTTSFromHttp();
                return;
            }
            for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
                contentProcessorListener.onTTSContentOperatorMediaPlayPlaying();
            }
            TextConfig textConfig = textConfigList.get(currentPlayIndex);
            textProcessor.fillPlayTextColor(originalText, textConfig);
            audioPlayer.startPlay(currentPlayIndex, file);
        } else {
            for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
                contentProcessorListener.onTTSContentOperatorMediaPlayWait();
            }
        }
    }

    public void contentProcessorPause() {
        if (audioPlayer != null) {
            audioPlayer.pausePlay();
        }
    }

    public void contentProcessorStop() {
        if (audioPlayer != null) {
            audioPlayer.stopPlay();
        }
    }

    public void changePlayState(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            startPlay();
        } else {
            contentProcessorPause();
        }
    }
    public void updateAudioConfigSpeed(int speed) {
        audioConfig.setSpeed(speed+"");
        currentHttpIndex = currentPlayIndex;
        LogUtil.e("更新了参数，此时正在播放第"+currentPlayIndex);
        fileOperator.clearFile();
        startTTSFromHttp();
    }
    public void updateAudioConfigSpeaker(String speakerName){
        audioConfig.setVoice_name(speakerName);
        currentHttpIndex = currentPlayIndex;
        LogUtil.e("更新了参数，此时正在播放第"+currentPlayIndex);
        fileOperator.clearFile();
        startTTSFromHttp();
    }
    public void updateAudioConfigRate(String rate){
        audioConfig.setAuf(rate);
        currentHttpIndex = currentPlayIndex;
        LogUtil.e("更新了参数，此时正在播放第"+currentPlayIndex);
        fileOperator.clearFile();
        startTTSFromHttp();
    }
    public void updateAudioConfigFormat(String format){
        audioConfig.setAue(format);
        currentHttpIndex = currentPlayIndex;
        LogUtil.e("更新了参数，此时正在播放第"+currentPlayIndex);
        fileOperator.clearFile();
        startTTSFromHttp();
    }
    @Override
    public void onTTSProcessorColorChange(SpannableString spannableString) {
        for (TTSContentProcessorListener contentProcessorListener : contentProcessorListenerList) {
            contentProcessorListener.onTTSContentOperatorTextColorChange(spannableString);
        }
    }

    public boolean contentProcessorIsPlaying() {
        if (audioPlayer != null) {
            return audioPlayer.isPlaying();
        }
        return false;
    }
    public void updateReadProgress(int progress){
        int wordIndex = (originalText.length()-1)*progress/100;
        LogUtil.e("拖动到某个字的索引是："+wordIndex);
        boolean isInLimited = false;
        for(TextConfig textConfig : textConfigList){
            if(wordIndex>=textConfig.getParaStart() && wordIndex<=textConfig.getParaEnd()){
                currentPlayIndex = textConfigList.indexOf(textConfig);
                LogUtil.e("拖动到："+currentPlayIndex);
                isInLimited = true;
                break;
            } else {
                isInLimited = false;
            }
        }
        if(isInLimited){
            LogUtil.e("在范围内");
            startPlay();
        } else {
            LogUtil.e("不在范围内");
        }
    }
    public void addTTSContentProcessorListener(TTSContentProcessorListener contentOperatorListener) {
        if (contentProcessorListenerList != null) {
            contentProcessorListenerList.add(contentOperatorListener);
        }
    }

    public void removeTTSContentProcessorListener(TTSContentProcessorListener contentOperatorListener) {
        if (contentProcessorListenerList != null && contentProcessorListenerList.size() > 0) {
            contentProcessorListenerList.remove(contentOperatorListener);
        }
    }

    public interface TTSContentProcessorListener {
        void onTTSContentOperatorMediaPlayWait();

        void onTTSContentOperatorMediaPlayPause();

        void onTTSContentOperatorMediaPlayPlaying();

        void onTTSContentOperatorMediaPlayError(String errorInfo);

        void onTTSContentOperatorMediaPlayComplete(int index,int sumSize);

        void onTTSContentOperatorHttpSuccess();

        void onTTSContentOperatorHttpFail(String info);

        void onTTSContentOperatorTextColorChange(SpannableString spannableString);
    }
}
