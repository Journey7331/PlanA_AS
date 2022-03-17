package com.example.plana.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseRegisterActivity;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.database.UserDB;

/**
 * @program: PlanA
 * @description:
 */
public class RegisterActivity1 extends BaseRegisterActivity
        implements View.OnClickListener {

    EditText etRegisterId;
    EditText etRegisterPassword;
    EditText etRegisterRepassword;
    Button btnRegister;
    TextView btnRegisterCancel;
    public static RegisterActivity1 registerActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        registerActivity = this;

        etRegisterId = findViewById(R.id.et_registerId);
        etRegisterPassword = findViewById(R.id.et_registerPassword);
        etRegisterRepassword = findViewById(R.id.et_registerRepassword);
        btnRegister = findViewById(R.id.btn_register);
        btnRegisterCancel = findViewById(R.id.btn_register_cancel);

        // InputMethodManager
        etRegisterId.requestFocus();
        InputMethodManager server = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        server.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        btnRegister.setOnClickListener(this);
        btnRegisterCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
            if (checkRegister1(etRegisterId, etRegisterPassword, etRegisterRepassword)) {
                String phone = etRegisterId.getText().toString();
                if (UserDB.checkPhoneExist(mysql, phone)) {
                    Toast.makeText(this, "这个手机号已经被注册过啦", Toast.LENGTH_SHORT).show();
                    etRegisterId.setText("");
                    etRegisterId.requestFocus();
                } else {
                    String pwd = etRegisterPassword.getText().toString();
                    Intent intent = new Intent(this, RegisterActivity2.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("pwd", pwd);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_next1, R.anim.slide_next2);
                    startActivity(intent, options.toBundle());
                }

            }
        } else if (v.getId() == R.id.btn_register_cancel) {
            // 退出栈顶
            finish();
        }
    }

}

