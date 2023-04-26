package com.xxzz.curriculum;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDelegate;

public class Config {
    static final public float minFontSize = 8; //最小字号
    static final public float defaultFontSize = 20; //默认字号
    static final public float changeFontSizeStep = 4;
    static final public float maxFontSize = 32;//最大字号
    static private final float[] fontSizeArray = {8, 12, 16, 20, 24, 28, 32};
    static public Config config;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private float readFontSize = defaultFontSize; //阅读字体大小
    private boolean musicStatus = false;//背景音乐状态
    private boolean nightStatus = false;//夜间模式

    private boolean verticalRead = false;

    public Config() {
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static Config getInstance() {
        if (config == null) config = new Config();
        return config;
    }

    //设置手动调节亮度
    public static void startHandBrightness(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
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
            editor.commit();
        }
    }

    public boolean isMusicStatus() {
        return musicStatus;
    }

    public void setMusicStatus(boolean musicStatus, Context parent) {
        sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("musicStatus", musicStatus);
        editor.commit();
        this.musicStatus = musicStatus;
    }

    public boolean isNightStatus() {
        return nightStatus;
    }

    public boolean isVerticalRead() {
        return verticalRead;
    }

    public void setVerticalRead(boolean w, Context parent) {
        sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("verticalRead", w);
        editor.commit();
        this.verticalRead = w;
    }

    private void setNightStatus(boolean nightStatus, Context parent) {
        sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("nightStatus", nightStatus);
        editor.commit();
        this.nightStatus = nightStatus;
    }

    public int getSpinnerPlace(Spinner readFontSize, Context parent) {
        sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        float fontSize = sharedPreferences.getFloat("readFontSize", 0);
        int flag = 0;
        if (!readFontSize.getSelectedItem().toString().equals(Float.toString(fontSize))) {
            for (int i = 0; i < fontSizeArray.length; i++) {
                if (fontSize == fontSizeArray[i]) flag = i;
            }
        }
        return flag;
    }

    public void saveSettingConfig(Context context) {
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putFloat("readFontSize", this.getReadFontSize());
        editor.putBoolean("musicStatus", this.isMusicStatus());
        editor.putBoolean("nightStatus", this.isNightStatus());
        editor.putBoolean("verticalRead", this.isVerticalRead());
        editor.commit();
    }

    public void readSettingConfig(Context parent) {
        //  读取设置
        SharedPreferences sharedPreferences = parent.getSharedPreferences("setting", Context.MODE_PRIVATE);
        this.readFontSize = sharedPreferences.getFloat("readFontSize", 0);
        this.nightStatus = sharedPreferences.getBoolean("nightStatus", false);
        this.musicStatus = sharedPreferences.getBoolean("musicStatus", true);
        this.verticalRead = sharedPreferences.getBoolean("verticalRead", false);
    }

    public void switchNightMode(Activity activity, boolean which) {
        if (which) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //切换为夜间模式
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //切换为日间间模式
        }
        setNightStatus(which, activity.getApplicationContext());
    }

    //设置自动亮度调节
    public void startAutoBrightness(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取当前屏幕的亮度
    public float getAppScreenBrightness(Context context) {
        float nowBrightnessValue = 0;
        ContentResolver resolver = context.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

//    //获取系统默认屏幕亮度值,屏幕亮度值范围（0-255）
//    public int getSysScreenBrightness(Context context) {
//        ContentResolver contentResolver = context.getContentResolver();
//        int defVal = 125;
//        return Settings.System.getInt(contentResolver,
//                Settings.System.SCREEN_BRIGHTNESS, defVal);
//    }

    //    public static void saveBrightness(Context context, int brightness) {
//        ContentResolver resolver = context.getContentResolver();
//        //  需要权限android.permission.WRITE_SETTINGS
//        startHandBrightness(context);
//        //保存到系统中
//        Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
//        android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
//        resolver.notifyChange(uri, null);
//    }
    //设置app亮度
    public void setAppScreenBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        window.setAttributes(lp);
    }

}
