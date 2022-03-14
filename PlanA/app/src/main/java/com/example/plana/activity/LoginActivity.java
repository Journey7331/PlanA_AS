package com.example.plana.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.database.UserDB;

/**
 * @program: PlanA
 * @description:
 */

public class LoginActivity extends BaseActivity
        implements View.OnClickListener {

    EditText et_username, et_password;
    Button btn_login;
    TextView tv_register, tv_skip;

    String phone;
    String password;
    MyDatabaseHelper mysql = new MyDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_loginUsername);
        et_password = findViewById(R.id.et_loginPassword);
        tv_register = findViewById(R.id.tv_register);
        tv_skip = findViewById(R.id.tv_skip);
        btn_login = findViewById(R.id.btn_login);
        phone = "";
        password = "";

        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_skip.setOnClickListener(this);

        // Done
        // TODO: 查看数据库中上次登录的用户有没有登出，如果没有登出则自动登录
        // auto login
        if (!UserDB.getPhone(mysql).equals("")) {
            Log.i("login", "** auto Log in");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            phone = et_username.getText().toString();
            password = et_password.getText().toString();
            if ("".equals(phone)) {
                Toast.makeText(this, "Please Enter Phone.", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("".equals(password)) {
                Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show();
                return;
            }
            int ret = UserDB.login(mysql, phone, password);
            if (ret == 0) {
                Toast.makeText(this, "Password Wrong.", Toast.LENGTH_SHORT).show();
            } else if (ret == -1) {
                Toast.makeText(this, "This Phone Hasn't Been Registered.", Toast.LENGTH_SHORT).show();
            } else if (ret == 1) {
                Toast.makeText(this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                hideKeyboard(this);
                // 当前登录用户的手机号存到 User 表中的第一个
                ContentValues values = new ContentValues();
                values.put(UserDB.phone, phone);
                UserDB.updateUser(mysql, 0, values);
                Log.i("Login", "** Log in, Phone was Saved to Uid:0");
                startActivity(new Intent(this, MainActivity.class));
                // 以防止在MyPage页面中推出到这里时，上次登录的账号密码还在
                finish();
            }
        } else if (v.getId() == R.id.tv_register) {
            Intent intent = new Intent(this, RegisterActivity1.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.tv_skip) {
            Toast.makeText(this, "Login Without Account.", Toast.LENGTH_SHORT).show();
            hideKeyboard(this);

            ContentValues values = new ContentValues();
            values.put(UserDB.phone, "");
            UserDB.updateUser(mysql, 0, values);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }
}
