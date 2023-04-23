package com.xxzz.curriculum.index;

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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;
        BookCollectionAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.setting_bookmark_listitem, null);
            viewHolder = new BookCollectionAdapter.ViewHolder();
//            viewHolder.bookName = view.findViewById(R.id.name);
//            viewHolder.page = view.findViewById(R.id.page);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (BookCollectionAdapter.ViewHolder) view.getTag();
        }
        viewHolder.bookName.setText(list.get(i).getBookName());
        viewHolder.page.setText(list.get(i).getPage() + "");
        return view;
    }

    class ViewHolder {
        TextView bookName;
        TextView page;
    }
}
