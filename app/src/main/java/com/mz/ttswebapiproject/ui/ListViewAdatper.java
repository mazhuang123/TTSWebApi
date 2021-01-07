package com.mz.ttswebapiproject.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mz.ttswebapiproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 作者：mazhuang
 * @Date 创建时间：2020/12/24 16:30
 * @Description 文件描述：
 */
public class ListViewAdatper extends BaseAdapter {
    private List<String> contentList = new ArrayList<>();
    private int selectPosition = -1;
    public void setData(List<String> nameList){
        contentList.clear();
        contentList.addAll(nameList);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object getItem(int i) {
        return contentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_item,viewGroup,false);
        final TextView nameView = view.findViewById(R.id.text_view);
        nameView.setText(contentList.get(i));
        if(i == selectPosition){
            nameView.setBackgroundColor(viewGroup.getResources().getColor(R.color.colorPrimary));
        } else {
            nameView.setBackgroundColor(viewGroup.getResources().getColor(R.color.white));
        }
        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectPosition!= i){
                    selectPosition = i;
                    nameView.setBackgroundColor(viewGroup.getResources().getColor(R.color.colorPrimary));
                }
                notifyDataSetChanged();
                listViewContentListener.onClickItem(i);
            }
        });
        return view;
    }
    public interface ListViewContentListener{
        void onClickItem(int position);
    }
    private ListViewContentListener listViewContentListener;
    public void addListViewContentListener(ListViewContentListener listViewContentListener){
        this.listViewContentListener = listViewContentListener;
    }
}
