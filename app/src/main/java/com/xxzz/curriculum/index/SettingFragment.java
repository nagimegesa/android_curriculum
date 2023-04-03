package com.xxzz.curriculum.index;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.xxzz.curriculum.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;

interface fontSizeInter {

    public void setBookFontSize(float fontSize);

}

interface BookMarkInter {

    public void addBookMark(String title, int page);

    public void deleteBookMark(String title, int page);

    public void getAllBookMark();
}

public class SettingFragment extends Fragment {
    static private SettingFragment fragment;

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
        String fileName = "bookmark.json";
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    public void addBookMark(String title, int page) {

    }

}

class FontConfig implements fontSizeInter {
    private float minFontSize = 4;
    private float defaultFontSize = 24;
    private float fontSize = 20;

    public void setBookFontSize(float fontSize) {
        if (fontSize < minFontSize)
            fontSize = minFontSize;
        this.fontSize = fontSize;
    }
}

class BookMark implements BookMarkInter {
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
}

class MusicServer {
    private static MediaPlayer mp = null;

    public static void play(Context context, int resource) {
        stop(context);
        mp = MediaPlayer.create(context, resource);
        mp.setLooping(true);
        mp.start();
    }

    public static void stop(Context context) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}

class EyeCareMode {

}

class NightMode {

}


