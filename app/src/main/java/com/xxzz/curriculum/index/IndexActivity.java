package com.xxzz.curriculum.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xxzz.curriculum.R;

import java.io.File;
import java.util.EventListener;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RadioGroup group = (RadioGroup) findViewById(R.id.bottom_radio);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                change_fragment(group.getCheckedRadioButtonId());
            }
        });

        change_fragment(R.id.index_button);

        boolean res = createBaseDir();
        if(res == false) {
            Context context = null;
            Log.w("xxzz_app", "create init dir failed");
            Toast.makeText(context, "初始化文件夹失败", Toast.LENGTH_LONG).show();
        }

    }

    private boolean createBaseDir() {
        File basePath = Environment.getExternalStorageDirectory();
        File appDataPath = new File(basePath, "xxzz_app");
        boolean res = true;
        if(!appDataPath.exists()) {
            res = appDataPath.mkdir();
            File bookPath = new File(appDataPath, "Book");
            File coverPath = new File(appDataPath, "cover");
            res &= bookPath.mkdir();
            res &= coverPath.mkdir();
        }
        return res;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_book:
                startAddBook();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startAddBook() {
        String s = switchToAddBook();
        // TODO : do something for add book
    }

    private String switchToAddBook() {
        // TODO : switch to the Read Activity with result back;
        return null;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.index_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    private void change_fragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.index_button:
                fragment = IndexFragment.getInstance();
                break;
            case R.id.setting_button:
                fragment = SettingFragment.getInstance();
                break;
            default:
                break;
        }

        if(fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_container, fragment);
            transaction.commit();
        }
    }
}