package com.mz.ttswebapiproject.listener;

import android.text.SpannableString;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 15:46
 * @Description 文件描述：
 */
public interface TTSManagerListener {

    void onMediaPlayWait();

    void onMediaPlayPause();

    void onMediaPlayPlaying(int index,boolean isPlaying);

    void onMediaPlayError(String errorInfo);

    void onMediaPlayComplete(int index, int sumSize);

    void onTextColorChange(SpannableString spannableString);

    void onMediaPlayOver();
}
