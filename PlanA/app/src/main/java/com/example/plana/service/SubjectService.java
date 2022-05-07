package com.example.plana.service;

import com.example.plana.bean.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SubjectService {

    @POST("getSubjectList.do")
    Call<ResponseBody> getSubjectList(
            @Body User user
    );

    @POST("deleteSubjectList.do")
    Call<ResponseBody> deleteSubjectList(
            @Body User user
    );

    @POST("updateSubjectList.do")
    @FormUrlEncoded
    Call<ResponseBody> updateSubjectList(
            @FieldMap Map<String, String> requestBody
    );


}
