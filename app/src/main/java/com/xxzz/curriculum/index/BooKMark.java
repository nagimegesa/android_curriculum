package com.xxzz.curriculum.index;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BooKMark extends BookCollection{


    public BooKMark() {
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
                //Log.d("", "line=" + line);
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

    public void addBookMark(String title, int page) {
    }

    public void deleteBookMark(String title, int page) {
    }

    public void getAllBookMark() {
    }

}
