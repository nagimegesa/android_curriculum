package com.xxzz.curriculum.index;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "bookmark.db";
    public static final int DB_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE bookmark(bookName VARCHAR(50) NOT NULL, page int NOT NULL,text varchar(100) not null, PRIMARY KEY(bookName,page))");
        sqLiteDatabase.execSQL("CREATE TABLE collection(bookName VARCHAR(50) NOT NULL PRIMARY KEY,page int NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
