package com.xxzz.curriculum.index;

public class BooKInfo {
    private  String name;
    private  String coverPath;
    private  String lastReadTime;
    public BooKInfo(String name, String coverPath,String lastReadTime) {
        this.name = name;
        this.coverPath = coverPath;
        this.lastReadTime=lastReadTime;
    }
    public String getName() {
        return name;
    }
    public String getCoverPath() {
        return coverPath;
    }
    public String getLastReadTime(){return lastReadTime;}
}
