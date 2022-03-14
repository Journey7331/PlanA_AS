package com.example.plana.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseRegisterActivity;
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

    AlertDialog.Builder builder;
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
            builder = new AlertDialog.Builder(this);
            // inflater
            LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.add_register_date, null);
            final DatePicker datePicker = view.findViewById(R.id.register_date_picker);

            builder.setView(view);
            builder.setNegativeButton("Cancel", (dialog, which) -> etBirth.setText(""));
            builder.setPositiveButton("Submit", (dialog, which) -> {
                cal.set(Calendar.YEAR, datePicker.getYear());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                etBirth.setText(sdf.format(cal.getTime()));
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (v.getId() == R.id.btn_register_back) {
            Intent intent = new Intent(this, RegisterActivity1.class);
            intent.putExtra("phone", phone);
            intent.putExtra("pwd", pwd);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_back2, R.anim.slide_back1);
            startActivity(intent, options.toBundle());
            finish();
        } else if (v.getId() == R.id.btn_register_submit) {
            if (checkRegister2(etName, etBirth, etEmail)) {
                name = etName.getText().toString();
                birth = etBirth.getText().toString();
                email = etEmail.getText().toString();

                ContentValues values = new ContentValues();
                values.put(UserDB.phone, phone);
                values.put(UserDB.pwd, pwd);
                values.put(UserDB.name, name);
                values.put(UserDB.birth, birth);
                values.put(UserDB.email, email);
                UserDB.insertUser(mysql, values);

                Toast.makeText(this,"Account Created.",Toast.LENGTH_SHORT).show();
                ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_back2, R.anim.slide_back1);
                startActivity( new Intent(this, LoginActivity.class), options.toBundle());
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegisterActivity1.class);
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_back2, R.anim.slide_back1);
        startActivity(intent, options.toBundle());
        finish();
    }
}
