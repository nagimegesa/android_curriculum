package com.xxzz.curriculum.join;


import static com.xxzz.curriculum.Utils.makeToast;
import static com.xxzz.curriculum.join.FileOperation.CheckFile;
import static com.xxzz.curriculum.join.FileOperation.IsJbk;
import static com.xxzz.curriculum.join.FileOperation.copyDir;
import static com.xxzz.curriculum.join.FileOperation.copyFileUsingStream;
import static com.xxzz.curriculum.join.FileOperation.deleteDFile;
import static com.xxzz.curriculum.join.UnzipUtil.unzipFile;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.xxzz.curriculum.R;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class JoinBookActivity extends AppCompatActivity implements View.OnClickListener {
    private int mTag = 1 ;
    private int REQUEST_CODE =7325;
    String BookPath = "/data/data/com.xxzz.curriculum/files/Book";
    String CoverPath = "/data/data/com.xxzz.curriculum/files/Cover";
    String jsonPath = "/data/data/com.xxzz.curriculum/files/jbk_config.json";

    private List<File> FileList;
    private ListViewAdaptor adapter;
    private ListView listView;
    File FileTep = Environment.getExternalStorageDirectory();
    String []returnimage  ;

    ImageButton imageButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_join_book);
        imageButton = (ImageButton) findViewById(R.id.imageBtn);

        accesspermisson();
        listView = (ListView) findViewById(R.id.join_list_view);
        InitListView(Environment.getExternalStorageDirectory().getPath());
        adapter = new ListViewAdaptor(JoinBookActivity.this,FileList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                File file =FileList.get(i);
                FileTep = file;
                //makeToast(JoinBookActivity.this,file.getName(),100);
                if(file.isDirectory()){
                    InitListView(file.getPath());
                    adapter = new ListViewAdaptor(JoinBookActivity.this,FileList);
                    listView.setAdapter(adapter);
                }
                else if(IsJbk(file)){
                    try {
                        File tmpPath = getCacheDir();
                        unzipFile(file.getPath(), tmpPath.getAbsolutePath());
                        if(CheckFile(tmpPath)){
                            copyDir(tmpPath.getAbsolutePath()+"/main",CoverPath);
                            copyDir(tmpPath.getAbsolutePath()+"/text",BookPath);
                            copyFileUsingStream(tmpPath.getAbsolutePath()+"/jbk_config.json",jsonPath);
                            makeToast(JoinBookActivity.this,"加入成功",100);
                        }
                        else
                            makeToast(JoinBookActivity.this,"所选文件不符合格式",100);
                        deleteDFile(tmpPath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    makeToast(JoinBookActivity.this,"所选文件不符合格式",100);
                }


            }
        });
           // openFileManager();
        //  getLayoutInflater();
        imageButton.setOnClickListener(this);
    }



    public  void InitListView(String path){

        FileList = new ArrayList<File>();
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f: files){
            FileList.add(f);
        }
    }

    public void accesspermisson(){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R ||
                Environment.isExternalStorageManager()) {
            Toast.makeText(this, "已获得访问所有文件的权限", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent,REQUEST_CODE);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == REQUEST_CODE){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()) {
                Toast.makeText(this, "没有访问所有文件的权限", Toast.LENGTH_SHORT).show();
                finish();
            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBtn:
                if(FileTep==Environment.getExternalStorageDirectory()){
                finish();
            }
                File file = FileTep.getParentFile();

                InitListView(file.getPath());
                adapter = new ListViewAdaptor(JoinBookActivity.this,FileList);
                listView.setAdapter(adapter);
                FileTep = FileTep.getParentFile();
                break;


        }
    }
}