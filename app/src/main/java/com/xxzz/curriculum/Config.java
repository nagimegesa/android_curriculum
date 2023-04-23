package com.xxzz.curriculum;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDelegate;

public class Config {
    static private final float[] fontSizeArray = {8, 12, 16, 20, 24, 28, 32};
    static public Config config;
    static public float minFontSize = 8; //最小字号
    static public float defaultFontSize = 20; //默认字号
    static public float maxFontSize = 32;//最大字号
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private float readFontSize = defaultFontSize; //阅读字体大小
    private boolean musicStatus = false;//背景音乐状态
    private boolean nightStatus = false;//夜间模式

    public Config() {
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static Config getInstance() {
        if (config == null)
            config = new Config();
        return config;
    }

    public float getReadFontSize() {
        return readFontSize;
    }

    public void setReadFontSize(float fontSize, Context parent) {
        sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //readFontSize = sharedPreferences.getFloat("readFontSize", 0);
        if (fontSize >= minFontSize && fontSize <= maxFontSize) {
            readFontSize = fontSize;
            editor.putFloat("readFontSize", fontSize);
            editor.apply();
        }
    }

    public boolean isMusicStatus() {
        return musicStatus;
    }

    public void setMusicStatus(boolean musicStatus, Context parent) {
        sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("musicStatus", musicStatus);
        editor.apply();
        this.musicStatus = musicStatus;
    }

    public boolean isNightStatus() {
        return nightStatus;
    }

    public void setNightStatus(boolean nightStatus, Context parent) {
        sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("nightStatus", musicStatus);
        editor.apply();
        this.nightStatus = nightStatus;
    }

    public void initReadConfig() {
        this.readFontSize = 20;
        this.musicStatus = false;
        this.nightStatus = false;
    }

    private int getSpinnerPlace(Spinner readFontSize, Context parent) {
        sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        float fontSize = sharedPreferences.getFloat("readFontSize", 0);
        int flag = 0;
        if (readFontSize.getSelectedItem().toString() != Float.toString(fontSize)) {
            for (int i = 0; i < fontSizeArray.length; i++) {
                if (fontSize == fontSizeArray[i])
                    flag = i;
            }
            //readFontSize.setSelection(flag);
        }
        return flag;
    }

    public void saveSettingConfig(Context context) {
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putFloat("readFontSize", this.getReadFontSize());
        editor.putBoolean("musicStatus", this.isMusicStatus());
        editor.putBoolean("nightStatus", this.isNightStatus());
        editor.commit();
    }

    public void readSettingConfig(Context parent) {
        //  读取设置
        SharedPreferences sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        readFontSize = sharedPreferences.getFloat("readFontSize", 0);
        nightStatus = sharedPreferences.getBoolean("nightStatus", true);
        musicStatus = sharedPreferences.getBoolean("musicStatus", true);
        //Log
    }

    public void switchNightMode(Activity activity) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {//当前为日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //切换为夜间模式
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //切换为日间间模式
        }
        activity.recreate();
    }
}
