package com.xxzz.curriculum.index;

public class BookCollection {
    private String bookName;
    private int page;

    public BookCollection(String bookName, int page) {
        this.bookName = bookName;
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public String getBookName() {
        return bookName;
    }
}
