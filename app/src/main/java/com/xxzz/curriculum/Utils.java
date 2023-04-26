package com.xxzz.curriculum;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

import com.xxzz.curriculum.join.FileOperation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    public static void makeToast(Context context, String message, int time) {
        Toast.makeText(context, message, time).show();
    }

    static public String readAllFile(Path path) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(path.toAbsolutePath()));
        InputStreamReader inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputReader);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        inputReader.close();
        inputStream.close();
        return builder.toString();
    }

    public static void writeFile(Path path, String buf) throws IOException {
        BufferedOutputStream inputStream = new BufferedOutputStream(Files.newOutputStream(path.toAbsolutePath()));
        OutputStreamWriter inputReader = new OutputStreamWriter(inputStream, StandardCharsets.UTF_8);
        BufferedWriter reader = new BufferedWriter(inputReader);
//        reader.write();
        reader.write(buf);
        reader.flush();
        reader.close();
        inputReader.close();
        inputStream.close();
    }

    public static void removeFiles(Path path) {
        FileOperation.deleteDFile(path.toFile());
        path.toFile().delete();
    }

    /**
     * 保存配置文件到本地xml
     */
    public void setConfig() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Settings", "setting.xml");
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            //serializer.attribute(null, "id", String.valueOf(0));
            // TODO: Write something into setting config
            serializer.endDocument();
            fos.close();
            //Toast.makeText
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取本地配置文件
     */
    public void getConfig() {
        try {
            File path = new File(Environment.getExternalStorageDirectory() + File.separator + "Settings", "Setting.xml");
            FileInputStream fis = new FileInputStream(path);
            XmlPullParser parser = Xml.newPullParser();// 获得pull解析器对象
            parser.setInput(fis, "utf-8");// 指定解析的文件和编码格式
            int eventType = parser.getEventType(); // 获得事件类型
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        // TODO: read setting Config
                        break;
//                    case XmlPullParser.END_TAG:
//                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
