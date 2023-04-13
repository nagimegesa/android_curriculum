package com.xxzz.curriculum.index;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import com.xxzz.curriculum.R;
import java.util.ArrayList;
import java.util.List;

public class IndexBookAdapter extends BaseAdapter {
    private Context context;
    private final List<String> datas = new ArrayList<String>();

    public IndexBookAdapter(Context context) {
        this.context = context;
    }

    /** 添加item数据 */
    public void addData(String text) {
        if (datas != null)
            datas.add(text);// 添加数据
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
        convertView = View.inflate(context, R.layout.item_book, null);
        return convertView;
    }
}
