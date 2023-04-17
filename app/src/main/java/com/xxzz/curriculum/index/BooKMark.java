package com.xxzz.curriculum.index;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BooKMark extends BookCollection {
    private int count = 0;
    private String bookName;
    private int page;
    private String text;
    public BooKMark() {}

    private void addBookMark(Context context,String name, int page, String text){
        MyHelper myHelper = new MyHelper(context);
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookName",name);
        values.put("page",page);
        values.put("text",text);
        db.insert("bookmark",null,values);
        db.close();
    }
    
    private void deleteBookMark(Context context,String name,int page){
        MyHelper myHelper = new MyHelper(context);
        SQLiteDatabase db = myHelper.getWritableDatabase();
        //delete参数（要操作的表名，条件，参数）
        //db.delete("data","ID=?", new String[] {ID+""});
        db.close();
    }

    private void modifyBookMark(Context context, String name, int page, String text){
        MyHelper myHelper = new MyHelper(context);
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("NAME",NAME);
        //update参数（表名，条件，参数）
        //db.update("data",values,"ID=?", new String[] {ID});
        db.close();
    }
    
    private void findBookMark(Context context, String name, int page, String text){
        MyHelper myHelper = new MyHelper(context);
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("bookmark", null, "ID=?", new String[] {name+""}, null, null ,null);
        //判断是否有数据
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
//                String id = cursor.getString(cursor.getColumnIndex("ID"));
//                String name = cursor.getString(cursor.getColumnIndex("NAME"));
//                //在TextView展示数据
//                //Show.setText(Show.getText().toString()+"\n"+"ID："+id+"     "+"NAME："+name);
            }
        }
    }


//    public JSONObject readInformation(String bookName, int page, String text) {
//        JSONObject object = new JSONObject();
//        try {
//            object.put("book_name", bookName);
//            object.put("page", page);
//            object.put("text", text);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return object;
//    }
//    public void writeBookMark(JSONObject object,int count){
//        JSONObject obj = new JSONObject();
//        JSONArray array = new JSONArray();
//        array.put(object);
//        try{
//            obj.put("count",count);
//            obj.put("info",array);
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//    }
//    public String readBookMark(File file) {
//        try {
//            InputStream is = this.getClass().getClassLoader().
//                    getResourceAsStream("bookmarks.json");  //读取文件
//            InputStreamReader streamReader = new InputStreamReader(is);
//            BufferedReader reader = new BufferedReader(streamReader);
//            String line="";
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            JSONObject object=new JSONObject(stringBuilder.toString());
//            // TODO: something
//            reader.close();
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
