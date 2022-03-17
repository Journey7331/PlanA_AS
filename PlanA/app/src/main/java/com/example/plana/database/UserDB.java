package com.example.plana.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.plana.bean.Todos;
import com.example.plana.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: PlanA
 * @description:
 */
public class UserDB implements MyDatabaseHelper.TableCreateInterface {

    public static String TableName = "users";   // 表名
    // 字段名
    // phonenumber password name email birthday
    public static String _id = "_id";           // 主键
    public static String phone = "phone";   // 手机号
    public static String pwd = "password";      // 密码
    public static String name = "name";         // 用户的昵称
    public static String email = "email";       // 电子邮箱
    public static String birth = "birthday";    // 生日

    public static UserDB getInstance() {
        return new UserDB();
    }

    public UserDB() {
    }

    // 创建数据表
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table if not exists " + UserDB.TableName + "("
                + BaseColumns._ID + " integer primary key autoincrement, "
                + UserDB.phone + " text,"
                + UserDB.pwd + " text,"
                + UserDB.name + " text,"
                + UserDB.email + " text,"
                + UserDB.birth + " text )";

        // 执行创建语句
        db.execSQL(sql);

        Log.i("create", "** User Table Created **");
    }

    // 更新数据库表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {
            String sql = "drop table if exists " + UserDB.TableName;
            db.execSQL(sql);
            onCreate(db);
        }
    }

    // 插入用户
    public static void insertUser(MyDatabaseHelper dbHelper, ContentValues userValues) {
        // 获得可写的数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 插入用户
        db.insert(UserDB.TableName, null, userValues);
        Log.i("insert", "** insert user, phone number: " + userValues.get(UserDB.phone) + " **");

        db.close();
    }

    // 修改用户 by id
    public static void updateUser(MyDatabaseHelper dbHelper, int id, ContentValues infoValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 根据id修改对应的记录, 所要修改的内容保存在传入参数infoValues中
        db.update(
                UserDB.TableName,
                infoValues,
                UserDB._id + " =? ",
                new String[]{id + ""}
        );
        Log.i("update", "** userid: " + id + " updated **");
        db.close();
    }

    // 查询用户 by phone
    @SuppressLint("Range")
    public static User getUser(MyDatabaseHelper dbHelper, String phone) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 此处要求查询 User.phone
        Cursor cursor = db.query(
                UserDB.TableName,
                null,
                UserDB.phone + " = ? AND " + UserDB._id + " != ? ",
                new String[]{phone, 0 + ""},
                null, null, null);

        // 移到第一个
        cursor.moveToFirst();

        User user = new User();
        user.set_id(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(UserDB.phone)));
        user.setName(cursor.getString(cursor.getColumnIndex(UserDB.name)));
        user.setPwd(cursor.getString(cursor.getColumnIndex(UserDB.pwd)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(UserDB.email)));
        user.setBirth(cursor.getString(cursor.getColumnIndex(UserDB.birth)));

        cursor.close();
        db.close();

        return user;
    }

    // 查重
    public static boolean checkPhoneExist(MyDatabaseHelper dbHelper, String phone) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean ret = false;
        Cursor cursor = db.query(
                UserDB.TableName,
                null,
                UserDB.phone + " = ? ",
                new String[]{phone + ""},
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            ret = true;
            Log.i("register", "** Phone Has Been Registered.");
        }

        cursor.close();
        db.close();
        return ret;
    }

    /*
     * return:
     *  1 all correct
     *  0 password wrong
     * -1 no this phone
     * */
    @SuppressLint("Range")
    public static int login(MyDatabaseHelper dbHelper, String phone, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int ret = -2;
        Cursor cursor = db.query(
                UserDB.TableName,
                null,
                UserDB.phone + " = ? AND " + UserDB._id + " != ? ",
                new String[]{phone + "", 0 + ""},
                null,
                null,
                null
        );
        if (cursor.getCount() == 0) {
            Log.i("Login", "** No This Phone.");
            ret = -1;
        } else {
            cursor.moveToFirst();
            int idGet = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            if (idGet != 0) {
                String pwd = cursor.getString(cursor.getColumnIndex(UserDB.pwd));
                if (pwd.equals(password)) {
                    Log.i("Login", "** All Correct. Log In Successfully.");
                    ret = 1;
                } else {
                    Log.i("Login", "** Password Wrong.");
                    ret = 0;
                }
            }
        }

        cursor.close();
        db.close();
        return ret;
    }

    // DEBUG
    @SuppressLint("Range")
    public static List<User> getAllUser(MyDatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + UserDB.TableName, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                User user = new User();
                user.set_id(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(UserDB.phone)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserDB.name)));
                user.setPwd(cursor.getString(cursor.getColumnIndex(UserDB.pwd)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserDB.email)));
                user.setBirth(cursor.getString(cursor.getColumnIndex(UserDB.birth)));
                users.add(user);
            }
        }

        cursor.close();
        db.close();
        return users;
    }


}
