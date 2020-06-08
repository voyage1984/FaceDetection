package com.example.facedetection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySQLite extends SQLiteOpenHelper {
    public MySQLite(Context context) {
        super(context, "user_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(id integer primary key autoincrement,name varchar(20),sex verchar(20),age integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void add_data(SQLiteDatabase db, String username, String sex, int age){
            ContentValues values = new ContentValues();
            values.put("name", username);
            values.put("sex", sex);
            values.put("age", age);
            db.insert("user", null, values);
            db.close();
    }

    public void delete(SQLiteDatabase db, int id) {
        db.delete("user","id = ?",new String[]{id + ""});
        db.close();
    }

    public void update(SQLiteDatabase db, int id, String username, String sex, int age) {
        ContentValues values = new ContentValues();
        values.put("name",username);
        values.put("sex",sex);
        values.put("age",age);

        db.update("user",values,"id = ?",new String[]{id + ""});
        db.close();
    }

    public String[] getUserByName(SQLiteDatabase db,String name){
        String[] args  = new String[] {name};

        Cursor cursor=db.rawQuery("select * from user where name == ?",args);

        String[] str = new String[]{};

        if(cursor.moveToNext())
        {
            String mName=cursor.getString(cursor.getColumnIndex("name"));
            String mSex=cursor.getString(cursor.getColumnIndex("sex"));
            String mAge = Integer.toString(cursor.getInt(cursor.getColumnIndex("age")));

            cursor.close();
             str = new String[]{mName,mSex,mAge};
        }
        return str;
    }

    public String[] getUserById(SQLiteDatabase db,String id){
        String[] args  = new String[] {id};

        Cursor cursor=db.rawQuery("select * from user where id == ?",args);

        String[] str = new String[]{};

        if(cursor.moveToNext())
        {
            String mName=cursor.getString(cursor.getColumnIndex("name"));
            String mSex=cursor.getString(cursor.getColumnIndex("sex"));
            String mAge = Integer.toString(cursor.getInt(cursor.getColumnIndex("age")));

            cursor.close();
            str = new String[]{mName,mSex,mAge};
        }
        return str;
    }

    public List<UserInfo> querydata(SQLiteDatabase db) {
        Cursor cursor = db.query("user",null,null,null,null,null,"id ASC");
        List<UserInfo> list = new ArrayList<UserInfo>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String username=cursor.getString(cursor.getColumnIndex("name"));
            String sex=cursor.getString(cursor.getColumnIndex("sex"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            list.add(new UserInfo(id, username, sex, age));
        }
        cursor.close();
        db.close();
        return list;
    }
}
