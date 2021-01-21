package com.mz.ttswebapiproject.module.text;

import com.mz.ttswebapiproject.bean.SentenceInfo;
import com.mz.ttswebapiproject.listener.TTSSentenceInfoLoadListener;
import com.mz.ttswebapiproject.listener.TTSTextLoadListener;
import com.mz.ttswebapiproject.util.TTSTextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 14:30
 * @Description 文件描述：https://blog.csdn.net/expect521/article/details/107107802
 */
public class TTSTextProcessor implements TTSTextLoadListener {
    private TTSDataSource ttsDataSource;
    private TTSSentenceInfoLoadListener ttsSentenceInfoLoadListener;
    public TTSTextProcessor() {
        ttsDataSource = new TTSDataSource();
        ttsDataSource.addTTSLoadListener(this);
    }

    public void requestPageData(String chapterId) {
        ttsDataSource.requestTextData(chapterId);
    }
    @Override
    public void onTextLoadSuccess(String content) {
        List<SentenceInfo> sentenceInfoList = computeParaPosition(content);
        ttsSentenceInfoLoadListener.obtainSentenceInfo(sentenceInfoList,content);
    }
    public void addTTSSentenceInfoLoadListener(TTSSentenceInfoLoadListener ttsSentenceInfoLoadListener){
        this.ttsSentenceInfoLoadListener = ttsSentenceInfoLoadListener;
    }
    public List<SentenceInfo> computeParaPosition(String content) {
        String[] contentArray = TTSTextUtil.splitSentence(content);
        List<SentenceInfo> sentenceInfoList = new ArrayList<>();
        int paraStart = 0;
        int paraEnd = 0;
        for (int i = 0; i < contentArray.length; i++) {
            String tempContent = contentArray[i];
            SentenceInfo sentenceInfo = new SentenceInfo();
            sentenceInfo.setIndex(i);
            sentenceInfo.setContent(contentArray[i]);
            sentenceInfo.setParaStart(paraStart);
            paraEnd = paraStart + tempContent.length()-1;
            sentenceInfo.setParaEnd(paraEnd);
            paraStart = paraEnd + 1;
            sentenceInfoList.add(sentenceInfo);
        }
        return sentenceInfoList;
    }

}

