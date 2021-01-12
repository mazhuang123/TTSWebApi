package com.mz.ttswebapiproject.listener;

import android.text.SpannableString;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/11 11:25
 * @Description 文件描述：
 */
public interface TTSTextProcessorListener {
    void onTTSProcessorColorChange(SpannableString spannableString);
}
