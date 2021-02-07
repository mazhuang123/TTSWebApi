package com.mz.ttswebapiproject.bean;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/8 9:58
 * @Description 文件描述：
 */
public class AudioConfig {
    private String auf = "audio/L16;rate=16000";
    private String aue = "raw";
    private String voice_name = "xiaoyan";
    private String speed = "50";
    private String volume = "20";
    private String pitch = "50";
    private String engine_type = "intp65";
    private String text_type = "text";
    private String test1;
    public String getAuf() {
        return auf;
    }

    public void setAuf(String auf) {
        this.auf = auf;
    }

    public String getAue() {
        return aue;
    }

    public void setAue(String aue) {
        this.aue = aue;
    }

    public String getVoice_name() {
        return voice_name;
    }

    public void setVoice_name(String voice_name) {
        this.voice_name = voice_name;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getEngine_type() {
        return engine_type;
    }

    public void setEngine_type(String engine_type) {
        this.engine_type = engine_type;
    }

    public String getText_type() {
        return text_type;
    }

    public void setText_type(String text_type) {
        this.text_type = text_type;
    }
}
