package com.xxzz.curriculum.read;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.xxzz.curriculum.Permission;
import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class ReadActivity extends AppCompatActivity {
    private final Object waitObject = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent intent = getIntent();
        String name = intent.getStringExtra("book_name");
        Book book = null;
        try {
            book = getBook(name);
        } catch (IOException | JSONException e) {
            Utils.makeToast(getApplicationContext(),"IO出错", Toast.LENGTH_LONG);
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // showBook(book);
    }
    private void showBook(Book book) {
        int readPage = book.getReadPage();
        String img = book.getIndexImage(readPage);
    }

    private Book getBook(String name) throws IOException, JSONException, InterruptedException {
        File baseFile = getFilesDir();
        Path p = baseFile.toPath().resolve("Book").resolve(name);
        File bookFile = p.toFile();
        File configFile = p.resolve("jbk_config.json").toFile();
        if(!bookFile.exists()) {
            Utils.makeToast(getApplicationContext(), "图书不存在", Toast.LENGTH_LONG);
            return null;
        }

        // Permission permission = new Permission();
        // permission.checkPermissions(this);

        String config = Utils.readAllFile(configFile.toPath());

        JSONObject context = new JSONObject(config);
        Book readBook = new Book();

        readBook.setName(context.getString("book_name"));
        readBook.setPages(context.getInt("pages_cnt"));
        JSONArray array = context.getJSONArray("pages");
        HashMap<Integer, String> pages2img = new HashMap<>();
        HashMap<Integer, String> pages2text = new HashMap<>();
        for(int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            int page = object.getInt("pages");
            String img = object.getString("image");
            String text = object.getString("text");
            pages2img.put(page, img);
            pages2text.put(page, text);
        }

        readBook.setImgPath(pages2img);
        readBook.setTextPath(pages2text);

        return readBook;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Permission.RequestCode) {
            Utils.makeToast(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT);
        }
    }
}