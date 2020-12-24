package com.mz.ttswebapiproject;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/22 10:38
 * @Description 文件描述：
 */
public class TextConfig {
    private String content;
    private int index;//某句话在分割后生成的数组中占的位置
    private int paraStart;
    private int paraEnd;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParaStart() {
        return paraStart;
    }

    public void setParaStart(int paraStart) {
        this.paraStart = paraStart;
    }

    public int getParaEnd() {
        return paraEnd;
    }

    public void setParaEnd(int paraEnd) {
        this.paraEnd = paraEnd;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
