package com.example.plana.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.plana.bean.User;
import com.example.plana.config.Constant;

import javax.security.auth.Subject;

/**
 * @program: PlanA
 * @description:
 */
public class SubjectDB implements MyDatabaseHelper.TableCreateInterface{

    public static String TableName = "subjects";

    public static String _id = "_id";           // 主键
    public static String owner_id = "owner_id"; // 用户编号
    public static String name = "name";         // 课程名
    public static String room = "room";         // 教室
    public static String teacher = "teacher";   // 老师
    public static String week_list = "week_list"; // 周次
    public static String start = "start";       // 开始节次
    public static String step = "step";         // 上课节数
    public static String day = "day";           // 周几上课

    public static SubjectDB subjectDB = new SubjectDB();
    public static SubjectDB getInstance(){
        return subjectDB;
    }

    public SubjectDB() {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + SubjectDB.TableName + "("
                + BaseColumns._ID + " integer primary key autoincrement, "
                + SubjectDB.owner_id + " integer,"
                + SubjectDB.name + " text,"
                + SubjectDB.room + " text,"
                + SubjectDB.teacher + " text,"
                + SubjectDB.week_list + " text,"
                + SubjectDB.start + " integer,"
                + SubjectDB.step + " integer,"
                + SubjectDB.day + " integer"
                + ")";
        db.execSQL(sql);
        Log.i(Constant.TAG.DATE_BASE_TAG,
                SubjectDB.TableName + " --- Table Created ---");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String sql = "drop table if exists " + SubjectDB.TableName;
            db.execSQL(sql);
            this.onCreate(db);
        }
    }

//    // TODO insert subject
//    public static void insertSubject(MyDatabaseHelper dbHelper, Subject subject, User user) {
//
//    }


}
