package com.xxzz.curriculum.index;

import android.content.Context;
public class Config{

    static private Config config;
    private Context parent;
    private float readFontSize; //阅读字体大小
    private boolean musicStatus;//背景音乐状态
    private boolean NightStatus;//夜间模式
    public Config(Context parent) {
        this.parent = parent;
        this.readFontSize = 20;
        this.musicStatus = false;
        this.NightStatus = false;
    }

    /**
     *  单例模式
     * @param context
     * @return
     */
    public static Config getInstance(Context context) {
        if (config == null)
            config = new Config(context);
        return config;
    }

    public void setReadFontSize(int readFontSize) {
        this.readFontSize = readFontSize;
    }

    public float getReadFontSize() {
        return readFontSize;
    }

    public void setMusicStatus(boolean musicStatus) {
        this.musicStatus = musicStatus;
    }

    public boolean isMusicStatus() {
        return musicStatus;
    }

    public void setNightStatus(boolean nightStatus) {
        NightStatus = nightStatus;
    }

    public boolean isNightStatus() {
        return NightStatus;
    }

    public void initReadConfig(){
        this.readFontSize = 20;
        this.musicStatus = false;
        this.NightStatus = false;
        //
    }


}
