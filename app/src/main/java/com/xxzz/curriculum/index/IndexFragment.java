package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.app.Person;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;
import com.xxzz.curriculum.read.Book;
import com.xxzz.curriculum.read.ReadActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

public class IndexFragment extends Fragment{
    public GridView gridview;
    private IndexBookAdapter adapter;
    private static List<BooKInfo> list=new ArrayList<>();
    private List<String> bookNameList=new ArrayList<String>();
    private static IndexFragment fragment;

    public IndexFragment()  {

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
//        ((Button)view.findViewById(R.id.index_add_button)).setOnClickListener(this::add);
//        ((Button)view.findViewById(R.id.index_del_button)).setOnClickListener(this::del);
        adapter = new IndexBookAdapter(getActivity(), list);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bundle = new Bundle();
//                bundle.putString("book_name", adapter.datas.get(position));
                Intent intent = new Intent();
                intent.putExtra("book_name",bookNameList.get(position));
                intent.setClass(getContext(), ReadActivity.class);
                startActivity(intent);
//                switch (position) {
//                    default:
//                        Toast.makeText(getContext(),"这是一本电子书",Toast.LENGTH_LONG).show();
//                        break;
//                }
            }
        });
        readBookInfoFromFile();
        return view;
    }

    private void readBookInfoFromFile(){
//        this.list = new ArrayList<>();
//        this.bookNameList = new ArrayList<String>();
        String res = null;
        try {
            FragmentActivity activity = getActivity();
            res = Utils.readAllFile(activity.getFilesDir().toPath().resolve("cover/test.json").toFile().toPath());
            JSONObject context = new JSONObject(res);
            JSONArray array = context.getJSONArray("cover");
            for (int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                bookNameList.add(object.getString("book_name"));
                BooKInfo bookinfo=new BooKInfo(object.getString("book_name"),object.getString("cover_path"),object.getString("last_read_time"));
                list.add(bookinfo);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public List<BooKInfo> getList() {
        return list;
    }
    public IndexBookAdapter getAdapter() {return adapter;}
    public List<String> getBookNameList() {
        return bookNameList;
    }
}