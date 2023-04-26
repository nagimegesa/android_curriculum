package com.xxzz.curriculum.read;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.xxzz.curriculum.Config;
import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;
import com.xxzz.curriculum.index.BookCollection;
import com.xxzz.curriculum.index.BookManager;
import com.xxzz.curriculum.index.DBHelper;
import com.xxzz.curriculum.index.IndexActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReadActivity extends AppCompatActivity {
    private BookReader reader;
    private boolean isBasicShowAble = false;
    private boolean isTopMenuShow = false;
    private boolean isLeftFragmentShow = false;

    private boolean isBottomSettingMenuShow = false;
    private boolean isNightMode = true;

    private DBHelper dbHelper;
    private Config config;
    // private final ArrayList<View> views = new ArrayList<>();

    public ReadActivity() {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        dbHelper = DBHelper.getInstance();
        initBook();
        initGUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reader.saveBookPage();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initBook() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("book_name");
        Book book = null;
        try {
            book = getBook(name);
        } catch (IOException | JSONException e) {
            Utils.makeToast(getApplicationContext(), "IO出错", Toast.LENGTH_LONG);
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assert book != null;
        reader = new BookReader(getFilesDir().getAbsolutePath(), book);
        if(intent.hasExtra("page")) {
            int page = intent.getIntExtra("page", 0);
            reader.setPageNow(page);
        }
        ViewPager2 pager = this.findViewById(R.id.read_viewpager);
        pager.setAdapter(new ReadPageAdapter(reader));
        SeekBar bar = findViewById(R.id.read_seek_bar);
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bar.setProgress(position);
            }

        });

        FrameLayout frameLayout = findViewById(R.id.read_center_frame);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBasicShowAble) {
                    Animator top = AnimatorInflater.loadAnimator(ReadActivity.this, R.animator.read_show_top_layout);
                    Animator bottom = AnimatorInflater.loadAnimator(ReadActivity.this, R.animator.read_show_bottom_menu);
                    top.setTarget(findViewById(R.id.read_top_layout));
                    bottom.setTarget(findViewById(R.id.read_bottom_menu));
                    top.start();
                    bottom.start();
                } else {
                    closeBasicMenu();
                }
                isBasicShowAble = !isBasicShowAble;
            }
        });
        pager.setCurrentItem(reader.getPageNow() - 1);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void initGUI() {
        config = Config.getInstance();
        config.readSettingConfig(this);
        SeekBar seekBar = findViewById(R.id.read_seek_bar);
        seekBar.setMax(reader.getBook().getPages() - 1);
        seekBar.setMin(0);
        ViewPager2 pager = findViewById(R.id.read_viewpager);
        seekBar.setOnSeekBarChangeListener(new SeekBarOnChange(new SeekBarOnChange.SeekBarOnChangeCallBack() {
            @Override
            public void onSeekBarChange(int process) {
                pager.setCurrentItem(process);
                reader.setPageNow(process + 1);
            }
        }));

        Button lastPageButton = findViewById(R.id.read_last_page);
        Button nextPageButton = findViewById(R.id.read_next_page);

        lastPageButton.setOnClickListener(v -> {
            int process = seekBar.getProgress();
            if (process > 0)
                seekBar.setProgress(process - 1);
        });

        nextPageButton.setOnClickListener(v -> {
            int process = seekBar.getProgress();
            if (process < seekBar.getMax())
                seekBar.setProgress(process + 1);
        });

        Button nightMode = findViewById(R.id.read_night_mode);
        Button setting = findViewById(R.id.read_setting);
        Button content = findViewById(R.id.read_content);
        isNightMode = config.isNightStatus();
        changeNightModeIcon(nightMode);
        nightMode.setOnClickListener((v) -> {
            changeNightLightMode((Button) v);
        });

        setting.setOnClickListener((v) -> {
            // TODO : set setting menu
            Animator animator = AnimatorInflater.loadAnimator(this, R.animator.read_show_bottom_setting_menu);
            animator.setTarget(findViewById(R.id.read_bottom_setting_menu));
            animator.start();
            isBottomSettingMenuShow = true;
        });

        content.setOnClickListener((v) -> {
            showLeftFragment();
            fillLeftFrameWithBookCollection(ReadActivity.this.findViewById(R.id.read_left_frame_listview));
        });

        Button back = findViewById(R.id.read_back_button);
        back.setOnClickListener((v) -> {
            ReadActivity.this.finish();
        });

        TextView view = findViewById(R.id.read_top_title);
        view.setText(reader.getBook().getName());

        Button topMenu = findViewById(R.id.read_top_menu_button);
        topMenu.setOnClickListener((v) -> {
            if (!isTopMenuShow) {
                Animator animator = AnimatorInflater.loadAnimator(ReadActivity.this, R.animator.read_show_top_menu);
                animator.setTarget(findViewById(R.id.read_top_menu));
                animator.start();
                isTopMenuShow = true;
            } else {
                closeTopMenu();
            }
        });

        Button addLoves = findViewById(R.id.read_add_love);
        addLoves.setOnClickListener((v) -> {
            BookManager manager = new BookManager();
            manager.addBookCollection(reader.getBook().getName(), reader.getPageNow());
            Utils.makeToast(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT);
        });

        Button addMark = findViewById(R.id.read_add_mark);
        addMark.setOnClickListener((v) -> {
            BookManager manager = new BookManager();
            AlertDialog.Builder builder = new AlertDialog.Builder(ReadActivity.this);
            builder.setTitle("输入理由:");
            EditText editText = new EditText(ReadActivity.this);
            builder.setView(editText);
            builder.setPositiveButton("确认", (d, w) -> {
                String text = String.valueOf(editText.getText());
                if (text.isEmpty()) d.dismiss();
                manager.addBookMark(reader.getBook().getName(), reader.getPageNow(),
                        String.valueOf(editText.getText()));
                Utils.makeToast(ReadActivity.this, "添加成功", Toast.LENGTH_SHORT);
            });

            builder.setNegativeButton("取消", null);
            builder.show();
        });

        SeekBar changeTextSize = findViewById(R.id.read_change_text_size);
        TextView fontSizeView = ReadActivity.this.findViewById(R.id.read_show_text_size);
        changeTextSize.setProgress((int) (config.getReadFontSize() / Config.changeFontSizeStep));
        fontSizeView.setText(config.getReadFontSize() + "px");
        changeTextSize.setMin(0);
        changeTextSize.setMax((int) ((int) ((Config.maxFontSize - Config.minFontSize)) / Config.changeFontSizeStep));
        changeTextSize.setOnSeekBarChangeListener(new SeekBarOnChange(new SeekBarOnChange.SeekBarOnChangeCallBack() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onSeekBarChange(int process) {
                // TODO : change Text Size
                float size = Config.minFontSize + process * Config.changeFontSizeStep;
                fontSizeView.setText(size + "px");
                config.setReadFontSize(size, getApplication());
                Objects.requireNonNull(pager.getAdapter()).notifyDataSetChanged();
            }
        }));

//        SeekBar changeLight = findViewById(R.id.read_change_light);
//        changeLight.setOnSeekBarChangeListener(new SeekBarOnChange(new SeekBarOnChange.SeekBarOnChangeCallBack() {
//            @Override
//            public void onSeekBarChange(int process) {
//                // TODO : change system light value
//            }
//        }));

        Button moreSetting = findViewById(R.id.read_more_setting);
        moreSetting.setOnClickListener((v) -> {
            Intent intent = new Intent(this, IndexActivity.class);
            intent.putExtra("which", IndexActivity.FragmentPage.SETTING_FRAGMENT.ordinal());
            startActivity(intent);
        });

        RadioGroup group = findViewById(R.id.read_bottom_radio);
        setDirectButton(pager, config.isVerticalRead());
        group.setOnCheckedChangeListener((g, id) -> {
            setDirectButton(pager, id == R.id.read_up_down);
            config.setVerticalRead(R.id.read_up_down == id, ReadActivity.this.getApplication());
        });

        Button collection = findViewById(R.id.read_book_collection);
        Button mark = findViewById(R.id.read_book_mark);
        ListView leftFrameListView = findViewById(R.id.read_left_frame_listview);

        leftFrameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeBasicMenu();
                BookCollection c = (BookCollection) leftFrameListView.getAdapter().getItem(position);
                seekBar.setProgress(c.getPage() - 1);
            }
        });
        collection.setOnClickListener((v) -> {
            fillLeftFrameWithBookCollection(leftFrameListView);
        });

        mark.setOnClickListener((v) -> {
            BookManager manager = new BookManager();
            List<BookCollection> collectionList =
                    manager.readBookMark(reader.getBook().getName());
            LeftFrameListViewAdaptor adaptor = new LeftFrameListViewAdaptor(getLayoutInflater(), collectionList);
            leftFrameListView.setAdapter(adaptor);
        });
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDirectButton(ViewPager2 pager, boolean forward) {
        pager.setOrientation(forward ? ViewPager2.ORIENTATION_VERTICAL : ViewPager2.ORIENTATION_HORIZONTAL);
        RadioButton up_button = findViewById(R.id.read_up_down);
        RadioButton left_button = findViewById(R.id.read_left_right);
        Drawable up = null, down = null;
        if (forward) {
            up = getDrawable(R.drawable.read_up_forward_down);
            down = getDrawable(R.drawable.read_left_forward_up);
        } else {
            up = getDrawable(R.drawable.read_up_forward_up);
            down = getDrawable(R.drawable.read_left_forward_down);
        }

        assert up != null && down != null;
        up.setBounds(0, 0, up.getIntrinsicWidth(), up.getIntrinsicHeight());
        up_button.setCompoundDrawables(null, up, null, null);

        down.setBounds(0, 0, down.getIntrinsicWidth(), down.getIntrinsicHeight());
        left_button.setCompoundDrawables(null, down, null, null);
    }

    private void fillLeftFrameWithBookCollection(ListView listView) {
        BookManager manager = new BookManager();
        List<BookCollection> collectionList =
                manager.readBookCollection(reader.getBook().getName());
        LeftFrameListViewAdaptor adaptor = new LeftFrameListViewAdaptor(getLayoutInflater(), collectionList);
        listView.setAdapter(adaptor);
    }

    private void showLeftFragment() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.read_show_left_fram_animation);
        animator.setTarget(findViewById(R.id.read_left_frame));
        animator.start();
        isLeftFragmentShow = true;
    }

    private void closeBasicMenu() {
        Animator top = AnimatorInflater.loadAnimator(ReadActivity.this, R.animator.read_close_top_layout);
        Animator bottom = AnimatorInflater.loadAnimator(ReadActivity.this, R.animator.read_close_bottom_menu);
        top.setTarget(findViewById(R.id.read_top_layout));
        bottom.setTarget(findViewById(R.id.read_bottom_menu));
        if (isLeftFragmentShow) closeLeftFragment();
        if (isTopMenuShow) closeTopMenu();
        if (isBottomSettingMenuShow) closeBottomSettingMenu();
        top.start();
        bottom.start();
    }

    private void closeBottomSettingMenu() {
        Animator animator = AnimatorInflater.loadAnimator(ReadActivity.this, R.animator.read_close_bottom_setting_menu);
        animator.setTarget(findViewById(R.id.read_bottom_setting_menu));
        animator.start();
        isBottomSettingMenuShow = false;
    }

    private void closeTopMenu() {
        Animator animator = AnimatorInflater.loadAnimator(ReadActivity.this, R.animator.read_close_top_menu);
        animator.setTarget(findViewById(R.id.read_top_menu));
        animator.start();
        isTopMenuShow = false;
    }

    private void closeLeftFragment() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.read_close_left_fram_animation);
        animator.setTarget(findViewById(R.id.read_left_frame));
        animator.start();
        isLeftFragmentShow = false;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeNightLightMode(Button button) {
        isNightMode = !isNightMode;
        config.switchNightMode(this, isNightMode);
        changeNightModeIcon(button);
    }

    private void changeNightModeIcon(Button button) {
        int drawableId = isNightMode ? R.drawable.read_menu_night : R.drawable.read_menu_light;
        String text = isNightMode ? "夜间模式" : "白天模式";
        button.setText(text);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable drawable = getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        button.setCompoundDrawables(null, drawable, null, null);
    }

    private Book getBook(String name) throws IOException, JSONException, InterruptedException {
        File baseFile = getFilesDir();
        Path p = baseFile.toPath().resolve("Book").resolve(name);
        File bookFile = p.toFile();
        File configFile = p.resolve("jbk_config.json").toFile();
        if (!bookFile.exists()) {
            Utils.makeToast(getApplicationContext(), "图书不存在", Toast.LENGTH_LONG);
            return null;
        }

        String config = Utils.readAllFile(configFile.toPath());

        JSONObject context = new JSONObject(config);
        Book readBook = new Book();

        readBook.setName(context.getString("book_name"));
        readBook.setPages(context.getInt("pages_counts"));
        readBook.setReadPage(context.getInt("last_read_page"));
        JSONArray array = context.getJSONArray("pages");
        HashMap<Integer, String> pages2img = new HashMap<>();
        HashMap<Integer, String> pages2text = new HashMap<>();
        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            int page = object.getInt("pages");
            String img = object.getString("image");
            String text = object.getString("text");
            pages2img.put(page, img);
            pages2text.put(page, text);
        }
        readBook.setImgPath(pages2img);
        readBook.setTextPath(pages2text);
        return readBook;
    }
}