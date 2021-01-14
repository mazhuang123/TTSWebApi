package com.mz.ttswebapiproject.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mz.ttswebapiproject.R;
import com.mz.ttswebapiproject.config.Config;
import com.mz.ttswebapiproject.dialogfragment.FormatDialogFragment;
import com.mz.ttswebapiproject.dialogfragment.SampleDialogFragment;
import com.mz.ttswebapiproject.dialogfragment.SpeakerDialogFragment;
import com.mz.ttswebapiproject.dialogfragment.SpeedDialogFragment;
import com.mz.ttswebapiproject.listener.TTSManagerListener;
import com.mz.ttswebapiproject.listener.TTSDataLoadProgressListener;
import com.mz.ttswebapiproject.listener.TTSConfigItemListener;
import com.mz.ttswebapiproject.listener.TTSSpeedListener;
import com.mz.ttswebapiproject.manager.TTSManager;
import com.mz.ttswebapiproject.manager.TTSDataKeeper;
import com.mz.ttswebapiproject.util.LogUtil;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/6 21:35
 * @Description 文件描述：
 */
public class TTSActivity extends AppCompatActivity implements View.OnClickListener,
        TTSManagerListener,
        SeekBar.OnSeekBarChangeListener,
        TTSConfigItemListener,
        TTSSpeedListener, TTSDataLoadProgressListener {
    private TextView contentView,requestIndexView,playIndexView;
    private Button startBtn,speakerBtn,sampleBtn,formatBtn;
    private ProgressBar progressBar;
    private SeekBar progressSeekBar;
    TTSManager ttsContentProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tts_layout);
        contentView = findViewById(R.id.content_view);
        requestIndexView = findViewById(R.id.request_index_view);
        playIndexView = findViewById(R.id.play_index_view);
        progressBar = findViewById(R.id.tts_listen_book_progress_play_view);
        startBtn = findViewById(R.id.startBtn);
        progressSeekBar = findViewById(R.id.seek_bar);
        speakerBtn = findViewById(R.id.speaker_btn);
        sampleBtn = findViewById(R.id.sample_btn);
        formatBtn = findViewById(R.id.format_btn);
        speakerBtn.setOnClickListener(this);
        sampleBtn.setOnClickListener(this);
        formatBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        progressSeekBar.setOnSeekBarChangeListener(this);
        Button speedBtn = findViewById(R.id.speed_btn);
        speedBtn.setOnClickListener(this);
        TTSDataKeeper.getInstance().setAudioPath(this);
        contentView.setText(Config.getContent());
        ttsContentProcessor = new TTSManager(Config.getContent());
        ttsContentProcessor.addTTSManagerListener(this);
        ttsContentProcessor.addTTSAudioRequestProgressListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startBtn:
                if(ttsContentProcessor.contentProcessorIsPlaying()){
                    ttsContentProcessor.changePlayState(false);
                } else {
                    ttsContentProcessor.changePlayState(true);
                }
                break;
            case R.id.speed_btn:
                SpeedDialogFragment speedFragment = new SpeedDialogFragment();
                speedFragment.addTTSSpeedListener(TTSActivity.this);
                speedFragment.show(getSupportFragmentManager(),"speed");
                break;
            case R.id.speaker_btn:
                SpeakerDialogFragment speakerFragment = new SpeakerDialogFragment();
                speakerFragment.addTTSConfigItemListener(this);
                speakerFragment.show(getSupportFragmentManager(),"speaker");
                break;
            case R.id.sample_btn:
                SampleDialogFragment sampleFragment = new SampleDialogFragment();
                sampleFragment.addSampleItemClickListener(this);
                sampleFragment.show(getSupportFragmentManager(),"sample");
                break;
            case R.id.format_btn:
                FormatDialogFragment formatFragment = new FormatDialogFragment();
                formatFragment.addTTSFormatItemClickListener(this);
                formatFragment.show(getSupportFragmentManager(),"format");
                break;
        }
    }

    private void startLoading(){
        if(progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    private void stopLoading(){
        if(progressBar!=null){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    public void setPlayState(boolean shouldPlay,boolean isStartLoad){
        if(startBtn != null){
            if (shouldPlay) {
                startBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.listen_pause_icon));
                if (isStartLoad){
                    startLoading();
                }else{
                    stopLoading();
                }
            } else {
                startBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.listen_play_icon));
                stopLoading();
            }
        }
    }

    @Override
    public void onMediaPlayWait() {
        setPlayState(true,true);
    }

    @Override
    public void onMediaPlayPause() {
        setPlayState(false,false);
    }

    @Override
    public void onMediaPlayPlaying(int index,boolean isPlaying) {
        setPlayState(true,false);
    }

    @Override
    public void onMediaPlayError(String errorInfo) {
        setPlayState(false,true);
    }

    @Override
    public void onMediaPlayComplete(int index, int sumSize) {
        float progress = (index+1)*100/sumSize;
        LogUtil.e("播放进度为："+progress);
        progressSeekBar.setProgress((int) progress);
    }


    @Override
    public void onTextColorChange(final SpannableString spannableString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contentView.setText(spannableString);
            }
        });
    }

    @Override
    public void onMediaPlayOver() {
        setPlayState(false,false);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        ttsContentProcessor.updateReadProgress(seekBar.getProgress());
    }

    @Override
    public void onTTSSpeedChanged(SeekBar seekBar, int i, boolean b) {
    }


    @Override
    public void onTTSSpeedStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onTTSSpeedStopTrackingTouch(SeekBar seekBar) {
        ttsContentProcessor.updateAudioConfigSpeed(seekBar.getProgress());
    }


    @Override
    public void onSpeakerItemClick(String content) {
        ttsContentProcessor.updateAudioConfigSpeaker(content);
    }

    @Override
    public void onSampleItemClick(String content) {
        ttsContentProcessor.updateAudioConfigRate(content);
    }

    @Override
    public void onFormatItemClick(String content) {
        ttsContentProcessor.updateAudioConfigFormat(content);
    }

    @Override
    public void onAudioRequestProgress(final int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestIndexView.setText("已经缓存到: "+index);
            }
        });
    }

}
