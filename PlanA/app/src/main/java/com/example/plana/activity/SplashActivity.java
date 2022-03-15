package com.example.plana.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cunoraz.gifview.library.GifView;
import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.database.UserDB;
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
    TextView splashTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 更换状态栏字体的颜色
        // Android 6.0 +
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        splashTextView = findViewById(R.id.tv_splash);
        gifView = findViewById(R.id.logo_gif);
        gifView.setGifResource(R.mipmap.logo);
        gifView.setVisibility(View.INVISIBLE);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id", null);
        auto_password = auto.getString("auto_pw", null);

        handler.postDelayed(runable, 200);

        new Handler().postDelayed(() -> {
            splashTextView.setText("一");
            gifView.setVisibility(View.VISIBLE);
            gifView.play();
        }, 200);

        new Handler().postDelayed(() -> {
            if (auto_id != null && auto_password != null) {
                login(auto_id, auto_password);
            } else {
                directToLoginActivity();
            }
        }, SPLASH_TIME);

    }

    /**
     * 实现自增长TextView
     */
    int time = 0;
    Handler handler = new Handler();
    Runnable runable = new Runnable() {
        @Override
        public void run() {
            if (time == 4) splashTextView.setText("一个");
            if (time == 9) splashTextView.setText("一个计");
            if (time == 17) splashTextView.setText("一个计划");
            time++;
//            Log.i(TAG, appName_cn.toString());
            handler.postDelayed(this, 100);
        }
    };


    /**
     * 本地自动登录
     */
    private void login(String phone, String pw) {
        int ret = UserDB.login(mysql, phone, pw);
        if (ret == 1) {
            Log.i(TAG, "自动登录 成功");
            My.Account = UserDB.getUser(mysql, phone);
            directToMainActivity();
        } else {
            Log.i(TAG, "自动登录 失败");
            My.Account = null;
            directToLoginActivity();
        }
    }


    /**
     * http post请求登录
     */
//    private void login1(final String id, final String pw) {
//        Response<ResponseBody> response = null;
//        try {
//            response = retrofitBuilder.build()
//                    .create(UserService.class)
//                    .login(id, pw)
//                    .execute();
//        } catch (IOException e) {
//            Log.d(TAG, "响应失败");
//            e.printStackTrace();
//            directToLoginActivity();
//        }
//
//        if (response.isSuccessful()) {
//            JsonObject jsonObject;
//            try {
//                jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
//                if ("ok".equals(jsonObject.get("result").getAsString())) {
//
//                    updateMy();
//                    // TODO after auto login
//
//                    directToMainActivity();
//                } else {
//                    directToLoginActivity();
//                }
//            } catch (IOException e) {
//                Log.d(TAG, "JSON 错误");
//                e.printStackTrace();
//                directToLoginActivity();
//            }
//
//        } else {
//            Log.d(TAG, "响应失败");
//            directToLoginActivity();
//        }
//    }


    /**
     * 转到登录页
     */
    private void directToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        gifView.pause();
        handler.removeCallbacks(runable);
        finish();
    }


    /**
     * 登陆成功后转到主页
     */
    private void directToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        gifView.pause();
        // 如果没有 removeCallbacks， 会导致内存泄露
        handler.removeCallbacks(runable);
        finish();
    }


    /**
     * 更新 My 中的信息
     */
    private void updateMy() {

    }


}
