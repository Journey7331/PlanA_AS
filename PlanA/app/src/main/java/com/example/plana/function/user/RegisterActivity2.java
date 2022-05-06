package com.example.plana.function.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseRegisterActivity;
import com.example.plana.config.Constant;
import com.example.plana.service.UserService;
import com.example.plana.utils.TimeCalcUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @program: PlanA
 * @description:
 */

public class RegisterActivity2 extends BaseRegisterActivity
        implements View.OnClickListener {

    public final String TAG = "RegisterActivity2";

    EditText etBirth, etName, etEmail;
    Button btnSubmit;
    TextView btnBack;
    String phone;
    String pwd;

    DatePicker datePicker;
    final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");

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
            final View view = inflater.inflate(R.layout.dialog_add_date, null);
            final DatePicker datePicker = view.findViewById(R.id.add_date_picker);

            AlertDialog alertDialog = new AlertDialog
                    .Builder(this, R.style.AlertDialogTheme)
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
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);

        } else if (v.getId() == R.id.btn_register_back) {
            // pop from activity stack
            finish();
        } else if (v.getId() == R.id.btn_register_submit) {
            if (checkRegister2(etName, etBirth, etEmail)) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                params.put("phone", phone);
                params.put("password", pwd);
                params.put("name", etName.getText().toString());
                params.put("birthday", etBirth.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("create_time", TimeCalcUtil.calToSimpleStr(cal));

                Log.e(TAG, TimeCalcUtil.calToSimpleStr(cal));
                register();

//                ContentValues values = new ContentValues();
//                values.put(UserDB.phone, phone);
//                values.put(UserDB.pwd, pwd);
//                values.put(UserDB.name, etName.getText().toString());
//                values.put(UserDB.birth, etBirth.getText().toString());
//                values.put(UserDB.email, etEmail.getText().toString());
//                UserDB.insertUser(sqlite, values);
//                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
//                if (RegisterActivity1.registerActivity != null) {
//                    RegisterActivity1.registerActivity.finish();
//                }
//                finish();
            }
        }
    }

    private void register() {
        retrofit.create(UserService.class)
                .register(params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        response(response);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                        Toast.makeText(RegisterActivity2.this, "网络原因，注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void response(Response<ResponseBody> response) {
        if (response.isSuccessful() && response.body() != null) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                if (object.optString("result").equals("ok")) {
                    registerFinish();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
                Toast.makeText(RegisterActivity2.this, "服务器原因，注册失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
            Toast.makeText(RegisterActivity2.this, "网络原因，注册失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerFinish() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        if (RegisterActivity1.registerActivity != null) {
            RegisterActivity1.registerActivity.finish();
        }
        finish();
    }

}
