package com.xxzz.curriculum.index;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.xxzz.curriculum.Config;
import com.xxzz.curriculum.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class SettingFragment extends Fragment {
    static private SettingFragment fragment;
    private View view;
    int flag = 3; // spinner -- 默认字号为20
    private float fontSize;
    private Spinner readFontSize;
    private SwitchCompat nightStatus, musicStatus;
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
        //
        Config.getInstance(parent).saveSettingConfig(parent);
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
            if (!file.exists()) {
                OutputStream out = Files.newOutputStream(file.toPath());
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // TODO: write bookCollection
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        readFontSize = view.findViewById(R.id.sp_setFontSize);
        nightStatus = (SwitchCompat) view.findViewById(R.id.sc_night_switch);
        musicStatus = view.findViewById(R.id.sc_music_switch);
        //
        //Config.getInstance(parent).readSettingConfig();
        //getSpinnerPlace();
        readFontSize.setSelection(flag); //设置默认选中项
        readFontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Config.getInstance(parent).setReadFontSize();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("fontSize", "NothingSelected");
            }
        });
        //
        nightStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Config.getInstance(parent).switchMode(getActivityFromView());
            }
        });


        return view;
    }

    public Activity getActivityFromView() {
        if (null != view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }
}



