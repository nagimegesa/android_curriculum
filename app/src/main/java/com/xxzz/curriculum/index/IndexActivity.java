package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xxzz.curriculum.Permission;
import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;
import com.xxzz.curriculum.read.ReadActivity;

import java.io.File;

public class IndexActivity extends AppCompatActivity {
    private Object waitObject = new Object();
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

        // Permission permission = new Permission();
        // permission.checkPermissions(this);
//        Intent intent = new Intent(IndexActivity.this, ReadActivity.class);
//        intent.putExtra("book_name", "aili_book");
//        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==Permission.RequestCode) {
            boolean res = createBaseDir();
            if(!res) {
                Utils.makeToast(getApplicationContext(), "初始化文件夹失败", Toast.LENGTH_LONG);
            }
            Utils.makeToast(getApplicationContext(), "初始化文件夹成功", Toast.LENGTH_LONG);
            Intent intent = new Intent(IndexActivity.this, ReadActivity.class);
            intent.putExtra("book_name", "aili_book");
            startActivity(intent);
        }
    }

    private boolean createBaseDir() {
        File appDataPath = getFilesDir();
        boolean res = true;
        if(!appDataPath.exists()) {
            res = appDataPath.mkdir();
        }
        File bookPath = new File(appDataPath, "Book");
        File coverPath = new File(appDataPath, "cover");
        if(!bookPath.exists())
            res &= bookPath.mkdir();
        if(!coverPath.exists())
            res &= coverPath.mkdir();
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