package com.mz.ttswebapiproject.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mz.ttswebapiproject.R;
import com.mz.ttswebapiproject.listener.TTSConfigItemListener;
import com.mz.ttswebapiproject.ui.ListViewAdatper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2021/1/11 16:57
 * @Description 文件描述：
 */
public class SpeakerDialogFragment extends BaseDialogFragment implements ListViewAdatper.ListViewContentListener{
    private TextView titleView;
    private ListView speakerListView;
    private ListViewAdatper speakerListViewAdatper;
    private List<String> speakList = new ArrayList<>();
    private TTSConfigItemListener ttsConfigItemListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tts_list_view_layout,container,false);
        titleView = view.findViewById(R.id.config_title_view);
        titleView.setText("发音人");
        speakerListView = view.findViewById(R.id.config_list_view);
        speakerListViewAdatper = new ListViewAdatper();
        initSpeakerList();
        speakerListView.setAdapter(speakerListViewAdatper);
        speakerListViewAdatper.setData(speakList);
        speakerListViewAdatper.addListViewContentListener(this);
        return view;
    }
    public void addTTSConfigItemListener(TTSConfigItemListener ttsConfigItemListener){
        this.ttsConfigItemListener = ttsConfigItemListener;
    }
    @Override
    public void onClickItem(int position) {
        ttsConfigItemListener.onSpeakerItemClick(speakList.get(position));
        getDialog().dismiss();
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
