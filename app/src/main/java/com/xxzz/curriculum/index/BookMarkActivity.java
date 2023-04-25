package com.xxzz.curriculum.index;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.read.ReadActivity;

import java.util.ArrayList;
import java.util.List;

public class BookMarkActivity extends AppCompatActivity {
    BookManager bookManager = new BookManager();
    private ListView listView;
    private BookMarkAdapter adapter;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        listView = findViewById(R.id.setting_bookmark_listview);
        dbHelper = DBHelper.getInstance();
        adapter = new BookMarkAdapter(BookMarkActivity.this, readBookMarkDB());

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookMarkActivity.this);
                builder.setTitle("确认删除?");
                builder.setNegativeButton("取消", (d, w)-> d.dismiss());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BooKMark mark = adapter.getItem(position);
                        bookManager.deleteBookMark(mark.getBookName(), mark.getPage());
                        adapter = new BookMarkAdapter(BookMarkActivity.this, readBookMarkDB());
                        listView.setAdapter(adapter);
                    }
                });
                builder.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BookMarkActivity.this, ReadActivity.class);
                TextView name = view.findViewById(R.id.bookmark_name);
                TextView page = view.findViewById(R.id.bookmark_page);
                intent.putExtra("book_name", name.getText().toString());
                intent.putExtra("page", Integer.valueOf(page.getText().toString()));
                startActivity(intent);
            }
        });
    }

    public List<BooKMark> readBookMarkDB() {
        return bookManager.readBookMark();
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