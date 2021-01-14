package com.mz.ttswebapiproject.module.request.http;

import android.util.Base64;

import com.google.gson.Gson;
import com.mz.ttswebapiproject.config.Config;
import com.mz.ttswebapiproject.listener.TTSDataLoadListener;
import com.mz.ttswebapiproject.manager.TTSDataKeeper;
import com.mz.ttswebapiproject.util.LogUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 11:47
 * @Description 文件描述：仅仅用于网络请求，不涉及任何业务逻辑
 */
public class TTSHttpModule {

    public void startPost(final String requestContent, final int index) {
        LogUtil.httpLog("开始网络请求第 " + index);
        String audioBeanJsonStr = new Gson().toJson(TTSDataKeeper.getInstance().getAudioConfig());
        String base64AudioStr = null;
        try {
            base64AudioStr = Base64.encodeToString(audioBeanJsonStr.getBytes("UTF-8"), Base64.NO_WRAP | Base64.URL_SAFE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("text", requestContent)
                .build();
        Request request = new Request.Builder()
                .addHeader("X-Param", base64AudioStr)
                .addHeader("X-Appid", Config.APP_ID)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .url(Config.TTS_URL)
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                for(TTSDataLoadListener ttsDataLoadListener: ttsDataLoadListenerList){
                    ttsDataLoadListener.onDataLoadError(requestContent, index, e.toString());
                }
                LogUtil.e(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
//                    LogUtil.e(headers.name(i) + ":" + headers.value(i));
                }
                byte[] resultBytes = response.body().bytes();
                ArrayList<byte[]> resultBytesList = new ArrayList<>();
                resultBytesList.add(resultBytes);
                LogUtil.httpLog("网络请求第 " + index + " 完成");
                for(TTSDataLoadListener ttsDataLoadListener: ttsDataLoadListenerList){
                    ttsDataLoadListener.onDataLoadSuccess(requestContent, index, resultBytesList);
                }
            }
        });
    }
    private List<TTSDataLoadListener> ttsDataLoadListenerList = new ArrayList<>();
    public void addDataLoadListener(TTSDataLoadListener ttsDataLoadListener){
        if(ttsDataLoadListenerList != null){
            ttsDataLoadListenerList.add(ttsDataLoadListener);
        }
    }
    public void removeDataLoadListener(TTSDataLoadListener ttsDataLoadListener){
        if(ttsDataLoadListenerList != null && ttsDataLoadListenerList.size() > 0){
            ttsDataLoadListenerList.remove(ttsDataLoadListener);
        }
    }
}
