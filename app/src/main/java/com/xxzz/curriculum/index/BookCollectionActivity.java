package com.xxzz.curriculum.index;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.read.ReadActivity;

import java.util.List;

;

public class BookCollectionActivity extends AppCompatActivity {
    BookManager bookManager = new BookManager();
    private ListView listView;
    private BookCollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        listView = findViewById(R.id.setting_collection_listview);
        List<BookCollection> list = readCollectionDB();
        adapter = new BookCollectionAdapter(BookCollectionActivity.this, list);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookCollectionActivity.this);
                builder.setTitle("确认删除?");
                builder.setNegativeButton("取消", (d, w)-> d.dismiss());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BookCollection collection = adapter.getItem(position);
                        bookManager.deleteBookCollection(collection.getBookName(), collection.getPage());
                        adapter = new BookCollectionAdapter(BookCollectionActivity.this, readCollectionDB());
                        listView.setAdapter(adapter);
                    }
                });
                builder.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookCollectionActivity.this, ReadActivity.class);
                TextView name = view.findViewById(R.id.bookmark_name);
                TextView page = view.findViewById(R.id.bookmark_page);
                intent.putExtra("book_name", name.getText().toString());
                intent.putExtra("page", Integer.valueOf(page.getText().toString()));
                startActivity(intent);
            }
        });
    }

    public List<BookCollection> readCollectionDB() {
        return bookManager.readBookCollection();
    }
}