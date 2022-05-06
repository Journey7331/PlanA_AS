package com.example.plana.function.user;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseRegisterActivity;
import com.example.plana.bean.User;
import com.example.plana.config.Constant;
import com.example.plana.service.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @program: PlanA
 * @description:
 */
public class RegisterActivity1 extends BaseRegisterActivity {

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

        btnRegister.setOnClickListener(l -> {
            if (checkRegister1(etRegisterId, etRegisterPassword, etRegisterRepassword)) {
                String phone = etRegisterId.getText().toString();
                checkPhoneExist(phone);
            }
        });

        btnRegisterCancel.setOnClickListener(l -> finish());

    }


    private void checkPhoneExist(String phone) {
        params.put("phone", phone);
        retrofit.create(UserService.class)
                .checkPhone(params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        response(response);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(RegisterActivity1.this, "网络原因，手机号验证失败", Toast.LENGTH_SHORT).show();
                        Log.i(Constant.TAG.NETWORK_TAG, "网络原因，手机号验证失败");
                    }
                });
    }

    private void response(Response<ResponseBody> response) {
        if (response.isSuccessful() && response.body() != null) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                if (object.optString("result").equals("fail")) {
                    String pwd = etRegisterPassword.getText().toString();
                    String phone = etRegisterId.getText().toString();
                    Intent intent = new Intent(this, RegisterActivity2.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("pwd", pwd);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_next1, R.anim.slide_next2);
                    startActivity(intent, options.toBundle());
                }else{
                    Toast.makeText(this, "这个手机号已经被注册过啦", Toast.LENGTH_SHORT).show();
                    etRegisterId.setText("");
                    etRegisterId.requestFocus();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
            }
        } else {
            Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
            Toast.makeText(RegisterActivity1.this, "手机号验证失败 (error:" + response.code()+")", Toast.LENGTH_SHORT).show();
        }
    }



}

