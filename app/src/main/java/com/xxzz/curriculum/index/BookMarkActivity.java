package com.xxzz.curriculum.index;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.xxzz.curriculum.R;

import java.util.ArrayList;
import java.util.List;

public class BookMarkActivity extends AppCompatActivity {
    private ListView listView;
    private bookMarkAdapter adapter;
    BookManager bookManager;
    private List<BooKMark> list;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        listView = findViewById(R.id.setting_bookmark_listview);
        //bookManager.addBookMark(context,"你好哈哈",2,"这是一个案例");
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        bookManager.addBookMark(context,"你好",1,"哈哈");

        readBookMarkDB();
        adapter = new bookMarkAdapter(BookMarkActivity.this,list);
        listView.setAdapter(adapter);
    }
    public void readBookMarkDB(){
        list = new ArrayList<BooKMark>();
        list = bookManager.readBookMark(context);
        //list.add(new BooKMark("你好哈哈",2,"这是一个案例"));
    }

}