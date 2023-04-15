package com.xxzz.curriculum.read;

import android.graphics.Bitmap;

public class Pages {
    private final String text;
    private final Bitmap map;
    private final int which;

    public Pages(int which, String text, Bitmap map) {
        this.which = which;
        this.text = text;
        this.map = map;
    }

    public Bitmap getMap() {
        return map;
    }

    public String getText() {
        return text;
    }

    public int getWhich() {
        return which;
    }
}
