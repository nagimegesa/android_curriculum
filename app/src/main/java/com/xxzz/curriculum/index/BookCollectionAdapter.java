package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxzz.curriculum.R;

import java.util.List;

public class BookCollectionAdapter extends BaseAdapter {
    private Context context;
    private List<BookCollection> list;

    public BookCollectionAdapter(Context context, List<BookCollection> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BookCollection getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(context).inflate(R.layout.setting_bookmark_listitem, null);
        TextView bookName = view.findViewById(R.id.bookmark_name);
        TextView page = view.findViewById(R.id.bookmark_page);
        TextView text = view.findViewById(R.id.bookmark_text);
        text.setVisibility(View.GONE);
        bookName.setText(list.get(i).getBookName());
        page.setText(String.valueOf(list.get(i).getPage()));
        return view;
    }
}
