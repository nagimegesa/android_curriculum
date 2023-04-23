package com.xxzz.curriculum.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxzz.curriculum.R;

import java.util.List;

public class bookMarkAdapter extends BaseAdapter {
    private Context context;
    private List<BooKMark> list;

    public bookMarkAdapter(Context context, List<BooKMark> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.setting_bookmark_listitem, null);
            viewHolder = new ViewHolder();
            viewHolder.bookName = view.findViewById(R.id.bookmark_name);
            viewHolder.page = view.findViewById(R.id.page);
            viewHolder.text = view.findViewById(R.id.bookmark_text);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.bookName.setText(list.get(i).getBookName());
        viewHolder.page.setText(list.get(i).getPage() + "");
        viewHolder.text.setText(list.get(i).getText());
        return view;
    }

    class ViewHolder {
        TextView bookName;
        TextView text;
        TextView page;
    }
}
