package com.mz.ttswebapiproject.listener;

import com.mz.ttswebapiproject.bean.SentenceInfo;

import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/20 17:19
 * @Description 文件描述：
 */
public interface TTSSentenceInfoLoadListener {
    void obtainSentenceInfo(List<SentenceInfo> configs,String content);

}
