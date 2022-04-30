package com.example.plana.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.plana.bean.TimerRecorder;
import com.example.plana.bean.Todos;
import com.example.plana.config.Constant;
import com.example.plana.utils.TimeCalcUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: PlanA
 * @description:
 */
public class TimerDB implements MyDatabaseHelper.TableCreateInterface{

    public static String TableName = "timer_recorder";

    public static String _id = "_id";
    public static String day = "day";
    public static String start_time = "start_time";
    public static String end_time = "end_time";
    public static String time = "time";
    public static String count_type = "count_type";
    public static String tag = "tag";

    public static TimerDB timerDB = new TimerDB();

    public static TimerDB getInstance() {
        return timerDB;
    }

    public TimerDB() {
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TimerDB.TableName + "("
                + BaseColumns._ID + " integer primary key autoincrement, "
                + TimerDB.day + " text,"
                + TimerDB.start_time + " text,"
                + TimerDB.end_time + " text,"
                + TimerDB.time + " integer,"
                + TimerDB.count_type + " text,"
                + TimerDB.tag + " text"
                + ")";
        db.execSQL(sql);
        Log.i(Constant.TAG.DATE_BASE_TAG,
                TimerDB.TableName + " --- Table Created ---");

//        // init
//        for (ContentValues value : getValues()) {
//            db.insert(TimerDB.TableName, null, value);
//        }
    }

    public static void dropTable(MyDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "delete from " + TimerDB.TableName + ";";
        db.execSQL(sql);
//        sql = "update sqlite_sequence SET seq = 0 where name = " + TimerDB.TableName + ";";
//        db.execSQL(sql);
        Log.i("dropTable", "dropTable:" + TimerDB.TableName);
        db.close();
    }

    private List<ContentValues> getValues() {
        List<ContentValues> list = new ArrayList<>();
        list.add(getValue("22.03.27",  18, 54, 30, TimerRecorder.NEGATIVE));
        list.add(getValue("22.03.28",  16, 27, 112, TimerRecorder.POSITIVE));
        list.add(getValue("22.04.02",  11, 3, 60, TimerRecorder.NEGATIVE));
        list.add(getValue("22.04.09",  9, 23, 36, TimerRecorder.POSITIVE));
        return list;
    }

    private ContentValues getValue(String day, int startHour, int startMin, int time, String count_type) {
        ContentValues value = new ContentValues();
        value.put(TimerDB.day, day);
        value.put(TimerDB.start_time, TimeCalcUtil.format02d(startHour,startMin));
        value.put(TimerDB.end_time, TimeCalcUtil.calcEndTime(startHour, startMin, time));
        value.put(TimerDB.time, time);
        value.put(TimerDB.count_type, count_type);
        return value;
    }


    /**
     * sqlite 数据库的 version 升级时会删除原表及数据
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String sql = "drop table if exists " + TimerDB.TableName;
            db.execSQL(sql);
            this.onCreate(db);
        }
    }


    /**
     * 插入 TimerRecorder
     * */
    public static void insertTimerRecorder(MyDatabaseHelper dbHelper, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(TimerDB.TableName, null, values);
        db.close();
    }

    /**
     * 获取全部 TimerRecorder
     * */
    @SuppressLint("Range")
    public static ArrayList<TimerRecorder> queryAllTimerRecorder(MyDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<TimerRecorder> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TimerDB.TableName, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                TimerRecorder record = new TimerRecorder();
                record.set_id(cursor.getInt(cursor.getColumnIndex(TimerDB._id)));
                record.setDay(cursor.getString(cursor.getColumnIndex(TimerDB.day)));
                record.setStartTime(cursor.getString(cursor.getColumnIndex(TimerDB.start_time)));
                record.setEndTime(cursor.getString(cursor.getColumnIndex(TimerDB.end_time)));
                record.setTime(cursor.getInt(cursor.getColumnIndex(TimerDB.time)));
                record.setCount_type(cursor.getString(cursor.getColumnIndex(TimerDB.count_type)));
                record.setTag(cursor.getString(cursor.getColumnIndex(TimerDB.tag)));
                list.add(record);
            }
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 根据id删除 Recorder
     * */
    public static void deleteRecorderById(MyDatabaseHelper dbHelper, String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TimerDB.TableName, BaseColumns._ID + "=?", new String[]{id + ""});
        db.close();
    }
}
