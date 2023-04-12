package com.xxzz.curriculum.index;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BooKMark extends BookCollection {
    private int count = 0;
    private String bookName;
    private int page;
    private String text;

    public BooKMark() {
    }

    public JSONObject readInformation(String bookName, int page, String text) {
        JSONObject object = new JSONObject();
        try {
            object.put("book_name", bookName);
            object.put("page", page);
            object.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
    public void writeBookMark(JSONObject object,int count){
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(object);
        try{
            obj.put("count",count);
            obj.put("info",array);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public String readBookMark(File file) {
        try {
            InputStream is = this.getClass().getClassLoader().
                    getResourceAsStream("bookmarks.json");  //读取文件
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(streamReader);
            String line="";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject object=new JSONObject(stringBuilder.toString());
            // TODO: something
            reader.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addBookMark(String title, int page) {
    }

    public void deleteBookMark(String title, int page) {
    }

    public void getAllBookMark() {
    }

}
