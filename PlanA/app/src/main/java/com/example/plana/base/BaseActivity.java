package com.example.plana.base;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plana.Constant;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.service.UserService;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

/**
 * @program: PlanA
 * @description: BaseActivity extends AppCompatActivity
 */
public abstract class BaseActivity extends AppCompatActivity {

    private long exitTime = 0;
    private String URL = Constant.URL;

    public MyDatabaseHelper mysql = new MyDatabaseHelper(this);
//    public Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(URL);

    // 隐藏键盘
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // 显示键盘
    public static void showKeyboard(Activity activity) {
        InputMethodManager server = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        server.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 按两下返回键退出程序
     * 注意 isTaskRoot() 判断是不是最后一个 activity
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isTaskRoot() && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime > 3000)) {
                Toast.makeText(getApplicationContext(), "再按一次返回键退出软件", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
