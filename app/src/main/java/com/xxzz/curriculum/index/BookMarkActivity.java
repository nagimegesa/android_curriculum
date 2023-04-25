package com.xxzz.curriculum.index;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.xxzz.curriculum.R;

import java.util.ArrayList;
import java.util.List;

public class BookMarkActivity extends AppCompatActivity {
    BookManager bookManager = new BookManager();
    private ListView listView;
    private bookMarkAdapter adapter;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.setting_bookmark_listview);
        dbHelper = new DBHelper(getApplicationContext());

        //bookManager.addBookMark(dbHelper.getWritableDatabase(), "你好", 1, "哈哈");
        adapter = new bookMarkAdapter(BookMarkActivity.this, readBookMarkDB());
        listView.setAdapter(adapter);
        //bookManager.findBookMark(dbHelper.getReadableDatabase(),0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BookMarkActivity.this, i + "号位置的条目被点击", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(BookMarkActivity.this);
                builder.setTitle( i + "号位置的删除按钮被点击，确认删除?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BooKMark booKMark = bookManager.findBookMark(dbHelper.getReadableDatabase(),which);
                        bookManager.deleteBookMark(dbHelper.getWritableDatabase(),booKMark.getBookName(),booKMark.getPage());
                        List<BooKMark> list = new ArrayList<BooKMark>();
                        list = readBookMarkDB();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });

    }

    public List<BooKMark> readBookMarkDB() {
        return bookManager.readBookMark(dbHelper.getReadableDatabase());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}