package com.mz.ttswebapiproject.module.request.offline_tts;

import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.msc.tts.IFlySpeechSynthesizerResult;
import com.iflytek.cloud.msc.tts.ISynthesizeResult;
import com.iflytek.cloud.msc.tts.SpeakSession;
import com.iflytek.cloud.util.ResourceUtil;
import com.mz.ttswebapiproject.listener.TTSDataLoadListener;
import com.mz.ttswebapiproject.ui.MyApplication;
import com.mz.ttswebapiproject.util.LogUtil;
import com.mz.ttswebapiproject.util.ToastUtil;

import java.util.ArrayList;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 20:19
 * @Description 文件描述：
 */
public class LingXiModule {

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认云端发音人
    public static String voicerCloud = "xiaoyan";
    // 默认本地发音人
    public static String voicerLocal = "xiaoyan";
    private TTSDataLoadListener ttsDataLoadListener;
    private String content;
    private int index;
    public LingXiModule() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(MyApplication.getContext(), mTtsInitListener);
        LogUtil.lingXi("ad");
    }
    public void startSynthesis(String content,int index){
        this.content = content;
        this.index = index;
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_XTTS);
        //设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH, getHighResourcePath());
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);

        //设置语速
        mTts.setParameter(SpeechConstant.SPEED, "500");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

        //保存合成音频文件
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/wav/test.wav");


        SpeakSession.setCallListener(mCallResult);
        int code = mTts.startSpeaking(content, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            LogUtil.lingXi("语音合成失败,错误码: " + code);
        }
    }
    private ISynthesizeResult mCallResult = new ISynthesizeResult() {

        @Override

        public void bufferData(IFlySpeechSynthesizerResult iFlySpeechSynthesizerResult, SpeechError error) {
            String format = iFlySpeechSynthesizerResult.getFormat();
            ArrayList<byte[]> arrayList = iFlySpeechSynthesizerResult.getmBuffer();
            String path = iFlySpeechSynthesizerResult.getPath();
            ttsDataLoadListener.onDataLoadSuccess(content,index,arrayList);
        }
    };
    //获取本地高品质发音人资源
    private String getHighResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(MyApplication.getContext(), ResourceUtil.RESOURCE_TYPE.assets, "tts/pureXtts_common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(MyApplication.getContext(), ResourceUtil.RESOURCE_TYPE.assets, "tts/pureXtts_" + voicerLocal + ".jet"));
        return tempBuffer.toString();
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                ToastUtil.showToast(MyApplication.getContext(), "初始化失败,错误码：" + code);
            }
        }
    };
    public void addDataLoadListener(TTSDataLoadListener TTSDataLoadListener){
        this.ttsDataLoadListener = TTSDataLoadListener;
    }
    public void removeDataLoadListener(){
        this.ttsDataLoadListener = null;
    }
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            LogUtil.lingXi("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            LogUtil.lingXi("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            LogUtil.lingXi("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                LogUtil.lingXi("播放完成");
            } else if (error != null) {
                LogUtil.lingXi(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBufferProgressWithText(int i, int i1, int i2, String s, String s1) {

        }
    };
}

