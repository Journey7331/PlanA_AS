package com.example.plana.function;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.User;
import com.example.plana.bean.rawbean.RawTodo;
import com.example.plana.config.Constant;
import com.example.plana.service.TodoService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @program: PlanA
 * @description:
 */
public class DemoActivity extends BaseActivity {

    TextView textView;
    ArrayList<RawTodo> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_retrofit_demo);
        textView = findViewById(R.id.retrofit_got_data);

        list = new ArrayList<>();

        findViewById(R.id.bt_retrofit).setOnClickListener(l -> {
            try {
//                getDataRx();
                getTodoListData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void getTodoListData() throws IOException {
        TodoService todoService = retrofit.create(TodoService.class);

        todoService.getTodoList(new User(1, "123", "123"))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    String dataString = object.optString("data");
                                    list.addAll(new Gson().fromJson(dataString, new TypeToken<ArrayList<RawTodo>>() {
                                    }.getType()));
                                    textView.setText(list.toString());
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
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                    }
                });
    }

}
