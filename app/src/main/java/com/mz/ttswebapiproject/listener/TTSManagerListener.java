package com.mz.ttswebapiproject.listener;

import android.text.SpannableString;

import com.mz.ttswebapiproject.bean.TextConfig;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 15:46
 * @Description 文件描述：
 */
public interface TTSManagerListener {

    void onMediaPlayWait();

    void onMediaPlayPause();

    void onMediaPlayError(String errorInfo);

    void onMediaPlayComplete(int index, int sumSize);

    void onMediaPlayOver();

    void onTextColorChange(SpannableString spannableString);

    void onManagerAudioRequestProgress(int index);

    void onManagerAudioRequestFailed(String errorInfo);
}
