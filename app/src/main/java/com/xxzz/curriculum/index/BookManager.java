package com.xxzz.curriculum.index;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookManager {

    public void addBookMark(String name, int page, String text) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookName", name);
        values.put("page", page);
        values.put("text", text);
        db.insert("bookmark", null, values);
        db.close();
    }

    public void deleteBookMark(String name, int page) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        db.delete("bookmark", "bookname=?" + " and " + "page=?", new String[]{name, String.valueOf(page)});
        db.close();
    }

    public void deleteBookCollection(String name, int page) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        db.delete("collection", "bookName=?" + " and " + "page=?", new String[]{name, String.valueOf(page)});
        db.close();
    }

    public void modifyBookMark(String name, int page, String text) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("text", text);
        db.update("bookmark", values, "bookName=?" + " and " + "page=?", new String[]{name, String.valueOf(page)});
        db.close();
    }

    @SuppressLint("Range")
    public BooKMark findBookMark(int position) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
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

    public int findBookMark(String name, int page) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.query("bookmark", new String[]{"bookName", "page", "text"}, "bookName=?" + "and" + "page=?",
                new String[]{name, String.valueOf(page)}, null, null, null);
        //
        return 0;
    }

    public List<BooKMark> readBookMark() {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        List<BooKMark> list = new ArrayList<BooKMark>();
        if (isTableExist("bookmark")) {
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
    public List<BookCollection> readBookMark(String name) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        List<BookCollection> list = new ArrayList<>();
        if (isTableExist("bookmark")) {
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

    public boolean isTableExist(String table) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
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

    public void addBookCollection(String name, int page) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookName", name);
        values.put("page", page);
        db.insert("collection", null, values);
        db.close();
    }

    @SuppressLint("Range")
    public BookCollection findBookCollection(int position) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
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
    public BookCollection findBookCollection(String name) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
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

    public List<BookCollection> readBookCollection(String name) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        List<BookCollection> list = new ArrayList<BookCollection>();
        if (isTableExist("collection")) {
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

    public List<BookCollection> readBookCollection() {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        List<BookCollection> list = new ArrayList<BookCollection>();
        if (isTableExist("collection")) {
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

    public void deleteBookInfo(String bookName) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        db.delete("collection", "bookName=?", new String[]{bookName});
        db.delete("bookmark", "bookName=?", new String[]{bookName});
//        db.execSQL("delete from collection where bookName=" + bookName);
//        db.execSQL("delete from bookMark where bookName=" + bookName);
    }
}
