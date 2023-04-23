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

    public void deleteBookMark(SQLiteDatabase db, String name, int page) {
        db.delete("bookmark", "bookName=?" + "and" + "page=?", new String[]{name, String.valueOf(page)});
        db.close();
    }

    public void modifyBookMark(SQLiteDatabase db, String name, int page, String text) {
        ContentValues values = new ContentValues();
        values.put("text", text);
        db.update("bookmark", values, "bookName=?" + "and" + "page=?", new String[]{name, String.valueOf(page)});
        db.close();
    }

    @SuppressLint("Range")
    public BooKMark findBookMark(SQLiteDatabase db, int position) {
        BooKMark booKMark = null;
        Cursor cursor = db.rawQuery("select * from bookmark", null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                if (cursor.getPosition() == position + 1) {
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

    public int findBookMark(SQLiteDatabase db, String name, int page) {
        Cursor cursor = db.query("bookmark", new String[]{"bookName", "page", "text"}, "bookName=?" + "and" + "page=?",
                new String[]{name, String.valueOf(page)}, null, null, null);
        //
        return 0;
    }

    public List<BooKMark> readBookMark(SQLiteDatabase db) {
        List<BooKMark> list = new ArrayList<BooKMark>();
        if (isTableExist(db, "bookmark")) {
            Cursor cursor = db.rawQuery("select * from bookmark", null);
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") BooKMark booKMark =
                            new BooKMark(
                                    cursor.getString(cursor.getColumnIndex("bookName")),
                                    cursor.getInt(cursor.getColumnIndex("page")),
                                    cursor.getString(cursor.getColumnIndex("text")));
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

    @SuppressLint("Range")
    public List<BooKMark> readBookMark(SQLiteDatabase db, String name) {
        List<BooKMark> list = new ArrayList<BooKMark>();
        if (isTableExist(db, "bookmark")) {
            Cursor cursor = db.rawQuery("select * from bookmark where bookName = ?", new String[]{name});
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    BooKMark booKMark = new BooKMark(
                            cursor.getString(cursor.getColumnIndex("bookName")),
                            cursor.getInt(cursor.getColumnIndex("page")),
                            cursor.getString(cursor.getColumnIndex("text")));
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

    public void addBookCollection(SQLiteDatabase db, String name, int page) {
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
                if (cursor.getPosition() == position + 1) {
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

    @SuppressLint("Range")
    public BookCollection findBookCollection(SQLiteDatabase db, String name) {
        BookCollection bookCollection = null;
        Cursor cursor = db.rawQuery("select * from collection where bookName = ?", new String[]{name});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                bookCollection = new BookCollection(
                        cursor.getString(cursor.getColumnIndex("bookName")),
                        cursor.getInt(cursor.getColumnIndex("page")));
            }
        }
        cursor.close();
        db.close();
        return bookCollection;
    }

    public List<BookCollection> readBookCollection(SQLiteDatabase db, String name) {
        List<BookCollection> list = new ArrayList<BookCollection>();
        if (isTableExist(db, "collection")) {
            Cursor cursor = db.rawQuery("select * from collection where bookName = ?", new String[]{name});
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") BookCollection bookCollection =
                            new BookCollection(
                                    cursor.getString(cursor.getColumnIndex("bookName")),
                                    cursor.getInt(cursor.getColumnIndex("page")));
                    list.add(bookCollection);
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

    public List<BookCollection> readBookCollection(SQLiteDatabase db) {
        List<BookCollection> list = new ArrayList<BookCollection>();
        if (isTableExist(db, "collection")) {
            Cursor cursor = db.rawQuery("select * from collection", null);
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") BookCollection bookCollection =
                            new BookCollection(
                                    cursor.getString(cursor.getColumnIndex("bookName")),
                                    cursor.getInt(cursor.getColumnIndex("page")));
                    list.add(bookCollection);
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
}
