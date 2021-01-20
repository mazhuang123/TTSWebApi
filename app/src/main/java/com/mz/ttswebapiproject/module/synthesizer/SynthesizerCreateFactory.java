package com.mz.ttswebapiproject.module.synthesizer;

import com.mz.ttswebapiproject.config.Config;
import com.mz.ttswebapiproject.module.synthesizer.http.HttpSynthesizer;
import com.mz.ttswebapiproject.module.synthesizer.offline_tts.LingXiSynthesizer;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/19 20:33
 * @Description 文件描述：创建合成器的工厂类
 *
 * https://www.cnblogs.com/zailushang1996/p/8601808.html
 */
public class SynthesizerCreateFactory implements SynthesizerCreateInterface {
    @Override
    public DataSynthesizer createSynthesizer(String synthesizerId) {
        DataSynthesizer dataSynthesizer = null;
        switch (synthesizerId){
            case Config.ENGINE_TYPE_HTTP:
                dataSynthesizer = new HttpSynthesizer();
                break;
            case Config.ENGINE_TYPE_LINGXI_OFFLINE:
                dataSynthesizer = new LingXiSynthesizer();
                break;
            default:
                dataSynthesizer = new LingXiSynthesizer();
                break;
        }
        return dataSynthesizer;
    }
}
