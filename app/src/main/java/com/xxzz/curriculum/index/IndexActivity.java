package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;
import com.xxzz.curriculum.join.JoinBookActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> launcher;
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
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , (res)-> {
                    Intent data = res.getData();
                    if(data == null) return;
                    if(res.getResultCode() != JoinBookActivity.REQUEST_OK) return;
                    ArrayList <BooKInfo> info = data.getParcelableArrayListExtra("book_info");
                    if(info.isEmpty()) {

                    }

                });
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
            case R.id.dete_book:
                deleteBook();
                break;
            case R.id.sort_book:
                IndexFragment.getInstance().getAdapter().refreshData(IndexFragment.getInstance().getList());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteBook() {

    }

    private void startAddBook() {
        switchToAddBook();
        // TODO : do something for add book
    }

    private void switchToAddBook() {
        // TODO : switch to the Read Activity with result back;
        Intent intent = new Intent(this, JoinBookActivity.class);

        launcher.launch(intent);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.read_bar_search);
        List<BooKInfo> result = IndexFragment.getInstance().getList();
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<BooKInfo> searchresult = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    if (equalSearch(result.get(i).getName(), newText)) {
                        searchresult.add(new BooKInfo(result.get(i).getName(), result.get(i).getCoverPath(), result.get(i).getLastReadTime()));
                    }
                }
                IndexFragment.getInstance().getAdapter().searchData(searchresult);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean equalSearch(String s1, String s2) {
        StringBuilder str = new StringBuilder(s1);
        for (char ch : s2.toCharArray()) {
            int i = str.indexOf(String.valueOf(ch));
            if (i < 0) {
                return false;
            }
            str = str.deleteCharAt(i);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    private void change_fragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.index_button:
                fragment = IndexFragment.getInstance();
                break;
            case R.id.setting_button:
                fragment = SettingFragment.getInstance(this);
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