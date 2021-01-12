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
public class SampleDialogFragment extends BaseDialogFragment implements ListViewAdatper.ListViewContentListener{
    private TextView titleView;
    private ListView sampeListView;
    private ListViewAdatper sampleAdapter;
    private TTSConfigItemListener ttsConfigItemListener;
    private List<String> rateList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tts_list_view_layout,container,false);
        titleView = view.findViewById(R.id.config_title_view);
        titleView.setText("采样率");
        sampeListView = view.findViewById(R.id.config_list_view);
        intRateList();
        sampleAdapter = new ListViewAdatper();
        sampeListView.setAdapter(sampleAdapter);
        sampleAdapter.setData(rateList);
        sampleAdapter.addListViewContentListener(this);
        return view;
    }
    public void setTitleContent(String content){
        titleView.setText(content);
    }
    public void addSampleItemClickListener(TTSConfigItemListener ttsConfigItemListener){
        this.ttsConfigItemListener = ttsConfigItemListener;
    }
    private void intRateList(){
        rateList.add("8K");
        rateList.add("16k");
    }

    @Override
    public void onClickItem(int position) {
        ttsConfigItemListener.onSampleItemClick(rateList.get(position));
    }
}
