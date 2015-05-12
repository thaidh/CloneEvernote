package com.example.thai.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.thai.myapplication.model.DiaryItem;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "zalo_action_log.db";
    private static final String TABLE_EVENT = "event_log";
    private SQLiteDatabase db;
    private volatile static DatabaseHelper instance = null;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }

            instance.createEventTable();
        }
        return instance;
    }

    private void createEventTable() {
        if (!isExistTable(TABLE_EVENT)) {
            db.execSQL("CREATE TABLE " + TABLE_EVENT + " (id INTEGER PRIMARY KEY AUTOINCREMENT, content, create_time)");
        }
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        db = context.openOrCreateDatabase(DB_NAME, 0, null);
    }

    private boolean isExistTable(String tableName) {
        boolean isExist = true;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM SQLITE_MASTER WHERE NAME= ? ", new String[]{tableName});
            if (cursor.getCount() == 0) {
                isExist = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isExist;
    }

    public void insertDiary(DiaryItem diary) {
        try {
            db.execSQL("INSERT INTO "
                            + TABLE_EVENT
                            + " (content, create_time)"
                            + " VALUES (?, ?)",
                    new String[]{diary.content, diary.createTime + ""});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLastDiary() {
        String diary = "";
        Cursor cursor = null;
        cursor = db.rawQuery("select content from " + TABLE_EVENT, null);
        if (cursor.moveToLast()) {
            diary = cursor.getString(cursor.getColumnIndex("content"));
        };
        return diary;
    }

    public ArrayList<DiaryItem> getAllDiary() {
        ArrayList<DiaryItem> arrDiary = new ArrayList<DiaryItem>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_EVENT, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            long createTime = cursor.getLong(cursor.getColumnIndex("create_time"));
            DiaryItem diary = new DiaryItem(id, content, createTime);
            arrDiary.add(diary);
        };
        return arrDiary;
    }

    public void clearAllLogs() {
        try {
            db.delete(TABLE_EVENT, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        synchronized (db) {
            if (db != null)
                db.close();
        }
        super.close();
    }
}