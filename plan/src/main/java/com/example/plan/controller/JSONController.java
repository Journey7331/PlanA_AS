package com.example.plan.controller;

import com.alibaba.fastjson.JSONObject;

public class JSONController {

    // void return
    public static String jsonTemplate(boolean data) {
        JSONObject json = new JSONObject();

        if (data) json.put("result", "ok");
        else json.put("result", "fail");
        return json.toString();
    }

    // data return
    public static String jsonDataPush(boolean result, Object data) {
        JSONObject json = new JSONObject();
        if (result){
            json.put("result", "ok");
            json.put("data", data);
        }else {
            json.put("result", "fail");
        }
        return json.toString();
    }
}
