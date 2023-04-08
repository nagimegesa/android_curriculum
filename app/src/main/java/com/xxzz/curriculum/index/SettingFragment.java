package com.xxzz.curriculum.index;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.xxzz.curriculum.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;

public class SettingFragment extends Fragment {
    static private SettingFragment fragment;
    private float defaultFontSize = 24;
    private Spinner readFontSize = null;
    String[] arr = {"8","12","16","20","24"};
    private Context parent;
    public SettingFragment(Context parent) {
        this.parent = parent;
    }

    /**
     * 单例模式, 写的时候只能用这个得到 SettingFragment 的实例, 不要直接调用 new
     *
     * @return SettingFragment实例
     */
    public static SettingFragment getInstance(Context context) {
        if (fragment == null)
            fragment = new SettingFragment(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File dir = new File(parent.getFilesDir() + "/BookMark/");
        //文件名
        String fileName = "bookmarks.json";
        try {
            //文件存在则不需创建
            if (!dir.exists()) {
                //创建文件夹
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            if(!file.exists()) {
                OutputStream out = Files.newOutputStream(file.toPath());
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        readFontSize = readFontSize.findViewById(R.id.sp_setFontSize);

        readFontSize.setSelection(0); //设置默认选中项
        readFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    public void addBookMark(String title, int page) {
    }

    public void deleteBookMark(String title, int page) {
    }

    public void getAllBookMark() {
    }

    public String readBookMark() {
        try {
            InputStream is = this.getClass().getClassLoader().
                    getResourceAsStream("");
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                Log.d("", "line=" + line);
                stringBuilder.append(line);
            }
            reader.close();
            reader.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setReadFontSize(float fontSize) {

        if (fontSize > 10) {
//            setText(String.valueOf(fontSize - 1));
//            setReadFontSize(fontSize - 1);
//            saveSetting(setting);
        }
        if (fontSize < 60) {
//            setText(String.valueOf(fontSize + 1));
//            setReadWordSize(fontSize + 1);
//            saveSetting(setting);
        }
    }

}



