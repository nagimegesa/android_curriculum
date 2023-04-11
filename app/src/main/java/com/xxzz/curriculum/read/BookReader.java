package com.xxzz.curriculum.read;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xxzz.curriculum.Utils;

import java.io.IOException;
import java.nio.file.Paths;

public class BookReader {
    private Book book;
    private final String cachePath;

    private int pageNow;
    public BookReader(String cachePath) {
        this.cachePath = cachePath + "/Book/";
    }
    public BookReader(String cachePath, Book book) {
        this(cachePath);
        this.book = book;
        this.pageNow = book.getReadPage();
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Pages getLastReadPages() throws IOException {
        int lastPage = book.getReadPage();
        return getIndexPage(lastPage);
    }

    public Book getBook() {
        return book;
    }

    public Pages getIndexPage(int index) throws IOException {
        if(index <= 0 || index > book.getPages()) return null;
        String imgPath = book.getIndexImage(index);
        String textPath = book.getIndexText(index);
        imgPath = cachePath + book.getName() + "/main/" + imgPath;
        textPath = cachePath + book.getName() + "/text/" + textPath;
        Bitmap map = BitmapFactory.decodeFile(imgPath);
        String text = Utils.readAllFile(Paths.get(textPath));
        return new Pages(index, text, map);
    }

    public int getPageNow() {
        return pageNow;
    }
}
