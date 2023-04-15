package com.xxzz.curriculum.index;

public class BooKInfo {
    private final String name;
    private final String coverPath;

    public BooKInfo(String name, String coverPath) {
        this.name = name;
        this.coverPath = coverPath;
    }

    public String getName() {
        return name;
    }

    public String getCoverPath() {
        return coverPath;
    }
}
