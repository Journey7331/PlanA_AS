package com.example.plana.service;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @program: PlanA
 * @description:
 */
public interface UserService {

    @POST("login.do")
    @FormUrlEncoded
    Call<ResponseBody> login(
            @FieldMap Map<String, String> map
    );


    @POST("checkPhoneExist.do")
    @FormUrlEncoded
    Call<ResponseBody> checkPhone(
            @FieldMap Map<String, String> map
    );


    @POST("register.do")
    @FormUrlEncoded
    Call<ResponseBody> register(
            @FieldMap Map<String, String> map
    );


}
