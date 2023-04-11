package com.xxzz.curriculum.join;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xxzz.curriculum.R;

import java.io.File;
import java.util.List;

public class ListViewAdaptor extends BaseAdapter {
    private final LayoutInflater inflater;
    private List<File> fileList;

    public ListViewAdaptor(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File f = fileList.get(position);
        @SuppressLint("ViewHolder")
        View view = inflater.inflate(R.layout.join_item, null);
        if (f.isDirectory()) {
        } else {
        }
        return view;
    }
}
