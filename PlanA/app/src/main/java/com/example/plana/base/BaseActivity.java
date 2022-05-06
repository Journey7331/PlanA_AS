package com.example.plana.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plana.bean.My;
import com.example.plana.bean.User;
import com.example.plana.config.Constant;
import com.example.plana.database.MyDatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @program: PlanA
 * @description: BaseActivity extends AppCompatActivity
 */
public class BaseActivity extends AppCompatActivity {

    private long exitTime = 0;
    private String URL = Constant.URL;

    public MyDatabaseHelper sqlite = new MyDatabaseHelper(this);

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public HashMap<String, String> params = new HashMap<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    /**
     * 更新 My 中的信息
     * @param dataString http 返回的 User信息 的字符串
     * @throws JSONException
     */
    public void updateMyAccount(String dataString) throws JSONException {
        JSONObject data = new JSONObject(dataString);

        User user = new User();
        user.setId(data.optInt("id"));
        user.setPhone(data.optString("phone"));
        user.setPwd(data.optString("password"));
        user.setName(data.optString("name"));
        user.setEmail(data.optString("email"));
        user.setBirth(data.optString("birthday"));

        My.Account = user;
    }


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
