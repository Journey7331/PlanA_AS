package com.example.plana.service;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @program: PlanA
 * @description:
 */
public interface UserService {

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(
            @Field("phone") String phone,
            @Field("password") String password
    );



}
