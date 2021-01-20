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
public class EngineTypeDialogFragment extends BaseDialogFragment implements ListViewAdatper.ListViewContentListener{
    private TextView titleView;
    private ListView engineTypeListView;
    private ListViewAdatper engineTypeAdapter;
    private TTSConfigItemListener ttsConfigItemListener;
    private List<String> formatList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tts_list_view_layout,container,false);
        titleView = view.findViewById(R.id.config_title_view);
        titleView.setText("引擎类型");
        engineTypeListView = view.findViewById(R.id.config_list_view);
        initEngineTypeList();
        engineTypeAdapter = new ListViewAdatper();
        engineTypeListView.setAdapter(engineTypeAdapter);
        engineTypeAdapter.setData(formatList);
        engineTypeAdapter.addListViewContentListener(this);
        return view;
    }
    public void addEngineTypeItemClickListener(TTSConfigItemListener ttsConfigItemListener){
        this.ttsConfigItemListener = ttsConfigItemListener;
    }
    @Override
    public void onClickItem(int position) {
        ttsConfigItemListener.onEngineTypeItemClick(formatList.get(position));
        getDialog().dismiss();
    }
    private void initEngineTypeList(){
        formatList.add("https");
        formatList.add("离线sdk");
    }
}
