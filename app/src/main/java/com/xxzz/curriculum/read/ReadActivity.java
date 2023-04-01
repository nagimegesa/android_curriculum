package com.xxzz.curriculum.read;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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
        ViewPager pager = this.findViewById(R.id.viewpager);
        pager.setAdapter(new ReadPageAdapter(views));
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener());
        pager.setCurrentItem(reader.getPageNow() - 1);

        View v = findViewById(R.id.read_bottom_setting);
        v.setVisibility(View.GONE);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() != MotionEvent.ACTION_DOWN)
            return super.dispatchTouchEvent(ev);
        View v = findViewById(R.id.read_bottom_setting);
        int stat = isShowSetting ? View.GONE : View.VISIBLE;
        v.setVisibility(stat);
        isShowSetting = !isShowSetting;
        return true;
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