package com.xxzz.curriculum.read;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xxzz.curriculum.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BookReader {
    private final String cachePath;
    private Book book;
    private int pageNow;

    public BookReader(String cachePath) {
        this.cachePath = cachePath + "/Book/";
    }

    public BookReader(String cachePath, Book book) {
        this(cachePath);
        this.book = book;
        this.pageNow = book.getReadPage();
    }

    public Pages getLastReadPages() throws IOException {
        int lastPage = book.getReadPage();
        return getIndexPage(lastPage);
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Pages getIndexPage(int index) throws IOException {
        if (index <= 0 || index > book.getPages()) return null;
        String imgPath = book.getIndexImage(index);
        String textPath = book.getIndexText(index);
        imgPath = cachePath + book.getName() + "/main/" + imgPath;
        textPath = cachePath + book.getName() + "/text/" + textPath;
        Bitmap map = BitmapFactory.decodeFile(imgPath);
        String text = Utils.readAllFile(Paths.get(textPath));
        return new Pages(index, text, map);
    }

    void saveBookPage() {
        try {
            Path p = new File(cachePath + book.getName() + "/jbk_config.json").toPath();
            String buf = Utils.readAllFile(p);
            JSONObject object = new JSONObject(buf);
            object.putOpt("last_read_page", pageNow);
            Utils.writeFile(p, object.toString());
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }
}
