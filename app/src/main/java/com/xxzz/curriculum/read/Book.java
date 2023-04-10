package com.xxzz.curriculum.read;

import java.util.HashMap;
import java.util.List;

public class Book {
    private String name;
    private int pages;

    private int readPage;
    private int readTime;

    private String coverPath;
    private HashMap<Integer, String> imgPath;
    private HashMap<Integer, String> textPath;


    public String getName() {
        return name;
    }

    public int getPages() {
        return pages;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setImgPath(HashMap<Integer, String> imgPath) {
        this.imgPath = imgPath;
    }

    public void setTextPath(HashMap<Integer, String> textPath) {
        this.textPath = textPath;
    }

    public void setReadPage(int readPage) {
        this.readPage = readPage;
    }

    public String getIndexText(int index) {
        return textPath.get(index);
    }

    public String getIndexImage(int index) {
        return imgPath.get(index);
    }

    public int getReadPage() {
        return readPage;
    }

    public int getReadTime() {
        return readTime;
    }
}
