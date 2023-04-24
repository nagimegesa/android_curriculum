package com.xxzz.curriculum.read;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.index.BooKMark;
import com.xxzz.curriculum.index.BookCollection;

import java.util.List;

public class LeftFrameListViewAdaptor extends BaseAdapter {
    private final LayoutInflater inflater;
    private List<BookCollection> list;

    public LeftFrameListViewAdaptor(LayoutInflater inflater, List<BookCollection> list) {
        this.list = list;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BookCollection getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"})
        View view = inflater.inflate(R.layout.read_left_frame_item, null, false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView page = view.findViewById(R.id.read_left_collection_page);
        BookCollection collection = list.get(position);
        page.setText("第" + String.valueOf(collection.getPage()) + "页");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView text = view.findViewById(R.id.read_left_mark_text);
        if (collection instanceof BooKMark) {
            BooKMark mark = (BooKMark) collection;
            text.setText(mark.getText());
        } else {
            text.setVisibility(View.GONE);
        }
        return view;
    }
}