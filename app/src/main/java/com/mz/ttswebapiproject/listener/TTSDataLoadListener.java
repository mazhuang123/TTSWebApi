package com.mz.ttswebapiproject.listener;
import java.util.ArrayList;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 20:33
 * @Description 文件描述：
 */
public interface TTSDataLoadListener {
    /**
     * 合成成功
     * @param requestContent
     * @param index
     * @param resultBytes
     */
    void onDataLoadSuccess(String requestContent, int index, ArrayList<byte[]> resultBytes);

    /**
     * 合成失败
     * @param requestContent
     * @param index
     * @param errorInfo
     */
    void onDataLoadError(String requestContent,int index,String errorInfo);
}
