package com.mz.ttswebapiproject.config;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/21 14:32
 * @Description 文件描述：
 */
public class Config {
    public static final String APP_ID = "5fbccb84";
    public static final String TTS_URL = "http://openapi.lingxicloud.com/v1/service/v1/tts";
    public static final String ENGINE_TYPE_HTTP = "https_engine";
    public static final String ENGINE_TYPE_LINGXI_OFFLINE = "lingxi_offline_engine";



    private static String content = "美国登月的旗子是就是普通尼龙做的," +
            "5美刀一面" +
            "而这个尼龙是聚酰胺纤维（锦纶）的一种说法。" +
            "通俗的说," +
            "尼龙就是一种塑料。" +
            "打个不恰当的比方," +
            "美国国旗就好比是塑料袋," +
            "发生塑性形变后不会自动复原。" +
            "而从发射到登月过程中又没有好好保管," +
            "放到发射仓里是一坨,拿出来是一坨," +
            "插到月球上还是那一坨,所以看起来有褶皱。" +
            "事实上在阿波罗登月计划中每次登月都会插旗子," +
            "到现在载人登月六次,月球上插了五面旗子。" +
            "最出名的那面," +
            "也就是阿波罗11号阿姆斯特朗插的," +
            "因为离返回舱太近被引擎排气吹翻了," +
            "所以现在只剩五面在月球上," +
            "量美洲之物力,攀苏联之比心," +
            "整个阿波罗登月计划耗费的财力超出人们的想象," +
            "而且只想着怎么超过苏联," +
            "不会像我国这次登月在国旗上下太多功夫。" +
            "他们要做的," +
            "只是插旗再拍个照," +
            "给全世界看：你美国爸爸终究是你美国爸爸。" +
            "反正没有人能上月球去看看旗子是啥样的。" +
            "我国嫦娥五号登月用的旗子据说是以国产高性能芳纶纤维材料为主、采用武汉纺织大学获国家科技进步一等奖的“高效短流程嵌入式复合纺纱技术”," +
            "用两种以上的材料制作的。" +
            "支架采用杆系结构," +
            "国旗由卷轴形式展开," +
            "不容易出现褶皱：";
    public static String getContent(){
        return content;
    }
}
