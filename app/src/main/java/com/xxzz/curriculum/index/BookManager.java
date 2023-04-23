package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookManager {

    public void addBookMark(SQLiteDatabase db, String name, int page, String text) {
        ContentValues values = new ContentValues();
        values.put("bookName", name);
        values.put("page", page);
        values.put("text", text);
        db.insert("bookmark", null, values);
        db.close();
    }

    public void deleteBookMark(SQLiteDatabase db, int position) {
        BooKMark booKMark= findBookMark(db,position);
        db.delete("bookmark","bookName=?",new String[]{booKMark.getBookName()});
        db.close();
    }

    public void modifyBookMark(SQLiteDatabase db, String name) {
        ContentValues values = new ContentValues();
        //db.update("data",,"bookName=?", new String[] {name});
        db.close();
    }

    @SuppressLint("Range")
    public BooKMark findBookMark(SQLiteDatabase db, int position) {
        BooKMark booKMark = null;
        Cursor cursor = db.rawQuery("select * from bookmark", null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                if (cursor.getCount() == position + 1) {
                    booKMark = new BooKMark(
                            cursor.getString(cursor.getColumnIndex("bookName")),
                            cursor.getInt(cursor.getColumnIndex("page")),
                            cursor.getString(cursor.getColumnIndex("text")));
                }
            }
        }
        cursor.close();
        db.close();
        return booKMark;
    }

    public List<BooKMark> readBookMark(SQLiteDatabase db) {
        List<BooKMark> list = new ArrayList<BooKMark>();
        if (isTableExist(db, "bookmark")) {
            Cursor cursor = db.rawQuery("select * from bookmark", null);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    @SuppressLint("Range") BooKMark booKMark =
                            new BooKMark(
                                    cursor.getString(cursor.getColumnIndex("bookName")),
                                    cursor.getInt(cursor.getColumnIndex("page")),
                                    cursor.getString(cursor.getColumnIndex("text")));
                    list.add(booKMark);
                } while (cursor.moveToNext());
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

    public boolean isTableExist(SQLiteDatabase db, String table) {
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

    public void addBookCollection(SQLiteDatabase db,String name,int page){
        ContentValues values = new ContentValues();
        values.put("bookName", name);
        values.put("page", page);
        db.insert("collection", null, values);
        db.close();
    }
    @SuppressLint("Range")
    public BookCollection findBookCollection(SQLiteDatabase db, int position) {
        BookCollection bookCollection = null;
        Cursor cursor = db.rawQuery("select * from collection", null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                if (cursor.getCount() == position + 1) {
                    bookCollection = new BookCollection(
                            cursor.getString(cursor.getColumnIndex("bookName")),
                            cursor.getInt(cursor.getColumnIndex("page")));
                }
            }
        }
        cursor.close();
        db.close();
        return bookCollection;
    }

    public List<BookCollection> readBookCollection(SQLiteDatabase db) {
        List<BookCollection> list = new ArrayList<BookCollection>();
        if (isTableExist(db, "collection")) {
            Cursor cursor = db.rawQuery("select * from collection", null);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    @SuppressLint("Range") BookCollection bookCollection =
                            new BookCollection(
                                    cursor.getString(cursor.getColumnIndex("bookName")),
                                    cursor.getInt(cursor.getColumnIndex("page")));
                    list.add(bookCollection);
                } while (cursor.moveToNext());
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
}
