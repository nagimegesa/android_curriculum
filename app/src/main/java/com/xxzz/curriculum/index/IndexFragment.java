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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndexFragment extends Fragment {

    private static IndexFragment fragment;
    private DragGridView gridview;
    private final List<BooKInfo> list = new ArrayList<>();
    private IndexBookAdapter adapter;
    private final List<String> bookNameList = new ArrayList<String>();

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
                try {
                    String res = Utils.readAllFile(getActivity().getFilesDir().toPath().resolve("cover/text.json").toFile().toPath());
                    JSONObject context = new JSONObject(res);
                    JSONArray array = context.getJSONArray("cover");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        if (object.getString("book_name").equals(bookNameList.get(position))) {
                            String s = String.valueOf(System.currentTimeMillis());
                            object.put("last_read_time", s);
                            Utils.writeFile(getActivity().getFilesDir().toPath().resolve("cover/text.json"), context.toString());
                        }
                    }
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
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
        try {
            creatBookInfoFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    void deleteBook(String bookName) throws IOException, JSONException {
        BookManager manager = new BookManager();
        manager.deleteBookInfo(bookName);
        deleteBookContentFile(bookName);
        deleteBookCoverFile(bookName);
        readBookInfoFromFile();
        adapter.setDatas(list);
        gridview.setAdapter(adapter);
    }

    private void deleteBookCoverFile(String bookName) throws IOException, JSONException {
        String coverPath = getActivity().getFilesDir() + "/cover/text.json";
        Path p = new File(coverPath).toPath();
        String buf = Utils.readAllFile(p);
        JSONObject object = new JSONObject(buf);
        int count = object.getInt("count");
        int removeIndex = -1;
        JSONArray array = object.getJSONArray("cover");
        for(int i = 0;i < array.length(); ++i) {
            JSONObject o = (JSONObject) array.get(i);
            if(o.getString("book_name").equals(bookName)) {
                removeIndex = i;
                break;
            }
        }

        if(removeIndex != -1) {
            array.remove(removeIndex);
            object.put("count", count - 1);
            object.put("cover", array);
        }
        Utils.writeFile(p, object.toString());
    }

    private void deleteBookContentFile(String bookName) {
        String path = getActivity().getFilesDir() + "/Book/" + bookName;
        Utils.removeFiles(new File(path).toPath());
    }

    private void creatBookInfoFile() throws IOException {
        String path = getActivity().getFilesDir() + "/cover";
        File file = new File(path, "text.json");
        if (!file.exists()) {
            File baseFile = new File(path);
            baseFile.mkdir();
            file.createNewFile();
            String sp = System.lineSeparator();
            Utils.writeFile(file.toPath(), "{" + sp +
                    "   \"count\": 0," + sp +
                    "   \"cover\": []" + sp +
                    "}");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        readBookInfoFromFile();
        adapter.setDatas(list);
        gridview.setAdapter(adapter);
    }

    public void updateBookList() {
        readBookInfoFromFile();
        adapter.setDatas(list);
        gridview.setAdapter(adapter);
    }
    void readBookInfoFromFile() {
        list.clear();
        bookNameList.clear();
        String res = null;
        try {
            FragmentActivity activity = getActivity();
            res = Utils.readAllFile(activity.getFilesDir().toPath().resolve("cover/text.json").toFile().toPath());
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

    public void setOnClickToRead() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String res = Utils.readAllFile(getActivity().getFilesDir().toPath().resolve("cover/text.json").toFile().toPath());
                    JSONObject context = new JSONObject(res);
                    JSONArray array = context.getJSONArray("cover");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        if (object.getString("book_name").equals(bookNameList.get(position))) {
                            String s = String.valueOf(System.currentTimeMillis());
                            object.put("last_read_time", s);
                            Utils.writeFile(getActivity().getFilesDir().toPath().resolve("cover/text.json"), context.toString());
                        }
                    }
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
                Intent intent = new Intent();
                intent.putExtra("book_name", bookNameList.get(position));
                intent.setClass(getContext(), ReadActivity.class);
                startActivity(intent);
            }
        });
    }
    public void setOnClickToDelete() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    deleteBook(getBookNameList().get(position));
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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