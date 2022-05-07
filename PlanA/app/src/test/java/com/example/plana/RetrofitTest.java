package com.example.plana;

import android.util.Log;

import com.example.plana.bean.Todos;
import com.example.plana.bean.User;
import com.example.plana.bean.rawbean.RawTodo;
import com.example.plana.config.Constant;
import com.example.plana.service.SubjectService;
import com.example.plana.service.TodoService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class RetrofitTest {


    @Test
    public void getSubject() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SubjectService subjectService = retrofit.create(SubjectService.class);
        subjectService.getSubjectList(new User(1, "123","123"))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    String dataString = object.optString("data");
                                    System.out.println(dataString);
//
//                                    list.addAll(new Gson().fromJson(dataString, new TypeToken<ArrayList<RawTodo>>() {
//                                    }.getType()));
//                                    textView.setText(list.toString());
                                } else Log.i(Constant.TAG.NETWORK_TAG, "数据导入失败");
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
                            }
                        } else  Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                    }
                });
    }

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
    public void uploadListTest() {
        TodoService todoService = new Retrofit.Builder().baseUrl(Constant.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(TodoService.class);

        todoService.deleteTodoList(new User(1, "123", "123"))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    System.out.println("ok");
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

        for (Todos todo : getList()) {
//            upload(todoService, todo);
        }
    }

    private void upload(TodoService todoService, Todos todo) {
        User user = new User(1, "123", "123");
        HashMap<String, String> params = new HashMap<>();
        params.put("owner_id", user.getId()+"");
        params.put("content", todo.getContent());
        params.put("done", String.valueOf(todo.isDone()));
        params.put("memo", todo.getMemo());
        params.put("due_date", todo.getDate());
        params.put("due_time", todo.getTime());
        params.put("importance", String.valueOf(todo.getLevel()));

        todoService.updateTodoList(params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    System.out.println("ok");
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


    private List<Todos> getList() {
        ArrayList<Todos> list = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            list.add(new Todos(i,"content" + i, "memo"+i, true,"2022-01-0"+i,"13:0"+i, 3));
        }
        return list;
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
