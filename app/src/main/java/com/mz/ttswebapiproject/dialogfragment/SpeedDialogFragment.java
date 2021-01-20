package com.mz.ttswebapiproject.dialogfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.mz.ttswebapiproject.R;
import com.mz.ttswebapiproject.listener.TTSSpeedListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/11 14:19
 * @Description 文件描述：https://www.cnblogs.com/guanxinjing/p/12851626.html
 */
public class SpeedDialogFragment extends BaseDialogFragment implements SeekBar.OnSeekBarChangeListener {
    private SeekBar speedSeekBar;
    private TTSSpeedListener ttsSpeedListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tts_speed_fragment_layout,container,false);
        speedSeekBar = view.findViewById(R.id.speed_fragment_seek_bar);
        speedSeekBar.setOnSeekBarChangeListener(this);
        return view;
    }

    public void addTTSSpeedListener(TTSSpeedListener ttsSpeedListener){
        this.ttsSpeedListener = ttsSpeedListener;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.e("sdf", "onProgressChanged: ");
        ttsSpeedListener.onTTSSpeedChanged(seekBar,i,b);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        ttsSpeedListener.onTTSSpeedStartTrackingTouch(seekBar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        ttsSpeedListener.onTTSSpeedStopTrackingTouch(seekBar);
        getDialog().dismiss();
    }
}
