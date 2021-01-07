package com.mz.ttswebapiproject.play;

import android.media.MediaPlayer;

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
 * @Description 文件描述：https://vimsky.com/article/3303.html
 * https://blog.csdn.net/qq_33642117/article/details/51841510
 *
 */
public class TTSAudioPlayerProcessor {
    private MediaPlayer tempMediaPlayer;
    private Map<String, MediaPlayer> mediaPlayerMap = new HashMap<>();
    private List<TTSAudioPlayerListener> ttsAudioPlayerListenerList = new ArrayList<>();
    public TTSAudioPlayerProcessor() {
        MediaPlayer currentMediaPlayer = new MediaPlayer();
        MediaPlayer nextMediaPlayer = new MediaPlayer();
        mediaPlayerMap.put(currentMediaPlayer.toString(), nextMediaPlayer);
        mediaPlayerMap.put(nextMediaPlayer.toString(), currentMediaPlayer);
        tempMediaPlayer = currentMediaPlayer;
    }
    public void startPlay(final int index, final File targetFile) {
        LogUtil.e("开始播放第" + index + "个");
        if(tempMediaPlayer == null){
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
                    LogUtil.e("第" + index + "播放完");
                    tempMediaPlayer = mediaPlayerMap.get(mediaPlayer.toString());
                    for (TTSAudioPlayerListener audioPlayerListener : ttsAudioPlayerListenerList) {
                        if (audioPlayerListener != null) {
                            audioPlayerListener.onMediaPlayerCompletion(index);
                        }
                    }
                }
            });
            tempMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    for (TTSAudioPlayerListener audioPlayerListener : ttsAudioPlayerListenerList) {
                        if (audioPlayerListener != null) {
                            audioPlayerListener.onMediaPlayerError("播放时出错");
                        }
                    }
                    return false;
                }
            });
            tempMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    for (TTSAudioPlayerListener audioPlayerListener : ttsAudioPlayerListenerList) {
                        if (audioPlayerListener != null) {
                            audioPlayerListener.onMediaPlayerPrepare();
                        }
                    }
                    mediaPlayer.start();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            for (TTSAudioPlayerListener audioPlayerListener : ttsAudioPlayerListenerList) {
                if (audioPlayerListener != null) {
                    audioPlayerListener.onMediaPlayerError(e.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            for (TTSAudioPlayerListener audioPlayerListener : ttsAudioPlayerListenerList) {
                if (audioPlayerListener != null) {
                    audioPlayerListener.onMediaPlayerError(e.toString());
                }
            }
        }
    }


    public void pausePlay() {
        if (tempMediaPlayer != null) {
            tempMediaPlayer.pause();
            for (TTSAudioPlayerListener audioPlayerListener : ttsAudioPlayerListenerList) {
                if (audioPlayerListener != null) {
                    audioPlayerListener.onMediaPlayerPause();
                }
            }
        }
    }

    public void stopPlay() {
        if (tempMediaPlayer != null) {
            tempMediaPlayer.stop();
            for (TTSAudioPlayerListener audioPlayerListener : ttsAudioPlayerListenerList) {
                if (audioPlayerListener != null) {
                    audioPlayerListener.onMediaPlayerStop();
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
    public void addTTSAudioPlayerListener(TTSAudioPlayerListener audioPlayerListener) {
        if (ttsAudioPlayerListenerList != null) {
            ttsAudioPlayerListenerList.add(audioPlayerListener);
        }
    }

    public void removeTTSAudioPlayerListener(TTSAudioPlayerListener audioPlayerListener) {
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
    public interface TTSAudioPlayerListener {
        void onMediaPlayerError(String errorInfo);

        void onMediaPlayerPrepare();

        void onMediaPlayerPause();

        void onMediaPlayerStop();

        void onMediaPlayerCompletion(int index);

    }
}
