package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.read.ReadActivity;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment{
    public GridView gridview;
    private IndexBookAdapter adapter;

    private List<BooKInfo> list;
    private static IndexFragment fragment;

    public IndexFragment() {
    }
    /**
     * 单例模式, 写的时候只能用这个得到 IndexFragment 的实例, 不要直接调用 new
     *
     * @return IndexFragment实例
     */
    public static IndexFragment getInstance() {
        if (fragment == null)
            fragment = new IndexFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public void add(View view) {
        adapter.addData("item");
        adapter.notifyDataSetChanged();
    }

    /** 移除item */
    public void del(View view) {
        adapter.delData();
        adapter.notifyDataSetChanged();
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_index,container,false);
        gridview=view.findViewById(R.id.list);
        ((Button)view.findViewById(R.id.index_add_button)).setOnClickListener(this::add);
        ((Button)view.findViewById(R.id.index_del_button)).setOnClickListener(this::del);

        adapter = new IndexBookAdapter(getActivity(), readBookInfoFromFile());

        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bundle = new Bundle();
//                bundle.putString("book_name", adapter.datas.get(position));
                Intent intent = new Intent();
                // intent.putExtra("book_name",adapter.datas.get(position));
                intent.setClass(getContext(), ReadActivity.class);
                startActivity(intent);
//                switch (position) {
//                    default:
//                        Toast.makeText(getContext(),"这是一本电子书",Toast.LENGTH_LONG).show();
//                        break;
//                }
            }
        });
        return view;
    }

    private List<BooKInfo> readBookInfoFromFile() {
        return new ArrayList<>();
    }
}