package com.xxzz.curriculum.read;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.xxzz.curriculum.R;
import com.xxzz.curriculum.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadActivity extends AppCompatActivity {
    private BookReader reader;
    private boolean isShowSetting = false;
    private final ArrayList<View> views = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initBook();
        initGUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        for(int i = 1; i <= reader.getBook().getPages(); ++i)  {
            ImageView img = views.get(i - 1).findViewById(R.id.read_main_img);
            TextView text = views.get(i - 1).findViewById(R.id.read_main_text);
            Pages pages = null;
            try {
                pages = reader.getIndexPage(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(pages != null) {
                img.setImageBitmap(pages.getMap());
                text.setText(pages.getText());
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initBook() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("book_name");
        Book book = null;
        try {
            book = getBook(name);
        } catch (IOException | JSONException e) {
            Utils.makeToast(getApplicationContext(),"IO出错", Toast.LENGTH_LONG);
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assert book != null;
        for(int i = 0;i < book.getPages(); ++i)
            views.add(getLayoutInflater().inflate(R.layout.activity_read_content, null));
        reader = new BookReader(getFilesDir().getAbsolutePath(), book);
        ViewPager pager = this.findViewById(R.id.read_viewpager);
        pager.setAdapter(new ReadPageAdapter(views));
        SeekBar bar = findViewById(R.id.read_seek_bar);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                bar.setProgress(position);
            }
        });
        pager.setOnTouchListener(new PagerOnTouch(new PagerOnTouch.TouchCallback() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(boolean isClick) {
                View v = findViewById(R.id.read_bottom_setting);
                int stat = isShowSetting ? View.GONE : View.VISIBLE;
                v.setVisibility(stat);
                isShowSetting = !isShowSetting;
            }
        }));
        pager.setCurrentItem(reader.getPageNow() - 1);
        View v = findViewById(R.id.read_bottom_setting);
        v.setVisibility(View.GONE);
    }

    private void initGUI() {
        SeekBar seekBar = findViewById(R.id.read_seek_bar);
        seekBar.setMax(reader.getBook().getPages() - 1);
        seekBar.setMin(0);
        ViewPager pager = findViewById(R.id.read_viewpager);
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
            if(process > 0)
                seekBar.setProgress(process - 1);
        });

        nextPageButton.setOnClickListener(v -> {
            int process = seekBar.getProgress();
            if(process < seekBar.getMax())
                seekBar.setProgress(process + 1);
        });

        RadioGroup group = findViewById(R.id.read_radio_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.read_content:
                        // TODO : set CONTENT GUI
                        break;
                    case R.id.read_night_mode:
                        // TODO : get ConfigClass and change NightMode
                        break;
                    case R.id.read_setting:
                        // TODO : get setting GUI
                        break;
                    default:break;
                }
            }
        });
    }

    private Book getBook(String name) throws IOException, JSONException, InterruptedException {
        File baseFile = getFilesDir();
        Path p = baseFile.toPath().resolve("Book").resolve(name);
        File bookFile = p.toFile();
        File configFile = p.resolve("jbk_config.json").toFile();
        if(!bookFile.exists()) {
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
        for(int i = 0; i < array.length(); ++i) {
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