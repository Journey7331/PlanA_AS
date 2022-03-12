package com.example.plana.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseRegisterActivity;

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
    MyDatabaseHelper mysql = new MyDatabaseHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        etRegisterId = findViewById(R.id.et_registerId);
        etRegisterPassword = findViewById(R.id.et_registerPassword);
        etRegisterRepassword = findViewById(R.id.et_registerRepassword);
        btnRegister = findViewById(R.id.btn_register);
        btnRegisterCancel = findViewById(R.id.btn_register_cancel);

        // auto fill
        if (getIntent().getStringExtra("phone") != null && getIntent().getStringExtra("pwd") != null) {
            etRegisterId.setText(getIntent().getStringExtra("phone"));
            etRegisterPassword.setText(getIntent().getStringExtra("pwd"));
            etRegisterRepassword.setText(getIntent().getStringExtra("pwd"));
        }

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
                    Toast.makeText(this, "Phone Has Been Registered.", Toast.LENGTH_SHORT).show();
                    etRegisterId.setText("");
                    etRegisterId.requestFocus();
                } else {
                    String pwd = etRegisterPassword.getText().toString();
                    Intent intent = new Intent(this, RegisterActivity2.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("pwd", pwd);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_next1, R.anim.slide_next2);
                    startActivity(intent, options.toBundle());
                    finish();
                }

            }
        } else if (v.getId() == R.id.btn_register_cancel) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

