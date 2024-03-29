package com.xxzz.curriculum.join;

import static com.xxzz.curriculum.Utils.makeToast;
import static com.xxzz.curriculum.join.FileOperation.CheckFile;
import static com.xxzz.curriculum.join.FileOperation.IsJbk;
import static com.xxzz.curriculum.join.FileOperation.copyDir;
import static com.xxzz.curriculum.join.FileOperation.deleteDFile;
import static com.xxzz.curriculum.join.UnzipUtil.unzipFile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;
import com.xxzz.curriculum.index.BooKInfo;
import com.xxzz.curriculum.index.IndexActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinBookActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_OK = 520;
    private static final int SMART_SELECT_BOOK_MSG = 1;
    private final int REQUEST_CODE = 7325;
    private String BookPath;
    private final SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中
    private final List<File> mCheckedData = new ArrayList<>();//将选中数据放入里面
    File FileTep = Environment.getExternalStorageDirectory();
    ArrayList<BooKInfo> booKInfoList = new ArrayList<>();
    BooKInfo booKInfo;
    private ListViewAdaptor adapter;
    private ListView listView;
    private boolean isSelectedAll = true;//用来控制点击全选，全选和全不选相互切换
    private LinearLayout mLlEditBar;//控制下方那一行的显示与隐藏
    private ProgressDialog pd;

    //定义Handler对象
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != SMART_SELECT_BOOK_MSG) return;
            //只要执行到这里就关闭对话框
            ReFresh((List<File>) msg.obj);
            pd.dismiss();
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_join_book);

        BookPath = getFilesDir() + "/Book/";

        accessPermission();//获取 权限
    }

    @Override
    protected void onStart() {
        super.onStart();
        init_view();
        setOnListViewItemClickListener();
        setOnListViewItemLongClickListener();
        List<File> list
                = InitFileList(Environment.getExternalStorageDirectory().getPath());
        //setStateCheckedMap(false);
        adapter = new ListViewAdaptor(JoinBookActivity.this, list, stateCheckedMap);
        listView.setAdapter(adapter);
    }

    private void setOnListViewItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                File file = (File) adapter.getItem(i);
                FileTep = file;
                //makeToast(JoinBookActivity.this,file.getName(),100);
                if (file.isDirectory()) {
                    List<File> list = InitFileList(file.getPath());
                    ReFresh(list);
                    //adapter.notifyDataSetChanged();
                } else if (IsJbk(file)) {
                    try {
                        if (Is_Book(file)) {
                            if (mLlEditBar.getVisibility() == View.VISIBLE)
                                updateCheckBoxStatus(view, i);
                            else {
                                Unzip_Copy(file);
                                TransmitData();
                            }

                        } else makeToast(JoinBookActivity.this, "所选文件不符合格式", 100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    //updateCheckBoxStatus(view,i);
                    makeToast(JoinBookActivity.this, "所选文件不符合格式", 100);
                }
            }
        });
    }


    private void setOnListViewItemLongClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mLlEditBar.setVisibility(View.VISIBLE);//显示下方布局
                adapter.setShowCheckBox(true);//CheckBox的那个方框显示
                //adapter.isShowCheckBox = true;
                try {
                    if (Is_Book((File) adapter.getItem(position))) {
                        updateCheckBoxStatus(view, position);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                adapter.notifyDataSetChanged();
                //adapter.notifyDataSetChanged();
                return true;
            }

        });
    }

    private void updateCheckBoxStatus(View view, int position) {

        ListViewAdaptor.ViewHolder holder = (ListViewAdaptor.ViewHolder) view.getTag();
        holder.FileCheckBox.toggle();//反转CheckBox的选中状态
        listView.setItemChecked(position, holder.FileCheckBox.isChecked());//长按ListView时选中按的那一项
        stateCheckedMap.put(position, holder.FileCheckBox.isChecked());//存放CheckBox的选中状态
        if (holder.FileCheckBox.isChecked()) {
            mCheckedData.add((File) adapter.getItem(position));//CheckBox选中时，把这一项的数据加到选中数据列表
        } else {
            mCheckedData.remove((File) adapter.getItem(position));//CheckBox未选中时，把这一项的数据从选中数据列表移除
        }
        adapter.notifyDataSetChanged();

    }

    private void setStateCheckedMap(boolean isSelectedAll) {
        for (int i = 0; i < adapter.getCount(); i++) {
            try {
                if (Is_Book((File) adapter.getItem(i)))
                    stateCheckedMap.put(i, isSelectedAll);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            listView.setItemChecked(i, isSelectedAll);
        }
    }

    @Override
    public void onBackPressed() {
        if (mLlEditBar.getVisibility() == View.VISIBLE) {
            cancel();
        } else GoPrevious();
        //super.onBackPressed();
    }

    public List<File> InitFileList(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        assert files != null;
        return new ArrayList<>(Arrays.asList(files));
    }

    public void init_view() {
        ImageView imageButton = findViewById(R.id.join_image_button);
        Button button_join = findViewById(R.id.join_button);
        Button button_auto = findViewById(R.id.smart_button);
        listView = findViewById(R.id.join_list_view);
        mLlEditBar = findViewById(R.id.edit_bar);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.select_all).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
        button_join.setOnClickListener(this);
        button_auto.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void cancel() {
        setStateCheckedMap(false);//将CheckBox的所有选中状态变成未选中
        mLlEditBar.setVisibility(View.GONE);//隐藏下方布局
        adapter.setShowCheckBox(false);//让CheckBox那个方框隐藏
        adapter.notifyDataSetChanged();//更新ListView
    }

    private void selectAll() {
        mCheckedData.clear();//清空之前选中数据
        if (isSelectedAll) {
            setStateCheckedMap(true);//将CheckBox的所有选中状态变成选中
            isSelectedAll = false;
            // TODO : to fix a bug
//            mCheckedData.addAll(h);//把所有的数据添加到选中列表中
            for (int i = 0; i < adapter.getCount(); i++) {
                try {
                    if (Is_Book((File) adapter.getItem(i)))
                        mCheckedData.add((File) adapter.getItem(i));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            setStateCheckedMap(false);//将CheckBox的所有选中状态变成未选中
            isSelectedAll = true;
        }
        adapter.notifyDataSetChanged();
    }

    public void accessPermission() {
//        XXPermissions.with(this).permission(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).request(new OnPermissionCallback() {
//            @Override
//            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
//                if(!allGranted) {
//                    Toast.makeText(JoinBookActivity.this, "没有获得权限", Toast.LENGTH_LONG).show();
//                    JoinBookActivity.this.finish();
//                }
//            }
//        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R ||
                Environment.isExternalStorageManager()) {
        } else {
            Toast.makeText(this, "请授权访问所有文件", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (!Environment.isExternalStorageManager()) {
                Toast.makeText(this, "没有访问所有文件的权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void processThread() {
        //构建一个下载进度条
        pd = ProgressDialog.show(JoinBookActivity.this, "加载中", "正在加载…");
        new Thread(() -> {
            //在这里执行长耗时方法
            List<File> list;
            try {
                list = GoSmart(Environment.getExternalStorageDirectory().getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //执行完毕后给handler发送一个消息
            Message message = handler.obtainMessage();
            message.what = SMART_SELECT_BOOK_MSG;
            message.obj = list;
            handler.sendMessage(message);
        }).start();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_image_button:
                GoPrevious();
                break;
            case R.id.join_button:
                GoHome();
                break;
            case R.id.smart_button:
                processThread();
                break;
            case R.id.cancel:
                cancel();
                break;
            case R.id.select_all:
                selectAll();
                break;
            case R.id.confirm:
                Add_Book();
                break;
        }
    }

    public void Unzip_Copy(File file) {
        try {
            File tmpPath = getCacheDir();
            unzipFile(file.getPath(), tmpPath.getAbsolutePath());
            if (CheckFile(tmpPath)) {
                String[] res = getBookNameAndCover(tmpPath.getAbsolutePath() + '/' + "jbk_config.json");
                String savePath = BookPath + res[0];
                if(!isExistBook(res[0])) {
                    copyDir(tmpPath.getAbsolutePath(), BookPath + res[0]);
                    makeToast(JoinBookActivity.this, "加入成功", 100);
                    booKInfoList.add(new BooKInfo(res[0], savePath + "/main/" + res[1], "0"));
                } else {
                    makeToast(JoinBookActivity.this, "书籍已存在", 100);
                }
            } else
                makeToast(JoinBookActivity.this, "所选文件不符合格式", 100);
            deleteDFile(tmpPath);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isExistBook(String re) {
        File[] files = new File(BookPath).listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().equals(re))
                return true;
        }
        return false;
    }

    private String[] getBookNameAndCover(String savePath) throws IOException, JSONException {
        String buf = Utils.readAllFile(new File(savePath).toPath());

        JSONObject object = new JSONObject(buf);
        String[] res = new String[2];
        res[0] = object.getString("book_name");
        res[1] = object.getString("cover");
        return res;
    }

    public void TransmitData() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("book_info", booKInfoList);
        setResult(REQUEST_OK, intent);
        finish();
    }

    private void Add_Book() {
        if(mCheckedData.size() != 0){
            for (File f : mCheckedData)
                Unzip_Copy(f);
            TransmitData();
        }



        cancel();
    }

    public boolean Is_Book(File file) throws IOException {
        if(!IsJbk(file)){
            return false;
        }
        File tmpPath = getCacheDir();
        unzipFile(file.getPath(), tmpPath.getAbsolutePath());
        if (CheckFile(tmpPath)) {
            deleteDFile(tmpPath);
            return true;
        }
        deleteDFile(tmpPath);
        return false;
    }

    public void GoPrevious() {
        File root = Environment.getExternalStorageDirectory();
        if (root.toPath().toString().equals(FileTep.toPath().toString())) {
            Intent intent = new Intent(JoinBookActivity.this, IndexActivity.class);
            startActivity(intent);
            //finish();
        } else {
            File file = FileTep.getParentFile();
            assert file != null;
            List<File> list = InitFileList(file.getPath());
            adapter.setFileList(list);
            adapter.notifyDataSetChanged();
            FileTep = FileTep.getParentFile();
        }

    }

    public void GoHome() {
        List<File> list = InitFileList(Environment.getExternalStorageDirectory().getPath());
        ReFresh(list);
    }


    public void goSmartImpl(String path, List<File> list, int n) throws IOException {
        File file = new File(path);
        if (!file.exists())
            return;
        if (n == 3) {
            return;
        }
        n++;
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isDirectory())
                    goSmartImpl(f.getPath(), list, n);
                else {
                    if (IsJbk(f)) {
                        File tmpPath = getCacheDir();
                        unzipFile(f.getPath(), tmpPath.getAbsolutePath());
                        if (CheckFile(tmpPath))
                            list.add(f);
                        deleteDFile(tmpPath);
                    }
                }
            }
        }
    }

    public List<File> GoSmart(String path) throws IOException {
        List<File> fileList = new ArrayList<>();
        goSmartImpl(path, fileList, 1);
        return fileList;
    }

    public void ReFresh(List<File> list) {
        adapter.setFileList(list);
        adapter.setStateCheckedMap(stateCheckedMap);
        listView.setAdapter(adapter);
    }
}