package com.xxzz.curriculum.read;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;
import com.xxzz.curriculum.index.IndexActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class ReadActivity extends AppCompatActivity {
    private BookReader reader;
    private boolean isBasicShowAble = false;
    private boolean isTopMenuShow = false;
    private boolean isLeftFragmentShow = false;

    private boolean isBottomSettingMenuShow = false;
    private boolean isNightMode = true;

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
        initBook();
        initGUI();
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initGUI() {
        SeekBar seekBar = findViewById(R.id.read_seek_bar);
        seekBar.setMax(reader.getBook().getPages() - 1);
        seekBar.setMin(0);
        ViewPager2 pager = findViewById(R.id.read_viewpager);
        seekBar.setOnSeekBarChangeListener(new SeekBarOnChange(new SeekBarOnChange.SeekBarOnChangeCallBack() {
            @Override
            public void onSeekBarChange(int process) {
                pager.setCurrentItem(process);
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
            Animator animator = AnimatorInflater.loadAnimator(this, R.animator.read_show_left_fram_animation);
            animator.setTarget(findViewById(R.id.read_left_frame));
            animator.start();
            isLeftFragmentShow = true;
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
            // TODO : add lovers
        });
        Button addMark = findViewById(R.id.read_add_mark);
        addMark.setOnClickListener((v) -> {
            //TODO : add mark
        });

        SeekBar changeTextSize = findViewById(R.id.read_change_text_size);
        changeTextSize.setOnSeekBarChangeListener(new SeekBarOnChange(new SeekBarOnChange.SeekBarOnChangeCallBack() {
            @Override
            public void onSeekBarChange(int process) {
                // TODO : change Text Size
            }
        }));

        SeekBar changeLight = findViewById(R.id.read_change_light);
        changeLight.setOnSeekBarChangeListener(new SeekBarOnChange(new SeekBarOnChange.SeekBarOnChangeCallBack() {
            @Override
            public void onSeekBarChange(int process) {
                // TODO : change system light value
            }
        }));

        Button moreSetting = findViewById(R.id.read_more_setting);
        moreSetting.setOnClickListener((v) -> {
            Intent intent = new Intent(this, IndexActivity.class);
            intent.putExtra("which", IndexActivity.FragmentPage.SETTING_FRAGMENT.ordinal());
            startActivity(intent);
        });

        RadioGroup group = findViewById(R.id.read_bottom_radio);

        group.setOnCheckedChangeListener((g, id) -> {
            int forward = id == R.id.read_up_down
                    ? ViewPager2.ORIENTATION_VERTICAL : ViewPager2.ORIENTATION_HORIZONTAL;
            pager.setOrientation(forward);
            RadioButton up_button = findViewById(R.id.read_up_down);
            RadioButton left_button = findViewById(R.id.read_left_right);
            Drawable up = null, down = null;
            if (id == R.id.read_up_down) {
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
        });
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
        int drawableId = isNightMode ? R.drawable.read_menu_night : R.drawable.read_menu_light;
        String text = isNightMode ? "夜间模式" : "白天模式";
        button.setText(text);
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