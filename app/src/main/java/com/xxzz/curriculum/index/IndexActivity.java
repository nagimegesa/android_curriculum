package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;
import com.xxzz.curriculum.join.JoinBookActivity;
import com.xxzz.curriculum.read.ReadActivity;

import java.io.File;
import java.util.List;

public class IndexActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RadioGroup group = findViewById(R.id.bottom_radio);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                change_fragment(group.getCheckedRadioButtonId());
            }
        });

        change_fragment(R.id.index_button);
        boolean canWrite =
                XXPermissions.isGranted(getApplicationContext(), Permission.WRITE_EXTERNAL_STORAGE);
        if (canWrite) {
            Utils.makeToast(getApplicationContext(), "创建文件夹", Toast.LENGTH_SHORT);
            createBaseDir();
        } else {
            XXPermissions.with(this)
                    .permission(Permission.WRITE_EXTERNAL_STORAGE)
                    // .permission(Permission.READ_EXTERNAL_STORAGE)
                    .permission(Permission.READ_MEDIA_AUDIO)
                    .permission(Permission.READ_MEDIA_IMAGES)
                    .permission(Permission.READ_MEDIA_VIDEO)
                    // .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                            if (permissions.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
                                createBaseDir();
                                Utils.makeToast(getApplicationContext(), "获得权限", Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }
//        Intent intent = new Intent(IndexActivity.this, ReadActivity.class);
//        intent.putExtra("book_name", "aili_book");
//        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int which = intent.getIntExtra("which", FragmentPage.INDEX_FRAGMENT.ordinal());
        if (which == FragmentPage.INDEX_FRAGMENT.ordinal()) {
            change_fragment(R.id.index_button);
        } else if (which == FragmentPage.SETTING_FRAGMENT.ordinal()) {
            change_fragment(R.id.setting_button);
        }
    }

    private boolean createBaseDir() {
        File appDataPath = getFilesDir();
        boolean res = true;
        if (!appDataPath.exists()) {
            res = appDataPath.mkdir();
        }
        File bookPath = new File(appDataPath, "Book");
        File coverPath = new File(appDataPath, "cover");
        if (!bookPath.exists())
            res &= bookPath.mkdir();
        if (!coverPath.exists())
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
        Intent intent = new Intent(this, JoinBookActivity.class);
        startActivity(intent);
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

        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_container, fragment);
            transaction.commit();
        }
    }

    public enum FragmentPage {
        INDEX_FRAGMENT,
        SETTING_FRAGMENT,
    }
}