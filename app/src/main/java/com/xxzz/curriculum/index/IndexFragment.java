package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.xxzz.curriculum.R;

public class IndexFragment extends Fragment{
    private GridView gridview;
    private String text;
    private static int count = 0;
    private IndexBookAdapter adapter;
    private static IndexFragment fragment;
    public IndexFragment() {
    }
    /**
     *  单例模式, 写的时候只能用这个得到 IndexFragment 的实例, 不要直接调用 new
     * @return IndexFragment实例
     */
    public static IndexFragment getInstance() {
        if(fragment == null)
            fragment = new IndexFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public void add(View view) {
        text = " --- item -- " + count + " ---";
        adapter.addData(text);
        adapter.notifyDataSetChanged();
        count++;
    }

    /** 移除item */
    public void del(View view) {
        adapter.delData();
        adapter.notifyDataSetChanged();
        // 判断是否>0
        if (count > 0)
            count--;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_index,container,false);
        gridview=view.findViewById(R.id.list);
        ((Button)view.findViewById(R.id.index_add_button)).setOnClickListener(this::add);
        ((Button)view.findViewById(R.id.index_del_button)).setOnClickListener(this::del);
        adapter = new IndexBookAdapter(getActivity());
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    default:
                        Toast.makeText(getContext(),"这是一本电子书",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        return view;
    }
}