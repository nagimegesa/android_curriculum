package com.xxzz.curriculum.index;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.xxzz.curriculum.R;

import java.util.List;

;

public class CollectionActivity extends AppCompatActivity {
    BookManager bookManager = new BookManager();
    private ListView listView;
    private BookCollectionAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        listView = findViewById(R.id.setting_collection_listview);
        dbHelper = new DBHelper(getApplicationContext());
        List<BookCollection> list = readCollectionDB();
        adapter = new BookCollectionAdapter(CollectionActivity.this, list);
        listView.setAdapter(adapter);
    }

    public List<BookCollection> readCollectionDB() {
        return bookManager.readBookCollection(dbHelper.getReadableDatabase());
    }
}