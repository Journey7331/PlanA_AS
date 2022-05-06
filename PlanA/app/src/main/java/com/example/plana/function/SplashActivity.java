package com.example.plana.function;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cunoraz.gifview.library.GifView;
import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.config.Constant;
import com.example.plana.function.user.LoginActivity;
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
public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME = 3000;
    private static final String TAG = "SplashActivity";

    private String auto_id;
    private String auto_password;

    GifView gifView;
    TextView splashTextView;
    RelativeLayout rlSplashPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        changeStatusBar();

        rlSplashPage = findViewById(R.id.rl_splash_page);
        splashTextView = findViewById(R.id.tv_splash);
        gifView = findViewById(R.id.logo_gif);
        gifView.setGifResource(R.mipmap.logo);
        gifView.setVisibility(View.INVISIBLE);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id", null);
        auto_password = auto.getString("auto_pw", null);

        splashAnimation();


        /*****
         XGPushConfig.enableDebug(MainApplication.getAppContext(),true);
         XGPushManager.registerPush(MainApplication.getAppContext(), new XGIOperateCallback() {
        @Override public void onSuccess(Object data, int flag) {
        //token在设备卸载重装的时候有可能会变
        Log.d("TPush", "注册成功，设备token为：" + data);
        }

        @Override public void onFail(Object data, int errCode, String msg) {
        Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
        }
        });
         /*****/

    }


    /**
     * 如果没有 removeCallbacks， 会导致内存泄露
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gifView.pause();
        TextHandler.removeCallbacks(TextRunable);
    }

    /**
     * 沉浸式状态栏
     */
    private void changeStatusBar() {
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 更换状态栏字体的颜色  Android 6.0 +
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
            TextHandler.postDelayed(this, 100);
        }
    };

    /**
     * 欢迎页动画
     */
    private void splashAnimation() {
        TextHandler.postDelayed(TextRunable, 200);

        Handler splashHandler = new Handler();
        Runnable splashRunnable = () -> {
            if (auto_id != null && auto_password != null) {
                params.put("phone", auto_id);
                params.put("password", auto_password);
                login();
            } else {
                directToLoginActivity();
            }
        };
        splashHandler.postDelayed(splashRunnable, SPLASH_TIME);

        // 把 delayed 的 splashRunnable 提前
        rlSplashPage.setOnClickListener(v -> {
            splashHandler.removeCallbacks(splashRunnable);
            splashHandler.post(splashRunnable);
        });

    }


    /**
     * 本地自动登录
     */
//    private void login(String phone, String pw) {
//        int ret = UserDB.login(sqlite, phone, pw);
//        if (ret == 1) {
//            Log.i(TAG, "自动登录 成功");
//            My.Account = UserDB.getUser(sqlite, phone);
//            directToMainActivity();
//        } else {
//            Log.i(TAG, "自动登录 失败");
//            My.Account = null;
//            directToLoginActivity();
//        }
//    }


    /**
     * http post请求登录
     */
    private void login() {
        retrofit.create(UserService.class)
                .login(params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        response(response);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                        directToLoginActivity();
                    }
                });
    }

    private void response(Response<ResponseBody> response) {
        if (response.isSuccessful() && response.body() != null) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                if (object.optString("result").equals("ok")) {
                    updateMyAccount(object.optString("data"));
                    directToMainActivity();
                    return;
                } else {
                    Log.i(Constant.TAG.NETWORK_TAG, "数据导入失败");
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
            }
        } else {
            Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
        }
        directToLoginActivity();
    }

    /**
     * 转到登录页
     */
    private void directToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 登陆成功后转到主页
     */
    private void directToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        String value = getIntent().getStringExtra("SCHEDULE_NOTIFY");
        if (value != null && value.equals("SCHEDULE_NOTIFY")) {
            intent.putExtra("SCHEDULE_NOTIFY", "SCHEDULE_NOTIFY");
        }
        startActivity(intent);
        finish();
    }
}
