package com.mz.ttswebapiproject.module.play.mediaplay;

import android.media.MediaPlayer;

import com.mz.ttswebapiproject.listener.TTSMediaPlayerModuleListener;
import com.mz.ttswebapiproject.util.FileOperator;
import com.mz.ttswebapiproject.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 11:53
 * @Description 文件描述：仅接收index，然后找到文件播放，不涉及到任何业务逻辑
 *
 * https://vimsky.com/article/3303.html
 * https://blog.csdn.net/qq_33642117/article/details/51841510
 *
 */
public class TTSMediaPlayerModule {
    private MediaPlayer tempMediaPlayer;
    private Map<String, MediaPlayer> mediaPlayerMap = new HashMap<>();
    private List<TTSMediaPlayerModuleListener> ttsAudioPlayerListenerList = new ArrayList<>();
    private int currentPlayIndex = -1;
    public TTSMediaPlayerModule() {
        MediaPlayer currentMediaPlayer = new MediaPlayer();
        MediaPlayer nextMediaPlayer = new MediaPlayer();
        mediaPlayerMap.put(currentMediaPlayer.toString(), nextMediaPlayer);
        mediaPlayerMap.put(nextMediaPlayer.toString(), currentMediaPlayer);
        tempMediaPlayer = currentMediaPlayer;
    }
    public void startPlay(int index) {
        LogUtil.e("开始播放第" + index + "个");
        if(tempMediaPlayer == null){
            return;
        }
        currentPlayIndex = index;
        File targetFile = FileOperator.getInstance().loadFileFromMap(currentPlayIndex);
        if (targetFile == null || !targetFile.exists()) {
            for (TTSMediaPlayerModuleListener ttsMediaPlayerModuleListener : ttsAudioPlayerListenerList) {
                ttsMediaPlayerModuleListener.onMediaPlayerFileNotFound(currentPlayIndex);
            }
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(targetFile);
            tempMediaPlayer.reset();
            tempMediaPlayer.setDataSource(fis.getFD());
            tempMediaPlayer.prepareAsync();
            tempMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    LogUtil.e("第" + currentPlayIndex + "播放完");
                    tempMediaPlayer = mediaPlayerMap.get(mediaPlayer.toString());
                    for (TTSMediaPlayerModuleListener audioPlayerListener : ttsAudioPlayerListenerList) {
                        if (audioPlayerListener != null) {
                            audioPlayerListener.onMediaPlayerCompletion(currentPlayIndex);
                        }
                    }
                }
            });
            tempMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    for (TTSMediaPlayerModuleListener audioPlayerListener : ttsAudioPlayerListenerList) {
                        if (audioPlayerListener != null) {
                            audioPlayerListener.onMediaPlayerError(currentPlayIndex,"播放时出错");
                        }
                    }
                    return false;
                }
            });
            tempMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    for (TTSMediaPlayerModuleListener audioPlayerListener : ttsAudioPlayerListenerList) {
                        if (audioPlayerListener != null) {
                            audioPlayerListener.onMediaPlayerPrepare(currentPlayIndex);
                        }
                    }
                    mediaPlayer.start();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            for (TTSMediaPlayerModuleListener audioPlayerListener : ttsAudioPlayerListenerList) {
                if (audioPlayerListener != null) {
                    audioPlayerListener.onMediaPlayerError(currentPlayIndex,e.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            for (TTSMediaPlayerModuleListener audioPlayerListener : ttsAudioPlayerListenerList) {
                if (audioPlayerListener != null) {
                    audioPlayerListener.onMediaPlayerError(currentPlayIndex,e.toString());
                }
            }
        }
    }


    public void pausePlay() {
        if (tempMediaPlayer != null) {
            tempMediaPlayer.pause();
            for (TTSMediaPlayerModuleListener audioPlayerListener : ttsAudioPlayerListenerList) {
                if (audioPlayerListener != null) {
                    audioPlayerListener.onMediaPlayerPause(currentPlayIndex);
                }
            }
        }
    }

    public void stopPlay() {
        if (tempMediaPlayer != null) {
            tempMediaPlayer.stop();
            for (TTSMediaPlayerModuleListener audioPlayerListener : ttsAudioPlayerListenerList) {
                if (audioPlayerListener != null) {
                    audioPlayerListener.onMediaPlayerStop(currentPlayIndex);
                }
            }
        }
    }
    public boolean isPlaying(){
        if(tempMediaPlayer != null){
            return tempMediaPlayer.isPlaying();
        }
        return false;
    }
    public void addTTSAudioPlayerListener(TTSMediaPlayerModuleListener audioPlayerListener) {
        if (ttsAudioPlayerListenerList != null) {
            ttsAudioPlayerListenerList.add(audioPlayerListener);
        }
    }

    public void removeTTSAudioPlayerListener(TTSMediaPlayerModuleListener audioPlayerListener) {
        if (ttsAudioPlayerListenerList != null && ttsAudioPlayerListenerList.size() > 0) {
            ttsAudioPlayerListenerList.remove(audioPlayerListener);
        }
    }

    public void clearAudioPlayer() {
        ttsAudioPlayerListenerList.clear();
        ttsAudioPlayerListenerList = null;
        tempMediaPlayer = null;
        mediaPlayerMap.clear();
        mediaPlayerMap = null;
    }
}
