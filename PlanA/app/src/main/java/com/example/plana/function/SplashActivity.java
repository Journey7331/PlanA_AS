package com.example.plana.function;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import com.example.plana.R;
import com.example.plana.function.user.LoginActivity;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.database.UserDB;

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

        changeStatusBar();

        splashTextView = findViewById(R.id.tv_splash);
        gifView = findViewById(R.id.logo_gif);
        gifView.setGifResource(R.mipmap.logo);
        gifView.setVisibility(View.INVISIBLE);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id", null);
        auto_password = auto.getString("auto_pw", null);


        splashAnimation();

    }


    /**
     * 沉浸式状态栏
     */
    private void changeStatusBar() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 更换状态栏字体的颜色
        // Android 6.0 +
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 实现自增长TextView
     */
    int time = 0;
    Handler TextHandler = new Handler();
    Runnable TextRunable = new Runnable() {
        @Override
        public void run() {
            if (time == 0) {
                gifView.setVisibility(View.VISIBLE);
                gifView.play();
                splashTextView.setText("一");
            }
            if (time == 4) splashTextView.setText("一个");
            if (time == 9) splashTextView.setText("一个计");
            if (time == 17) splashTextView.setText("一个计划");
            time++;
//            Log.i(TAG, appName_cn.toString());
            TextHandler.postDelayed(this, 100);
        }
    };

    /**
     * 欢迎页动画
     */
    private void splashAnimation() {
        TextHandler.postDelayed(TextRunable, 200);

        Handler splashHandler = new Handler();
        Runnable splashRunable = () -> {
            if (auto_id != null && auto_password != null) {
                login(auto_id, auto_password);
            } else {
                directToLoginActivity();
            }
        };
        splashHandler.postDelayed(splashRunable, SPLASH_TIME);

        // 提前结束
        gifView.setOnClickListener(v -> {
            splashHandler.removeCallbacks(splashRunable);
            splashHandler.post(splashRunable);
        });
    }


    /**
     * 本地自动登录
     */
    private void login(String phone, String pw) {
        int ret = UserDB.login(sqlite, phone, pw);
        if (ret == 1) {
            Log.i(TAG, "自动登录 成功");
            My.Account = UserDB.getUser(sqlite, phone);
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
        TextHandler.removeCallbacks(TextRunable);
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
        TextHandler.removeCallbacks(TextRunable);
        finish();
    }


    /**
     * 更新 My 中的信息
     */
    private void updateMy() {

    }


}
