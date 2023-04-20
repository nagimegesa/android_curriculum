package com.xxzz.curriculum.index;

public class BooKMark extends BookCollection {
    private String text;

    public BooKMark(String bookName, int page, String text) {
        super(bookName, page);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
