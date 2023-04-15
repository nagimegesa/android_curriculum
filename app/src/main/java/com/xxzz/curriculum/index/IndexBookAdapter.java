package com.xxzz.curriculum.index;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzz.curriculum.R;

import java.util.List;

public class IndexBookAdapter extends BaseAdapter {
    private Context context;
    private List<BooKInfo> datas;
    public IndexBookAdapter(Context context, List<BooKInfo> datas) {
        this.context = context;
        this.datas = datas;
    }
    static class BooKHolder{
        public ImageView image;
        public TextView name;
    }
    /** 添加item数据 */
    public void addData(String text) {

    }

    /** 移除item数据 */
    public void delData() {
        if (datas != null && datas.size() > 0)
            datas.remove(datas.size() - 1);// 移除最后一条数据
    }

    @Override
    public int getCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BooKHolder bookholder=new BooKHolder();
        convertView = View.inflate(context, R.layout.item_book, null);
        bookholder.name=(TextView)convertView.findViewById(R.id.book_name);
        bookholder.name.setText(datas.get(position).getName());
        return convertView;
    }
}