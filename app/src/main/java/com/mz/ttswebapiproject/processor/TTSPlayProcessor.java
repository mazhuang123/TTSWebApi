package com.mz.ttswebapiproject.processor;

import com.mz.ttswebapiproject.bean.SentenceInfo;
import com.mz.ttswebapiproject.config.PlayState;
import com.mz.ttswebapiproject.listener.TTSMediaPlayerModuleListener;
import com.mz.ttswebapiproject.listener.TTSPlayProcessorListener;
import com.mz.ttswebapiproject.module.play.mediaplay.TTSMediaPlayerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 11:18
 * @Description 文件描述：用来处理播放顺序，播放时机，并对外提供播放回调
 */
public class TTSPlayProcessor implements TTSMediaPlayerModuleListener {
    private TTSMediaPlayerModule ttsMediaPlayerModule;
    private TTSPlayProcessorListener ttsPlayProcessorListener;
    private List<SentenceInfo> sentenceInfoList;
    private int currentIndex;
    private boolean mIsPlaying;
    public TTSPlayProcessor() {
        ttsMediaPlayerModule = new TTSMediaPlayerModule();
        ttsMediaPlayerModule.addTTSAudioPlayerListener(this);
        sentenceInfoList = new ArrayList<>();
    }
    public void addTTSPlayProcessorListener(TTSPlayProcessorListener ttsPlayProcessorListener){
        this.ttsPlayProcessorListener = ttsPlayProcessorListener;
    }
    public void removeTTSPlayProcessorListener(){
        this.ttsPlayProcessorListener = null;
    }
    public void loadTextData(List<SentenceInfo> configs){
        sentenceInfoList.addAll(configs);
    }
    /**
     * 仅限从第0个播放
     */
    public void playStart(){
        play();
    }

    public void playPause(){
        ttsMediaPlayerModule.pausePlay();
    }

    /**
     * 仅限从指定index开始播放，理论是仅需调用一次
     * @param currentIndex
     */
    public void playResume(int currentIndex){
        this.currentIndex = currentIndex;
        if(isLimited()){
            play();
        } else {
            ttsPlayProcessorListener.onPlayProcessorState(PlayState.OVER,currentIndex);
        }
    }

    /**
     * 从当前index开始播放
     */
    public void playCurrent(){
        if(isLimited()){
            play();
        } else {
            ttsPlayProcessorListener.onPlayProcessorState(PlayState.OVER,currentIndex);
        }
    }
    public void play(){
        ttsMediaPlayerModule.startPlay(currentIndex);
    }
    /**
     * 从下一个开始播放
     */
    public void playNext(){
        currentIndex++;
        if(isLimited()){
            ttsMediaPlayerModule.startPlay(currentIndex);
        } else {
            ttsPlayProcessorListener.onPlayProcessorState(PlayState.OVER,currentIndex);
        }
    }
    public void seekToPlayProress(int progress){

    }

    public boolean isLimited() {
        if (currentIndex < sentenceInfoList.size()) {
            return true;
        }
        return false;
    }
    public void playStop(){
        ttsMediaPlayerModule.stopPlay();
    }

    public boolean isMediaPlaying(){
         if(ttsMediaPlayerModule!=null){
             if(ttsMediaPlayerModule.isPlaying()){
                 ttsPlayProcessorListener.onPlayProcessorState(PlayState.PLAYING,currentIndex);
             }
              return ttsMediaPlayerModule.isPlaying();
         }
         return false;
    }
    public int getCurrentPlayIndex(){
        return currentIndex;
    }
    @Override
    public void onMediaPlayerFileNotFound(int index) {
        ttsPlayProcessorListener.onPlayProcessorFileNotFound(index);
    }

    @Override
    public void onMediaPlayerError(int index,String errorInfo) {
        ttsPlayProcessorListener.onPlayProcessorState(PlayState.ERROR,index);
    }

    @Override
    public void onMediaPlayerPause(int index) {
        ttsPlayProcessorListener.onPlayProcessorState(PlayState.PAUSE,index);
    }

    @Override
    public void onMediaPlayerStop(int index) {
        ttsPlayProcessorListener.onPlayProcessorState(PlayState.STOP,index);
    }

    @Override
    public void onMediaPlayerItemCompletion(int index) {
        ttsPlayProcessorListener.onPlayProcessorItemCompletion(index,(index*100)/ sentenceInfoList.size());
        playNext();
    }

    @Override
    public void onMediaPlayerPlaying(int index,boolean isPlaying) {
        ttsPlayProcessorListener.onPlayProcessorPlaying(index,isPlaying, sentenceInfoList.get(index));
    }

}
