package com.xxzz.curriculum.index;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BooKInfo implements Parcelable {
    public static final Creator<?> CREATOR = new Creator<BooKInfo>() {
        @Override
        public BooKInfo createFromParcel(Parcel source) {
            BooKInfo info = new BooKInfo();
            info.name = source.readString();
            info.coverPath = source.readString();
            info.lastReadTime = source.readString();
            return info;
        }

        @Override
        public BooKInfo[] newArray(int size) {
            return new BooKInfo[size];
        }
    };
    private String name;
    private String coverPath;
    private String lastReadTime;

    private BooKInfo() {
    }

    public BooKInfo(String name, String coverPath, String lastReadTime) {
        this.name = name;
        this.coverPath = coverPath;
        this.lastReadTime = lastReadTime;
    }

    public String getName() {
        return name;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public String getLastReadTime() {
        return lastReadTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(coverPath);
        dest.writeString(lastReadTime);
    }
}
