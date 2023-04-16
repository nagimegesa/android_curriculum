package com.xxzz.curriculum;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDelegate;

public class Config {
    static public Config config;
    static public float minFontSize = 8; //最小字号
    static public float defaultFontSize = 20; //默认字号
    static public float maxFontSize = 32;//最大字号
    static private float fontSizeArray[] = {8, 12, 16, 20, 24, 28, 32};
    private Context parent;
    private float readFontSize = defaultFontSize; //阅读字体大小
    private boolean musicStatus = false;//背景音乐状态
    private boolean nightStatus = false;//夜间模式

    public Config(Context parent) {
//        this.parent = parent;
//        this.readFontSize = 20;
//        this.musicStatus = false;
//        this.nightStatus = false;
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static Config getInstance(Context context) {
        if (config == null)
            config = new Config(context);
        return config;
    }

    public float getReadFontSize() {
        return readFontSize;
    }

    public void setReadFontSize(float fontSize) {
        SharedPreferences sharedPreferences = parent.getSharedPreferences("setting", parent.MODE_PRIVATE);
        readFontSize = sharedPreferences.getFloat("readFontSize", 0);
        if (fontSize > minFontSize) {
//            setReadFontSize(fontSize - 1);
//            saveSetting(setting);
        }
        if (fontSize < maxFontSize) {
//            setText(String.valueOf(fontSize + 1));
//            setReadWordSize(fontSize + 1);
//            saveSetting(setting);
        }
    }

    public boolean isMusicStatus() {
        return musicStatus;
    }

    public void setMusicStatus(boolean musicStatus) {
        this.musicStatus = musicStatus;
    }

    public boolean isNightStatus() {
        return nightStatus;
    }

    public void setNightStatus(boolean nightStatus) {
        this.nightStatus = nightStatus;
    }

    public void initReadConfig() {
        this.readFontSize = 20;
        this.musicStatus = false;
        this.nightStatus = false;
        //
    }

//    /**
//     * 保存配置文件到本地xml
//     */
//    public void setConfig() {
//        try {
//            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Settings", "setting.xml");
//            FileOutputStream fos = new FileOutputStream(file);
//            // 获得一个序列化工具
//            XmlSerializer serializer = Xml.newSerializer();
//            serializer.setOutput(fos, "utf-8");
//            // 设置文件头
//            serializer.startDocument("utf-8", true);
//
//            //serializer.attribute(null, "id", String.valueOf(0));
//            // TODO: Write something into setting config
//
//            serializer.endDocument();
//            fos.close();
//            //Toast.makeText
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 读取本地配置文件
//     */
//    public void getConfig() {
//        try {
//            File path = new File(Environment.getExternalStorageDirectory() + File.separator + "Settings", "Setting.xml");
//            FileInputStream fis = new FileInputStream(path);
//            XmlPullParser parser = Xml.newPullParser();// 获得pull解析器对象
//            parser.setInput(fis, "utf-8");// 指定解析的文件和编码格式
//            int eventType = parser.getEventType(); // 获得事件类型
//
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String tagName = parser.getName(); // 获得当前节点的名称
//                switch (eventType) {
//                    case XmlPullParser.START_TAG:
//                        // TODO: read setting Config
//                        break;
////                    case XmlPullParser.END_TAG:
////                        break;
//                    default:
//                        break;
//                }
//                eventType = parser.next(); // 获得下一个事件类型
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }

    private int getSpinnerPlace(Spinner readFontSize){
        SharedPreferences sharedPreferences = parent.getSharedPreferences("setting", parent.MODE_PRIVATE);
        float fontSize = sharedPreferences.getFloat("readFontSize", 0);
        int flag = 0;
        if (readFontSize.getSelectedItem().toString() != Float.toString(fontSize)) {
            for(int i=0;i< fontSizeArray.length;i++){
                if(fontSize == fontSizeArray[i])
                    flag = i;
            }
            //readFontSize.setSelection(flag);
        }
        return flag;
    }
    public void saveSettingConfig(Context context) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putFloat("readFontSize", this.getReadFontSize());
        editor.putBoolean("musicStatus", this.isMusicStatus());
        editor.putBoolean("nightStatus", this.isNightStatus());
        editor.commit();
    }

    public void readSettingConfig() {
        //  读取设置
        SharedPreferences sharedPreferences = parent.getSharedPreferences("setting", parent.MODE_PRIVATE);
        readFontSize = sharedPreferences.getFloat("readFontSize", 0);
        nightStatus = sharedPreferences.getBoolean("nightStatus", true);
        musicStatus = sharedPreferences.getBoolean("musicStatus", true);
        //Log
    }

    //切换夜间模式
    public void switchMode(Activity activity) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            //当前为日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //切换为夜间模式
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //切换为日间间模式
        }
        activity.recreate();
    }
}
