package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;
import com.xxzz.curriculum.read.ReadActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndexFragment extends Fragment {
    private static final List<BooKInfo> list = new ArrayList<>();
    private static IndexFragment fragment;
    private final List<String> bookNameList = new ArrayList<String>();
    public DragGridView gridview;
    private IndexBookAdapter adapter;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        gridview = view.findViewById(R.id.list);
        adapter = new IndexBookAdapter(getActivity(), list);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("book_name", bookNameList.get(position));
                intent.setClass(getContext(), ReadActivity.class);
                startActivity(intent);
            }
        });
        gridview.setOnChangeListener(new DragGridView.OnChanageListener() {
            @Override
            public void onChange(int from, int to) {
                BooKInfo temp = list.get(from);
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(list, i, i + 1);
                    }
                } else if (from > to) {
                    for (int i = from; i > to; i--) {
                        Collections.swap(list, i, i - 1);
                    }
                }
                list.set(to, temp);
                adapter.notifyDataSetChanged();
            }
        });
        readBookInfoFromFile();
        return view;
    }

    private void readBookInfoFromFile() {
        String res = null;
        try {
            FragmentActivity activity = getActivity();
            res = Utils.readAllFile(activity.getFilesDir().toPath().resolve("cover/test.json").toFile().toPath());
            JSONObject context = new JSONObject(res);
            JSONArray array = context.getJSONArray("cover");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (!bookNameList.contains(object.getString("book_name"))) {
                    bookNameList.add(object.getString("book_name"));
                    BooKInfo bookinfo = new BooKInfo(object.getString("book_name"), object.getString("cover_path"), object.getString("last_read_time"));
                    list.add(bookinfo);
                }
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BooKInfo> getList() {
        return list;
    }

    public IndexBookAdapter getAdapter() {
        return adapter;
    }

    public List<String> getBookNameList() {
        return bookNameList;
    }
}