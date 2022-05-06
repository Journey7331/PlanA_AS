package com.example.plana.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.plana.bean.Todos;
import com.example.plana.config.Constant;

import java.util.ArrayList;

/**
 * @program: PlanA
 * @description:
 */
public class DeletedTodosDB implements MyDatabaseHelper.TableCreateInterface {
    // 表名
    public static String TableName = "delete_todos";
    // 字段名
    public static String _id = "_id";
    public static String content = "content";
    public static String memo = "memo";
    public static String done = "done";
    public static String date = "date";
    public static String time = "time";
    public static String level = "level";

    public static DeletedTodosDB deletedTodosDB = new DeletedTodosDB();

    public static DeletedTodosDB getInstance() {
        return deletedTodosDB;
    }

    public DeletedTodosDB() {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + DeletedTodosDB.TableName + "("
                + BaseColumns._ID + " integer primary key autoincrement, "
                + DeletedTodosDB.content + " text,"
                + DeletedTodosDB.memo + " text,"
                + DeletedTodosDB.done + " text,"
                + DeletedTodosDB.date + " text,"
                + DeletedTodosDB.time + " text,"
                + DeletedTodosDB.level + " float"
                + ")";

        db.execSQL(sql);
        Log.i(Constant.TAG.DATE_BASE_TAG,
                DeletedTodosDB.TableName + " --- Table Created ---");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String sql = "drop table if exists " + DeletedTodosDB.TableName;
            db.execSQL(sql);
            this.onCreate(db);
        }
    }

    public static void dropTable(MyDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "delete from " + DeletedTodosDB.TableName + ";";
        db.execSQL(sql);
//        sql = "update sqlite_sequence SET seq = 0 where name = " + DeletedTodosDB.TableName + ";";
//        db.execSQL(sql);
        Log.i("dropTable", "dropTable:" + DeletedTodosDB.TableName);
        db.close();
    }

    public static void insertTodo(MyDatabaseHelper dbHelper, ContentValues eventValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DeletedTodosDB.TableName, null, eventValues);
        String content = eventValues.get(DeletedTodosDB.content).toString();
        Log.i("insert", "** insert an event: " + content + " **");
        db.close();
    }


    public static void deleteTodoById(MyDatabaseHelper dbHelper, String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DeletedTodosDB.TableName, BaseColumns._ID + "=?", new String[]{id + ""});
        db.close();
    }

    @SuppressLint("Range")
    public static ArrayList<Todos> queryAllTodos(MyDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Todos> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + DeletedTodosDB.TableName, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Todos todos = new Todos();
                todos.setId(cursor.getInt(cursor.getColumnIndex(DeletedTodosDB._id)));
                todos.setContent(cursor.getString(cursor.getColumnIndex(DeletedTodosDB.content)));
                todos.setMemo(cursor.getString(cursor.getColumnIndex(DeletedTodosDB.memo)));
                // Done  |  DateBase：String  |  Class: Boolean
                todos.setDone(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DeletedTodosDB.done))));
                todos.setDate(cursor.getString(cursor.getColumnIndex(DeletedTodosDB.date)));
                todos.setTime(cursor.getString(cursor.getColumnIndex(DeletedTodosDB.time)));
                todos.setLevel(cursor.getFloat(cursor.getColumnIndex(DeletedTodosDB.level)));
                list.add(todos);
            }
        }

        cursor.close();
        db.close();
        return list;
    }


}
