package com.example.plana;

import android.util.Log;

import com.example.plana.bean.Todos;
import com.example.plana.bean.User;
import com.example.plana.bean.rawbean.RawTodo;
import com.example.plana.config.Constant;
import com.example.plana.service.TodoService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @program: PlanA
 * @description:
 */
public class RetrofitTest {

    @Test
    public void name() throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TodoService todoService = retrofit.create(TodoService.class);

        Call<ResponseBody> call = todoService.getTodoList(new User(1, "123", "123"));

        Response<ResponseBody> response = call.execute();


        ArrayList<RawTodo> arrayList = new Gson().fromJson(response.body().string(), new TypeToken<ArrayList<RawTodo>>() {
        }.getType());
//        list.addAll(arrayList);
        System.out.println(response.body().string());

    }


    @Test
    public void RxJavaTest() {

        ArrayList<RawTodo> list = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        TodoService todoService = retrofit.create(TodoService.class);

        todoService.getTodoListBy(new User(2, "123", "123"))
                .subscribeOn(Schedulers.io())       // 切换到IO线程进行网络请求
                .observeOn(Schedulers.newThread())  // 切换回到主线程 处理请求结果
//                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, ArrayList<RawTodo>>() {
                    @Override
                    public ArrayList<RawTodo> apply(ResponseBody responseBody) throws Throwable {
                        return new Gson().fromJson(responseBody.string(), new TypeToken<ArrayList<RawTodo>>() {
                        }.getType());
                    }
                })
                .subscribe(new Consumer<ArrayList<RawTodo>>() {
                    @Override
                    public void accept(ArrayList<RawTodo> arrayList) throws Throwable {
                        list.addAll(arrayList);
                        System.out.println("arrayList = " + arrayList);
                        System.out.println("list = " + list);
                    }
                });


        while (true) {

        }

//        System.out.println(list);


    }
}
