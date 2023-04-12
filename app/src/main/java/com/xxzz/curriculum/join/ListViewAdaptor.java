package com.xxzz.curriculum.join;

import static com.xxzz.curriculum.join.FileOperation.CheckFile;
import static com.xxzz.curriculum.join.FileOperation.IsJbk;
import static com.xxzz.curriculum.join.FileOperation.deleteDFile;
import static com.xxzz.curriculum.join.UnzipUtil.unzipFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xxzz.curriculum.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListViewAdaptor extends BaseAdapter {
    private List<File> fileList;
    private LayoutInflater inflater;
    private Context context;

    public ListViewAdaptor(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public ListViewAdaptor(Context context, List<File> listView) {
        this.context = context;
        this.fileList = listView;

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

    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;;


        if(convertView == null){
            //加载布局文件，将布局文件转换成View对象
            //view =LayoutInflater.from(context).inflate(R.layout.moive_item,null);
            view = LayoutInflater.from(context).inflate(R.layout.join_item,null);
            //创建ViewHolder对象
            viewHolder = new ViewHolder();
            //实例化ViewHolder
            viewHolder.FileImage = view.findViewById(R.id.file_image);
            viewHolder.FileName = view.findViewById(R.id.file_name);
            //将viewHolder的对象存储到View中
            view.setTag(viewHolder);
        }else{
            view = convertView;
            //取出ViewHolder
            viewHolder = (ViewHolder)view.getTag();
        }
        File f = fileList.get(position);

        if(f.isDirectory()) {
            viewHolder.FileName.setText(f.getName());
            viewHolder.FileImage.setImageResource(R.drawable.folder_open);
        }
        else if(IsJbk(f)){
            File tmpPath = context.getCacheDir();
            try {
                unzipFile(f.getPath(), tmpPath.getAbsolutePath());
            if(CheckFile(tmpPath)){
                viewHolder.FileName.setText(f.getName());
                viewHolder.FileImage.setImageResource(R.drawable.book);
            }
            else {
                viewHolder.FileName.setText(f.getName());
                viewHolder.FileImage.setImageResource(R.drawable.file_text2);
            }
                deleteDFile(tmpPath);
            } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }
        else {
            viewHolder.FileName.setText(f.getName());
            viewHolder.FileImage.setImageResource(R.drawable.file_text2);
        }
        return view;
    }

    class ViewHolder{
        ImageView FileImage;
        TextView FileName;
    }
}