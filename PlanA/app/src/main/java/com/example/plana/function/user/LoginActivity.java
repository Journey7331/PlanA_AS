package com.example.plana.function.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plana.R;
import com.example.plana.function.MainActivity;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.database.UserDB;

/**
 * @program: PlanA
 * @description:
 */

public class LoginActivity extends BaseActivity
        implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    EditText et_username, et_password;
    Button btn_login;
    TextView tv_register;

    String phone;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        et_username = findViewById(R.id.et_loginUsername);
        et_password = findViewById(R.id.et_loginPassword);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        phone = "";
        password = "";

        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            phone = et_username.getText().toString();
            password = et_password.getText().toString();
            if ("".equals(phone)) {
                Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("".equals(password)) {
                Toast.makeText(
                        LoginActivity.this,
                        "请输入密码",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            int ret = UserDB.login(sqlite, phone, password);
            if (ret == 0) {
                Toast.makeText(
                        LoginActivity.this,
                        "密码错误",
                        Toast.LENGTH_SHORT
                ).show();
            } else if (ret == -1) {
                Toast.makeText(
                        LoginActivity.this,
                        "这个手机号暂时没有被注册",
                        Toast.LENGTH_SHORT
                ).show();
            } else if (ret == 1) {
                Toast.makeText(
                        LoginActivity.this,
                        "登录成功",
                        Toast.LENGTH_SHORT
                ).show();
//                hideKeyboard(this);

                My.Account = UserDB.getUser(sqlite, phone);

                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("auto_id", phone);
                autoLogin.putString("auto_pw", password);
                autoLogin.apply();

                Log.d(TAG, My.Account.toString());
                Log.i(TAG, "已经登录的账号密码保存到 SharedPreferences");

                directToMainActivity();
            }
        }

        if (v.getId() == R.id.tv_register) {
            directToRegisterActivity();
        }

    }


    /**
     * 登陆成功后转到主页
     */
    private void directToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.finish();
        }
        startActivity(intent);
        finish();
    }

    /**
     * 转到注册页
     * */
    private void directToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity1.class);
        startActivity(intent);
    }

}
