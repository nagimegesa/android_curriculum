package com.xxzz.curriculum.index;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xxzz.curriculum.read.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookManager {
    DBHelper DBHelper;
    SQLiteDatabase db;
//    private int count = 0;

    public void addBookMark(Context context,String name, int page, String text) {
        DBHelper = new DBHelper(context);
        db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookName", name);
        values.put("page", page);
        values.put("text", text);
        db.insert("bookmark", null, values);
        db.close();
    }

    public void deleteBookMark(Context context, String ID) {
        DBHelper = new DBHelper(context);
        db = DBHelper.getWritableDatabase();
        //delete参数（要操作的表名，条件，参数）
        db.delete("data", "ID=" + ID, new String[]{ID + ""});
        db.close();
    }

    public void modifyBookMark(Context context, String name, int page, String text) {
        DBHelper = new DBHelper(context);
        db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //update参数（表名，条件，参数）
        //db.update("data",values,"ID=?", new String[] {ID});
        db.close();
    }

    public int findBookMark(){
        return 0;
    }
    public List<BooKMark> readBookMark(Context context) {
        DBHelper = new DBHelper(context);
        db = DBHelper.getWritableDatabase();
        List<BooKMark> list = new ArrayList<BooKMark>();
        if(isTableExist("bookmark")) {
            Cursor cursor = db.rawQuery("select * from bookmark", null);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    BooKMark booKMark= new BooKMark(cursor.getString(0),Integer.parseInt(cursor.getString(1)),cursor.getString(2));
                    list.add(booKMark);
                }
                cursor.close();
                db.close();
                return list;
            } else {
                cursor.close();
                db.close();
            }
        }
        return list;
    }

    public boolean isTableExist(String table) {
        Cursor c = db.rawQuery("select count(*) from sqlite_master where type='table' and name='" + table + "'", null);
        if (c != null) {
            while (c.moveToNext()) {
                int count = c.getInt(0);
                if (count > 0) {
                    c.close();
                    return true;
                }
            }
        } else {
            c.close();
        }
        return false;
    }
}
