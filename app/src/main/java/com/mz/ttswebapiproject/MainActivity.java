package com.mz.ttswebapiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @Author 作者：mazhuang
 *
 * @Date 创建时间： 2020/12/8 10:41
 *
 * @Description 文件描述: https://www.jianshu.com/p/da4a806e599b
 * https://www.cnblogs.com/badaoliumangqizhi/p/14023058.html
 *
 * https://github.com/452896915/jieba-android
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener , TTSContentProcessor.TTSContentProcessorListener {
    private String content = "美国登月的旗子是就是普通尼龙做的," +
            "5.5美刀一面" +
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
    private TextView contentView;
    private Button startBtn;
    private ProgressBar progressBar;
    TTSContentProcessor ttsContentProcessor;
    private boolean isRequested;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentView = findViewById(R.id.content_view);
        progressBar = findViewById(R.id.tts_listen_book_progress_play_view);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(this);
        contentView.setText(content);
        ttsContentProcessor = new TTSContentProcessor(this,content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startBtn:
                if(ttsContentProcessor.contentProcessorIsPlaying()){
                    ttsContentProcessor.changePlayState(false);
                    setPlayState(false,false);
                    ttsContentProcessor.contentProcessorPause();
                } else {
                    if(!isRequested){
                        setPlayState(true,true);
                        ttsContentProcessor.addTTSContentProcessorListener(this);
                        ttsContentProcessor.startTTSFromHttp(new AudioConfig(),0);
                    } else {
                        ttsContentProcessor.changePlayState(true);
                        setPlayState(true,false);
                        ttsContentProcessor.startPlay();
                    }
                }
                break;
        }
    }

    private void startLoading(){
        if(progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    private void stopLoading(){
        if(progressBar!=null){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    public void setPlayState(boolean isPlaying,boolean isStartLoad){
        if(startBtn != null){
            if (isPlaying) {
                startBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.listen_pause_icon));
                if (isStartLoad){
                    startLoading();
                }else{
                    stopLoading();
                }
            } else {
                startBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.listen_play_icon));
                stopLoading();
            }
        }
    }
    @Override
    public void onTTSContentOperatorMediaPlayError(String errorInfo) {

    }

    @Override
    public void onTTSContentOperatorMediaPlayComplete() {

    }

    @Override
    public void onTTSContentOperatorHttpSuccess() {
        isRequested = true;
    }

    @Override
    public void onTTSContentOperatorHttpFail(String info) {
    }

    @Override
    public void onTTSContentOperatorTextColorChange(final SpannableString spannableString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contentView.setText(spannableString);
            }
        });
    }
}
