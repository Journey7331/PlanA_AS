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
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.config.Constant;
import com.example.plana.function.MainActivity;
import com.example.plana.service.UserService;

import org.apache.commons.lang3.StringUtils;
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

public class LoginActivity extends BaseActivity {

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

        changeStatusBar();
        et_username = findViewById(R.id.et_loginUsername);
        et_password = findViewById(R.id.et_loginPassword);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        phone = "";
        password = "";

        tv_register.setOnClickListener(l -> {
            directToRegisterActivity();
        });

        btn_login.setOnClickListener(l -> {
            phone = et_username.getText().toString();
            password = et_password.getText().toString();
            if (StringUtils.isEmpty(phone)) {
                Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            } else if (StringUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            } else {
                login();
            }
        });
    }

    /**
     * 异步请求登陆
     */
    private void login() {
        params.put("phone", phone);
        retrofit.create(UserService.class)
                .checkPhone(params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        checkPhoneResponse(response);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                        networkErrorToast();
                    }
                });
    }

    /**
     * 先验证手机是否存在
     * 存在则请求验证 账号密码
     */
    private void checkPhoneResponse(Response<ResponseBody> response) {
        if (response.isSuccessful() && response.body() != null) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                if (object.optString("result").equals("ok")) {
                    params.put("password", password);
                    retrofit.create(UserService.class)
                            .login(params)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    response(response);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                                    networkErrorToast();
                                }
                            });
                } else {
                    et_username.requestFocus();
                    Toast.makeText(LoginActivity.this, "这个手机号暂时没有被注册", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                networkErrorToast();
                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
            }
        } else {
            Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
            networkErrorToast();
        }
    }

    /**
     * 处理账号密码验证的响应
     */
    private void response(Response<ResponseBody> response) {
        if (response.isSuccessful() && response.body() != null) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                if (object.optString("result").equals("ok")) {
                    updateMyAccount(object.optString("data"));

                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("auto_id", phone);
                    autoLogin.putString("auto_pw", password);
                    autoLogin.apply();

                    Log.d(TAG, My.Account.toString());
                    Log.i(TAG, "已经登录的账号密码保存到 SharedPreferences");

                    directToMainActivity();
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                networkErrorToast();
                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
            }
        } else {
            Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
            networkErrorToast();
        }
    }

    /**
     * 网络原因，验证失败 Toast
     */
    private void networkErrorToast() {
        Toast.makeText(LoginActivity.this, "网络原因，验证失败", Toast.LENGTH_SHORT).show();
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
     */
    private void directToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity1.class);
        startActivity(intent);
    }

    /**
     * 沉浸式状态栏
     */
    private void changeStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

}
