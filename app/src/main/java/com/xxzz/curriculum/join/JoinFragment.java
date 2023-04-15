package com.xxzz.curriculum.join;

import static com.xxzz.curriculum.Utils.makeToast;
import static com.xxzz.curriculum.join.FileOperation.CheckFile;
import static com.xxzz.curriculum.join.FileOperation.IsJbk;
import static com.xxzz.curriculum.join.FileOperation.copyDir;
import static com.xxzz.curriculum.join.FileOperation.copyFileUsingStream;
import static com.xxzz.curriculum.join.FileOperation.deleteDFile;
import static com.xxzz.curriculum.join.UnzipUtil.unzipFile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.index.IndexFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JoinFragment extends Fragment {

    private static JoinFragment fragment;
    private List<File> FileList;
    private ListViewAdaptor adapter;
    private ListView listView;
    File FileTep = Environment.getExternalStorageDirectory();
    String BookPath = "/data/data/com.xxzz.curriculum/files/Book";
    String CoverPath = "/data/data/com.xxzz.curriculum/files/Cover";
    String jsonPath = "/data/data/com.xxzz.curriculum/files/jbk_config.json";
    public JoinFragment(){

    }
    public static JoinFragment getFragment(){
          if(fragment==null)
            fragment = new JoinFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_join,container,false);
        listView = view.findViewById(R.id.join_list_view);
        InitListView(Environment.getExternalStorageDirectory().getPath());
        adapter = new ListViewAdaptor(getActivity(),FileList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                File file =FileList.get(i);
                FileTep = file;
                //makeToast(JoinBookActivity.this,file.getName(),100);
                if(file.isDirectory()){
                    InitListView(file.getPath());
                    adapter = new ListViewAdaptor(getActivity(),FileList);
                    listView.setAdapter(adapter);
                }
                else if(IsJbk(file)){
                    try {
                        File tmpPath = getActivity().getCacheDir();
                        unzipFile(file.getPath(), tmpPath.getAbsolutePath());
                        if(CheckFile(tmpPath)){
                            copyDir(tmpPath.getAbsolutePath()+"/main",CoverPath);
                            copyDir(tmpPath.getAbsolutePath()+"/text",BookPath);
                            copyFileUsingStream(tmpPath.getAbsolutePath()+"/jbk_config.json",jsonPath);
                            makeToast(getActivity(),"加入成功",100);
                        }
                        else
                            makeToast(getActivity(),"所选文件不符合格式",100);
                        deleteDFile(tmpPath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    makeToast(getActivity(),"所选文件不符合格式",100);
                }


            }
        });
        return inflater.inflate(R.layout.fragment_join, container, false);
    }

    public  void InitListView(String path){
        FileList = new ArrayList<File>();
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f: files){
            FileList.add(f);
        }
    }
}