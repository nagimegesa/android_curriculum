package com.xxzz.curriculum.index;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxzz.curriculum.R;

import java.util.Collections;
import java.util.Comparator;
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
    public void refreshData(List<BooKInfo> datas) {
        Collections.sort(datas, new Comparator<BooKInfo>() {
            @Override
            public int compare(BooKInfo b1, BooKInfo b2) {
                return Integer.valueOf(b2.getLastReadTime())-Integer.valueOf(b1.getLastReadTime());
            }
        });
        notifyDataSetChanged();
    }
    public void searchData(List<BooKInfo> datas){
        this.datas=datas;
        notifyDataSetChanged();
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
        View v = View.inflate(context, R.layout.item_book, null);
        bookholder.name=(TextView)v.findViewById(R.id.book_name);
        bookholder.image = (ImageView) v.findViewById(R.id.book_image);
        bookholder.name.setText(datas.get(position).getName());
        Bitmap bitmap = BitmapFactory.decodeFile(datas.get(position).getCoverPath());
        bookholder.image.setImageBitmap(bitmap);
        return v;
    }
}