package com.example.plana.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.plana.bean.Todos;

import java.util.ArrayList;

/**
 * @program: PlanA
 * @description:
 */
public class TodosDB implements MyDatabaseHelper.TableCreateInterface {
    // 表名
    public static String TableName = "todos";
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

    public static TodosDB todosDB = new TodosDB();

    public static TodosDB getInstance() {
        return todosDB;
    }

    public TodosDB() {

    }

    // 创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TodosDB.TableName + "("
                + BaseColumns._ID + " integer primary key autoincrement, "
                + TodosDB.content + " text,"
                + TodosDB.memo + " text,"
                + TodosDB.done + " text,"
                + TodosDB.date + " text,"
                + TodosDB.time + " text,"
                + TodosDB.level + " float"
                + ")";

        db.execSQL(sql);
        Log.i("create", "** Todos Table Created **");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String sql = "drop table if exists " + TodosDB.TableName;
            db.execSQL(sql);
            this.onCreate(db);
        }
    }

    // 插入Event
    public static void insertEvent(MyDatabaseHelper dbHelper, ContentValues eventValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(TodosDB.TableName, null, eventValues);
        String content = eventValues.get(TodosDB.content).toString();
        Log.i("insert", "** insert an event: " + content + " **");
        db.close();
    }


    // 删除Event By Id
    public static void deleteEventById(MyDatabaseHelper dbHelper, String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TodosDB.TableName, BaseColumns._ID + "=?", new String[]{id + ""});
        db.close();
    }

    // 更新Event By Id
    public static void updateEventById(MyDatabaseHelper dbHelper, String id, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(
                TodosDB.TableName,
                values,
                BaseColumns._ID + " = ? ",
                new String[]{id + ""}
        );
        String content = values.get(TodosDB.content).toString();
        Log.i("update", "** Update an event: " + content);

        db.close();
    }

    // 更新Event Done State by Id
    public static void updateEventDoneState(MyDatabaseHelper dbHelper, String id, boolean status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodosDB.done, status + "");    // Change Boolean To String
        db.update(TodosDB.TableName, values, BaseColumns._ID + " = ? ", new String[]{id + ""});
        Log.i("update", "** Done State Change: " + status);

        db.close();
    }

    // 获取数据库中所有的Event
    @SuppressLint("Range")
    public static ArrayList<Todos> queryAllEvent(MyDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Todos> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TodosDB.TableName, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Todos todos = new Todos();
                todos.set_id(cursor.getInt(cursor.getColumnIndex(TodosDB._id)));
                todos.setContent(cursor.getString(cursor.getColumnIndex(TodosDB.content)));
                todos.setMemo(cursor.getString(cursor.getColumnIndex(TodosDB.memo)));
                // Done  |  DateBase：String  |  Class: Boolean
                todos.setDone(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(TodosDB.done))));
                todos.setDate(cursor.getString(cursor.getColumnIndex(TodosDB.date)));
                todos.setTime(cursor.getString(cursor.getColumnIndex(TodosDB.time)));
                todos.setLevel(cursor.getFloat(cursor.getColumnIndex(TodosDB.level)));
                list.add(todos);
            }
        }

        cursor.close();
        db.close();
        return list;
    }


}
