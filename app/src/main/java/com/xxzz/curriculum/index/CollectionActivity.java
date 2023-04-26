package com.xxzz.curriculum.index;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.xxzz.curriculum.R;

import java.util.List;

public class CollectionActivity extends AppCompatActivity {
    BookManager bookManager = new BookManager();
    private ListView listView;
    private BookCollectionAdapter adapter;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//
        listView = findViewById(R.id.setting_collection_listview);

        adapter = new BookCollectionAdapter(CollectionActivity.this, readCollectionDB());
        listView.setAdapter(adapter);
    }
    public List<BookCollection> readCollectionDB() {
        return bookManager.readBookCollection();
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