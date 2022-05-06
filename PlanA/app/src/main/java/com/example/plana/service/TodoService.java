package com.example.plana.service;

import com.example.plana.bean.Todos;
import com.example.plana.bean.User;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TodoService {

    @POST("getTodoList.do")
    Call<ResponseBody> getTodoList(
            @Body User user
    );


    @POST("getTodoList.do")
    Observable<ResponseBody> getTodoListBy(
            @Body User user
    );




}
