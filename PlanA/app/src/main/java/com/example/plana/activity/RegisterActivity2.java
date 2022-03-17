package com.example.plana.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseRegisterActivity;
import com.example.plana.config.Constant;
import com.example.plana.database.UserDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @program: PlanA
 * @description:
 */

public class RegisterActivity2 extends BaseRegisterActivity
        implements View.OnClickListener {

    EditText etBirth, etName, etEmail;
    Button btnSubmit;
    TextView btnBack;
    String phone;
    String pwd;
    String name, email, birth;

    DatePicker datePicker;
    final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        name = "";
        email = "";
        birth = "";

        etName = findViewById(R.id.et_registerName);
        etBirth = findViewById(R.id.et_registerBirth);
        etEmail = findViewById(R.id.et_registerEmail);
        btnSubmit = findViewById(R.id.btn_register_submit);
        btnBack = findViewById(R.id.btn_register_back);
        datePicker = findViewById(R.id.register_date_picker);

        etBirth.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_registerBirth) {
            // inflater
            LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.add_register_date, null);
            final DatePicker datePicker = view.findViewById(R.id.register_date_picker);

            AlertDialog alertDialog = new AlertDialog
                    .Builder(this, R.style.AppTheme_NumberPicker)
                    .setView(view)
                    .setNegativeButton("取消", (dialog, which) -> etBirth.setText(""))
                    .setPositiveButton("确认", (dialog, which) -> {
                        cal.set(Calendar.YEAR, datePicker.getYear());
                        cal.set(Calendar.MONTH, datePicker.getMonth());
                        cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                        etBirth.setText(sdf.format(cal.getTime()));
                    }).create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.ColorBlueGrey);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.ColorBlueGrey);

        } else if (v.getId() == R.id.btn_register_back) {
            // pop from activity stack
            finish();
        } else if (v.getId() == R.id.btn_register_submit) {
            if (checkRegister2(etName, etBirth, etEmail)) {

                ContentValues values = new ContentValues();
                values.put(UserDB.phone, phone);
                values.put(UserDB.pwd, pwd);
                values.put(UserDB.name, etName.getText().toString());
                values.put(UserDB.birth, etBirth.getText().toString());
                values.put(UserDB.email, etEmail.getText().toString());
                UserDB.insertUser(mysql, values);

                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                if (RegisterActivity1.registerActivity != null) {
                    RegisterActivity1.registerActivity.finish();
                }
                finish();
            }
        }
    }

}
