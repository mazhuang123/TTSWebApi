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
public class FormatDialogFragment extends BaseDialogFragment implements ListViewAdatper.ListViewContentListener{
    private TextView titleView;
    private ListView formatListView;
    private ListViewAdatper formatAdapter;
    private TTSConfigItemListener ttsConfigItemListener;
    private List<String> formatList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tts_list_view_layout,container,false);
        titleView = view.findViewById(R.id.config_title_view);
        titleView.setText("编码格式");
        formatListView = view.findViewById(R.id.config_list_view);
        initFormatList();
        formatAdapter = new ListViewAdatper();
        formatListView.setAdapter(formatAdapter);
        formatAdapter.setData(formatList);
        formatAdapter.addListViewContentListener(this);
        return view;
    }
    public void setTitleContent(String content){
        titleView.setText(content);
    }
    public void addTTSFormatItemClickListener(TTSConfigItemListener ttsConfigItemListener){
        this.ttsConfigItemListener = ttsConfigItemListener;
    }
    @Override
    public void onClickItem(int position) {
        ttsConfigItemListener.onFormatItemClick(formatList.get(position));
    }
    private void initFormatList(){
        formatList.add("wav/pcm");
        formatList.add("mp3");
    }
}
