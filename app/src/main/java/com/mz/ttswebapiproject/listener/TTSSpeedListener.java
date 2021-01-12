package com.mz.ttswebapiproject.listener;

import android.util.Log;
import android.widget.SeekBar;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/11 16:50
 * @Description 文件描述：
 */
public interface TTSSpeedListener {


    void onTTSSpeedChanged(SeekBar seekBar, int i, boolean b);

    void onTTSSpeedStartTrackingTouch(SeekBar seekBar);

    void onTTSSpeedStopTrackingTouch(SeekBar seekBar);
}
