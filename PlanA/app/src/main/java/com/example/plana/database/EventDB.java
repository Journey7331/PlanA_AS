package com.example.plana.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.plana.bean.Event;

import java.util.ArrayList;

/**
 * @program: PlanA
 * @description:
 */
public class EventDB implements MyDatabaseHelper.TableCreateInterface {
    // 表名
    public static String TableName = "events";
    // 字段名
    public static String _id = "_id";           // 主键
    public static String content = "content";   // 内容
    public static String memo = "memo";         // 备注
    public static String done = "done";         // 是否完成
    public static String date = "date";         // ddl
    public static String time = "time";         // 时间
    public static String level = "level";       // 优先度
//    public static String type = "type";         // 类型
//    public static String color = "color";       // 颜色

    public static EventDB eventDB = new EventDB();

    public static EventDB getInstance() {
        return eventDB;
    }

    public EventDB() {

    }

    // 创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + EventDB.TableName + "("
                + BaseColumns._ID + " integer primary key autoincrement, "
                + EventDB.content + " text,"
                + EventDB.memo + " text,"
                + EventDB.done + " text,"
                + EventDB.date + " text,"
                + EventDB.time + " text,"
                + EventDB.level + " float"
                + ")";

        db.execSQL(sql);
        Log.i("create", "** Event Table Created **");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String sql = "drop table if exists " + EventDB.TableName;
            db.execSQL(sql);
            this.onCreate(db);
        }
    }

    // 插入Event
    public static void insertEvent(MyDatabaseHelper dbHelper, ContentValues eventValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(EventDB.TableName, null, eventValues);
        String content = eventValues.get(EventDB.content).toString();
        Log.i("insert", "** insert an event: " + content + " **");
        db.close();
    }


    // 删除Event By Id
    public static void deleteEventById(MyDatabaseHelper dbHelper, String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(EventDB.TableName, BaseColumns._ID + "=?", new String[]{id + ""});
        db.close();
    }

    // 更新Event By Id
    public static void updateEventById(MyDatabaseHelper dbHelper, String id, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(
                EventDB.TableName,
                values,
                BaseColumns._ID + " = ? ",
                new String[]{id + ""}
        );
        String content = values.get(EventDB.content).toString();
        Log.i("update", "** Update an event: " + content);

        db.close();
    }

    // 更新Event Done State by Id
    public static void updateEventDoneState(MyDatabaseHelper dbHelper, String id, boolean status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventDB.done, status + "");    // Change Boolean To String
        db.update(EventDB.TableName, values, BaseColumns._ID + " = ? ", new String[]{id + ""});
        Log.i("update", "** Done State Change: " + status);

        db.close();
    }

    // 获取数据库中所有的Event
    @SuppressLint("Range")
    public static ArrayList<Event> queryAllEvent(MyDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Event> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + EventDB.TableName, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Event event = new Event();
                event.set_id(cursor.getInt(cursor.getColumnIndex(EventDB._id)));
                event.setContent(cursor.getString(cursor.getColumnIndex(EventDB.content)));
                event.setMemo(cursor.getString(cursor.getColumnIndex(EventDB.memo)));
                // Done  |  DateBase：String  |  Class: Boolean
                event.setDone(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(EventDB.done))));
                event.setDate(cursor.getString(cursor.getColumnIndex(EventDB.date)));
                event.setTime(cursor.getString(cursor.getColumnIndex(EventDB.time)));
                event.setLevel(cursor.getFloat(cursor.getColumnIndex(EventDB.level)));
                list.add(event);
            }
        }

        cursor.close();
        db.close();
        return list;
    }


}
