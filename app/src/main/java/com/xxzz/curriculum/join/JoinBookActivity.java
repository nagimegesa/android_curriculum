package com.xxzz.curriculum.join;


import static com.xxzz.curriculum.join.FileOperation.ChectFile;
import static com.xxzz.curriculum.join.FileOperation.copyDir;
import static com.xxzz.curriculum.join.FileOperation.copyFileUsingStream;
import static com.xxzz.curriculum.join.FileOperation.deleteDFile;
import static com.xxzz.curriculum.join.UnzipUtil.unzipFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.xxzz.curriculum.Permission;
import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class JoinBookActivity extends AppCompatActivity {
    private int mTag = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_book);
        Permission permission = new Permission();
        // permission.checkPermissions(this);
        if(permission.checkPermissions(this)) {
            openFileManager();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Permission.RequestCode) {
            //Utils.makeToast(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT);
            openFileManager();
        }
    }
    // 打开文件管理器选择文件
    private void openFileManager() {

        // 打开文件管理器选择文件
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //打开多个文件
        startActivityForResult(intent, mTag);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == mTag) {
                //File file = null;
                //使用ACTION_GET_CONTENT时 选择文件时多个调用用ClipData
                ClipData clipData = data.getClipData();
                if (clipData!=null)
                {
                    for (int i=0;i<clipData.getItemCount();i++)
                    {
                        ClipData.Item itemAt = clipData.getItemAt(i);
                        //String path1 = FileUtils.getPath();//使用工具类对uri进行转化
                        try {
                            String path = URLDecoder.decode(itemAt.getUri().getPath(), "UTF-8");
                            path = path.split(":")[1];
                            String BookPath = "/storage/emulated/0/xxzz_app/Book";
                            String CoverPath = "/storage/emulated/0/xxzz_app/Cover";
                            File tmpPath = getCacheDir();
                            unzipFile(path, tmpPath.getAbsolutePath());
                            if(ChectFile(tmpPath)){
                                copyDir(tmpPath.getAbsolutePath()+"/main",CoverPath);
                                copyDir(tmpPath.getAbsolutePath()+"/text",BookPath);
                                copyFileUsingStream(tmpPath.getAbsolutePath()+"/jbk_config.json","/storage/emulated/0/xxzz_app/jbk_config.json");
                            }
                            deleteDFile(tmpPath);
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        // file = new File(itemAt.getUri().getPath());

                    }
                }

                    Uri uri = data.getData();
                if (uri != null) {
                    try {
                        String path = URLDecoder.decode(uri.getPath(), "UTF-8");
                        path = path.split(":")[1];
                        String BookPath = "/storage/emulated/0/xxzz_app/Book";
                        String CoverPath = "/storage/emulated/0/xxzz_app/Cover";
                        File tmpPath = getCacheDir();
                        unzipFile(path, tmpPath.getAbsolutePath());
                        if(ChectFile(tmpPath)){
                            copyDir(tmpPath.getAbsolutePath()+"/main",CoverPath);
                            copyDir(tmpPath.getAbsolutePath()+"/text",BookPath);
                            copyFileUsingStream(tmpPath.getAbsolutePath()+"/jbk_config.json","/storage/emulated/0/xxzz_app/jbk_config.json");
                        }
                        deleteDFile(tmpPath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
////                    if (file == null) {
////                        Toast.makeText(this, "文件地址未找到：", Toast.LENGTH_SHORT).show();
////                        return;
////                    }
                }
                return;
            }
        }
    }


}