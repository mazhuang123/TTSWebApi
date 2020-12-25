package com.mz.ttswebapiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        TTSContentProcessor.TTSContentProcessorListener,SeekBar.OnSeekBarChangeListener {
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
    private SeekBar seekBar,speedSeekBar;
    private ListView speakerListView,rateListView,formatListView;
    private ListViewAdatper speakerListViewAdatper,rateAdapter,formatAdapter;
    TTSContentProcessor ttsContentProcessor;
    private List<String> speakList = new ArrayList<>();
    private List<String> rateList = new ArrayList<>();
    private List<String> formatList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentView = findViewById(R.id.content_view);
        progressBar = findViewById(R.id.tts_listen_book_progress_play_view);
        startBtn = findViewById(R.id.startBtn);
        seekBar = findViewById(R.id.seek_bar);
        speedSeekBar = findViewById(R.id.speed_seek_bar);
        speakerListView = findViewById(R.id.speaker_list_view);
        rateListView = findViewById(R.id.rate_list_view);
        formatListView = findViewById(R.id.format_list_view);
        startBtn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        initSpeakerList();
        intRateList();
        initFormatList();
        speakerListViewAdatper = new ListViewAdatper();
        rateAdapter = new ListViewAdatper();
        formatAdapter = new ListViewAdatper();
        speakerListView.setAdapter(speakerListViewAdatper);
        rateListView.setAdapter(rateAdapter);
        formatListView.setAdapter(formatAdapter);
        speakerListViewAdatper.setData(speakList);
        rateAdapter.setData(rateList);
        formatAdapter.setData(formatList);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.e("此时的语速为："+seekBar.getProgress());
                ttsContentProcessor.updateAudioConfigSpeed(seekBar.getProgress());
            }
        });
        speakerListViewAdatper.addListViewContentListener(new ListViewAdatper.ListViewContentListener() {
            @Override
            public void onClickItem(int position) {
                ttsContentProcessor.updateAudioConfigSpeaker(speakList.get(position));
            }
        });

        rateAdapter.addListViewContentListener(new ListViewAdatper.ListViewContentListener() {
            @Override
            public void onClickItem(int position) {
                String rate = "audio/L16;rate=8000";
                if(position == 0){
                    rate = "audio/L16;rate=8000";
                } else if(position == 1){
                    rate = "audio/L16;rate=16000";
                }
                ttsContentProcessor.updateAudioConfigRate(rate);
            }
        });
        formatAdapter.addListViewContentListener(new ListViewAdatper.ListViewContentListener() {
            @Override
            public void onClickItem(int position) {
                if(position == 0){
                    ttsContentProcessor.updateAudioConfigFormat("raw");
                } else if(position == 1){
                    ttsContentProcessor.updateAudioConfigFormat("lame");
                }
            }
        });

        contentView.setText(content);
        ttsContentProcessor = new TTSContentProcessor(this,content);
        ttsContentProcessor.init();
        ttsContentProcessor.addTTSContentProcessorListener(this);
        speakerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ttsContentProcessor.updateAudioConfigSpeaker(speakList.get(i));
            }
        });
        rateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String rate = "audio/L16;rate=8000";
                if(i == 0){
                    rate = "audio/L16;rate=8000";
                } else if(i == 1){
                    rate = "audio/L16;rate=16000";
                }
                ttsContentProcessor.updateAudioConfigRate(rate);
            }
        });
        formatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    ttsContentProcessor.updateAudioConfigFormat("raw");
                } else if(i == 1){
                    ttsContentProcessor.updateAudioConfigFormat("lame");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startBtn:
                if(ttsContentProcessor.contentProcessorIsPlaying()){
                    ttsContentProcessor.changePlayState(false);
                } else {
                    ttsContentProcessor.changePlayState(true);
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
    public void onTTSContentOperatorMediaPlayWait() {
        setPlayState(true,true);
    }

    @Override
    public void onTTSContentOperatorMediaPlayPause() {
        setPlayState(false,false);
    }

    @Override
    public void onTTSContentOperatorMediaPlayPlaying() {
        setPlayState(true,false);
    }

    @Override
    public void onTTSContentOperatorMediaPlayError(String errorInfo) {
        setPlayState(false,true);
    }

    @Override
    public void onTTSContentOperatorMediaPlayComplete(int index,int sumSize) {
        float progress = (index+1)*100/sumSize;
        LogUtil.e("播放进度为："+progress);
        seekBar.setProgress((int) progress);
    }

    @Override
    public void onTTSContentOperatorHttpSuccess() {
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        ttsContentProcessor.updateReadProgress(seekBar.getProgress());
    }
    private void intRateList(){
        rateList.add("8K");
        rateList.add("16k");
    }
    private void initFormatList(){
        formatList.add("wav/pcm");
        formatList.add("mp3");
    }
    private void initSpeakerList() {
        speakList.add("aisbabyxu");
        speakList.add("aisbaoma");
        speakList.add("aisCatherine");
        speakList.add("aisdalong");
        speakList.add("aisduck");
        speakList.add("aisjinger");
        speakList.add("aisJohn");
        speakList.add("aisLaoma");
        speakList.add("aismengchun");
        speakList.add("aismingma");
        speakList.add("aisnn");
        speakList.add("aisxa");
        speakList.add("aisxa_angry");
        speakList.add("aisxa_happy");
        speakList.add("aisxa_neutral");
        speakList.add("aisxa_sad");
        speakList.add("aisxbao");
        speakList.add("aisxbo");
        speakList.add("aisxcao");
        speakList.add("aisxcao_angry");
        speakList.add("aisxcao_happy");
        speakList.add("aisxcao_neutral");
        speakList.add("aisxcao_sad");
        speakList.add("aisxchun");
        speakList.add("aisxgang");
        speakList.add("aisxhui");
        speakList.add("aisxiaoyu");
        speakList.add("aisxjia");
        speakList.add("aisxkun");
        speakList.add("aisxlin");
        speakList.add("aisxling");
        speakList.add("aisxma");
        speakList.add("aisxmei");
        speakList.add("aisxmm");
        speakList.add("aisxqian");
        speakList.add("aisxqiang");
        speakList.add("aisxrong");
        speakList.add("aisxrui");
        speakList.add("aisxshi");
        speakList.add("aisxshi2");
        speakList.add("aisxshi3");
        speakList.add("aisxtong");
        speakList.add("aisxwz");
        speakList.add("aisxxin");
        speakList.add("aisxxing");
        speakList.add("aisxyan");
        speakList.add("aisxyang");
        speakList.add("aisxyao");
        speakList.add("aisxying");
        speakList.add("aisyfeng");
        speakList.add("aisyping");
        speakList.add("aiszl");
        speakList.add("babyxu");
        speakList.add("catherine");
        speakList.add("dalong");
        speakList.add("donaldduck");
        speakList.add("fangliang");
        speakList.add("henry");
        speakList.add("jiajia");
        speakList.add("jinger");
        speakList.add("john");
        speakList.add("laura");
        speakList.add("nannan");
        speakList.add("niyang");
        speakList.add("robert");
        speakList.add("vils");
        speakList.add("vimary");
        speakList.add("vixf");
        speakList.add("vixy");
        speakList.add("vixying");
        speakList.add("wenqi");
        speakList.add("xiaoai");
        speakList.add("xiaobo");
        speakList.add("xiaofeng");
        speakList.add("xiaohong");
        speakList.add("xiaohou");
        speakList.add("xiaohui");
        speakList.add("xiaojing");
        speakList.add("xiaokun");
        speakList.add("xiaolin");
        speakList.add("xiaoma");
        speakList.add("xiaomei");
        speakList.add("xiaomeng");
        speakList.add("xiaoqi");
        speakList.add("xiaoqian");
        speakList.add("xiaoqiang");
        speakList.add("xiaorong");
        speakList.add("xiaorui");
        speakList.add("xiaoshi");
        speakList.add("xiaowanzi");
        speakList.add("xiaoxin");
        speakList.add("xiaoyan");
        speakList.add("xiaoyang");
        speakList.add("xiaoyao");
        speakList.add("xiaoyi");
        speakList.add("xiaoyin");
        speakList.add("xiaoyu");
        speakList.add("yefang");
        speakList.add("yufeng");
        speakList.add("zhiling");
        speakList.add("catherine");
        speakList.add("henry");
        speakList.add("jianzong");
        speakList.add("xiaomei");
        speakList.add("xiaoqi");
        speakList.add("xiaoyan");
        speakList.add("xiaoyu");
        speakList.add("yufeng");
        speakList.add("yufeng");
        speakList.add("chuchu");
        speakList.add("dahuilang");
        speakList.add("geyou_Actor");
        speakList.add("lichun_MG");
        speakList.add("liying_Actor");
        speakList.add("liyitong");
        speakList.add("miguxiHappy");
        speakList.add("sabeining");
        speakList.add("sabeiningSJ");
        speakList.add("wangguan_MG");
        speakList.add("xiaoai_novel");
        speakList.add("xiaoai_talking");
        speakList.add("xiaobin_talking");
        speakList.add("xiaohou");
        speakList.add("xiaoxi");
        speakList.add("yiping");
        speakList.add("yuanye");

    }
}
