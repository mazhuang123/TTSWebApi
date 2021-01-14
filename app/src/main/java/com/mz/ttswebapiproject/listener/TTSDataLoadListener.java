package com.mz.ttswebapiproject.listener;

import java.util.ArrayList;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/12 20:33
 * @Description 文件描述：
 */
public interface TTSDataLoadListener {

    void onDataLoadSuccess(String requestContent, int index, ArrayList<byte[]> resultBytes);

    void onDataLoadError(String requestContent,int index,String errorInfo);
}
