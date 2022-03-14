package com.example.plana.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;
import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.service.UserService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @program: PlanA
 * @description:
 */
public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME = 3000;
    private static final String TAG = "SplashActivity";

    private String auto_id;
    private String auto_password;

    GifView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id", null);
        auto_password = auto.getString("auto_pw", null);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (auto_id != null && auto_password != null) {
                    login(auto_id, auto_password);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME);

        gifView = findViewById(R.id.logo_gif);
        gifView.setGifResource(R.mipmap.logo);
        gifView.setVisibility(View.VISIBLE);
        gifView.play();

    }


    /**
     * http post请求登录
     */
    private void login(final String id, final String pw) {
        Response<ResponseBody> response = null;
        try {
            response = retrofitBuilder.build()
                    .create(UserService.class)
                    .login(id, pw)
                    .execute();
        } catch (IOException e) {
            Log.d(TAG, "响应失败");
            e.printStackTrace();
            directToLoginActivity();
        }

        if (response.isSuccessful()) {
            JsonObject jsonObject;
            try {
                jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
                if ("ok".equals(jsonObject.get("result").getAsString())) {

                    // TODO after auto login

                } else {
                    directToLoginActivity();
                }
            } catch (IOException e) {
                Log.d(TAG, "JSON 错误");
                e.printStackTrace();
                directToLoginActivity();
            }

        } else {
            Log.d(TAG, "响应失败");
            directToLoginActivity();
        }
    }


    /**
     * 转到登录页
     */
    private void directToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        gifView.pause();
        finish();
    }


    /**
     *
     */
    private void updateMy(String url) {

    }


}
