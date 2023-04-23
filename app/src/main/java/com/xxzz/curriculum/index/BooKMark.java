package com.xxzz.curriculum.index;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BooKMark extends BookCollection {
    private final int count = 0;
    private String bookName;
    private int page;
    private String text;

    public BooKMark() {
    }

    private void addBookMark(Context context, String name, int page, String text) {
        DBHelper DBHelper = new DBHelper(context);
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookName", name);
        values.put("page", page);
        values.put("text", text);
        db.insert("bookmark", null, values);
        db.close();
    }

    private void deleteBookMark(Context context, String name, int page) {
        DBHelper DBHelper = new DBHelper(context);
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        //delete参数（要操作的表名，条件，参数）
        //db.delete("data","ID=?", new String[] {ID+""});
        db.close();
    }

    private void modifyBookMark(Context context, String name, int page, String text) {
        DBHelper DBHelper = new DBHelper(context);
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.close();
    }

    private void findBookMark(Context context, String name, int page, String text) {
        DBHelper DBHelper = new DBHelper(context);
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor cursor = db.query("bookmark", null, "ID=?", new String[]{name + ""}, null, null, null);
        //判断是否有数据
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
            }
        }
    }
}
