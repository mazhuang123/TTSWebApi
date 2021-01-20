package com.mz.ttswebapiproject.manager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.mz.ttswebapiproject.R;
import com.mz.ttswebapiproject.bean.TextConfig;
import com.mz.ttswebapiproject.listener.TTSManagerListener;
import com.mz.ttswebapiproject.listener.TTSPlayProcessorListener;
import com.mz.ttswebapiproject.processor.TTSDataLoadProcessor;
import com.mz.ttswebapiproject.listener.TTSDataLoadProcessorListener;
import com.mz.ttswebapiproject.module.text.TTSTextProcessor;
import com.mz.ttswebapiproject.processor.TTSPlayProcessor;
import com.mz.ttswebapiproject.ui.MyApplication;
import com.mz.ttswebapiproject.util.FileOperator;
import com.mz.ttswebapiproject.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 11:53
 * @Description 文件描述：中间处理类
 */
public class TTSManager implements TTSDataLoadProcessorListener, TTSPlayProcessorListener {
    private TTSDataLoadProcessor ttsDataLoadProcessor;
    private TTSPlayProcessor ttsPlayProcessor;
    private String originalText;
    private TTSTextProcessor textProcessor;
    private List<TextConfig> textConfigList = new ArrayList<>();
    private int fileNoExistIndex = -1;
    private SpannableString spannableString;
    private ForegroundColorSpan playSpan;
    private BackgroundColorSpan requestSpan;
    private TextConfig requestTextConfig, playTextConfig;
    private TTSManagerListener ttsManagerListener;

    public TTSManager(String content) {
        this.originalText = content;
        requestTextConfig = new TextConfig();
        playTextConfig = new TextConfig();
        spannableString = new SpannableString(originalText);
        playSpan = new ForegroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.blue));
        requestSpan = new BackgroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.yellow));
        init();
    }

    public void init() {
        initTextProcessor();
        initDataLoadProcessor();
        initTTSPlayProcessor();
    }

    public void initTextProcessor() {
        textProcessor = new TTSTextProcessor();
        textConfigList.addAll(textProcessor.computeParaPosition(originalText));
    }

    public void initDataLoadProcessor() {
        ttsDataLoadProcessor = new TTSDataLoadProcessor();
        ttsDataLoadProcessor.loadTextData(textConfigList);
        ttsDataLoadProcessor.addTTSHttpProcessorListener(this);
    }

    public void initTTSPlayProcessor() {
        ttsPlayProcessor = new TTSPlayProcessor();
        ttsPlayProcessor.loadTextData(textConfigList);
        ttsPlayProcessor.addTTSPlayProcessorListener(this);

    }

    public void startTTSFromHttp() {
        if (ttsDataLoadProcessor != null) {
            ttsDataLoadProcessor.removeTTSHttpProcessorListener();
            ttsDataLoadProcessor = null;
        }
        initDataLoadProcessor();
        ttsDataLoadProcessor.requestRetryWithoutIndex();
    }


    @Override
    public void onAudioRequestSuccess(String requestContent, int index) {
        requestTextConfig = textConfigList.get(index);
        ttsManagerListener.onManagerAudioRequestProgress(index);
        fillTextColor();
        LogUtil.play("缓存完了：" + index);
        if (index == 0) {
            ttsPlayProcessor.playStart();
        } else if (index == fileNoExistIndex) {
            ttsPlayProcessor.playResume(index);
        }
    }

    @Override
    public void onAudioRequestFailed(String errorInfo) {
        ttsManagerListener.onManagerAudioRequestFailed(errorInfo);
    }

    private void fillTextColor() {
        spannableString.setSpan(playSpan, 0, playTextConfig.getParaEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(requestSpan, 0, requestTextConfig.getParaEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ttsManagerListener.onTextColorChange(spannableString);
    }

    public boolean contentProcessorIsPlaying() {
        if (ttsPlayProcessor != null) {
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
        TTSDataKeeper.getInstance().getAudioConfig().setSpeed(speed + "");
        FileOperator.getInstance().clearFile();
        startTTSFromHttp();
    }

    public void updateAudioConfigSpeaker(String speakerName) {
        TTSDataKeeper.getInstance().getAudioConfig().setVoice_name(speakerName);
        FileOperator.getInstance().clearFile();
        startTTSFromHttp();
    }

    public void updateAudioConfigRate(String rate) {
        TTSDataKeeper.getInstance().getAudioConfig().setAuf(rate);
        FileOperator.getInstance().clearFile();
        startTTSFromHttp();
    }

    public void updateAudioConfigFormat(String format) {
        TTSDataKeeper.getInstance().getAudioConfig().setAue(format);
        FileOperator.getInstance().clearFile();
        startTTSFromHttp();
    }

    public void switchEngineType(String engineType) {
        ttsDataLoadProcessor.configSynthesizerType(engineType);

    }

    public void updateReadProgress(int progress) {
        int wordIndex = (originalText.length() - 1) * progress / 100;
        LogUtil.e("拖动到某个字的索引是：" + wordIndex);
        boolean isInLimited = false;
        for (TextConfig textConfig : textConfigList) {
            if (wordIndex >= textConfig.getParaStart() && wordIndex <= textConfig.getParaEnd()) {
                int currentPlayIndex = textConfigList.indexOf(textConfig);
                ttsPlayProcessor.playResume(currentPlayIndex);
                break;
            }
        }
    }

    public void addTTSManagerListener(TTSManagerListener ttsManagerListener) {
        this.ttsManagerListener = ttsManagerListener;
    }

    public void removeTTSContentProcessorListener() {
        ttsManagerListener = null;
    }

    @Override
    public void onPlayProcessorFileNotFound(int index) {
        ttsManagerListener.onMediaPlayWait();
        fileNoExistIndex = index;
        LogUtil.play("文件不存在" + index);
        //todo 判断是否该重新请求数据
        ttsDataLoadProcessor.requestRetry(index);
    }

    @Override
    public void onPlayProcessorError(int index, String errorInfo) {
        ttsManagerListener.onMediaPlayError(errorInfo);
    }


    @Override
    public void onPlayProcessorPause(int index) {
        ttsManagerListener.onMediaPlayPause();
    }

    @Override
    public void onPlayProcessorStop(int index) {

    }

    @Override
    public void onPlayProcessorCompletion(int index) {
        ttsManagerListener.onMediaPlayComplete(index, textConfigList.size());
    }

    @Override
    public void onPlayProcessorPlaying(int index, boolean isPlaying) {
        playTextConfig = textConfigList.get(index);
        fillTextColor();
    }

    @Override
    public void onPlayerProcessorPlayState(int index, boolean isPlaying) {

    }

    @Override
    public void onPlayProcessorOver() {
        ttsManagerListener.onMediaPlayOver();
    }
}
